package part2.simple_search;

public class SimpleSearch 
{
	public static void main(String[] args) 
	{
		//Create the index.
		SimpleLuceneIndexer indexer = new SimpleLuceneIndexer("medlars/MED.ALL", "index/index_simple");
		indexer.createIndex();
		
		//Make the queries to the Lucene.
		SimpleLuceneSearcher searcher = new SimpleLuceneSearcher("index/index_simple", "text" , "medlars/MED.QRY" , 100);
		searcher.search();
	}
}