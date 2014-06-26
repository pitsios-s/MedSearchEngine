package part2.bigram_search;

public class BigramSearch 
{
	public static void main(String[] args) 
	{
		//Create the index.
		BigramLuceneIndexer indexer = new BigramLuceneIndexer("medlars/MED.ALL", "index/index_bigrams");
		indexer.createIndex();
		
		//Make the queries to the Lucene.
		BigramLuceneSearcher searcher = new BigramLuceneSearcher("index/index_bigrams", "text" , "medlars/MED.QRY" , 100 );
		searcher.search();
	}
}