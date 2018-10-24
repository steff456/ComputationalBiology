package uniandes.algorithms.alntools;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import ngsep.alignments.ReadAlignment;
import ngsep.alignments.io.ReadAlignmentFileReader;
/**
 * Skeleton of a class implementing a program to calculate alignment statistics
 * from a BAM file
 * @author Jorge Duitama
 *
 */
public class BasicAlignmentStatistics {

	private int totalReads = 0;
	private int totalAlignments = 0;
	private int alignedReads = 0;
	private int unalignedReads = 0;
	private int pairProperlyAligned = 0;
	private int mateInDifferentSequence = 0;
	private int mateUnaligned = 0;
	private int uniqueAlignments = 0;
	private int secondaryAlignments = 0;
	private int [] alignmentQualitiesDistribution = new int [256];
	
	/**
	 * Main method to run the program
	 * @param args Array with one element, the path to the alignments file
	 * @throws Exception If the file can not be read
	 */
	public static void main(String[] args) throws Exception {
		BasicAlignmentStatistics instance = new BasicAlignmentStatistics();
		String temp = "";
		try {
			temp = args[0];
		}
		catch(Exception e){
			temp = "/Users/tefa/Documents/BioComp/ComputationalBiology/Tarea 4/Seg5_bowtie2_sorted.bam";			
		}
		instance.processAlignmentsFile(temp);
		instance.printStatistics(temp);
	}

	/**
	 * Updates the statistics processing the given file
	 * @param filename Path to the BAM file to process
	 * @throws IOException If the file can not be read
	 */
	public void processAlignmentsFile(String filename) throws IOException {
		
		try (ReadAlignmentFileReader reader = new ReadAlignmentFileReader(filename)){
			Iterator<ReadAlignment> it = reader.iterator();
			while (it.hasNext()) {
				ReadAlignment aln = it.next();
				totalReads += aln.getReadLength();
				totalAlignments += 1;
				if(aln.isReadUnmapped())
					unalignedReads += 1;
				else alignedReads +=1;
				if(aln.isProperPair())
					pairProperlyAligned += 1;
				if(aln.isMateDifferentSequence())
					mateInDifferentSequence += 1;
				if(aln.isMateUnmapped())
					mateUnaligned += 1;
				if(aln.isUnique())
					uniqueAlignments += 1;
				if(aln.isSecondary())
					secondaryAlignments += 1;
				alignmentQualitiesDistribution[aln.getAlignmentQuality()] += 1;
				// TODO: Utilzar los metodos del objeto aln para actualzar las estadisticas
			}
		}
	}
	/**
	 * Prints the statistics to standard output
	 */
	public void printStatistics(String file) {
		// TODO: Implementar metodo
		System.out.println("Basic Alignment Statistics for BAM file: " + file);
		System.out.println("--------------------------------------------------");
		System.out.println("Total Reads: " + totalReads);
		System.out.println("Total Alignments: " + totalAlignments);
		System.out.println("Aligned Reads: " + alignedReads);
		System.out.println("Unaligned Reads: " + unalignedReads);
		System.out.println("Pair Properly Aligned: " + pairProperlyAligned);
		System.out.println("Mate in Different Sequence: " + mateInDifferentSequence);
		System.out.println("Mate Unaligned: " + mateUnaligned);
		System.out.println("Unique Alignments: " + uniqueAlignments);
		System.out.println("Secondary Alignments: " + secondaryAlignments);
		System.out.println("--------------------------------------------------");
		System.out.println("Alignment Quality Distribution");
		System.out.println("Quality\t Number of Reads");			
		for (int i = 0; i < alignmentQualitiesDistribution.length; i++) {
			System.out.println(i + "\t " + alignmentQualitiesDistribution[i]);
		}
	}

}
