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
    
	try
	    {
		//Viterbi.loadCorpusForTraining("corpus_tagset.txt", "corpus_simple_tagset.txt", "corpus", "datafile.txt");

    		Viterbi v = new Viterbi("corpus_tagset.txt", "corpus_simple_tagset.txt", "datafile.txt");
		
		TextParser parser = new TextParser(v);
		/*System.out.println("Hey guys, I'm going for a run.\nJenny is a wonderful person.");
		ArrayList<ArrayList<Pair<String, POS>>> parsed = parser.parse("Hey guys, I'm going for a run.\nJenny is a wonderful person.");
		System.out.println("Checkpoint1\n");
    		for(int i = 0 ; i < parsed.size(); i++)
		    {
			for(int j = 0; j < parsed.get(i).size(); j++)
			    {
        			System.out.println(parsed.get(i).get(j).get_first() + " / " + parsed.get(i).get(j).get_second().getName());
			    } 		
		    }*/
			
		test (parser);

		System.out.println ("Hello, World");
	    }
	catch(WrongFormatException E) {System.out.println(E.getMessage());}
	catch(POSNotFoundException E) {System.out.println(E.getMessage());}
	catch (FileNotFoundException E) {System.out.println(E.getMessage());}
    }
    
    private static void test (TextParser parser) throws FileNotFoundException
    {
		// Find all corpus data files in directory
		File dir = new File("corpus");
		File[] fl = dir.listFiles();
	
		// If none, error
		if (fl == null) {
		    System.out.println ("Directory not valid.");
		    System.exit(1);
		}
		
		Scanner scanner;
	
		for (int i = 0; i < 1; i++)
		    {
			scanner = null;
			int POSIndex = -1;
	    	
			try 
			    {
				scanner = new Scanner(new BufferedReader(new FileReader(fl[i])));
				String text = "";
		    
				ArrayList<Integer> PIndices = new ArrayList<Integer>();
        	
        	int sk = 0;
        	
				// scan through file
				while (scanner.hasNext()) 
				    {
					String s = scanner.next();
					         	
					// figure out word/symbol combinations
					int lastIndex = s.lastIndexOf("/");
					text = text + " " + s.substring(0, lastIndex).toLowerCase();
					String symbol = s.substring(lastIndex + 1).replaceAll(POS.getIgnoreRegex(), "");
                		
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
					sk++;
				    }

				System.out.println (text);
			
				ArrayList<ArrayList<Pair<String, POS>>> parsed = parser.parse(text);
			
				int compared = 0;
				int correct = 0;
				
				System.out.println ("WHEE:" + parsed.size());
				System.out.println ("WHEE1:" + parsed.get(0).size());
			
				for (int j = 0; j < parsed.size(); j++) {
				    for (int k = 0; k < parsed.get(j).size(); k++) {
					System.out.println(parsed.get(j).get(k).get_first().equals(""));
					/*if (parsed.get(j).get(k).get_second().getIndex() == PIndices.get(compared))
					    correct++;*/
					compared++;
					System.out.println(compared);
				    }
				}
			
				System.out.println ("Correct: " + correct + "\t" + "Compared: " + compared);
				System.out.println ("This sucks: " + PIndices.size() + " " + sk);
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