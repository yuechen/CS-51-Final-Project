import java.util.HashMap;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Parts of speech class statically contains a list of the parts of speech 
 * objects, and POS objects represent individual parts of speech, each with a
 * unique index, name, and symbol (as defined by the corpus tagset).
 */
public class POS
{
    /** unique index for each part of speech */
    private int index;
    
    /** a generalized index for the part of speech
      * i.e. verb "to be", present tense, 3rd person singular, negated => verb
      */
    private int gIndex;

    /** symbol (in tagged corpus) of each part of speech */
    private String symbol;

    /** name of a part of speech, such as "the verb to be"*/
    private String name;
    
    /** general name of the part of speech, such as "noun" or "adjective" or "verb" */
    private String gName;
    
    /** regex of the tags to ignore when reading corpus */
    private static String ignoreRegex = "";
    
    /** number of gIndices */
    private static int numgIndices = 0;

    /** HashMap of symbols to indices */
    private static HashMap<String, Integer> symbolToIndex = new HashMap<String, Integer>();

    /** ArrayList of all the existing <code>POS</code> objects, indexed by the
     * indicies of the <code>POS</code> objects*/
    private static ArrayList<POS> indexToPOS = new ArrayList<POS>();

    /**
     * Returns the default POS for objects not found in the dictionary
     */
    public static POS get_default()
    {
	   return indexToPOS.get(0);
    }

    /** 
     * Constructs the POS object with the data provided the constructor.
     * @param symbol text literal representing POS, as defined by corpus
     * @param name text literal representing the name of a part of speech
     * @return none
     */
    private POS (String symbol, String name, int gIndex, String gName)
    {
		this.symbol = symbol;
		this.name = name;
		this.gIndex = gIndex;
		this.gName = gName;
		this.index = indexToPOS.size();
    }
    
    /**
     * Gets the POS index of the POS object
     * @return POS index
     */
    public int getIndex ()
    {
		return index;
    }
    
    /**
     * Gets the POS gIndex of the POS object
     * @return general POS index
     */
    public int getgIndex ()
    {
		return gIndex;
    }

    /**
     * Gets the symbol, as defined by the corpus tagset, of the POS object
     * @return symbol of the POS
     */
    public String getSymbol ()
    {
		return symbol;
    }
    
    /**
     * Gets the name, as defined by the corpus tagset, of the POS object
     * @return name of the POS
     */
    public String getName ()
    {
		return name;
    }
    
    /**
     * Gets the general POS name, as defined by the corpus_simple_tagset.
     * @return general POS name of the POS
     */
    public String getgName() {
    	return gName;
    }

    /**
     * Gets the number of parts of speech defined by the corpus tagset
     * @return number of parts of speech
     */
    public static int numPOS ()
    {
		return indexToPOS.size();
    }
    
    /**
     * Gets the number of parts of speech defined by the corpus tagset
     * @return number of parts of speech
     */
    public static int numgIndices ()
    {
		return numgIndices;
    }
    
    /**
     * Gets the the regular expression for the tags to be ignored when
     * reading parts of speech from the corpus.
     * @return regular expression for tags to be ignored
     */
    public static String getIgnoreRegex ()
    {
		return ignoreRegex;
    }

    /**
     * Returns a <code>POS</code> object given an index
     * @param index the index of the part of speech object
     * @return the part of speech with the given index
     */
    public static POS getPOSbyIndex (int index) throws POSNotFoundException
    {
    	if (index >= indexToPOS.size())
    		throw new POSNotFoundException("No part of speech with index " + index + " was found.");

	    return indexToPOS.get(index);
    }
    
    /**
     * Gets the <code>POS</code> index of a part of speech, given its symbol, as 
     * defined by the corpus tagset. <br>
     * If the part of speech is not found, throws POSNotFoundException
     * @param symbol text literal representing POS, as defined by corpus tagset
     * @return integer index of that part of speech
     */
    public static int getIndexBySymbol (String symbol) throws POSNotFoundException
    {
		if (symbolToIndex.containsKey(symbol))
			return symbolToIndex.get(symbol);
		else
			throw new POSNotFoundException("No part of speech with symbol " + symbol + " was found.");
    }

    /**
     * Loads lists of parts of speech from tagset files into memory.
     * @param tagset text file containing the legend for the corpus. The corpus 
     * tagset should be in the form of having each part of speech on its own 
     * line, each POS symbol exactly 1 "word" long followed by a tab and the 
     * real English english term for the POS such as <br>
 	 * [POS symbol] \t [POS term] <br>
 	 * e.g. <br>
 	 * cs	conjunction, subordinating <br>
 	 * If there are parts of POS determiners in the corpus that should be ignored,
 	 * there should be a section at the top of the corpus_tagset file delimited by
 	 * ---IGNORE---. Each part of a tag to be ignored is put on each line. See the
 	 * default corpus_tagset for an example.<br>
 	 * @param gtagset text file containing a legend for the tags. Maps a simplified
 	 * tag name to a comma-separated list of POSIndices, such as <br>
 	 * [POS name] \t [POS symbol 1], [POS symbol 2], [POS symbol 3]... <br>
 	 * e.g. <br>
 	 * verb	vb+at,vb+in,vb+jj,vb+ppo... <br>
 	 * noun nn+bez,nn+hvd,nn+hvz,nn+in,nn+md,nn+nn... <br>
     * @return none
     */
    public static void loadFromFile (String tagset, String gtagset)
    	throws FileNotFoundException, WrongFormatException, POSNotFoundException
    {
		// deal with gtagset
		
		/* Initialize a scanner. Clear the current HashMap in case this is called twice
		 * during execution.
		 */
		Scanner s = null;
		HashMap<String, Integer> symbolTogIndex = new HashMap<String, Integer>();
		HashMap<Integer, String> gIndexTogName = new HashMap<Integer, String>();
		
		try
		{
			/* Read from gtagset into the two HashMaps, so that we can map specific
			 * indices to simplified indices, and from simplified indices to simplified
			 * names.
			 */
			s = new Scanner(new BufferedReader(new FileReader(gtagset)));
			s.useDelimiter ("\n");
			
			int j = 0;
			while (s.hasNext())
			{
				// symbols and gNames are separated by tabs
				String[] pair = s.next().split("\t");
				
				// check to see if there was actually a tab!
				if (pair.length != 2)
					throw new WrongFormatException("Corpus simple tagset not formatted correctly.");
					
				String pName = pair[0];
				String[] pSymbols = pair[1].split(" ");
				
				// insert the correct data into the data structures
				for (int i = 0; i < pSymbols.length; i++)
				{
					gIndexTogName.put(j, pName);
					symbolTogIndex.put(pSymbols[i], j);
				}
				
				// increment the index
				j++;
			}
		}
		finally
		{
			// need to close the corpus_simple_tagset file
			if (s != null)
                s.close();
		}
		
		// store the number of gIndices
		numgIndices = gIndexTogName.size();
		
		// deal with tagset
    	indexToPOS.clear();
    	symbolToIndex.clear();
		s = null;

        try 
        {
            s = new Scanner(new BufferedReader(new FileReader(tagset)));
        	s.useDelimiter ("\n");
        	
        	// find out which tags to ignore and store in ignore_regex
        	if (s.findInLine ("---IGNORE---") != null)
        	{
        		s.useDelimiter ("---IGNORE---");
        		Scanner s2 = new Scanner (s.next().trim());
        		s.useDelimiter ("\n");
        		s.next();
        		
        		s2.useDelimiter("\n");
        		while (s2.hasNext())
        			ignoreRegex = ignoreRegex + "|" + s2.next();
        			
        		ignoreRegex = ignoreRegex.substring(1);
        	}
        	
        	// read the tagset file and retrieve data
        	POS p;
            while (s.hasNext())
            {
            	// delimited by tabs
                String[] pair = (s.next()).split("\t");
                // check to see if there was actually a tab!
				if (pair.length != 2)
					throw new WrongFormatException("Corpus tagset not formatted correctly.");
				
				if (!symbolTogIndex.containsKey(pair[0]))
					throw new POSNotFoundException("A part of speech with symbol " + pair[0] + " was not found.");
					
				int gIndex = symbolTogIndex.get(pair[0]);
                p = new POS (pair[0], pair[1], gIndex, gIndexTogName.get(gIndex));
                
                indexToPOS.add (p);
				symbolToIndex.put (p.getSymbol(), p.getIndex());
            }
        }
        finally 
        {
        	// close the file
            if (s != null)
                s.close();
        }
    }
}
