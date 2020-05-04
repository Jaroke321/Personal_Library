import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * 
 * @author Jacob Keller
 * @since March 5, 2020
 */
public class Library {
	
	// An ArrayList of the current book data
	private ArrayList<Book> library = new ArrayList<Book>();
	
	/**
	 * Takes a single Book object and adds it to the library list
	 * @param book A book object that will be added to the list
	 */
	public void add(Book book) {
		
		this.library.add(book);
		
	}
	
	/**
	 * Adds a Book object to the current Library. Takes in all of the values
	 * for a Book object as Strings and checks that they are valid before
	 * creating a BBook and adding it to the Library.
	 * @param name The Title of the Book
	 * @param author The Author of the Book
	 * @param series The series that the book belongs to
	 * @param pages The number of pages the book contains. NA if String is empty
	 * @param word The number of words the Book contains. NA if String is empty
	 * @param start The Date the Book was started. NA if String is empty
	 * @param end The Date the Book was finished. NA if the String is empty
	 * @return true if the Book is added successfully. false otherwise.
	 */
	public boolean add(String name, String author, String series, String pages,
			String word, String start, String end) {
		
		boolean success = true;  // Initialize boolean to true
		int pageCount = -1;      // Used to store the number of pages
		int wordCount = -1;      // Used to store the number of words
		String bSeries = "NA";   // Initializes series to NA
		
		try {
			
			// Check that the name, author, and page number Strings are not empty
			if(name.strip().isEmpty() || author.strip().isEmpty() 
					|| pages.strip().isEmpty()) {
				
				success = false;
				System.out.println("ERROR: Book Title, Author, and Page Count are required");
			}
			else {
				// Get the Page Count as an integer
				pageCount = Integer.parseInt(pages);
			}
			
			// Get the word count if entered and check validity 
			if(!word.strip().isEmpty()) {
				wordCount = Integer.parseInt(word);
			}
			
			// Update the series only if the user entered a value
			if(!series.strip().isEmpty()) {
				bSeries = series;
			}
			
		}
		catch(NumberFormatException e) {
			System.out.println("ERROR: The number of words or the page number " +
					"you entered is invalid.");
			success = false;
		}
		
		// All of the values are valid
		if(success) {
			// Create a Book object and add it to the existing Library
			Book b = new Book(name, author, bSeries, pageCount, wordCount, start, end);
			this.library.add(b);
		}
		
		return success;
	}
	
	/**
	 * Adds an entire ArrayList of Books to the Library list
	 * @param bookList The ArrayList of type Book
	 */
	public void add(ArrayList<Book> bookList) {
		this.library.addAll(bookList);
	}
	
	/**
	 * 
	 * @param positon
	 * @return
	 */
	public Book get(int positon) {
		return this.library.get(positon);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Book> getAll() {
		return this.library;
	}
	
	/**
	 * 
	 * @return
	 */
	public int size() {
		return this.library.size();
	}
	
	/**
	 * 
	 * @param pos
	 */
	public void remove(int pos) {
		this.library.remove(pos);
	}
	
	/**
	 * Searches through the entire Library of Books and removes 
	 * a Book if it matches the one passed as a parameter. Only
	 * removes the first instance of that book.
	 * @param b The Book object that is to be removed
	 */
	public void remove(Book b) {
		
		// Cycle through each book in the library
		for(int i = 0; i < this.library.size() - 1; i++) {
			
			// Get the current book in the list
			Book temp = this.library.get(i);
			// Compare the current book with b
			if(temp.equals(b)) {
				this.library.remove(i);
			}
		}
	}
	
	/**
	 * Searches the list of books by a title
	 * @param title The title of the book user is searching for
	 * @return A book object representing the book with the given title.
	 * Empty book object if the title does not exist.
	 */
	public Book searchTitle(String title) {
		
		// Create an empty book object
		Book book = new Book();
		
		// Go through each book in the library
		for(Book b : this.library) {
			// Compare the title with the title being searched for
			if(b.getTitle().equalsIgnoreCase(title)) {
				book.copy(b);  // Copy the data over to book
			}
		}
		
		// return the book whether or not it was found
		return book;
	}
	
	/**
	 * Searches the list of books for a specific author.
	 * @param author The author being searched for.
	 * @return An ArrayList with all of the books by the given author.
	 * ArrayList will be empty if the author is not found.
	 */
	public ArrayList<Book> searchAuthor(String author) {
		
		// Create an empty ArrayList of Books
		ArrayList<Book> bookList = new ArrayList<Book>();
		
		// Go through each book in the library
		for(Book book : this.library) {
			// Compare book author with author being searched for
			if(book.getAuthor().equalsIgnoreCase(author)) {
				bookList.add(book);  // Add the book to the list
			}
		}
		
		// Return the list 
		return bookList;
	}
	
	/**
	 * Searches the list of books for a specific series.
	 * @param series The series to be searched for.
	 * @return An ArrayList with all of the books in the 
	 * given series. ArrayList will be empty if the series
	 * is not found.
	 */
	public ArrayList<Book> searchSeries(String series) {
		
		// Create an empty ArrayList of Books
		ArrayList<Book> bookList = new ArrayList<Book>();
		
		// Go through each of the book in the library
		for(Book book : this.library) {
			// Compare the series name of book with the series being searched for
			if(book.getSeries().equalsIgnoreCase(series)) {
				bookList.add(book);  // Add the book to the list
			}
		}
		
		// Return the list
		return bookList;
		
	}
	
	/**
	 * Sorts the list of books by the author of each book using a 
	 * bubble sort algorithm.
	 */
	public void sortByAuthor() {
		
		boolean sorted = false;  // Set boolean initially to false
		
		// Loop through the list until it is sorted
		while(!sorted) {
			
			sorted = true;
			
			// Cycle through the entire list
			for(int i = 0; i < this.library.size() - 1; i++) {
				
				// Get the Book items to compare
				Book book1 = this.library.get(i);
				Book book2 = this.library.get(i + 1);
				
				String bookOneAuthor = book1.getAuthor().split(" ")[1];
				String bookTwoAuthor = book2.getAuthor().split(" ")[1];
				
				// Compare the current Book item and the next in the list
				if(bookOneAuthor.compareToIgnoreCase(bookTwoAuthor) > 0) {
					
					// Swap the two books in the list
					Book temp = book2;
					this.library.set(i + 1, book1);
					this.library.set(i, temp);
					
					sorted = false;  // Set sorted to false so loop continues
					
				}  // END IF
			}  // END FOR
		}  // END WHILE
	}  // SortByAuthor()
	
	/**
	 * Sorts the list of books by the title of each book using 
	 * a bubble sort algorithm.
	 */
	public void sortByTitle() {
		
		boolean sorted = false;
		
		while(!sorted) {
			
			sorted = true;
			
			for(int i = 0; i < this.library.size() - 1; i++) {
				
				Book book1 = this.library.get(i);
				Book book2 = this.library.get(i + 1);
				
				if(book1.getTitle().compareToIgnoreCase(book2.getTitle()) > 0) {
					
					Book temp = book2;
					this.library.set(i + 1, book1);
					this.library.set(i, temp);
					
					sorted = false;
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Sorts the list of books by the series name using a 
	 * bubble sort algorithm.
	 */
	public void sortBySeries() {
		
		boolean sorted = false;
		
		while(!sorted) {
			
			sorted = true;
			
			for(int i = 0; i < this.library.size() - 1; i++) {
				
				Book book1 = this.library.get(i);
				Book book2 = this.library.get(i + 1);
				
				if(book1.getSeries().compareToIgnoreCase(book2.getSeries()) > 0) {
					
					Book temp = book2;
					this.library.set(i + 1, book1);
					this.library.set(i, temp);
					
					sorted = false;
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Sorts the list of books by the Date they were finished
	 * using a bubble sort algorithm.
	 */
	public void sortByEndDate() {
		
		boolean sorted = false;  // Initialize sort value to false
		
		// Go until the list is sorted
		while(!sorted) {
			
			sorted = true;
			
			// Cycle through the entire list
			for(int i = 0; i < this.library.size() - 1; i++) {
				
				// Get each books endDate and split by '/' delimiter
				String endDate1 = this.library.get(i).getEndDate();
				String endDate2 = this.library.get(i + 1).getEndDate();
				
				if(!((endDate1.equals("NA")) || (endDate2.equals("NA")))) {  // Make sure both dates can be compared
					
					String[] date1 = endDate1.split("-");
					String[] date2 = endDate2.split("-");
				
					// Create Date objects for the Books at position i and i + 1
					LocalDate book1 = LocalDate.of(Integer.parseInt(date1[0]), Integer.parseInt(date1[1]), Integer.parseInt(date1[2]));
					LocalDate book2 = LocalDate.of(Integer.parseInt(date2[0]), Integer.parseInt(date2[1]), Integer.parseInt(date2[2]));
				
					if(book1.isBefore(book2)) {
					
						// Switch the two books
						Book temp = this.library.get(i + 1);
						this.library.set(i + 1, this.library.get(i));
						this.library.set(i, temp);
					
						sorted = false;
					
					}
				
				} 
				else {  // One of the two end dates is NA
					
					Book temp = this.library.get(i+1);  // Get the book at the position i + 1
					this.library.remove(i+1);           // Remove the Book at position i + 1
					this.library.add(0, temp);          // Add the Book back at the start of the list
				}
				
			}
			
		}
		
	}
	
	/**
	 * Sorts the list of books by the page count using a 
	 * bubble sort algorithm.
	 */
	public void sortByPages() {
		
		boolean sorted = false;  // Used as a flag
		
		while(!sorted) {
			
			sorted = true;
			
			for(int i = 0; i < this.library.size() - 1; i++) {
				
				// Get the page numbers of the current book and the one ahead of it
				int book1 = this.library.get(i).getNumPages();
				int book2 = this.library.get(i + 1).getNumPages();
				
				// Compare the two
				if(book1 > book2) {
					
					// Swap them if book1 has more pages than book2 
					Book temp = this.library.get(i + 1);
					this.library.set(i + 1, this.library.get(i));
					this.library.set(i, temp);
					
					sorted = false;
				}
			}
			
		}  // END WHILE
		
	}  // sortByPages()
	
	/**
	 * Sorts the list of books by the word count using a 
	 * bubble sort algorithm.
	 */
	public void sortByWords() {
		
		boolean sorted = false;
		
		while(!sorted) {
			
			sorted = true;
			
			for(int i = 0; i < this.library.size() - 1; i++) {
				
				int book1 = this.library.get(i).getWordCount();
				int book2 = this.library.get(i + 1).getWordCount();
				
				if(book1 > book2) {
					
					Book temp = this.library.get(i + 1);
					this.library.set(i + 1, this.library.get(i));
					this.library.set(i, temp);
					
					sorted = false;
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Calculates the amount of days it took for a book to be read
	 * @param b The book used for the calculation
	 * @return An Integer representing the number of days it took
	 * to read the book
	 */
	public long getTimeToRead(Book b) {
		
		long days = -1;  // Default return value if either value is NA
		
		// Check that neither values are NA
		if(!(b.getStartDate().equalsIgnoreCase("NA")) && !(b.getEndDate().equalsIgnoreCase("NA"))) {
			
			try {
				
				LocalDate start = LocalDate.parse(b.getStartDate());  // Get the books start date
				LocalDate end = LocalDate.parse(b.getEndDate());      // Get the books end date
				
				days = ChronoUnit.DAYS.between(start, end);           // Calculate the days between the two dates
				
			} catch (DateTimeParseException e) {
				System.out.println("ERROR: There was a problem parsing the String into a Date object.");
			}
		}
		// Return the days
		return days;
		
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		int count = 1;
		
		// Cycle through all of the books and number them off
		for(Book b : this.library) {
			sb.append(count);
			sb.append(".  ");
			sb.append(b.getTitle());
			sb.append("  by ");
			sb.append(b.getAuthor());
			sb.append("\n");
			
			count++;
		}
		
		return sb.toString();
	}

}
