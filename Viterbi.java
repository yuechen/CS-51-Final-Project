package tagger;

/**
 * As a class, <code>Viterbi</code> contains both the HMM model 
 * and the functions to run the Viterbi algorithm on it. 
 * <p>
 * The class can train itself from probabilities provided by
 * a dictionary, and then save and load the needed data into a 
 * file. 
 * <p>
 * The class is generic, with constructors accepting a list of 
 * states and a list of parts of speech, as well as a dictionary
 * to reference.
 */
public class Viterbi<S, O>
{
    private final O[] outputs;
    private final S[] states;
    private float[][] emission;
    private float[][] transmission;
    private final Dictionary dict;

    /**
     * Basic constructor
     * @param outputs a list of possible outputs of the HMM
     * @param states a list of possible states of the HMM
     * @param dict the reference dictionary
     */
    private Viterbi(O[] outputs, S[] states, Dictionary dict)
    {
	this.outputs = outputs;
	this.states = states;
	this.dict = dict;
    }

    /**
     * Constructor; initializes the probability table from the 
     * given saved training file.
     * @param outputs a list of possible outputs of the HMM
     * @param states a list of possible states of the HMM
     * @param dict the reference dictionary
     * @param file the saved file name of the data
     */
    public Viterbi(O[] outputs, S[] states, Dictionary dict,
		     String file)
    {
	this(outputs, states, dict);
	
	// initalizes the probability arrays
	this.emission = new float[outputs.size][states.size];
	this.transition = new float[states.size][states.size];

	// read file and load probabilities
    }
    
    /**
     * Trains the tagger and saves the training file.
     * @param saveLocation where the probabilities are to be saved
     * @param words list of words 
     * @param pos list of parts of speech
     * @param emission array of number of times each word appears
                       in the training data for each part of speech;
		       should be of size 
		       [number of words][number of parts of speech]
     * @param transmission array of number of times parts of speech
                       appear consecutively; <code>transmission[i][j]</code>
		       should indicate the number of times that 
		       <code>j</code> appeared immediately after
		       <code>i</code>; should be of size [number of 
		       parts of speech][number of parts of speech]
     * @return none
     */
    public static void train(String saveLocation, O[] words, 
			     S[] pos, int[][] emission, 
			     int[][] transmission)
    {
	
    }

    /**
     * Takes a set of outputs and determines the most likely original
     * state.
     * @param results list of outputs
     * @return list of (state, output) pairs
     */
    public List<Pair<O,S>> parse(List<S> results)
    {
    }

}