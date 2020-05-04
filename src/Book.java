
/**
 * 
 * @author Jacob Keller
 * @since March 5, 2020
 *
 */
public class Book {

	private String title;
	private String author;
	private String series;
	private int numPages;
	private int wordCount;
	private String startDate;
	private String endDate;
	
	public Book() {}
	
	/**
	 * Constructor that does not take dates
	 * @param title
	 * @param author
	 * @param series
	 * @param numPages
	 * @param wordCount
	 */
	public Book(String title, String author, String series, int numPages, int wordCount) {
		
		this.title = title;
		this.author = author;
		this.series = series;
		this.numPages = numPages;
		this.wordCount = wordCount;
	}
	
	/**
	 * Constructor that takes dates into account
	 * @param title
	 * @param author
	 * @param series
	 * @param numPages
	 * @param wordCount
	 * @param startDate
	 * @param endDate
	 */
	public Book(String title, String author, String series, int numPages, int wordCount, String startDate, String endDate) {
		
		this.title = title;
		this.author = author;
		this.series = series;
		this.numPages = numPages;
		this.wordCount = wordCount;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	/**
	 * Takes a Book object as a parameter and performs a hard copy
	 * of its data.
	 * @param book The book to be copied
	 */
	public void copy(Book book) {
		// Set all of the attributes to the attributes in book
		this.title = book.getTitle();
		this.author = book.getAuthor();
		this.series = book.getSeries();
		this.numPages = book.getNumPages();
		this.wordCount = book.getWordCount();
		this.startDate = book.getStartDate();
		this.endDate = book.getEndDate();
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n");
		sb.append(this.title);
		sb.append(" by ");
		sb.append(this.author);
		sb.append("\nBook Stats:\n Number of Pages: ");
		sb.append(this.numPages);
		sb.append("\n Word Count: ");
		sb.append(this.wordCount);
		sb.append("\n Start Date: ");
		sb.append(this.startDate);
		sb.append("\n End Date: ");
		sb.append(this.endDate);
		
		return sb.toString();
	}
	
	/**
	 * Compares the current Book object with a Book object passed as 
	 * an argument
	 * @param b The Book object being compared to
	 * @return true if the books are the same. false otherwise.
	 */
	public boolean equals(Book b) {
		
		boolean isEqual = false;  // Initialize return value to false
		
		if((this.title.equalsIgnoreCase(b.getTitle())) && (this.author.equalsIgnoreCase(b.getAuthor())) 
				&& (this.series.equalsIgnoreCase(b.getSeries()))) {
			isEqual = true;
		}
		
		return isEqual;
	}
	
	/**
	 * Checks whether the Book object is empty and has no 
	 * title or author 
	 * @return true if the Book is empty. false otherwise.
	 */
	public boolean isEmpty() {
		
		 boolean empty = false;  // Initialize boolean to false
		 
		 // Check the title and author of the current book
		 if((this.title == null) || (this.author == null)) {
			 empty = true;  // If one of these is null than the book is effectively empty
		 }
		 // Return boolean value
		 return empty;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public void setSeries(String series) {
		this.series = series;
	}
	
	public String getSeries() {
		return this.series;
	}
	
	public int getNumPages() {
		return this.numPages;
	}
	
	public void setNumPages(int num) {
		this.numPages = num;
	}
	
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	
	public int getWordCount() {
		return this.wordCount;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getStartDate() {
		return this.startDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getEndDate() {
		return this.endDate;
	}
	
}
