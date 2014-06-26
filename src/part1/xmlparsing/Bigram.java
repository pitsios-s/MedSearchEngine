package part1.xmlparsing;

/**
 * @author Stamatis Pitsios
 *
 * Every instance of this class represents a bigram , i.e two words that were found
 * together in the collection of xml files. 
 */
public class Bigram 
{
	/**
	 * The first word.
	 */
	private String wordOne;
	
	/**
	 * The second word.
	 */
	private String wordTwo;
	
	
	
	
	/**
	 * Default constructor.
	 */
	public Bigram()
	{
		this("" , "");
	}
	
	
	
	
	/**
	 * Constructor with one argument.
	 * 
	 * @param word one of the words of the bigram.
	 * 
	 * The second word will be the empty string.
	 */
	public Bigram(String word)
	{
		this(word , "");
	}
	
	
	
	
	/**
	 * Two argument constructor.
	 * 
	 * @param word1 The first word the bigram.
	 * @param word2 The second word of the bigram.
	 */
	public Bigram(String word1 , String word2)
	{
		this.wordOne = word1;
		this.wordTwo = word2;
	}
	

	
	
	public String getWordOne() 
	{
		return wordOne;
	}
	
	
	
	
	public void setWordOne(String wordOne)
	{
		this.wordOne = wordOne;
	}
	
	
	
	
	public String getWordTwo() 
	{
		return wordTwo;
	}
	
	
	
	
	public void setWordTwo(String wordTwo)
	{
		this.wordTwo = wordTwo;
	}
	
	
	
	
	@Override
	public String toString()
	{
		return this.wordOne+"_"+this.wordTwo;
	}
	
	
	
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null || this == null) return false;
		
		if(obj == this) return true;
		
		if(!(obj instanceof Bigram)) return false;
		
		Bigram bigram = (Bigram)obj;
		
		if(bigram.wordOne.equals(this.wordOne) && bigram.wordTwo.equals(this.wordTwo)) return true;
		
		return false;
	}
	
	
	
	
	@Override
	public int hashCode()
	{
		return (41*(41 + wordOne.hashCode()) + wordTwo.hashCode() );
	}
}