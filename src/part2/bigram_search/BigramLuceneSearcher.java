package part2.bigram_search;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import part2.medlars.MedlarsDocument;
import part2.medlars.MedlarsDocumentReader;
import part2.util.SimpleAnalyzer;


/**
 * @author Stamatis Pitsios
 *
 * This class provides the necessary functionality to query the Lucene
 * search engine.
 */
public class BigramLuceneSearcher
{	
	
	/**
	 * The location of the index.
	 */
	private String indexLocation;
		
	/**
	 * The queries that we will use for searching.
	 */
	private List<MedlarsDocument> queries;
	
	/**
	 * The reader that will read the queries from the file.
	 */
	private MedlarsDocumentReader reader;
	
	/**
	 * The path of the file where the results of the search will be kept.
	 */
	private static final String resultsPath = "medlars/RESULTS_BIGRAMS.txt";
	
	/**
	 * The number of the documents that we want to retrieve.
	 */
	private int numOfDocuments;
	
	/**
	 * A simple analyzer object , usefull to handle the documents.
	 */
	private SimpleAnalyzer simpleAnalyzer;
	
	/**
	 * The field that we will be searching.
	 */
	private String searchField;
	
	
	
	
		
	/**
	 * Constructor.
	 * 
	 * @param indexLocation The folder that keeps the indexes.
	 * @param searchField The field to search in.
	 * @param queriesPath The path to the file that contains the queries.
	 * @param numOfDocuments The number of the documents that we want to retrieve.
	 */
	public BigramLuceneSearcher(String indexLocation , String searchField , String queriesPath , int numOfDocuments)
	{
		this.indexLocation = (indexLocation);
		this.reader = new MedlarsDocumentReader(queriesPath);
		this.queries = reader.getDocuments();
		this.numOfDocuments = numOfDocuments;
		this.simpleAnalyzer = new SimpleAnalyzer();
		this.searchField = searchField;
	}
	
	
	
	
	/**
	 * Searches the index created with all the queries and saves the results to a file.
	 */
	public void search()
	{
		//Useful to count how much time did it take to get the answers for all the queries.
		long start = System.currentTimeMillis();
		
		//Add possible bigrams to the end of the associate query.
		this.boostQueries();
		
		try
		{
			System.out.println("Starting to query Lucene Engine...");
			
			//Access the index using indexReader
	        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(new File(this.indexLocation))); 
	        
	        //Create an IndexSearcher for searching in indexes.
	        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
	        
	        //Define which analyzer to use for the normalization of user's query.
            Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_4_9);
            
            //Create a query parser on the field "TEXT"
            QueryParser parser = new QueryParser(Version.LUCENE_4_9 , this.searchField , analyzer);
            QueryParser.escape(")");
            //A string that will hold the query.
            String q = "";
            
            //The query.
            Query query ;
            
            //A writer to write the results of the query to the file.
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(resultsPath)));
            
            //For each query , get the results and save them to a file.
            for(MedlarsDocument doc : queries)
            {
            	q = doc.getText();
            	query = parser.parse(q);
            	this.saveResults(bw, query, indexSearcher , doc);
            }
            
            //Close the writer.
            bw.close();
            
	        //Close indexReader.
            indexReader.close();
            
            //Print  the elapsed time.
            long end = System.currentTimeMillis();
            System.out.println("Time took to get the top " + this.numOfDocuments + " answers for all the queries is : " + (double)(end-start)/(double)1000 + " seconds.");
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Gets the results for the given query and saves them to a file in Trec Eval's format.
	 * 
	 * @param bw The writer.
	 * @param query The query.
	 * @param indexSearcher The IndexSearcher.
	 * @param doc The query as a MedlarsDocument object.
	 */
	private void saveResults(BufferedWriter bw , Query query , IndexSearcher indexSearcher , MedlarsDocument doc)
	{
		try
		{
			//Search the index using the indexSearcher.
	        TopDocs results = indexSearcher.search(query, this.numOfDocuments);
	        ScoreDoc[] hits = results.scoreDocs;
	        
	        //Save the results.
	        for(int i=0; i<hits.length; i++)
	        {
	            Document hitDoc = indexSearcher.doc(hits[i].doc);
	            bw.write(String.valueOf(doc.getId()) + " 0 " + String.valueOf(hitDoc.get("id")) + " " +String.valueOf(i+1)+ " " + String.valueOf(hits[i].score) + " STANDARD\n");
	        }
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * This function creates new queries by adding the bigrams to the old ones.
	 */
	private void boostQueries()
	{
		//A list that will temporalily keep the new queries.s
		List<MedlarsDocument> newQueries = new ArrayList<MedlarsDocument>();
		
		for(MedlarsDocument query : queries)
		{
			//The text of the new query. Initialy empty.
			String newQuery = "";
			
			//A String that will keep all the bigrams found in this document and that are also contained in the Bindex.
            String bigrams = "";  
			
			//The id of the new query. It will be the same as the associate old query's ID.
			int id = query.getId();
			
			//Get all the words of the document.
			List<String> words = this.simpleAnalyzer.wordsOfText(query.getText());
			
			//For all the words of the document.
			for(int i = 0; i < words.size(); i++)
            {     					
            	if(i < words.size() - 1)
            	{
            		//Get the current word.
            		String firstWord = words.get(i);
            		
            		//Get the next word.
            		String secondWord = words.get(i+1);
            		
            		//Create a bigram in the format that bigrams are contained in the Bindex.
            		String bigram = firstWord+"_"+secondWord;
            		
            		//If the bigram above is contained in the Bindex , then add it to the 'bigrams' string.
            		if(this.simpleAnalyzer.isInBindex(bigram))
            		{
            			bigrams += " "+firstWord+secondWord;
            		}
            	}
            }
			
			//Now create the final query , by adding the bigrams to the end of it.
            newQuery += query.getText() + " " + bigrams;
			
			//Add the new query to the list.
			newQueries.add(new MedlarsDocument(newQuery, id));
		}
		
		//Delete the previous queries.
		this.queries.clear();
		
		//Add the new ones.
		this.queries.addAll(newQueries);
	}
}