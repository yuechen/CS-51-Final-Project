/**
 * Contains the Pair class.
 */

public class Pair<V1, V2>
{
    private V1 value1;
    private V2 value2;

    /**
     * Basic constructor
     * @param v1 first element of the pair
     * @param v2 second element of the pair
     */
    public Pair(V1 v1, V2 v2)
    {
    	value1 = v1;
		value2 = v2;	
    }
    /**
     * Returns the first element of the pair
     * @return the first element of the pair
     */ 
    public V1 get_first()
    {
		return value1;
    }

    /**
     * Returns the second element of the pair
     * @return the second element of the pair
     */ 
    public V2 get_second()
    {
		return value2;
    }
}
