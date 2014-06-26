package part2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import part1.stemming.PorterStemmer;





/**
 * @author Stamatis Pitsios
 *
 * This class is used to provide some functions to analyze our documents.
 * It will be used for the second part to add the bigrams to index.
 */
public class SimpleAnalyzer 
{

	/**
	 * A set that will hold all the stop words.
	 */
	private Set<String> stopwords;
	
	/**
	 * The bindex that contain wikipedia's bigrams.
	 */
	private Set<String> bindex;
	
	/** 
     * Porter Stemmer object. 
     */
    private PorterStemmer stemmer;
    
    
    
    
    /**
     * Constructor.
     */
    public SimpleAnalyzer() 
    {
		
		this.stopwords = new HashSet<String>();
		this.fillStopwordsList();
		
		this.bindex = new HashSet<String>();
		this.loadBINDEX();
		
		this.stemmer = new PorterStemmer();
	}
    
    
    
    
    /**
	 * This functions gets as input a text and returns a list with all
	 * the words that were found in it , after applying stemming and 
	 * stop words removal. This will be useful for adding single words
	 * and bigrams in the index.
	 * 
	 * @param text The text to be tokenized.
	 * 
	 * @return words A set of the words of the text , stemmed and with removed stopwords.
	 */
	public List<String> wordsOfText(String text)
	{
		List<String> words = new ArrayList<String>();
		
		StringTokenizer tok = new StringTokenizer(text , "[- :]+");
		
		while(tok.hasMoreTokens())
		{
			String token = tok.nextToken();
			
			if(stopwords.contains(token.trim())) continue;
			
			String stemmed = this.stem(token);
			
			words.add(stemmed);
		}
		
		return words;
	}
	
	
	
	
	/**
	 * Given a String , this method tells us whether it is contained in our BINDEX or not.
	 * 
	 * @param bigram The bigram that we want to check if it is in our BINDEX.
	 * 
	 * @return true If the bigram is contained in the BINDEX , false otherwise.
	 */
	public boolean isInBindex(String bigram)
	{
		return this.bindex.contains(bigram);
	}
	
	
	
	
	/**
     * Opens the stopwords file and adds that words to the set.
     */ 
    private void fillStopwordsList() 
    {
        try
        {
        	BufferedReader br = new BufferedReader(new FileReader(new File("stopwords/stopwords.txt")));
        	
        	String word = br.readLine();
        	
        	while(word != null)
        	{
        		word.trim();
        		if(!word.equals("")) this.stopwords.add(word);
        		word = br.readLine();
        	}
        	
        	br.close();
        }
        
        catch(Exception e)
        {
        	System.err.println("Exception occured : " + e.getMessage());
        }
    }
    
    
    
    
    /**
     * Reads the bigrams from the file.
     */
    private void loadBINDEX()
    {
    	 try
         {
         	BufferedReader br = new BufferedReader(new FileReader(new File("bindex/BINDEX.txt")));
         	
         	String word = br.readLine();
         	
         	while(word != null)
         	{
         		word.trim();
         		if(!word.equals("")) this.bindex.add(word);
         		word = br.readLine();
         	}
         	
         	br.close();
         }
         
         catch(Exception e)
         {
         	System.err.println("Exception occured : " + e.getMessage());
         }
    }
    
    
    
    
    /**
     * Stems a word.
     * 
     * @param word-the word to stem
     * 
     * @returns String stemmed_word-the stemmed word
     */    
    private String stem(String word)
    {
        char[] w = word.toCharArray();
        
        for (int c = 0; c < w.length; c++)
        {
            Character ch = w[c];
            
            Character.toLowerCase(ch);
            
            if (Character.isLetter(ch) || Character.isDigit(ch)) 
            {
                stemmer.add(ch);
            }
        }
        stemmer.stem();
        String stemmed_word = stemmer.toString();
        return stemmed_word;
    }
}