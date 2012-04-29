/**
 * An exception for when a part of speech is not found, such as when the user gives an  
 * incomplete list of parts of speech.
 */
class POSNotFoundException extends Exception
{
	public POSNotFoundException (String msg)
	{
		super(msg);
	}
}

/**
 * An exception for when a file given is in the incorrect format.
 */
class WrongFormatException extends Exception
{
	public WrongFormatException (String msg)
	{
		super(msg);
	}
}