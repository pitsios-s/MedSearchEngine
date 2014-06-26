package part2.bigrams;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import part2.medlars.MedlarsDocument;
import part2.medlars.MedlarsDocumentReader;
import part2.util.SimpleAnalyzer;

/**
 * @author Stamatis Pitsios
 *
 * This class contains the necessary methods in order to provide some
 * useful statistics about the bigrams that were found in the documents
 * and in the queries of the MEDLARS collection.
 */
public class BigramCounter
{
	
	/**
	 * A reader for the documents.
	 */
	private MedlarsDocumentReader docReader;
	
	/**
	 * A reader for the queries.
	 */
	private MedlarsDocumentReader queryReader;
	
	/**
	 * An object for handling the documents and the queries.
	 */
	private SimpleAnalyzer analyzer;
	
	/**
	 * The total number of bigrams that were found in med documents.
	 */
	private int bigramsInDocuments;
	
	/**
	 * The total number of bigrams that were found in med queries.
	 */
	private int bigramsInQueries;
	
	/**
	 * The number of common bigrams between documents and queries.
	 */
	private int commonBigrams;
	
	/**
	 * A set that will keep the bigrams found in the med documents.
	 */
	private Set<String> docBigrams;
	
	/**
	 * A set that will keep the bigrams found in the med queries.
	 */
	private Set<String> queryBigrams;
	
	
	
	
	/**
	 * Constructor.
	 * 
	 * @param docsPath The path to the med documents.
	 * @param queriesPath The path to the med queries.
	 */
	public BigramCounter(String docsPath , String queriesPath)
	{
		this.docReader = new MedlarsDocumentReader(docsPath);
		this.queryReader = new MedlarsDocumentReader(queriesPath);
		this.analyzer = new SimpleAnalyzer();
		this.bigramsInDocuments = 0;
		this.bigramsInQueries = 0;
		this.commonBigrams++;
		this.docBigrams = new HashSet<String>();
		this.queryBigrams = new HashSet<String>();
	}
	
	
	
	
	/**
	 * This functions displays the following things :
	 * 
	 * 1. The total number of bigrams in med documents.
	 * 2. The total number of bigrams in med queries.
	 * 3. The number of unique bigrams in med docs.
	 * 4. The number of unique bigrams in med queries.
	 * 5. The number of query bigrams that also appear in the documents.
	 */
	public void displayStatistics()
	{
		long start = System.currentTimeMillis();
		
		System.out.println("Starting to compute statistics. Please wait...\n");
		System.out.println("--------------------------------------------------");
		
		this.findDocumentBigrams();
		this.findQueryBigrams();
		
		System.out.println("1. Total bigrams in documents = " + this.bigramsInDocuments);
		System.out.println("2. Unique bigrams in documents = " + this.docBigrams.size());
		System.out.println("3. Total bigrams in queries = " + this.bigramsInQueries);
		System.out.println("4. Unique bigrams in queries = " + this.queryBigrams.size());
		System.out.println("5. Common bigrams = " + this.commonBigrams);
		System.out.println("--------------------------------------------------");
		
		long end = System.currentTimeMillis();
		System.out.println("Total execution time : " + (double)(end-start)/(double)1000 + " seconds.\n");
	}
	
	
	
	
	/**
	 * This function finds the bigrams in the med documents that are also contained in our BINDEX.
	 */
	private void findDocumentBigrams()
	{
		//Get all the documents.
		List<MedlarsDocument> docs = this.docReader.getDocuments(); 
		
		//For each document.
		for(MedlarsDocument doc : docs)
		{
			//Get all the words of the document.
			List<String> words = this.analyzer.wordsOfText(doc.getText());
			
			//For all the words of the document.
			for(int i = 0; i < words.size(); i++)
            {            	
            	if(i < words.size() - 1)
            	{
            		//Create a bigram.
            		String bigram = words.get(i)+"_"+words.get(i+1);
            		
            		//If the bigram is contained in the BINDEX...
            		if(this.analyzer.isInBindex(bigram))
            		{
            			//Increase the number of bigrams.
            			this.bigramsInDocuments++;
            			
            			//Add the bigram to the set.
            			this.docBigrams.add(bigram);
            		}
            	}
            }
		}
	}
	
	
	
	
	/**
	 * This function finds the bigrams in the med queries that are also contained in our BINDEX.
	 */
	private void findQueryBigrams()
	{
		//Get all the documents.
		List<MedlarsDocument> queries = this.queryReader.getDocuments(); 
		
		//For each document.
		for(MedlarsDocument query : queries)
		{
			//Get all the words of the document.
			List<String> words = this.analyzer.wordsOfText(query.getText());
			
			//For all the words of the document.
			for(int i = 0; i < words.size(); i++)
            {            	
            	if(i < words.size() - 1)
            	{
            		//Create a bigram.
            		String bigram = words.get(i)+"_"+words.get(i+1);
            		
            		//If the bigram is contained in the BINDEX...
            		if(this.analyzer.isInBindex(bigram))
            		{
            			//Increase the number of bigrams.
            			this.bigramsInQueries++;
            			
            			//Add the bigram to the set.
            			this.queryBigrams.add(bigram);
            			
            			//If the bigram is also contained in the documents , then increase the number of common bigrams.
            			if(this.docBigrams.contains(bigram)) this.commonBigrams++;
            		}
            	}
            }
		}
	}
}