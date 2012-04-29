import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;

import java.util.Arrays;
import java.util.Set;
import java.util.Iterator;

// add import statements as necessary

/*
 * Contains main method
 */
class POSTaggerApp {
    public static void main(String[] args) 
    {
    
	int length = args.length;

	// for intialization of GUI
	if (length == 0)
		System.out.println("GUI intialization goes here");

	// for testing
	else if (args[0].compareTo("test") == 0)
	{
		try
	    	{
			// initialize Viterbi and text parser
    			Viterbi v = new Viterbi("corpus_tagset.txt", "corpus_simple_tagset.txt", "datafile.txt");
			TextParser parser = new TextParser(v);
		
			// test
			test (v, args[0]);
	    	}
		catch(Exception E) {System.out.println(E.getMessage());}
	}
	
	// for viewing parts of speech associated with remaining test
	else if (args[0].compareTo("view") == 0)
	{try {
		// creates the sentence
		String sent = "";
		for (int i = 1; i < length; i++)
		{
			sent += args[i] + " ";
		}

		System.out.println("Input text:");
		System.out.println(sent);

		// initialization and parsing
		Viterbi v = new Viterbi("corpus_tagset.txt", "corpus_simple_tagset.txt", "datafile.txt");
		TextParser parser = new TextParser(v);
		ArrayList<ArrayList<Pair<String, POS>>> parsed = parser.parse(sent);
     		
		// printing out
		System.out.println("Parsed text:");
		for(int i = 0 ; i < parsed.size(); i++)
		{
			for(int j = 0; j < parsed.get(i).size(); j++)
			{
        			System.out.println(parsed.get(i).get(j).get_first() + " / " + parsed.get(i).get(j).get_second().getName());
			}	 		
		}
	}	
	catch(Exception E) {System.out.println(E.getMessage());}
	}
	
	else if (args[0].compareTo("compute") == 0)
	{
		if (length == 5)
		{
			try {Viterbi.loadCorpusForTraining(args[1], args[2], args[3], args[4]);}
			catch(Exception E) {System.out.println(E.getMessage());}
		}
		else
			System.out.println("Incorrect number of arguments.");
	}
    }
    
    /**
     * Tests the accuracy of the Viterbi algorithm against a pre-tagged data set
     * @param viterbi A viterbi object loaded with probabilities. 
     * @param directory The directory of the files to be tested, with pre-tagged parts of speech.
     */
    private static void test (Viterbi viterbi, String directory) throws FileNotFoundException, POSNotFoundException
    {
		// Find all corpus data files in directory
		File dir = new File(directory);
		File[] fl = dir.listFiles();

		// If none, error
		if (fl == null) {
		    System.out.println ("Directory not valid.");
		    System.exit(1);
		}
	
		Scanner scanner;
	
		for (int i = 30; i < fl.length; i++)
		    {
			scanner = null;
			int POSIndex = -1;
				    	
			try 
			    {
				scanner = new Scanner(new BufferedReader(new FileReader(fl[i])));
				String text = "";
		    
				ArrayList<Integer> PIndices = new ArrayList<Integer>();
				ArrayList<Pair<String, POS>> PIndices2 = new ArrayList<Pair<String, POS>>();
				ArrayList<ArrayList<String>> sentences = 
					new ArrayList<ArrayList<String>>();
        		ArrayList<String> sent = new ArrayList<String>();
        	
				// scan through file
				while (scanner.hasNext()) 
				    {
					String s = scanner.next();
					
					// figure out word/symbol combinations
					int lastIndex = s.lastIndexOf("/");
					String symbol = s.substring(lastIndex + 1).replaceAll(POS.getIgnoreRegex(), "");
					sent.add(" " + s.substring(0, lastIndex).toLowerCase());
					
					if (symbol.equals(".") || !scanner.hasNext()) {
						sentences.add(sent);
						sent = new ArrayList<String>();
					}
                	
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
			    
					PIndices.add(POSIndex);
				    }
				
				for (int j = 0; j < sentences.size(); j++) {
					PIndices2.addAll(viterbi.parse(sentences.get(j)));
				}
			
				int compared = 0;
				int correct = 0;
				
				for (int j = 0; j < PIndices.size(); j++) {
					//System.out.println (j + "\t" + PIndices.get(j) + "\t" +
						//PIndices2.get(j).get_second().getIndex());
				    if (PIndices.get(j) == PIndices2.get(j).get_second().getIndex())
				    	correct++;
				    
				    compared++;
				}
			
				System.out.println ("Correct: " + correct + "\t" +
					"Compared: " + compared);
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
   }
}
