package uniandes.algorithms.alntools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		System.out.println("Generating file at: " + outFile);
		instance.findAlnMaxLength(temp, maxInsertLength, outFile);
		System.out.println("Finished!");
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
		pw.write("Sequence Name\t Initial Position Mate\t Final Position\n");
		//Reads input file
		try (ReadAlignmentFileReader reader = new ReadAlignmentFileReader(file)){
			Iterator<ReadAlignment> it = reader.iterator();
			while (it.hasNext()) {
				ReadAlignment aln = it.next();
				// TODO: Check if the insert length is greater than the expected
				if(aln.getInferredInsertSize() > maxInsertLength) {
					String out = String.format("%s\t\t %s\t\t\t %s\n", 
											   aln.getSequenceName(), 
											   aln.getMateFirst() + "", 
											   (aln.getFirst() + aln.getInferredInsertSize()) + "");
					pw.write(out);
				}
			}
		}
	}
	
}
