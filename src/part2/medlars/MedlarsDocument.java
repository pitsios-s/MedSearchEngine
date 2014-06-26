package part2.medlars;

/**
 * @author Stamatis Pitsios
 *
 * Every instance of this class represents a document that was found in medlars collection.
 */
public class MedlarsDocument
{
	
	/**
	 * The text of the document.
	 */
	private String text;
	
	/**
	 * The id of the document.
	 */
	private int id;
	
	
	
	
	
	
	/**
	 * Default Constructor.
	 */
	public MedlarsDocument()
	{
		this("" , -1);
	}
	
	
	
	
	/**
	 * Overloaded constructor 1.
	 * 
	 * @param text The document's text.
	 */
	public MedlarsDocument(String text)
	{
		this(text , -1);
	}
	
	
	
	
	/**
	 * Overloaded constructor 2.
	 * 
	 * @param id The id of the document.
	 */
	public MedlarsDocument(int id)
	{
		this("" , id);
	}
	
	
	
	
	/**
	 * Overloaded constructor 3.
	 * 
	 * @param text The document's text.
	 * @param id The id of the document.
	 */
	public MedlarsDocument(String text , int id)
	{
		this.text = text;
		this.id = id;
	}
	
	
	
	
	
	
	/**
	 * Returns the text of the document.
	 * 
	 * @return text The text of the document.
	 */
	public String getText()
	{
		return text;
	}
	
	
	
	
	/**
	 * Sets the text of the document to a new value.
	 * 
	 * @param text The new text of the document.
	 */
	public void setText(String text)
	{
		this.text = text;
	}
	
	
	
	
	/**
	 * Returns the id of the document.
	 * 
	 * @return id The id of the document.
	 */
	public int getId()
	{
		return id;
	}
	
	
	
	
	/**
	 * Sets the id of the document to a new value.
	 * 
	 * @param id The new id of the document.
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	
	
	
	
	
	@Override
	public String toString()
	{
		return "Document ID = \n\t" + String.valueOf(this.id)+"\n\nDocument text = \n\t" + this.text+"\n";
	}
	
	
	
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null) return false;
		
		if(obj == this) return true;
		
		if(!(obj instanceof MedlarsDocument)) return false;
		
		MedlarsDocument doc = (MedlarsDocument)obj;
		
		return (this.id == doc.id && this.text.equals(doc.text));
	}
	
	
	
	
	@Override
	public int hashCode()
	{
		return 41 * (41 + this.id) + this.text.hashCode();
	}
}