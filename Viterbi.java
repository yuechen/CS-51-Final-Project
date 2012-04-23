import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
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
 * and the directory of the corpus. 
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
 * "word1" index_POS1 log(probability POS1) index_POS2 log(probabilty POS2) ... <br>
 * ... <br>
 * "wordlast" index_POS1 log(probability POS1) index_POS1 log(probabilty POS2) ...  <br>
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
    /*private*/ HashMap<String, float[]> p_emission = new HashMap<String, float[]>();

    /**
     * The logs of the transmission probabilities of the model,
     * representated as a two-dimensional array. Here,
     * transmission[POS 1][POS 2] refers to the 
     * transmission probability for moving from 
     * POS 1 to POS 2. 
     */
    /*private*/ float[][] p_transmission;

    /**
     * Basic constructor
     */
    private Viterbi(String tagset, String gtagset)
    {
    }

    /**
     * Constructor; initializes the probability table from the 
     * given saved training file. (See above for format of file.)
     * @param tagset the file containing a list of parts of speech
     * @param gtagset text file containing a legend for the tags; maps a simplified
 	 * tag name to a comma-separated list of POSIndices
     * @param datafile the name of the file of saved probability data
     * @return none
     */
    public Viterbi(String tagset, String gtagset, String datafile) 
    	throws WrongFormatException
    {
	// tries to load the tagset
	try
	{
	    POS.loadFromFile (tagset, gtagset);
	}
	catch (IOException e)
	{
	    System.out.println("oops. The tagset could not be loaded properly.");
	    System.exit(1);

	    // TO-DO: better error handling?
	}

	Scanner sc = null;
	// deals with the actual data file
	try 
	{
	    // tries to open the file
	    sc = new Scanner(new BufferedReader(new FileReader(datafile)));

	    // reads in the first two numerical values
	    this.numPOS = sc.nextInt();
	    sc.nextLine();
	    this.numWords = sc.nextInt();
	    sc.nextLine();

	    // basic check that the number of parts of speech matches up
	    if (this.numPOS != POS.numPOS())
		throw new Exception("The training file does not seem to match the indicated tagset.");

	    // initializes the probability array
	    this.p_transmission = new float[this.numPOS][this.numPOS];
	    
	    // reads file and loads emission probabilities
	    String word;
	    float[] probability;
	    String[] line;
	    int numEntries;
	    
	    for (int i = 0; i < numWords; i++)
	    {
		probability = new float[this.numPOS];
		word = sc.next("\\S+");
		sc.skip(" ");
		line = sc.nextLine().split(" ");
		numEntries = (line.length+1)/2;

		// sets default probabilities
		for (int j = 0; j < numPOS; j++)
		{
		    probability[j] = Float.NEGATIVE_INFINITY;
		}
		
		// gets actual probabilities
		for (int j = 0; j < numEntries; j+=2)
		{
		    probability[Integer.parseInt(line[j])] = Float.parseFloat(line[j+1]);
		}

		// adds to hash map
		this.p_emission.put(word, probability);
	    }
	    
	    // reads file and loads transmission probabilities
	    for (int i = 0; i < numPOS; i++)
	    {
		// gets POS and checks that it has the right index
		word = sc.next("\\S+");
		sc.skip(" ");
		if (i != POS.getIndexBySymbol(word))
		    throw new Exception("The training file does not seem to match the indicated tagset.");

		line = sc.nextLine().split(" ");
		numEntries = (line.length+1)/2;

		// sets default probabilities
		for (int j = 0; j < numPOS; j++)
		{
		    p_transmission[i][j] = Float.NEGATIVE_INFINITY;
		}
		
		// gets actual probabilities
		for (int j = 0; j < numEntries; j+=2)
		{
		    p_transmission[i][Integer.parseInt(line[j])] = Float.parseFloat(line[j+1]);
		}
	    }
	}
	catch (Exception e)
	{
	    if (e instanceof IOException)
		System.out.println("oops. The training data file could not be loaded properly.");
	    else
		System.out.println(e.getMessage());

	    System.exit(1);
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
     * @param gtagset text file containing a legend for the tags; maps a simplified
 	 * tag name to a comma-separated list of POSIndices
     * @param corpusDirectory the name of the directory containing
     the corpus
     * @param saveLocation where the probabilities are to be saved
     * @return none
     */
    public static void loadCorpusForTraining (String tagset, String gtagset,
					      String corpusDirectory,
					      String saveLocation) throws WrongFormatException
    {
	int numWords = 0;
	
	// load all of the corpus tags from the corpus_tagset
	try
    {
	   	POS.loadFromFile (tagset, gtagset);
	}
	catch (IOException e)
	{
	 	System.out.println ("File I/O Error.");
	 	System.exit(1);
	}
	
	int numPOS = POS.numPOS();
		
	/* Hashmap of number of times each word appears
	 * in the training data for each part of speech;
	 * as a hashmap of Strings to integer arrays, with each integer
	 * representing a POS index.
	 */ 
	HashMap<String, int[]> word_to_pos = new HashMap<String, int[]>();
		
	/* Two dimension of number of times each POS appears
	 * after a specific POS.
	 */
	int[][] pos_to_pos = new int[numPOS][numPOS];
	int POSIndex = -1;
	int lastPOSIndex = -1;
	
	/* One dimension array of number of times each POS appears in the corpus */
	int[] pos_frequencies = new int[numPOS];

	// Find all corpus data files in directory
	File dir = new File(corpusDirectory);
	File[] fl = dir.listFiles();
	
	// If none, error
	if (fl == null) {
	   	System.out.println ("Directory not valid.");
	   	System.exit(1);
	}

	// Begin reading from corpus files
	Scanner scanner;
	    
	for (int i = 0; i < fl.length; i++)
	{
	    scanner = null;
	    	
	    try 
	    {
		    scanner = new Scanner(new BufferedReader(new FileReader(fl[i])));
        	
        	// scan through file
		    while (scanner.hasNext()) 
	    	{
			    String s = scanner.next();
            	
            	// figure out word/symbol combinations
			    int lastIndex = s.lastIndexOf("/");
			    String word = s.substring(0, lastIndex).toLowerCase();
			    String symbol = s.substring(lastIndex + 1).
                		replaceAll(POS.getIgnoreRegex(), "");
                		
                // get the index of the POS, if none, error
			    try 
			    {
                	POSIndex = POS.getIndexBySymbol(symbol);
			    } 
			    catch (POSNotFoundException e) 
			    {
                	System.out.println ("POS not found.");
                	System.exit(1);
			    }
                	
			    int[] arr;
                	
			    // add to word_to_pos
			    if (word_to_pos.containsKey(word))
			    {
                	word_to_pos.get(word)[POSIndex]++;
			    }
			    else 
			    {
                	arr = new int[numPOS];
                	arr[POSIndex]++;
                	word_to_pos.put (word, arr);
			    }
                	
			    // add to pos_to_pos
			    if (lastPOSIndex < 0) 
			    {
                	lastPOSIndex = POSIndex;
                	continue;
			    } else {
                	pos_to_pos[lastPOSIndex][POSIndex]++;
                	lastPOSIndex = POSIndex;
			    }
			    
			    // add to pos_frequencies
			    pos_frequencies[POSIndex]++;
			}
        } 
	    catch (Exception e)
		{
			// better error handling, but for now, exit program
		    System.exit(1);
		}
	    finally 
        {
        	// close file
		    if (scanner != null)
		    {
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
	numWords = word_to_pos.size();
	PrintWriter saveFile = null;

	try
        {
	    // open file to be saved
	    saveFile = new PrintWriter(new FileWriter(saveLocation));

	    // write numPOS
	    saveFile.println(numPOS);
	    
	    // write numWords
	    saveFile.println(numWords);
	    
	    String line;
	    
	    // write emission probabilities
	    Set<Map.Entry<String, int[]>> prob_e = word_to_pos.entrySet();
	    Iterator<Map.Entry<String, int[]>> it = prob_e.iterator();
	    Map.Entry<String, int[]> e;
	    int[] p;

	    for (int i = 0; i < numWords; i++)
	    {
		e = it.next();
		p = e.getValue();
		line = e.getKey() + " ";

		for (int j = 0; j < numPOS; j++)
		{
		    if (p[j] > 0)
			line += j + " " + Math.log((float)p[j]/pos_frequencies[j]) + " ";
		}

		saveFile.println(line);
	    }
	    
	    // write transmission probabilities
	    for (int i = 0; i < numPOS; i++)
	    {
		line = POS.getPOSbyIndex(i).getSymbol() + " ";
		for (int j = 0; j < numPOS; j++)
		{
		    if (pos_to_pos[i][j] > 0)
			line += j + " " + Math.log((float)pos_to_pos[i][j]/pos_frequencies[i]) + " ";
		}
		
		saveFile.println(line);
	    }
	}
	catch (IOException e)
	{
	    System.out.println("File could not be saved.");
	    System.exit(1);
	}
	finally
	{
	    saveFile.close();
	}
    }

    /**
     * Takes a set of outputs and determines the most likely original
     * state.
     * @param results list of outputs
     * @return list of (state, output) pairs
     */
    public ArrayList<Pair<String, POS>> parse(ArrayList<String> results)
      {
	  int length = results.size(); // number of pieces of the sentence
	  float[][] probs = new float[numPOS][length]; // probability table
	  int[][] parts = new int[numPOS][length]; // parts of speech fo the words

	  // variables for looping below
	  float value; // current value
	  int index; // index of the previous word for the maximum of value
	  float[] emission; // tmp variable for emission probabilities
	  String word; 
	  float vtmp;
	  boolean allneg; // if all the values for a part of speech are negative infinity

	  // iterates over all possible words
	  for (int i = 0; i < length; i++) // per word
	  {    
		// trims any zeros, decapalizes for matching
		word = results.get(i).trim().toLowerCase();
	       	
		// sets allneg back to true
		allneg = true;

		// if the dictionary does not contain the word, sets a default
		if(! p_emission.containsKey(word))
	      	{
			index = 0;
			value = Float.NEGATIVE_INFINITY;

			// find index of largest value before this
			if (i > 0)
			{
				index = maxInRowIndex(probs, i-1, numPOS);
				value = probs[index][i-1];
			}

			// set all other probabilities really small find previous index
			for (int j = 0; j < numPOS; j++)
			{
		      		probs[j][i] = value;
				parts[j][i] = index;
			} 
	      	}
		// if the word is in our dictionary
	      	else	
	      	{
			// get the table of emission values
			emission = p_emission.get(word);
			
			// loop over all possible POS assigments
		  	for (int j = 0; j < numPOS; j++)
	          	{
		      		value = Float.NEGATIVE_INFINITY;
		      		index = 0;
		      		
				// loop over all possible POS assignments for the previous word
		      		for (int k = 0; k < numPOS; k++) 
		      		{
					if (i > 0)
						vtmp = probs[k][i-1] + p_transmission[k][j] + emission[j];
					else
						vtmp = emission[j];

			  		if (vtmp > value)
			  		{
			      			value = vtmp;
			      			index = k;
			  		}
		      		}

				// set values in tables
				if (value > Float.NEGATIVE_INFINITY)
					System.out.println(word + " " + value + " / ");
				if (value > Float.NEGATIVE_INFINITY)
					allneg = false;
				parts[j][i] = index;
		      		probs[j][i] = value;
		  	}

			// if all possible parts of speech have value negative infinity
			if (allneg)
			{
				index = maxInRowIndex(probs, i-1, numPOS);
				value = probs[index][i-1];

				for (int j = 0; j < numPOS; j++)
				{
					probs[j][i] = value + emission[j];
					parts[j][i] = index;
				}
			}
	      	}
	  }
	  
	  // find the most likely part of speech for the last word
	  int POSIndex = 0;
	  for (int i = 1; i < numPOS; i++)
	      if (probs[i][length-1] > probs[i-1][length-1])
		  POSIndex = i;

	  return create_for_parse(results, probs, parts, length-1, POSIndex);
      	}

	/** 
	 * Helper method for parse
	 * Find the index of the maximum value on a given column of an array
	 */
    	private int maxInRowIndex(float[][] array, int index2, int size1)
	{
		float value = Float.NEGATIVE_INFINITY;
		int index = 0;

		for (int i = 0; i < size1; i++)
			if (array[i][index2] > value)
			{
				index = i;
				value = array[i][index2];
			}

		return index;
	}

    	/**
     	* Helper method for parse. 
     	*/
    private ArrayList<Pair<String, POS>> create_for_parse(ArrayList<String> words, float[][] probs, 
							  int[][] parts, int wIndex, int pIndex)
    {
	if (wIndex < 0)
	    return new ArrayList<Pair<String,POS>>();
	
	ArrayList<Pair<String,POS>> sentence = 
	    create_for_parse(words, probs, parts, wIndex-1, parts[pIndex][wIndex]);
	sentence.add(new Pair<String, POS>(words.get(wIndex), POS.getPOSbyIndex(pIndex)));
	return sentence;
    }
}
