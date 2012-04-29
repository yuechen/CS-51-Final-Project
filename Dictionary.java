import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Dictionary {
	
	/** HashMap containing all the word-definition mappings */
	private HashMap<String, String> dict = new HashMap<String, String>();
	
	/** 
     * Constructs the Dictionary object with the dictionary file provided.
     * The dictionary file must be in the following format: <br>
     * [WORD] | [PREFIX-] | [-SUFFIX] <-- all caps, no spaces, one line <br>
     * [lines containing definition] <-- no restrictions <br>
     * See webster_dictionary.txt for example of 1913 Merriam-Webster Unabridged English
     * dictionary, structured correctly.
     * @param dictionaryFile the text file where the dictionary may be found.
     * @return none
     */
	public Dictionary (String dictionaryFile) throws FileNotFoundException
    {
    	Scanner s = null;
    	try 
    	{
    		// begin scanning through file
	    	s = new Scanner(new BufferedReader(new FileReader(dictionaryFile)));
	    	String word = "";
	    	String def = "";
	    	String line = "";
	    
	    	while (s.hasNext()) {
	    		// get line
        		line = s.nextLine();
        		
        		// if line delimites a new term, put into HashMap the term just defined
        		// else, expand term's definition
        		if (line.matches("^\\-[A-Z]+$|^[A-Z]+\\-$|^[A-Z]+$"))
				{
					// if term's already defined, as with another part of speech, append
					// current definition
					if (dict.containsKey(word))
						def = dict.get(word) + 
						"\n------------------------------------------------------------" +
						"---------\n\n" + def;
			    	dict.put(word, def);
			    	
			    	// begin processing for next term
		    		word = line.trim();
		    		def = s.nextLine();
        		}
        		else
        		{
        			// expand current definition
		    		def += "\n" + line;
		    		
		    		// if reached the end, add last term to HashMap
		    		if (!s.hasNext())
						dict.put(word, def);
        		}
	    	}
        
        	// remove the empty definition
	    	dict.remove("");
	    }
        finally
        {
        	// close the file
            if (s != null)
                s.close();
        }
    }
    
    /**
     * Returns the definition for any word.
     * @param word the term one is searching for
     * @return the definition of the term, if found.
     	Otherwise, returns "No definition found."
     */
    public String lookup (String word) {
    	if (dict.containsKey(word))
    		return dict.get(word);
    	else
    		return "No definition found.";
    }
}