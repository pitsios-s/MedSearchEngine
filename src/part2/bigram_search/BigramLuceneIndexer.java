package part2.bigram_search;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import part2.medlars.MedlarsDocument;
import part2.medlars.MedlarsDocumentReader;
import part2.util.SimpleAnalyzer;

import java.io.File;
import java.util.List;


/**
 * @author Stamatis Pitsios
 *
 * Thic class provides the necessary functionality , to create indexes 
 * based on simple words and bigrams that were found during the first part of the project.
 */
public class BigramLuceneIndexer 
{

	/**
	 * The path of the file that contains the medlars collection.
	 */
	private String docsFile;
	
	/**
	 * The name of the directory that Lucene is going to store the indexes.
	 */
	private String indexLocation;
	
	/**
	 * Our reader.
	 */
	private MedlarsDocumentReader reader;
	
	/**
	 * A simple analyzer object , usefull to handle the documents.
	 */
	private SimpleAnalyzer simpleAnalyzer;
	
    
	
	
	/**
	 * Constructor.
	 * 
	 * @param docsPath The path of the medlars collection file.
	 * @param indexPath The folder that the indexes are going to be stored.
	 */
	public BigramLuceneIndexer(String docsPath , String indexPath)
	{
		this.docsFile = docsPath;
		this.indexLocation = indexPath;
		this.reader = new MedlarsDocumentReader(this.docsFile);
		this.simpleAnalyzer = new SimpleAnalyzer();
	}
	
	
	
	
	/**
	 * Creates the Lucene indexes.
	 */
	public void createIndex()
	{
		//Get all the documents of the collection.
		List<MedlarsDocument> docs = reader.getDocuments();
		
		//Useful to count how much time did it take to get the answers for all the queries.
		long start = System.currentTimeMillis();
		
		try
		{
			System.out.println("Indexing to directory '" + this.indexLocation + "'. Please wait...");
			
			//Create the directory where the indexes will be stored.
			Directory dir = FSDirectory.open(new File(indexLocation));
	            
			//Define which analyzer to use for the normalization of documents.
	        //Analyzer analyzer = new NGramAnalyzer(this.stopwords);
			Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_4_9);
			
	        //Configure IndexWriter.
	        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_9 , analyzer);
	        
	        //Create a new index in the directory, removing any previously indexed documents:
            iwc.setOpenMode(OpenMode.CREATE);
            
            //Create the IndexWriter with the configuration as above. 
            IndexWriter indexWriter = new IndexWriter(dir, iwc);
            
            //Index every document.
            for(MedlarsDocument doc : docs)
            {
            	this.indexDocument(indexWriter, doc);
            }
            
            //Close the output stream.
            indexWriter.close();
            
            //Print  the elapsed time.
            long end = System.currentTimeMillis();
            System.out.println("Time took to create index : " + (double)(end-start)/(double)1000 + " seconds.\n");
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Creates a Document by adding Fields in it and 
     * indexes the Document with the IndexWriter
	 * 
	 * @param indexWriter The indexWriter that will index Documents
	 * @param document The document to be indexed
	 */
	private void indexDocument(IndexWriter indexWriter , MedlarsDocument document)
	{
		try
		{
			//Make a new , empty document.
            Document doc = new Document();
            
            //Get all the words of the current med document.
            List<String> words = this.simpleAnalyzer.wordsOfText(document.getText());
            
            /*
             * Create the fields of the document and add them to the index.
             */
            
            //Add the id of the document.
            IntField docID = new IntField("id" , document.getId() , Field.Store.YES);
            doc.add(docID);
                        
            //A String that will keep all the bigrams found in this document and that are also contained in the Bindex.
            String bigrams = "";           
            
            //now try to add possible bigrams to the index.
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
            
            //Create the final text by adding the normal text and then the bigrams to end of it.
            String finalText = document.getText() + " " + bigrams;
            TextField fText = new TextField("text" , finalText , Field.Store.NO);
            doc.add(fText);
            
            if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE) 
            {
                //New index, so we just add the document (no old document can be there).
                indexWriter.addDocument(doc);
            } 
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}