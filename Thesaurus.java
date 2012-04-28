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
	// opens scanner
	String br = System.getProperty("line.separator");
	Scanner sc = new Scanner(new BufferedReader(new FileReader(file)));

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
				line += br + tmp;
		}

		// adds the pair to the map
		map.put(word, line);
	    }

	System.out.println("done");

    }

    /** Looks up the synonyms for a given word
     * @param word word to be looked up
     * @return list of synonyms, linebreak-separated. If none, returns an empty string.
     */
    public String lookup(String word)
    {
	String value = map.get(word);
	if (value != null)
	    return value;
	else
	    return "";
    }
}
