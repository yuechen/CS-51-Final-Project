import java.util.Scanner;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;

class Thesaurus {

    /** A map from words to strings of synonyms separated by linebreaks */
    private HashMap<String, String> map;

    /**
     * Constructs a thesauraus from the given file.
     * The file is expected to be formatted in parts of the form
     * word
     * synonyms (one per line)
     * [linebreak]
     * @param file the location of the saved thesaurus
     */
    public Thesaurus(String file) throws FileNotFoundException
    {
    Scanner sc = null;
    
    try
    {
	// opens scanner
	String br = System.getProperty("line.separator");
	sc = new Scanner(new BufferedReader(new FileReader(file)));

	// initializes map
	map = new HashMap<String, String>();

	// a few variables to use while iterating
	String word = "";
	String line = "";
	String tmp;

	// loops through by word
	while(sc.hasNextLine())
	   {
		word = sc.nextLine();
		tmp = " ";
		line = "";

		// loops through by synonym
		while(sc.hasNextLine() && !tmp.isEmpty())
		{
			tmp = sc.nextLine();
			if (!tmp.isEmpty())
			{
				line += tmp + br;
			}
		}

		// adds the pair to the map
		map.put(word, line);
	    }

	}
	finally
	{
		if (sc != null)
			sc.close();
	}	
    }

    /** Looks up the synonyms for a given word
     * @param word word to be looked up
     * @return list of synonyms, linebreak-separated. If none, returns an empty string.
     */
    public String lookup(String word)
    {
	String br = System.getProperty("line.separator");

	// get rid of spaces and capitalization
	word = word.trim().toLowerCase();
	int length = word.length();
	String response = "";
	
	// checks original form
	String value = map.get(word);
	if (value != null && !value.isEmpty())
		response += value + br;

	// checks for pluralizations
	if (length > 0 && word.charAt(length - 1) == 's')
	{
		value = map.get(word.substring(0,length-1));
		if (value != null && !value.isEmpty())
			response += value;
		
		if (length > 1 && word.charAt(length-2) == 'e')
		{
			value = map.get(word.substring(0, length-2));
			if (value != null && !value.isEmpty())
				response += value;
		}
	}

	if (!response.isEmpty())
	    return response;
	else
	    return "No synonyms found.";
    }
}
