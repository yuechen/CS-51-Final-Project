package tagger;

/*
 * Contains the HMM and Viterbi.
 */

public class Viterbi
{
    /*
     * Constructor
     */
    public Viterbi(String file)
    {
    }

    /* 
     * Initializes the object from the given training file.
     * file: file from training
     */
    public void init(String file)
    {
    }
    
    /*
     * Trains the tagger and saves the training file
     * POSFile -- the file with a list of parts of speech
     * inputFile -- the folder/file for training
     * return: none
     */
    public static void train(String POSFile, String inputFile)
    {
	
    }

    /*
     * Takes a sentence and determines the parts of speech.
     * sentence: the sentence to be tagged
     * return: List of <String, POS> pairs
     */
    public List< Pair<String, POS> >parse (String sentence)
    {
    }

}