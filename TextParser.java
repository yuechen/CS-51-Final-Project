import java.util.*;

/**
 * Contains the text parser.
 * <br> Splits a String block of text into a linked list of sentences
 * in which each element is a linked list of the words in that sentence.
 */
public class TextParser
{
	/**
	* <code>input</code> String holds initial block of text.
	*/
	private static String input;
	
	private Viterbi viterbi;
		
	/**
	* <code>sentlist</code> Linked List holds resulting list of sentences.
	*/

	private static ArrayList<ArrayList<Pair<String, POS>>> sentlist = new ArrayList<ArrayList<Pair<String, POS>>>();
	
	/**
	* Constructor initializes input variable to an empty string and
	* sentlist to null.
	*/
	public TextParser(Viterbi v)
	{
	 	input = "";
		sentlist = null;
		viterbi = v;
	}
		
	/**
	* Parses a block of text into pairs of Strings and parts of speech.
	* @param text input block of text
	* @return list of pairs of Strings and parts of speech
	*/
	public static ArrayList<ArrayList<Pair<String, POS>>> parse(String text)
   {
		input = text;

		String sentarray[] =
			input.split("((?<=([.!?]+\\s-(Mr\\.|Mrs\\.|Ms\\.)))|(?=([.!?]+\\s-(Mr\\.|Mrs\\.|Ms\\.))))");
		int sents = sentarray.length;
		
		String wordarray[][] = new String[sents][];
		for (int i = 0; i < sents; i++)
		{
			wordarray[i] = sentarray[i].split("((?<=(\\p{Punct}-('-)))|((?=(\\p{Punct}-('-))))");
			
			ArrayList<String> wordlist = new ArrayList<String>();
			for (int j = 0; j < wordarray[i].length; j++)
			{
				wordlist.add(wordarray[i][j]);
			}
			sentlist.add(Viterbi(wordlist));
		}
		return sentlist;
   }
}
