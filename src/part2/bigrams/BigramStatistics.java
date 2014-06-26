package part2.bigrams;

public class BigramStatistics 
{
	public static void main(String[] args)
	{
		BigramCounter counter = new BigramCounter("medlars/MED.ALL" , "medlars/MED.QRY");
		counter.displayStatistics();
	}
}