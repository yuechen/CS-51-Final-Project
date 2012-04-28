import java.util.ArrayList;

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
			System.out.println("Hey guys, I'm going for a run.\nJenny is a wonderful person.");
		   ArrayList<ArrayList<Pair<String, POS>>> parsed = parser.parse("Hey guys, I'm going for a run.\nJenny is a wonderful person.");
			System.out.println("Checkpoint1\n");
    		for(int i = 0 ; i < parsed.size(); i++)
    		{
				for(int j = 0; j < parsed.get(i).size(); j++)
				{
        			System.out.println(parsed.get(i).get(j).get_first() + " / " + parsed.get(i).get(j).get_second().getName());
   			} 		
			}
		}
		catch(Exception E) {System.out.println("Error");}
    }
}