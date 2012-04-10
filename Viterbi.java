package tagger;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;

/**
 * As a class, <code>Viterbi</code> contains both the HMM model 
 * and the functions to run the Viterbi algorithm on it. 
 * <p>
 * The class can train itself from probabilities provided by
 * a dictionary, and then save and load the needed data into a 
 * file. There are two required files for training: a corpus tagset
 * and thedirectory of the corpus. 
 * <p> 
 * The corpus tagset should be in the form of having
 * each part of speech on its own line, each POS symbol exactly 1 "word"
 * long followed by a tab and the real English english term for the POS
 * such as <br>
 * [POS symbol] \t [POS term] 
 * e.g.
 * cs	conjunction, subordinating
 * <p>
 * Each corpus text file should be a compete English text, with each word
 * tagged with a part of speech. It should conform to the format set by
 * the Brown corpus, i.e. in the form [word]/[part of speech].
 * <p>
 * The training file will be printed in the form <br>
 * numPOS <br>
 * numWords <br>
 * "word1" log(probability POS1) log(probabilty POS2) ... log(probability POSlast) <br>
 * ... <br>
 * "wordlast" log(probability POS1) log(probabilty POS2) ... log(probability POSlast) <br>
 * "POS1" log(probaility POS1 following) log(probability POS2 following) ... log(probability POSlast following)<br>
 * ... <br>
 * "POSlast" log(probaility POS1 following) log(probability POS2 following) ... log(probability POSlast following)<br>
 * where "word1", "POS1", etc are replaced by the actual words or
 * names of parts of speech
 * <p>
 * In particular, the corpus directory may contain subdirectories,
 * but should not contain non-corpus files.
 * <p>
 */
public class Viterbi
{
    /**
     * The number of words.
     */
    private int numWords;
    
    /**
     * The number of parts of speech.
     */
    private int numPOS;

    /**
     * The logs of the emission probabilities of the model, 
     * represented as a HashMap of Strings (words) to arrays
     * of probabilities.
     */
    private HashMap<String, float[]> p_emission;

    /**
     * The logs of the transmission probabilities of the model,
     * representated as a two-dimensional array. Here,
     * transmission[POS 1][POS 2] refers to the 
     * transmission probability for moving from 
     * POS 1 to POS 2. 
     */
    private float[][] p_transmission;

    /**
     * Basic constructor
     */
    private Viterbi(String tagset)
    {
    }

    /**
     * Constructor; initializes the probability table from the 
     * given saved training file. (See above for format of file.)
     * @param tagset the file containing a list of parts of speech
     * @param datafile the name of the file of saved probability data
     * @return none
     */
    public Viterbi(String tagset, String datafile)
    {
	this();
	
	POS.loadfromFile(tagset);

	// initalizes the probability arrays

	// read file and load probabilities
    }

    /**
     * Iterates through the corpus and calculates the frequencies of
     * neighborings parts of speech and the frequences of each part
     * of speech for each word. 
     * @param tagset the file containing the tagset, as defined in the class 
     		  description.
     * @param corpusDirectory the name of the directory containing
                              the corpus
     * @param saveLocation where the probabilities are to be saved
     * @return none
     */
    public static void loadCorpusForTraining (String tagset, 
					      String corpusDirectory,
					      String saveLocation)
    {
		int numWords = 0;

		POS.loadFromFile(tagset);
		int numPOS = POS.numPOS();
		
		/* Hashmap of number of times each word appears
         * in the training data for each part of speech;
	 	 * as a hashmap of Strings to integer arrays, with each integer
	 	 * representing a POS index.
	 	 */ 
		HashMap<String, int[]> word_to_pos = new HashMap<String, int[]>();
		File dir = new File(tagset);
	    File[] fl = dir.listFiles();
	    
	    for (int i = 0; i < fl.length; i++)
	    	System.out.println (fl[i]);
	    

	/* array of number of times parts of speech
	 * appear consecutively; pos_to_pos[i][j]
	 * should indicate the number of times that 
	 * j appeared immediately after i; should be 
	 * of size [number of parts of speech][number of parts of speech]
	 */
	int[][] pos_to_pos = new int[numPos][numPos];

	/* the number of times each part of speech occurs */
	int[] posFrequencies = new int[numPos];

	// read and load corpus file frequencies
	File corpusDir = new File(corpusDirectory);
	BufferedReader in;

	for (int i = 0; i<5; i++) //iterate over files in directory with String fileName -- current insides only for compilation purposes
	{
	    in = new BufferedReader(new FileReader(fileName));
		// deal with frequencies
	}

	BufferedWriter saveFile = new BufferedWriter(new FileWriter(saveDirectory));

	// save training files and compute probabilities as saving

	// write numPOS
	saveFile.write(numPOS.toString());
	saveFile.write(newLine());

	// write numWords
	saveFile.write(numWords.toString());
	saveFile.write(newLine());
	
	// write words_to_pos log probabilities

	// write pos_to_pos log probailities
    }

    /**
     * Takes a set of outputs and determines the most likely original
     * state.
     * @param results list of outputs
     * @return list of (state, output) pairs
     */
    public List<Pair<String, POS>> parse(ArrayList<S> results)
    {
    }
}
