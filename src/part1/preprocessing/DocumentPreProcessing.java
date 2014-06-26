package part1.preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import part1.xmlparsing.Bigram;
import part1.stemming.PorterStemmer;


/**
 * @author Stamatis Pitsios
 */
public class DocumentPreProcessing 
{

	/**
	 * A set that will hold all the stop words.
	 */
	private Set<String> stopwords;
	
	
	/**
	 * A set that will contain all the bigrams that were
	 */
	private Set<Bigram> BINDEX;
	
	
    /** 
     * Porter Stemmer object. 
     */
    private PorterStemmer stemmer = new PorterStemmer();
    
    
    
    
    /**
     * Constructor.
     */
    public DocumentPreProcessing() 
    {
        this.stopwords = new HashSet<String>();
        this.BINDEX = new HashSet<Bigram>();
        this.fillStopwordsList();
    }
    
    
    
    
    /**
     * This functions get as parameter a text that was found in xml file and returns a set of all the bigrams that were found in it.
     * 
     * @param text The text to be processed in order to get the bigrams.
     * 
     * @return A set of all the bigrams that were found in the text.
     */
    public Set<Bigram> getBigrams(String text)
    {
    	this.BINDEX.clear();
    	
    	this.getAnchoredElements(text);
    	
    	return this.BINDEX;
    }
    
    
     
    
    /**
     * Search all the text to find the anchored elements.
     * 
     * @param unprocessedElements All the text that was found in the <Text>...</Text> tags.
     */
    private void getAnchoredElements(String text)
    {
		int start = text.indexOf("[[");
			
		while(start > 0)
		{
			int end = text.indexOf("]]" , start);
				
			if(start < 0 || end < 0) continue;
			
			int start2 = text.indexOf("[[" , start+2);
			
			if(start2 < end && start2 > 0)
			{
				int end2 = text.indexOf("]]", end+2);

				String content1 = text.substring(start2 , end+2);
				this.processAnchoredElement(content1);
				
				String content2 = text.replace(content1 , "").substring(start , end2+2-content1.length());
				this.processAnchoredElement(content2);
				
				start = text.indexOf("[[" , end2);
			}
			
			else
			{
				String content = text.substring(start , end+2);

				this.processAnchoredElement(content);
				
				start = text.indexOf("[[" , end);
			}
		}	
    }
    
    
    
    
    /**
     * This function gets all the elements that are contained in the [[ ]] , process them and returns a set with the contents of the square brackets.
     * 
     * @param unprocessedElements a list that contains anchored elements , e.g. [[Category:Climate forcing]]
     */
    private void processAnchoredElement(String element)
    {
    	Set<String> processedElements = new HashSet<String>();
    	
    	//If the elements contains a tag that we don't want , exit.
    	if(!(this.isSuitableElement(element))) return;
    	
    	String cleanElement = this.removeSuitableTags(element);
    			
    	//If the anchored text contains the or operator "|"...
    	if(cleanElement.contains("|"))
    	{
    		StringTokenizer tok = new StringTokenizer(cleanElement , "|");
    			
    		//get all the different names of this link.
    		while(tok.hasMoreTokens())
    		{
    			processedElements.add(tok.nextToken());
    		}
    	}
    		
    	else
    	{
    		processedElements.add(cleanElement);
    	}	
    	
    	this.preprocess(processedElements);
    }
    
    
    
    
    /**
     * This function gets a string as parameter and tells us if the string contains some tags we do not want or not.
     * 
     * @param element The string that we want to test for the existence of not valid tags.
     * 
     * @return true if the element does not contain invalid tags , false otherwise.
     */
    private boolean isSuitableElement(String element)
    {
    	if( element.trim().contains("http://") |
    		element.trim().contains("File:")|
    		element.trim().contains("s:")|
    		element.trim().contains("Image:")|
    		element.trim().contains("WP:")|
    		element.trim().contains("MOS:")|
    		element.trim().contains("Template:")|
    		element.trim().contains("Wikipedia:")|
    		element.trim().startsWith(":")) return false;
    	
    	return true;
    }
    
    
    
    
    /**
     * This method gets as input a string and removes some tags that are valid.
     * 
     *  @param element The string to remove the valid tags.
     *  
     *  @return cleanElement The string after the removal of tags.
     */
    private String removeSuitableTags(String element)
    {
    	String cleanElement = element;
    	
    	//remove square brcleancleanElementckets.
    	if(cleanElement.contains("[[")) cleanElement = cleanElement.replace("[[", "");
    	if(cleanElement.contains("]]")) cleanElement = cleanElement.replace("]]", "");
    		
    	//remove "Category:" tag.
    	if(cleanElement.trim().startsWith("Category:")) cleanElement = cleanElement.replace("Category:" , "");
    	
    	//remove "Book:" tag.
    	if(cleanElement.contains("Book:")) cleanElement = cleanElement.replace("Book:" , "");
    	
    	//remove "wikt:" tag.
    	if(cleanElement.contains("wikt:")) cleanElement = cleanElement.replace("wikt:" , "");
    	
    	//remove "w:" tag.
    	if(cleanElement.contains("w:")) cleanElement = cleanElement.replace("w:" , "");
    	
    	//remove "de:" tag.
    	if(cleanElement.contains("de:")) cleanElement = cleanElement.replace("de:" , "");
    	
    	return cleanElement;
    }
    
    
    
    
    /**
     * Removes stopwords and applies stemming in the list of given anchored elements.
     * 
     * @param unprocessed A list elements to apply preprocessing
     */
    private void preprocess(Set<String> unprocessed) 
    {
        Set<String> preprocessed_elements = new HashSet<String>();
        
        for(String phrase : unprocessed)
        {
        	 StringTokenizer st = new StringTokenizer(phrase , "[- :]+");
        	 
        	 String preprocessed_term = new String("");
        	 
             while (st.hasMoreTokens())
             {
                 String word = st.nextToken();
                 String lowercase_word = word.toLowerCase();
                 
                 if (!stopwords.contains(lowercase_word)) 
                 {
                     String stemmed_word = stem(lowercase_word);
                     preprocessed_term = preprocessed_term.concat(" ");
                     preprocessed_term = preprocessed_term.concat(stemmed_word);
                 }
             }
             
             if(!preprocessed_term.trim().equals("")) preprocessed_elements.add(preprocessed_term);
        }
        
        this.addBigram(preprocessed_elements);
    }
    
    
    
    
    /**
     * This function add to the Bindex all the bigrams that were found in the current text.
     * 
     * @param preprocesseditems A list of the preprocessed items.
     */
    private void addBigram(Set<String> preprocesseditems)
    {    	
    	for(String item : preprocesseditems)
    	{
    		StringTokenizer tok = new StringTokenizer(item);
    		
    		if(tok.countTokens() == 2) this.BINDEX.add(new Bigram(tok.nextToken(), tok.nextToken()));
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
}