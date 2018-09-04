package uniandes.algorithms.readsanalyzer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ngsep.sequences.RawRead;
/**
 * Stores abundances information on a list of subsequences of a fixed length k (k-mers)
 * @author Jorge Duitama
 */
public class KmersTable implements RawReadProcessor {

	/**
	 * Table storing the kmers sequence and respective abundance.
	 */
	private Map<String, Integer> kmersTable;
	
	//Length of kmersize
	private int kmersize;
	
	//Max abundance seen
	private int maxAb = 1;
	
	/**
	 * Creates a new table with the given k-mer size
	 * @param kmerSize length of k-mers stored in this table
	 */
	public KmersTable(int kmerSize) {
		// TODO: Implementar metodo
		kmersTable = new HashMap<String, Integer>();
		kmersize = kmerSize;
	}

	/**
	 * Identifies k-mers in the given read
	 * @param read object to extract new k-mers
	 */
	public void processRead(RawRead read) {
		// TODO Implementar metodo. Calcular todos los k-mers del tamanho dado en la constructora y actualizar la abundancia de cada k-mer
		String sequence = read.getSequenceString();
		int i = 0;
		while(i < sequence.length() - kmersize) {
			String kact = sequence.substring(i, (i+kmersize));
			if(!kmersTable.containsKey(kact)) {
				kmersTable.put(kact, 1);
			}
			else {
				int ab = getAbundance(kact);
				kmersTable.replace(kact, ++ab);
				if(++ab > maxAb)
					maxAb = ab;
			}
			i++;
		}
	}
	
	/**
	 * List with the different k-mers found up to this point
	 * @return Set<String> set of k-mers
	 */
	public Set<String> getDistinctKmers() {
		// TODO Implementar metodo
		return kmersTable.keySet();
	}
	
	/**
	 * Calculates the current abundance of the given k-mer 
	 * @param kmer sequence of length k
	 * @return int times that the given k-mer have been extracted from given reads
	 */
	public int getAbundance(String kmer) {
		// TODO Implementar metodo
		return kmersTable.get(kmer);
	}
	
	/**
	 * Calculates the distribution of abundances
	 * @return int [] array where the indexes are abundances and the values are the number of k-mers
	 * observed as many times as the corresponding array index. Position zero should be equal to zero
	 */
	public int[] calculateAbundancesDistribution() {
		// TODO Implementar metodo
		int[] dist = new int[maxAb];
		for(String kmer : getDistinctKmers()) {
			int actAb = getAbundance(kmer);
			dist[actAb]++;
		}
		return dist;
	}
}
