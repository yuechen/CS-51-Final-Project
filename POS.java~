package tagger;

import java.util.HashMap;

/**
 * Parts of speech class statically contains a list of the parts of speech so
 * far, and POS objects represent individual objects, each with a unique index for the
 * sake of manipulation.
 */
 
public class POS
{
    /** unique index for each part of speech */
    private int POSIndex;

    /** symbol or name of each part of speech */
    private String symbol;

    /** hashMap of names to indices */
    private static HashMap<String, Integer> nameToIndex = new HashMap<String, Integer>();

    /** 
     * Constructs the POS object.
     * @param symbol text literal representing POS, as defined by corpus
     * @return none
     */
    public POS (String symbol)
    {
	POSIndex = getPOSIndex (symbol);
    }

    /**
     * Adds a POS to the POS Dictionary.
     * @param symbol text literal representing POS, as defined by corpus
     * @returns none
     */
    public static void addPOS (String symbol)
    {
	
    }

    /**
     * Removes a POS from the POS Dictionary.
     * @param num POS index, representing a specific part of speech
     * @returns none
     */
    public static void removePOS (int num)
    {
	
    }
    
    /**
     * Gets the POS index of the current POS object
     * @return POS index
     */
    public int getIndex ()
    {
	return POSIndex;
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

	return other.POSIndex == this.POSIndex;
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
     * Gets the POS index of a part of speech, given its name, as defined by the corpus.
     * @param symbol text literal representing POS, as defined by corpus
     * @returns integer index of that part of speech
     */
    public static int getPOSIndex (String symbol)
    {
	
    }
}
