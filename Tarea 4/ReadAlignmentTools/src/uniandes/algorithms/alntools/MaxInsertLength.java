package uniandes.algorithms.alntools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import ngsep.alignments.ReadAlignment;
import ngsep.alignments.io.ReadAlignmentFileReader;

public class MaxInsertLength {
		
	/**
	 * Main method to run the program
	 * @param args Array with one element, the path to the alignments file
	 * @throws Exception If the file can not be read
	 */
	public static void main(String[] args) throws Exception {
		MaxInsertLength instance = new MaxInsertLength();
		String temp = "";
		int maxInsertLength = 0;
		String outFile = "";
		try {
			temp = args[0];
			maxInsertLength = Integer.parseInt(args[1]);
			outFile = args[2];
		}
		catch(Exception e){
			temp = "/Users/tefa/Documents/BioComp/Seg5_bowtie2_sorted.bam";			
			maxInsertLength = 600;
			outFile= "/Users/tefa/Desktop/insertL.txt";
		}
		instance.findAlnMaxLength(temp, maxInsertLength, outFile);
//		instance.processAlignmentsFile(temp);
//		instance.printStatistics(temp);
		// Sequence name = cromosomas
	}

	/**
	 * Find alignments with an insert length greater than the maximum given by parameter
	 * Writes a file with the alignments found.
	 * @param temp
	 * @param maxInsertLength
	 * @param outFile
	 * @throws IOException 
	 */
	public void findAlnMaxLength(String file, int maxInsertLength, String outFile) throws IOException {
		PrintWriter pw = new PrintWriter(new File(outFile));
		pw.write("Running the program with the following parameters: \n");
		pw.write("Input file: " + file + "\n");
		pw.write("Maximum Length: " + maxInsertLength + "\n");
		pw.write("------------------------------------------------------\n");
		
		//Reads input file
		try (ReadAlignmentFileReader reader = new ReadAlignmentFileReader(file)){
			Iterator<ReadAlignment> it = reader.iterator();
			while (it.hasNext()) {
				ReadAlignment aln = it.next();
				// TODO: Check if the insert length is greater than the expected
				if(aln.getInferredInsertSize() > maxInsertLength) {
					pw.write("Sequence name: " + aln.getSequenceName()+ "\n");
					pw.write("Initial position of mate: " + aln.getFirst() + "\n");					
					pw.write("Final position: " + (aln.getFirst() + aln.getInferredInsertSize()) +"\n");
					pw.write("-------------\n");
				}
			}
		}
	}

}
