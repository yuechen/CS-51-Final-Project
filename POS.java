package tagger;

import java.util.HashMap;
import java.util.ArrayList;

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

    /** hashMap of symbols to indices */
    private static HashMap<String, Integer> symbolToIndex = new HashMap<String, Integer>();

    /** ArrayList of all the existing <code>POS</code> objects, indexed by the
     * indicies of the <code>POS</code> objects*/
    private static ArrayList<POS> indexToPOS = new ArrayList<POS>();

    /** 
     * Constructs the POS object and adds to the static list as well as to the
     * symbol-to-integer listing.
     * @param symbol text literal representing POS, as defined by corpus
     * @param name text literal representing the name of a part of speech
     * @return none
     */
    public POS (String symbol, String name)
    {
		this.symbol = symbol;
		this.name = name;
		this.index = indexToPOS;

		indexToPOS.add(this);
		addPOS(symbol, name);
    }
 
    /**
     * Adds a POS to the POS Dictionary.
     * @param symbol text literal representing POS, as defined by corpus
     * @param name text literal representing the name of a part of speech
     * @return none
     */
    private static void addPOS (String symbol, String name)
    {
	
    }

    /**
     * Removes a POS from the POS Dictionary.
     * @param num POS index, representing a specific part of speech
     * @return none
     */
    public static void removePOS (int index)
    {
	
    }
    
    /**
     * Gets the POS index of the current POS object
     * @return POS index
     */
    public int getIndex ()
    {
		return index;
    }

    /**
     * Gets the symbol, as defined by the corpus, of the current POS object
     * @return symbol of the POS
     */
    public int getSymbol ()
    {
		return symbol;
    }

    /**
     * Compares the current POS to another. Returns true if they are equal, false otherwise.
     * @param other the other POS to compare
     * @return true if the same, false otherwise
     */
    public boolean compareTo (POS other)
    {
	// compare symbols or indices?

	return other.index == this.index;
    }

    /**
     * Gets the number of parts of speech currently in the dictionary.
     * @return size of the dictionary
     */
    public static int getNumPOS ()
    {
	return nameToIndex.size();
    }

    /**
     * Gets the name of the current part of speech.
     * @return the name of the part of speech
     */
    public String getName();
    {
	return name;
    }

    /**
     * Returns a <code>POS</code> object given an index.
     * @param index the index of the part of speech object
     * @return the part of speech with the given index
     */
    public static POS getPOSbyIndex (int index)
    {
	    return indexToPOS.get(index);
    }
    
    /**
     * Gets the <code>POS</code> index of a part of speech, given its symbol, as defined by the corpus.
     * <p> 
     * If the part of speech is not found, throws an exception
     * @param symbol text literal representing POSNotFoundException, as defined by corpus
     * @return integer index of that part of speech
     */
    public static int getPOSIndexBySymbol (String symbol) throws POSNotFoundException
    {
	
    }

    /**
     * Loads a list of parts of speech into memory from a file.
     * @param posList text file containing the parts of speech as a list, with the number of parts of speech on the first line and for each successive line, "POS_symbol POS_name"
     * @return none
     */
    public static void loadFromFile(String posList)
    {
	
    }
}

/**
 * An exception for when a part of speech is not found, such as when the user gives an incomplete list of parts of speech.
 */
class POSNotFoundException extends Exception
{
}
