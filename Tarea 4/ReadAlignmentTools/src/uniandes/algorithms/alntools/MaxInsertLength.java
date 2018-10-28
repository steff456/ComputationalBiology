package uniandes.algorithms.alntools;

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
		try {
			temp = args[0];
			maxInsertLength = Integer.parseInt(args[1]);
		}
		catch(Exception e){
			temp = "/Users/tefa/Documents/BioComp/Seg5_bowtie2_sorted.bam";			
			maxInsertLength = 600;
		}
//		instance.processAlignmentsFile(temp);
//		instance.printStatistics(temp);
		// Sequence name = cromosomas
	}

}
