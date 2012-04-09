package tagger;

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
	* <code>sentlist</code> Linked List holds resulting list of sentences.
	*/
	private sentlist = new java.util.LinkedList();
	
	/**
	* Constructor initializes input variable to an empty string and
	* sentlist to null.
	*/
	public TextParser()
	{
	 	input = "";
		sentlist = null;
	}
	
	/**
	* Parses a block of text into pairs of Strings and parts of speech.
	* @param text input block of text
	* @return list of pairs of Strings and parts of speech
	*/
	public static List< Pair<String, POS> > parse(String text)
   {
		input = text
		String[] sentarray = input.split("[.!?]+\\s-(Mr//.|Mrs\\.|Ms\\.)")
   }

}