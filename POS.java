import java.util.HashMap;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;

/**
 * An exception for when a part of speech is not found, such as when the user gives an incomplete list of parts of speech.
 */
class POSNotFoundException extends Exception
{
}

/**
 * Parts of speech class statically contains a list of the parts of speech 
 * objects, and POS objects represent individual parts of speech, each with a
 * unique index, name, and symbol (as defined by the corpus tagset).
 */
public class POS
{
    /** unique index for each part of speech */
    private int index;

    /** symbol (in tagged corpus) of each part of speech */
    private String symbol;

    /** name of a part of speech, such as "noun" or "adjective"*/
    private String name;
    
    /** regex of the tags to ignore when reading corpus */
    private static String ignoreRegex = "";

    /** hashMap of symbols to indices */
    private static HashMap<String, Integer> symbolToIndex = new HashMap<String, Integer>();

    /** ArrayList of all the existing <code>POS</code> objects, indexed by the
     * indicies of the <code>POS</code> objects*/
    private static ArrayList<POS> indexToPOS = new ArrayList<POS>();

    /** 
     * Constructs the POS object with the data provided the constructor.
     * @param symbol text literal representing POS, as defined by corpus
     * @param name text literal representing the name of a part of speech
     * @return none
     */
    private POS (String symbol, String name)
    {
		this.symbol = symbol;
		this.name = name;
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
     * Gets the number of parts of speech defined by the corpus tagset
     * @return number of parts of speech
     */
    public static int numPOS ()
    {
		return indexToPOS.size();
    }
    
    /**
     * Gets the the regular expression for the tags to be ignored when
     * assigning parts of speech.
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
    public static POS getPOSbyIndex (int index)
    {
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
		else {
			System.out.println (symbol + " doesn't exist...");
			throw new POSNotFoundException();
		}
    }

    /**
     * Loads a list of parts of speech into memory from a file.
     * @param tagset text file containing the legend for the corpus. The corpus 
     * tagset should be in the form of having each part of speech on its own 
     * line, each POS symbol exactly 1 "word" long followed by a tab and the 
     * real English english term for the POS such as <br>
 	 * [POS symbol] \t [POS term] 
 	 * e.g.
 	 * cs	conjunction, subordinating
     * @return none
     */
    public static void loadFromFile (String tagset) throws IOException
    {
    	indexToPOS.clear();
    	symbolToIndex.clear();
		Scanner s = null;

        try {
            s = new Scanner(new BufferedReader(new FileReader(tagset)));
        	s.useDelimiter ("\n");
        	
        	// find out which tags to ignore and store in ignore_regex
        	if (s.findInLine ("---IGNORE---") != null) {
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
            while (s.hasNext()) {
                String[] pair = (s.next()).split("\t");
                p = new POS (pair[0], pair[1]);
                
                indexToPOS.add (p);
				symbolToIndex.put (p.getSymbol(), p.getIndex());
            }
        } finally {
            if (s != null) {
                s.close();
            }
        }
        
        // testing code
        /*for (int i = 0; i < indexToPOS.size(); i++)
        	System.out.println (i + "\t" + indexToPOS.get(i).getIndex() + "\t" + indexToPOS.get(i).getSymbol() + "\t" + indexToPOS.get(i).getName());
        
        Set<String> ks = symbolToIndex.keySet();
        Iterator<String> i = ks.iterator();
        while (i.hasNext()) {
        	String k = i.next();
        	System.out.println (k + "\t" + symbolToIndex.get(k));
        }
        */
    }
}
