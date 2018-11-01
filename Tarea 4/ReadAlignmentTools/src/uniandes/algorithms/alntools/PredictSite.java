package uniandes.algorithms.alntools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ngsep.genome.GenomicRegionImpl;

public class PredictSite {
	private static HashMap<String, ArrayList<GenomicRegionImpl>> reads = new HashMap<String, ArrayList<GenomicRegionImpl>>();

	public static void main(String[] args) throws Exception {
		PredictSite instance = new PredictSite();
		String file = "";
		try {
			file = args[0];
		}
		catch(Exception e){
			file= "/Users/tefa/Desktop/insertL.txt";
		}
		
		instance.loadData(file);
		instance.findDeletions();
		// Sequence name = cromosomas
	}
	
	public void loadData(String file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		System.out.println("Reading data ...");
		// Discard the header lines
		String line = br.readLine();
		line = br.readLine();
		line = br.readLine();
		line = br.readLine();
		line = br.readLine();
		line = br.readLine();
		while(line != "" && line != null) {
			String[] data = line.split("\t");
			String seqName = data[0].trim();
			int first = Integer.parseInt(data[2].trim());
			int last = Integer.parseInt(data[data.length-1].trim());
			GenomicRegionImpl nlec = new GenomicRegionImpl(seqName, first, last);
			if(!inHashMap(seqName)) {
				ArrayList<GenomicRegionImpl> temp = new ArrayList<GenomicRegionImpl>();
				temp.add(nlec);
				reads.put(seqName, temp);
			}
			else {
				ArrayList<GenomicRegionImpl> temp = reads.get(seqName);
				temp.add(nlec);
			}
			line = br.readLine();
		}
		System.out.println("Finished loading data ...");
		
	}
	
	public boolean inHashMap(String key) {
		Set keys = reads.keySet();
		Iterator it = keys.iterator();
		while(it.hasNext()) {
			String actKey = (String) it.next();
			if(actKey.equals(key)) {
				return true;
			}
		}
		return false;
	}
	
	public void findDeletions() {
		Set keys = reads.keySet();
		Iterator it = keys.iterator();
		while(it.hasNext()) {
			ArrayList<GenomicRegionImpl> alns = reads.get(it.next());
			int max = getMaxRepeat(alns);
			for(GenomicRegionImpl aln: alns) {
				int actDiff = aln.getLast() - aln.getFirst();
				if(actDiff != max) {
					System.out.println("actDiff: " + actDiff + ";" + max);
					String s = String.format("%s;%s;%s", aln.getSequenceName(), aln.getFirst(), aln.getLast());
					System.out.println(s);
				}
			}
		}
	}
	
	public int getMaxRepeat(ArrayList<GenomicRegionImpl> alns) {
		int max = 0;
		int ref = 0;
		HashMap<Integer, Integer> differences = new HashMap<Integer, Integer>();
		for(GenomicRegionImpl aln: alns) {
			int actDiff = aln.getLast() - aln.getFirst();
			if(!differences.keySet().contains(actDiff)) {
				differences.put(actDiff, 1);
			}
			else {
				int temp = differences.get(actDiff);
				temp++;
				differences.put(actDiff, temp);
				if(temp > max) {
					max = temp;
					ref = actDiff;
				}
			}
		}
		return ref;
	}
}
