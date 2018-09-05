package uniandes.algorithms.readsanalyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import htsjdk.samtools.util.RuntimeEOFException;
import ngsep.math.Distribution;
import ngsep.sequences.RawRead;

/**
 * Represents an overlap graph for a set of reads taken from a sequence to assemble
 * @author Jorge Duitama
 *
 */
public class OverlapGraph implements RawReadProcessor {

	private int minOverlap;
	private Map<String,Integer> readCounts = new HashMap<>();
	private Map<String,ArrayList<ReadOverlap>> overlaps = new HashMap<>();

	//Max abundance seen
	private int maxAb = 1;

	//Max succesors seen
	private int maxSucc = 1;

	/**
	 * Creates a new overlap graph with the given minimum overlap
	 * @param minOverlap Minimum overlap
	 */
	public OverlapGraph(int minOverlap) {
		this.minOverlap = minOverlap;
	}

	/**
	 * Adds a new read to the overlap graph
	 * @param read object with the new read
	 */
	public void processRead(RawRead read) {
		String sequence = read.getSequenceString();
		//TODO: Paso 1. Agregar la secuencia al mapa de conteos si no existe.
		//Si ya existe, solo se le suma 1 a su conteo correspondiente y no se deben ejecutar los pasos 2 y 3 
		if(!readCounts.containsKey(sequence))
		{
			readCounts.put(sequence, 1);

			//TODO: Paso 2. Actualizar el mapa de sobrelapes con los sobrelapes en los que la secuencia nueva sea predecesora de una secuencia existente
			//2.1 Crear un ArrayList para guardar las secuencias que tengan como prefijo un sufijo de la nueva secuencia
			//2.2 Recorrer las secuencias existentes para llenar este ArrayList creando los nuevos sobrelapes que se encuentren.
			//2.3 Después del recorrido para llenar la lista, agregar la nueva secuencia con su lista de sucesores al mapa de sobrelapes 

			//TODO: Paso 3. Actualizar el mapa de sobrelapes con los sobrelapes en los que la secuencia nueva sea sucesora de una secuencia existente
			// Recorrer el mapa de sobrelapes. Para cada secuencia existente que tenga como sufijo un prefijo de la nueva secuencia
			//se agrega un nuevo sobrelape a la lista de sobrelapes de la secuencia existente

			ArrayList<ReadOverlap> newOverlaps = new ArrayList<ReadOverlap>();
			Iterator<String> it = overlaps.keySet().iterator();
			while(it.hasNext()) {
				String act = it.next();
				//Calculate overlap suff: seq, pref: act
				int lenOverlap1 = getOverlapLength(sequence, act);
				if(lenOverlap1 > minOverlap) {
					ReadOverlap ro = new ReadOverlap(sequence, act, lenOverlap1);
					newOverlaps.add(ro);
				}
				//Calculate overlap suff: act, pref: seq
				int lenOverlap2 = getOverlapLength(act, sequence);
				if(lenOverlap2 > minOverlap) {
					ReadOverlap ro = new ReadOverlap(act, sequence, lenOverlap2);
					overlaps.get(act).add(ro);
					if(overlaps.get(act).size() > maxSucc)
						maxSucc = overlaps.get(act).size();
				}
			}
			//Adds the key and new overlaps to the map
			overlaps.put(sequence, newOverlaps);
			if(newOverlaps.size() > maxSucc)
				maxSucc = newOverlaps.size();
		}
		else
		{
			int num = readCounts.get(sequence);
			readCounts.replace(sequence, ++num);
			if(++num > maxAb)
				maxAb = num;
		}
	}

	/**
	 * Returns the length of the maximum overlap between a suffix of sequence 1 and a prefix of sequence 2
	 * @param sequence1 Sequence to evaluate suffixes
	 * @param sequence2 Sequence to evaluate prefixes
	 * @return int Maximum overlap between a prefix of sequence2 and a suffix of sequence 1
	 */
	private int getOverlapLength(String sequence1, String sequence2) {
		// TODO Implementar metodo
		int i = 0; //suffix
		int count = 0;
		int j = 0; //prefix
		while(i < sequence2.length() && j<sequence1.length()) {
			if(sequence1.charAt(i) == sequence2.charAt(j)) {
				count++;
				i++;
				j++;
			}
			else {
				i++;
				j=0;
				count=0;
			}
		}
		return count;
	}

	/**
	 * Returns a set of the sequences that have been added to this graph 
	 * @return Set<String> of the different sequences
	 */
	public Set<String> getDistinctSequences() {
		//TODO: Implementar metodo
		return readCounts.keySet();
	}

	/**
	 * Calculates the abundance of the given sequence
	 * @param sequence to search
	 * @return int Times that the given sequence has been added to this graph
	 */
	public int getSequenceAbundance(String sequence) {
		//TODO: Implementar metodo
		return readCounts.get(sequence);
	}

	/**
	 * Calculates the distribution of abundances
	 * @return int [] array where the indexes are abundances and the values are the number of sequences
	 * observed as many times as the corresponding array index. Position zero should be equal to zero
	 */
	public int[] calculateAbundancesDistribution() {
		//TODO: Implementar metodo
		int[] distr = new int[maxAb];
		Set<String> seqs = getDistinctSequences();
		for(String seq: seqs) {
			int num = getSequenceAbundance(seq);
			distr[num]++;
		}
		return distr;
	}

	/**
	 * Calculates the distribution of number of successors
	 * @return int [] array where the indexes are number of successors and the values are the number of 
	 * sequences having as many successors as the corresponding array index.
	 */
	public int[] calculateOverlapDistribution() {
		// TODO: Implementar metodo
		int[] arr = new int[(maxSucc+1)];
		Set<String> seqs = overlaps.keySet();
		for(String seq: seqs) {
			int numSucc = overlaps.get(seq).size();
			arr[numSucc]++;
		}
		return arr;
	}

	/**
	 * Predicts the leftmost sequence of the final assembly for this overlap graph
	 * @return String Source sequence for the layout path that will be the left most subsequence in the assembly
	 */
	public String getSourceSequence () {
		// TODO Implementar metodo recorriendo las secuencias existentes y buscando una secuencia que no tenga predecesores
		Set<String> seen = new HashSet<>(overlaps.keySet());
		for(String seq: getDistinctSequences()) {
			ArrayList<ReadOverlap> act = overlaps.get(seq);
			if(act!=null) {
				for(ReadOverlap ro: act) {
					seen.remove(ro.getDestSequence());
				}
			}
		}
		return seen.iterator().next();
	}

	/**
	 * Calculates a layout path for this overlap graph
	 * @return ArrayList<ReadOverlap> List of adjacent overlaps. The destination sequence of the overlap in 
	 * position i must be the source sequence of the overlap in position i+1. 
	 */
	public ArrayList<ReadOverlap> getLayoutPath() {
		ArrayList<ReadOverlap> layout = new ArrayList<ReadOverlap>();
		HashSet<String> visitedSequences = new HashSet<String>(); 
		// TODO Implementar metodo. Comenzar por la secuencia fuente que calcula el método anterior
		// Luego, hacer un ciclo en el que en cada paso se busca la secuencia no visitada que tenga mayor sobrelape con la secuencia actual.
		// Agregar el sobrelape a la lista de respuesta y la secuencia destino al conjunto de secuencias visitadas. Parar cuando no se encuentre una secuencia nueva
		String act = getSourceSequence();
		ArrayList<ReadOverlap> succ = overlaps.get(act);
		while(!succ.isEmpty()) {
			succ.sort(new abundanceComparator());
			ReadOverlap ro = succ.get(0);
			layout.add(ro);
			visitedSequences.add(act);
			act = ro.getDestSequence();
			succ = overlaps.get(act);
		}
		return layout;
	}
	/**
	 * Predicts an assembly consistent with this overlap graph
	 * @return String assembly explaining the reads and the overlaps in this graph
	 */
	public String getAssembly () {
		ArrayList<ReadOverlap> layout = getLayoutPath();
		StringBuilder assembly = new StringBuilder();
		boolean first = true;
		// TODO Recorrer el layout y ensamblar la secuencia agregando al objeto assembly las bases adicionales que aporta la región de cada secuencia destino que está a la derecha del sobrelape 
		for(ReadOverlap lay: layout) {
			if(first) {
				String left = lay.getSourceSequence();
				assembly.append(left);
				first = false;
			}
			String right = lay.getDestSequence();
			assembly.append(right.substring(lay.getOverlap(), right.length()));
		}
		return assembly.toString();
	} 
	
	public String toString(){
		String s = "";
		int count = 0;
		for(String seq: overlaps.keySet()) {
			System.out.println("**** ---- " + count);
			for(ReadOverlap ro: overlaps.get(seq)) {
				System.out.println(ro.getSourceSequence());
				System.out.println(ro.getDestSequence());
				System.out.println(ro.getOverlap());
				System.out.println("------");
			}
			count++;
		}
		System.out.println(overlaps.keySet().size());
		System.out.println(readCounts.keySet().size());
		return s;
	}

}

class abundanceComparator implements Comparator<ReadOverlap>{

	@Override
	public int compare(ReadOverlap o1, ReadOverlap o2) {
		boolean comp1 = o1.getOverlap() > o2.getOverlap();
		boolean comp2 = o1.getOverlap() == o2.getOverlap();
		return comp1 ? -1 : comp2 ? 0 : 1;
	}

}
