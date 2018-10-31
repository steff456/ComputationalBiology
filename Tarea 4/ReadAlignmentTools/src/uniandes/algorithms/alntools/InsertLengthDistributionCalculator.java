package uniandes.algorithms.alntools;

import java.io.IOException;
import java.util.Iterator;

import ngsep.alignments.ReadAlignment;
import ngsep.alignments.io.ReadAlignmentFileReader;

/**
 * Skeleton of a class implementing a program to calculate 
 * the insert length distribution from a given BAM file  
 * @author Jorge Duitama
 *
 */
public class InsertLengthDistributionCalculator {

	public static final int N_BINS = 50;
	
	//TODO: Definir un atributo para guardar la distribucion de tamaño de inserto
	public int[] insertLengthDistr = new int[N_BINS];
	public int big = 0;
	
	/**
	 * Main method to run the program
	 * @param args Array with one element, the path to the alignments file
	 * @throws Exception If the file can not be read
	 */
	public static void main(String[] args) throws Exception {
		InsertLengthDistributionCalculator instance = new InsertLengthDistributionCalculator();
		String temp = "";
		try {
			temp = args[0];
		}
		catch(Exception e){
			temp = "/Users/tefa/Documents/BioComp/Seg5_bowtie2_sorted.bam";			
		}
		instance.processAlignmentsFile(temp);
		instance.printDistribution ();
	}
	/**
	 * Updates the insert length distribution processing the given file
	 * @param filename Path to the BAM file to process
	 * @throws IOException If the file can not be read
	 */
	public void processAlignmentsFile(String filename) throws IOException {
		
		try (ReadAlignmentFileReader reader = new ReadAlignmentFileReader(filename)){
			Iterator<ReadAlignment> it = reader.iterator();
			while (it.hasNext()) {
				ReadAlignment aln = it.next();
				// TODO: Pedir al objeto aln el insert length que se predice a partir del alineamiento de la lectura y su par
				// Si el insert length es positivo, se procesa su valor en la distribucion
				// verificar si la lectura esta alineada y si el alineamiento es primario
				int size = aln.getInferredInsertSize();
				if(!aln.isSecondary() && size > 0){
					int bin = size/N_BINS;
					if(bin < insertLengthDistr.length)
						insertLengthDistr[bin]++;
					else big++;
				}
			}
		}	
	}

	/**
	 * Prints the distribution to standard output
	 */
	public void printDistribution() {
		// TODO: Implementar metodo
		System.out.println("Insert Length Distribution Calulator");
		System.out.println("--------------------------------------------------");
		System.out.println("Insert Length\t Number of Reads");		
		int i = 0;
		for (; i < insertLengthDistr.length; i++) {
			System.out.println((i*N_BINS) + "\t \t " + insertLengthDistr[i]);
		}
		System.out.println((i*N_BINS) + "\t \t " + big);
	}

}
