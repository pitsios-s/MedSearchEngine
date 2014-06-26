package part1.xmlparsing;

import part1.preprocessing.DocumentPreProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Stamatis Pitsios
 */
public class MyParser 
{

	/**
	 * The path of the file that we want to parse.
	 */
	private String path;
	
	/**
	 * In Set list we are going to keep the WHOLE text that was found in the <TEXT> ... </TEXT> tag.
	 */
	private Set<Bigram> BINDEX;
	
	/**
	 * The string that will hold the text.
	 */
	private String Text;
				
	/**
	 * An object that will provide us with some methods to preprocess the anchored text and keep the bigrams.
	 */
	private DocumentPreProcessing preprocessing;
		
	
	
	
	public MyParser(String path)
	{
		this.path = path;
		this.BINDEX = new HashSet<Bigram>();
		this.Text = "";
		this.preprocessing = new DocumentPreProcessing();
	}
	
	
	
	
	public void parse()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		
			String line;
			line = br.readLine();
			
			while(line != null)
			{
				//If we are parsing through a <TEXT>...</TEXT> tag...
				if(line.equals("<TEXT>"))
				{
					line = br.readLine();
				
					while(!line.equals("</TEXT>"))
					{
						Text += line+"\n";
						line = br.readLine();
					}
					
					this.addNewBigrams(Text);
					Text = "";
				}
				line = null;
				line = br.readLine();
			}
			
			br.close();
		}
		
		catch(Exception e)
        {
        	System.err.println("Exception occured : " + e.getMessage());
        }
	}
	
	
	
	
	/**
	 * @return The list that contains all the bigrams that were found.
	 */
	public Set<Bigram> getBigrams()
	{
		return this.BINDEX;
	}
	
	
	
	
	/**
	 * This method takes as argument a text that was found in the <TEXT>...</TEXT> tags and retreives all the bigrams that were found , if any.
	 * 
	 * @param text The text that we want to find bigrams in.
	 */
	private void addNewBigrams(String text)
	{
		BINDEX.addAll(preprocessing.getBigrams(text));
	}
}