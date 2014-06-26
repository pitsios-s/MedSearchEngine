package part2.simple_search;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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



/**
 * @author Stamatis Pitsios
 *
 * This class provides the necessary functionality to query the Lucene
 * search engine.
 */
public class SimpleLuceneSearcher
{	
	
	/**
	 * The location of the index.
	 */
	private String indexLocation;
	
	/**
	 * The field that we will be searching.
	 */
	private String searchField;
	
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
	private static final String resultsPath = "medlars/RESULTS_SIMPLE.txt";
	
	/**
	 * The number of the documents that we want to retrieve.
	 */
	private int numOfDocuments;
	
	
	
	
	/**
	 * Constructor.
	 * 
	 * @param indexLocation The folder that keeps the indexes.
	 * @param searchField The field to search in.
	 * @param queriesPath The path to the file that contains the queries.
	 * @param numOfDocuments The number of the documents that we want to retrieve.
	 */
	public SimpleLuceneSearcher(String indexLocation , String searchField , String queriesPath , int numOfDocuments)
	{
		this.indexLocation = (indexLocation);
		this.searchField = searchField;
		this.reader = new MedlarsDocumentReader(queriesPath);
		this.queries = reader.getDocuments();
		this.numOfDocuments = numOfDocuments;
	}
	
	
	
	
	/**
	 * Searches the index created with all the queries and saves the results to a file.
	 */
	public void search()
	{
		//Useful to count how much time did it take to get the answers for all the queries.
		long start = System.currentTimeMillis();
		
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
            
            //Print the elapsed time.
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
}