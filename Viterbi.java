import java.util.LinkedList;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.Arrays;
import java.util.Set;
import java.util.Iterator;

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
 * "POS1" log(probability POS1 following) log(probability POS2 following) ... log(probability POSlast following)<br>
 * ... <br>
 * "POS2" log(probability POS1 following) log(probability POS2 following) ... log(probability POSlast following)<br>
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
    private HashMap<String, float[]> p_emission = new HashMap<String, float[]>();

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
    public Viterbi(String tagset, String datafile) throws IOException
    {
	// tries to load the tagset
	try
	{
	    POS.loadFromFile (tagset);
	}
	catch (IOException e)
	{
	    System.out.println("oops. The tagset could not be loaded properly.");
	    System.exit(0);

	    // TO-DO: better error handling?
	}

	// deals with the actual data file
	try 
	{
	    // tries to open the file
	    Scanner sc = new Scanner(new BufferedReader(new FileReader(datafile)));

	    // reads in the first two numerical values
	    this.numPOS = sc.nextInt();
	    sc.nextLine();
	    this.numWords = sc.nextInt();
	    sc.nextLine();

	    // basic check that the number of parts of speech matches up
	    if (this.numPOS != POS.numPOS())
		throw new Exception("The training file does not seem to match the indicated tagset.");

	    // initializes the probability array
	    this.transition = new float[this.numPOS][this.numPOS];

	    // reads file and loads emission probabilities
	    String word;
	    float[] probability;
	    for (int i = 0; i < numWords; i++)
	    {
		probability = new float[this.numPOS];
		word = sc.next("\s++");
		sc.skip(" ");
		
		// gets actual probabilities
		for (int j = 0; j < numPOS; j++)
		    probability[j] = sc.nextFloat();

		sc.nextLine();

		// adds to hash map
		this.p_emission.put(word, probability);
	    }

	    // reads file and loads transmission probabilities
	    for (int i = 0; i < numPOS; i++)
	    {
		// gets POS and checks that it has the right index
		word = sc.next("\s++");
		sc.skip(" ");
		if (i != POS.getIndexBySymbol("word"))
		    throw new Exception("The training file does not seem to match the indicated tagset.");

		// puts actual probabilities
		for (int j = 0; j < numPOS; j++)
		    this.p_transmission[i][j] = sc.nextFloat();

		sc.nextLine();
	    }
	}
	catch (Exception e)
	{
	    if (e instanceof IOException)
		System.out.println("oops. The training data file could not be loaded properly.");
	    else
		System.out.println(e.getMessage());
	}
	finally
	{
	    if (sc != null)
		sc.close();
	}
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
					      String saveLocation) throws IOException
    {
	int numWords = 0;

	try
        {
	    POS.loadFromFile (tagset);
	}
	catch (IOException e)
	{
	    System.out.println ("File I/O Error.");
	}
	int numPOS = POS.numPOS();
		
	/* Hashmap of number of times each word appears
         * in the training data for each part of speech;
	 * as a hashmap of Strings to integer arrays, with each integer
	 * representing a POS index.
	 */ 
	HashMap<String, int[]> word_to_pos = new HashMap<String, int[]>();
		
	/* Two dimension of number of times each POS appears
	 * after a specific POS. */
	int[][] pos_to_pos = new int[numPOS][numPOS];
	int POSIndex = -1;
	int lastPOSIndex = -1;
		
	File dir = new File(corpusDirectory);
	File[] fl = dir.listFiles();
	    
	if (fl == null) {
	    System.out.println ("Directory not valid.");
	    throw new IOException();
	}
	    
	Scanner scanner;
	    
	for (int i = 0; i < fl.length; i++) {
	    scanner = null;
	    	
	    try 
	    	{
		    scanner = new Scanner(new BufferedReader(new FileReader(fl[i])));
        	
		    while (scanner.hasNext()) 
			{
			    String s = scanner.next();
            		
			    int lastIndex = s.lastIndexOf("/");
			    String word = s.substring(0, lastIndex).toLowerCase();
			    String symbol = s.substring(lastIndex + 1).
                		replaceAll(POS.getIgnoreRegex(), "");
                	
			    try {
                		POSIndex = POS.getIndexBySymbol(symbol);
			    } catch (POSNotFoundException e) {
                		System.out.println ("POS not found.");
                		return;
			    }
                	
			    int[] arr;
                	
			    // add to word_to_pos
			    if (word_to_pos.containsKey(word)) {
                		word_to_pos.get(word)[POSIndex]++;
			    }
			    else {
                		arr = new int[numPOS];
                		arr[POSIndex]++;
                		word_to_pos.put (word, arr);
			    }
                	
			    // add to pos_to_pos
			    if (lastPOSIndex < 0) {
                		lastPOSIndex = POSIndex;
                		continue;
			    } else {
                		pos_to_pos[lastPOSIndex][POSIndex]++; 
			    }
			}
        	} 
	    finally 
        	{
		    if (scanner != null) {
                	scanner.close();
		    }
        	}
	}
	    
	// test code
	/*
	  Set<String> ks = word_to_pos.keySet();
	  Iterator<String> iter = ks.iterator();
	  while (iter.hasNext()) {
	  String k = iter.next();
	  if (k.toLowerCase().equals("the"))
	  System.out.println (k + "\t" + Arrays.toString(word_to_pos.get(k)));
	  }
        
	  System.out.println ("Total number of words: " + word_to_pos.size());
        */
        
        /* array of number of times parts of speech
	 * appear consecutively; pos_to_pos[i][j]
	 * should indicate the number of times that 
	 * j appeared immediately after i; should be 
	 * of size [number of parts of speech][number of parts of speech]
	 */
	 
	 int[][] pos_to_pos = new int[numPos][numPos];

	 the number of times each part of speech occurs 
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

	 // write pos_to_pos log probailities*/
    }

    /**
     * Takes a set of outputs and determines the most likely original
     * state.
     * @param results list of outputs
     * @return list of (state, output) pairs
     */
    /*public List<Pair<String, POS>> parse(ArrayList<S> results)
      {
      }*/
}