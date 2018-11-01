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
		
		instance.findSite(file);
		// Sequence name = cromosomas
	}
	
	public void findSite(String file) throws IOException {
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
}
