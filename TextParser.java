import java.util.regex.Pattern;
import java.util.ArrayList;
import java.lang.*;

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
	private String input;
	
	/**
	* <code>viterbi</code> initializes the Viterbi object.
	*/
	private Viterbi viterbi;
	
	/**
	* Constructor initializes input variable to an empty string, 
	* sentlist to null, and viterbi to the Viterbi object parameter.
	*/
	public TextParser(Viterbi v)
	{
	 	input = "";
		viterbi = v;
	}
		
	/**
	* Parses a block of text into pairs of Strings and parts of speech.
	* @param text input block of text
	* @return list of pairs of Strings and parts of speech
	*/
	public ArrayList<ArrayList<Pair<String, POS>>> parse(String text)
   {
		/*
		* sentlist ArrayList holds resulting list of sentences,
		* which are ArrayLists of words.
		*/
		ArrayList<ArrayList<Pair<String, POS>>> sentlist =
			new ArrayList<ArrayList<Pair<String, POS>>>();
	
		//add whitespace to the end so last punctuation mark is not neglected
		input = text + " ";
		
		/*
		* Split input by ". ", "! ", and "? ". Does not split when punctuation
		* is not followed by space to avoid certain abbreviations.
		* Keeps the punctuation as separate element in array.
		*/
		String sentarray[] =
			input.split("((?<=([.!?]+\\s))|(?=([.!?]+\\s)))");
		int sents = sentarray.length / 2;
		
		//combine punctuation with sentences for an array of sentences with punctuation
		String apsentarray[] = new String[sents];
		for (int i = 0; i < sents; i++)
		{
			apsentarray[i] = sentarray[2 * i] + sentarray[2 * i + 1];
		}
		
		//split sentences into words by space and punctuation, add to sentlist ArrayList
		String wordarray[][] = new String[sents][];
		for (int i = 0; i < sents; i++)
		{
			//split into words and punctuation, keeping punctuation as elements in array
			wordarray[i] =
				apsentarray[i].split("((?<=(([.?!,:;\"]|\\s+)))|(?=(([.?!,:;\"]|\\s+))))");
			
			//retains all white space by concatenating it to the beginning of the next word.
			for(int j = 0; j < wordarray[i].length; j++)
			{
				if (Pattern.matches(wordarray[i][j], "\\s+") && j < wordarray[i].length - 1)
				{
					String s = wordarray[i][j];
					wordarray[i][j+1] = s + wordarray[i][j+1];
				}
				
				//if the whitespace is at the end of the sentence, concats to end of last word.
				else if (Pattern.matches(wordarray[i][j], "\\s"))
				{
					String s = wordarray[i][j];
					wordarray[i][j-1] = wordarray[i][j-1] + s;
				}
			}
			
			//add words to wordlist ArrayList,
			ArrayList<String> wordlist = new ArrayList<String>();
			for (int j = 0; j < wordarray[i].length; j++)
			{
				//skips over white space elements since already accounted for
				if (Pattern.matches(wordarray[i][j], "\\s"))
					continue;
				else
					wordlist.add(wordarray[i][j]);
			}
		   
			//add wordlist Arraylist as a sentence element to sentlist ArrayList
			sentlist.add(viterbi.parse(wordlist));
		}
		return sentlist;
   }
}
