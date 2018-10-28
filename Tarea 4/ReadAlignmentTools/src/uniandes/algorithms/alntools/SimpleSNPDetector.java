package uniandes.algorithms.alntools;

import java.util.List;

import ngsep.alignments.ReadAlignment;
import ngsep.discovery.AlignmentsPileupGenerator;
import ngsep.discovery.PileupListener;
import ngsep.discovery.PileupRecord;
import ngsep.genome.ReferenceGenome;
import ngsep.sequences.QualifiedSequence;

/**
 * Skeleton of a program that implements a simple algorithm to identify SNPs relative to a 
 * reference genome from an alignments file in SAM format
 * 
 * @author Jorge Duitama
 *
 */
public class SimpleSNPDetector implements PileupListener {

	private ReferenceGenome genome;
	
	/**
	 * Main method that executes the program
	 * @param args Array with two arguments. The path to the reference genome and the path to the alignments file
	 * @throws Exception If the files can not be read
	 */
	public static void main(String[] args) throws Exception {
		String genomeFile = "";
		String alignmentsFile = "";
		try {
			genomeFile = args[0];
			alignmentsFile = args[1];			
		}
		catch(Exception e) {
			genomeFile = "/Users/tefa/Documents/BioComp/yeastGenome.fa";
			alignmentsFile = "/Users/tefa/Documents/BioComp/Seg5_bowtie2_sorted.bam";
		}
		SimpleSNPDetector instance = new SimpleSNPDetector();
		instance.genome = new ReferenceGenome(genomeFile);
		AlignmentsPileupGenerator generator = new AlignmentsPileupGenerator();
		generator.addListener(instance);
		generator.processFile(alignmentsFile);
	}

	@Override
	public void onPileup(PileupRecord record) {
		String seqName = record.getSequenceName();
		int referencePos = record.getPosition();
		char referenceBase = genome.getReferenceBase(seqName, referencePos);
		List<ReadAlignment> alnsPos = record.getAlignments();
		// [0]: A, [1]: G, [2]: T, [3]: C
		// TODO: Recorrer la lista de alineamientos y para cada uno ubicar la base
		// en la lectura que corresponde a la posicion referencePos.
		// Imprimir los datos de todas las posiciones que tengan al menos un llamado a una base diferente a la referencia
		for(ReadAlignment aln:alnsPos) {
			int ref = aln.getReferencePosition(referencePos);
			CharSequence actSeq = aln.getAlleleCall(referencePos);
			if( actSeq != null && actSeq.length()>0 ) {
				char actBase = actSeq.charAt(0);
				if(actBase != referenceBase) {
					System.out.println("Sequence Name: " + aln.getSequenceName());
					System.out.println("SNP Position: " + referencePos);
					System.out.println("Reference Base: "+ referenceBase);
					System.out.println("Read Alignment: " + aln.getReadName());
					System.out.println("Alternative Base: " + actBase);
					System.out.println("------------------");
				}				
			}
		}
	}

	@Override
	public void onSequenceEnd(QualifiedSequence seq) {
		System.out.println("Finished sequence: "+seq.getName());
		
	}

	@Override
	public void onSequenceStart(QualifiedSequence seq) {
		System.out.println("Started sequence: "+seq.getName());
		
	}

}
