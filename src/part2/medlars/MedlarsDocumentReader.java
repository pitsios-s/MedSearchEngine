package part2.medlars;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Stamatis Pitsios
 *
 * This class provides the necessary methods to read all the documents or queries of medlars collection.
 */
public class MedlarsDocumentReader 
{
	
	/**
	 * The name of the file that contains medlars collection.
	 */
	private String fileName;
	
	
	
	
	/**
	 * Constructor 
	 * 
	 * @param path The path of the file that contains the medlars documents.
	 */
	public MedlarsDocumentReader(String path)
	{
		this.fileName = path;
	}
	
	
	
	
	/**
	 * Finds and returns a list with all the documents of medlars collection.
	 * 
	 * @return docs A list with all the MedlarsDocument objects.
	 */
	public List<MedlarsDocument> getDocuments()
	{
		List<MedlarsDocument> docs = new ArrayList<MedlarsDocument>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(this.fileName)));
			
			//A line read from the file.
			String line = null;
			
			//The text of a document.
			String docText = "";
			
			//The id of a document.
			int docID = -1;
			
			//Indicates if we are parsing through the text of a document.
			boolean parsingText = false;
			
			while( (line = br.readLine()) != null  )
			{
				//Remove all white space characters from line.
				String trimmed = line.trim();
				
				//If it is an empty line , continue.
				if(trimmed.equals("")) continue;
				
				//Else, if line indicates the begin of a new document.
				else if(trimmed.startsWith(".I"))
				{
					if(parsingText)
					{
						docs.add(new MedlarsDocument(docText, docID));
						parsingText = false;
						docID = -1;
						docText = "";
					}
					
					docID = Integer.parseInt(trimmed.substring(2).trim());
				}
				
				//Else, if the line indicates the begin of the text , change the associate flag. 
				else if(trimmed.startsWith(".W"))
				{
					parsingText = true;
				}
				
				else
				{
					if(parsingText)
					{
						docText += line+"\n";
					}
				}
			}
			
			br.close();
			
			if(parsingText)
			{
				docs.add(new MedlarsDocument(docText, docID));
				parsingText = false;
				docID = -1;
				docText = "";
			}
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return docs;
	}
}