package part2.simple_search;

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

import java.io.File;
import java.util.List;

/**
 * @author Stamatis Pitsios
 *
 * This class provides all the necessary functions , in order to create the indexes on medlars collection.
 */
public class SimpleLuceneIndexer
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
	 * Constructor.
	 * 
	 * @param docsPath The path of the medlars collection file.
	 * @param indexPath The folder that the indexes are going to be stored.
	 */
	public SimpleLuceneIndexer(String docsPath , String indexPath)
	{
		this.docsFile = docsPath;
		this.indexLocation = indexPath;
		this.reader = new MedlarsDocumentReader(this.docsFile);
	}
	
	
	
	
	/**
	 * Creates the Lucene indexes.
	 */
	public void createIndex()
	{
		//Get all the documents of the collection.
		List<MedlarsDocument> docs = reader.getDocuments();
		
		//Useful to count how much time did it take to create the index.
		long start = System.currentTimeMillis();
		
		try
		{
			System.out.println("Indexing to directory '" + this.indexLocation + "'. Please wait...");
			
			//Create the directory where the indexes will be stored.
			Directory dir = FSDirectory.open(new File(indexLocation));
	            
			//Define which analyzer to use for the normalization of documents.
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
            
            //close the writer stream.
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
     * indexes the Document with the IndexWriter.
	 * 
	 * @param indexWriter The indexWriter that will index Documents.
	 * @param document The document to be indexed.
	 */
	private void indexDocument(IndexWriter indexWriter , MedlarsDocument document)
	{
		try
		{
			//Make a new , empty document.
            Document doc = new Document();
            
            /*
             * Create the fields of the document and add them to the index.
             */
            
            //Add the id of the document.
            IntField docID = new IntField("id" , document.getId() , Field.Store.YES);
            doc.add(docID);
            
            //Add the words to the index.
            TextField docText = new TextField("text" , document.getText() , Field.Store.NO);
            doc.add(docText);
            
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