package part1.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Set;

import part1.xmlparsing.Bigram;
import part1.xmlparsing.MyParser;


/**
 * @author Stamatis Pitsios
 */
public class Main 
{
	public static void main(String[] args)
	{
		try
		{	
			long start = System.currentTimeMillis();
			
			MyParser parser = new MyParser("docs/wiki_med_articles.xml");
			
			System.out.println("Started parsing the xml files. Please be patient...");
			
			parser.parse();
			
			Set<Bigram> BINDEX = parser.getBigrams();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("bindex/BINDEX.txt")));
			
			for(Bigram bigram : BINDEX)
			{
				bw.write(bigram+"\n");
			}
			
			bw.close();
			
			System.out.println("OUTPUT: BINDEX.TXT ,\n"+BINDEX.size()+" bigrams found.");
			
			long end = System.currentTimeMillis();
			
			System.out.println("execution time = " + (double)(end-start)/(double)1000 + " seconds.");
		}
		
		catch(Exception e)
        {
        	System.err.println("Exception occured : " + e.getMessage());
        }
	}
}