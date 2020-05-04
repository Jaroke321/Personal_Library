import java.io.*;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ListView;
import javafx.scene.control.DatePicker;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.time.*;
import java.util.*;

/**
 * 
 * @author Jacob Keller
 * @since March 5, 2020
 *
 */
public class Main extends Application{
	
	private final String DATA_FILE = "bookData";
	private final String READ_FILE = "ReadingData";
	
	@Override
	public void start(Stage primaryStage) {
		
		primaryStage.setTitle("Personal Library");    // Set the window title
		
		Library lib = load();                         // Load Book data from file
		Scene scene = new Scene(loadMyBooks(lib));    // Load the initial screen
		
		// Set the scene and show
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.setWidth(1200);
		primaryStage.setHeight(700);
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Loads the main view of the personal library application. Includes the search bar,
	 * the side navigation bar, and the list holding all of the current books in the 
	 * data file.
	 * @param lib The Library object that holds all of the Book data
	 * @return A VBox object that holds all of the views that make up the main
	 * view of the application.
	 */
	public VBox loadMyBooks(Library lib) {
		
		Library allBooks = load();
		
		// Create the List View that will hold all of the book data
		ListView bookList = new ListView();
		bookList.setTranslateY(40);
		bookList.setPrefHeight(555);
		bookList.setPrefWidth(1075);
				
		// Populate the bookList
		populateListView(bookList, lib.getAll());
		
		// Create menuItems for sorting
		MenuItem sortTitle = new MenuItem("Title");
		MenuItem sortAuthor = new MenuItem("Author");
		MenuItem sortSeries = new MenuItem("Series");
		MenuItem sortPages = new MenuItem("Page Count");
		MenuItem sortWord = new MenuItem("Word Count");
		
		// Create a select button
		Button select = new Button("Select");              // Used to select menu items
		select.setPrefWidth(100);
		select.setPrefHeight(40);
		select.setTranslateX(50);
		select.setTranslateY(32);
		select.setStyle("-fx-background-color: #3264a8; "
				+ "-fx-text-fill: #f5f6f7;");
		
		// Create a Add Book button
		Button addBook = new Button("Add Book");
		addBook.setPrefWidth(100);
		addBook.setPrefHeight(40);
		addBook.setTranslateX(550);
		addBook.setTranslateY(32);
		addBook.setStyle("-fx-background-color: #3264a8; "
				+ "-fx-text-fill: #f5f6f7;");
		
		//Create action events for sort menu items
		sortTitle.setOnAction(value -> {
			System.out.println("EVENT: Sort by Title was pressed!");
			lib.sortByTitle();
			populateListView(bookList, lib.getAll());
		});
		
		sortAuthor.setOnAction(value -> {
			System.out.println("EVENT: Sort by Author was pressed!");
			lib.sortByAuthor();
			populateListView(bookList, lib.getAll());
		});
		
		sortSeries.setOnAction(value -> {
			System.out.println("EVENT: Sort by Series was pressed!");
			lib.sortBySeries();
			populateListView(bookList, lib.getAll());
		});
		
		sortPages.setOnAction(value -> {
			System.out.println("EVENT: Sort by pages was pressed!");
			lib.sortByPages();
			populateListView(bookList, lib.getAll());
		});
		
		sortWord.setOnAction(value -> {
			System.out.println("EVENT: Sort by Word was pressed!");
			lib.sortByWords();
			populateListView(bookList, lib.getAll());
		});
		
		// Create action events for select and search buttons
		select.setOnAction(value -> {
			System.out.println("EVENT: Select button has been pressed");
			int index = bookList.getSelectionModel().getSelectedIndex();     // Get the index of the list the user has selected
			select.getScene().setRoot(loadSelectedBook(index, lib));         // Send the selectedBook to loadSelectedBook()
		});
		
		// Create an action event for the Add Book Button
		addBook.setOnAction(value -> {
			System.out.println("EVENT: Loading New Book Form");
			addBook.getScene().setRoot(loadAddBook(new Book()));
		});
		
		// Generate the searchBar
		ToolBar search = generateSearchBar("My Books", allBooks);
		
		// Generate the menu bar
		ToolBar menu = generateNavMenu(allBooks);
		
		// Create sorting menu button
		MenuButton sorting = new MenuButton("Sort By:", null, sortTitle, sortAuthor, sortSeries, sortPages, sortWord);
		sorting.setTranslateY(32);
		sorting.setTranslateX(25);
		sorting.setPrefWidth(100);
		sorting.setPrefHeight(40);
		sorting.setStyle("-fx-background-color: #3264a8");
		
		// HBox holding the two buttons, select and sortBy above the list
		HBox labelAndSort = new HBox(sorting, select, addBook);
		// VBox holding labelAndSort and the bookList
		VBox sortSelectList = new VBox(labelAndSort, bookList);
		// HBox holding sortAndSelect and the Navigation Menu
		HBox menuAndList = new HBox(menu, sortSelectList);
		
		// Create the finalized VBox
		VBox vbox = new VBox(search, menuAndList);
		
		// Return the VBox
		return vbox;
	}  // loadMyBooks
	
	/**
	 * Creates and returns a VBox with all of the necessary views for the
	 * Statistics page.
	 * @param lib The Library object that is holding all of the Book data
	 * @return The main View that will be used as the Statistics page
	 */
	public VBox loadStats() {
		
		Library allBooks = load();
		
		ToolBar menu = generateNavMenu(allBooks);
		ToolBar search = generateSearchBar("Statistics", allBooks);
		
		ListView lv = new ListView();
		lv.setPrefHeight(555);
		lv.setPrefWidth(1075);
		
		// Load all of the reading data as Strings
		TreeMap<LocalDate, Double> readingMap = loadReadingData();
		
		// get general stats on Books
		ArrayList<Double> genStats = getGenStats(allBooks, readingMap);
		BarChart dayChart = genDayGraph(readingMap);
		BarChart monthChart = genMonthGraph(allBooks);
		double[] monthData = getMonthData(allBooks, readingMap);
		double[] dayData = getDayData(readingMap);
		
		// Create all of the Labels
		Label genLabel = new Label("General Stats");
		Label totalBooksLabel = new Label("Total number of Books: " + 
				String.valueOf(genStats.get(0)) + " Books");
		Label avgPageLabel = new Label("Average pages per book: " +
				String.format("%.2f", genStats.get(1)) + " Pages");
		Label pagesReadLabel = new Label("Total Pages Read: " +
				String.valueOf(genStats.get(2)) + " Pages");
		Label readStreakLabel = new Label("Current Reading Streak: " +
				String.valueOf(genStats.get(3)) + " Days");
		Label avgPagesRead = new Label("Average Pages Per Day Read: " + 
				String.format("%.2f", genStats.get(4)));
		
		// Page Labels
		Label dayLabel = new Label("Page Stats");
		Label pagesToday = new Label("Pages Read Today: " + 
				String.format("%.0f", dayData[0]));
		Label pagesThisMonth = new Label("Pages Read This Month: " + 
				String.format("%.0f", monthData[1]));
		Label pagesThisYear = new Label("Pages Read This Year: " +
				String.format("%.0f", dayData[1]));
		
		// Books Labels
		Label monthLabel = new Label("Book Stats");
		Label booksThisMonth = new Label("Books Read This Month: " + 
				String.format("%.0f", monthData[0]));
		Label booksThisYear = new Label("Books Read This Year: " + 
				String.format("%.0f", monthData[2]));
		
		// Set all of the attributes for the labels
		genLabel.setFont(new Font(24));
		genLabel.setTranslateX(15);
		genLabel.setTranslateY(10);
		totalBooksLabel.setFont(new Font(16));
		avgPageLabel.setFont(new Font(16));
		pagesReadLabel.setFont(new Font(16));
		readStreakLabel.setFont(new Font(16));
		avgPagesRead.setFont(new Font(16));
		
		// Day Labels
		dayLabel.setFont(new Font(24));
		dayLabel.setTranslateX(15);
		dayLabel.setTranslateY(5);
		pagesToday.setFont(new Font(16));
		pagesThisYear.setFont(new Font(16));
		
		// MonthLabels
		monthLabel.setFont(new Font(24));
		monthLabel.setTranslateX(15);
		monthLabel.setTranslateY(5);
		booksThisMonth.setFont(new Font(16));
		pagesThisMonth.setFont(new Font(16));
		booksThisYear.setFont(new Font(16));
		
		
		// Create VBox for the left half of the general stats
		VBox genLeftSide = new VBox(totalBooksLabel, avgPageLabel);
		genLeftSide.setSpacing(10);         // Set spacing between individual items
		genLeftSide.setTranslateX(20);      // Shift to the right
		genLeftSide.setTranslateY(5);       // Shift down
		
		// Create VBox for the right side of the general Stats
		VBox genRightSide = new VBox(pagesReadLabel, readStreakLabel, avgPagesRead);
		genRightSide.setSpacing(10);
		genRightSide.setTranslateX(150);
		genRightSide.setTranslateY(5);
		
		// Create the HBox that holds the two halves of the general stats section
		HBox genStatVals = new HBox(genLeftSide, genRightSide);
		genStatVals.setPrefHeight(100);  // Sets the height of the general listView item
		
		VBox generalView = new VBox(genLabel, genStatVals);
		generalView.setSpacing(20);      // Sets spacing between title and stats
		
		
		// ALL OF THE VIEWS CONTAINING THE DAY STATISTICS
		VBox dayStats = new VBox(pagesToday, pagesThisMonth, pagesThisYear); // VBox holding any day stats from the getDayStats method
		dayStats.setSpacing(20);
		
		HBox dayStatVals = new HBox(dayChart, dayStats);      // HBox holding the BarChart and the stats
		dayStatVals.setSpacing(30);
		
		VBox dayView = new VBox(dayLabel, dayStatVals);       // The entire Day section
		dayView.setSpacing(50);
		dayView.setPrefHeight(500);
		
		// ALL OF THE VIEWS CONTAINING THE MONTH STATISTICS
		VBox monthStats = new VBox(booksThisMonth, booksThisYear);  // VBox containing all of the stats for month
		monthStats.setSpacing(20);
		
		HBox monthStatVals = new HBox(monthChart, monthStats);       // HBox containing the BarChart and the stats
		monthStatVals.setSpacing(30);
		
		VBox monthView = new VBox(monthLabel, monthStatVals);        // The entire Month section
		monthView.setSpacing(50);
		monthView.setPrefHeight(500);
		
		// Add views to ListView to display
		lv.getItems().add(generalView);
		lv.getItems().add(dayView);
		lv.getItems().add(monthView);
		
		// Combine the Navigation View with the ListView
		HBox main = new HBox(menu, lv);
		// Add the search bar
		VBox view = new VBox(search, main);
		// Return the entire view for Analytics
		return view;
		
	}
	
	/**
	 * Loads the selected book view where a Book object is displayed to the
	 * user
	 * @param b The Book object to be displayed
	 * @param lib The Library of books that were displayed in the main view that the
	 * user is selecting from.
	 * @return A VBox object ready to be displayed, holding all of 
	 * the Books data.
	 */
	public VBox loadSelectedBook(int index, Library lib) {
		
		Library allBooks = load();
		
		ToolBar menu = generateNavMenu(allBooks);                   // Load the navigation menu
		ToolBar search = generateSearchBar("Book Info", allBooks);  // Load the search bar
		
		Book b = lib.get(index);                               // Get the correct book from the Library
		
		Button delete = new Button("Delete Book");             // Create a delete button to delete the current book
		Button edit = new Button("Edit");                      // Create an edit button to edit the current Book
		Button back = new Button("Back");                      // Create a back button to return to the previous page
		Label bookName = new Label(b.getTitle());              // Get the Books name as a Label
		Label bookAuthor = new Label("By: " + b.getAuthor());  // Get the Books author as a Label
		Label bookSeries = new Label(b.getSeries());           // Get the Books series as a Label
		Label bookWords = new Label(String.valueOf(b.getWordCount()) + 
				" Words");                                     // Get the Books word count as a Label
		Label bookPages = new Label(String.valueOf(b.getNumPages()) + 
				" Pages");                                     // Get the Books page count as a Label
		Label start = new Label(b.getStartDate());             // Get the Books start Date as a Label
		Label end = new Label(b.getEndDate());                 // Get the Books endDate as a label
		Label genreSeries = new Label("Genre / Series: ");     // Make a Label for the genre or series
		Label wordLabel = new Label("Word count:");            // Used as a label for the word count of the Book
		Label pageLabel = new Label("Page Count:");            // Used as a label for the page count of the Book
		Label startLabel = new Label("Reading Start Date:");   // Used as a label for the start Date of the Book
		Label endLabel = new Label("Reading End Date:");       // Used as a label for the end date of the Book
		
		// Set the Font sizes of the different labels
		genreSeries.setFont(new Font(16));
		bookSeries.setFont(new Font(18));
		bookName.setFont(new Font(24));
		bookAuthor.setFont(new Font(18));
		wordLabel.setFont(new Font(16));
		pageLabel.setFont(new Font(16));
		bookPages.setFont(new Font(18));
		bookWords.setFont(new Font(18));
		startLabel.setFont(new Font(16));
		endLabel.setFont(new Font(16));
		start.setFont(new Font(18));
		end.setFont(new Font(18));
		
		bookName.setStyle("-fx-text-fill: #f5f6f7;");    // Set the text color of the title
		bookAuthor.setStyle("-fx-text-fill: #f5f6f7;");  // Set the text color of the author
		bookName.setTranslateX(20);                      // Shift the title to the right by 20
		bookAuthor.setTranslateX(25);                    // Shift the authors name to the right by 25
		bookName.setPrefWidth(350);                      // Set the preferred width of the title to 350
		bookName.setWrapText(true);                      // Set the wrap property to true
		bookAuthor.setPrefWidth(325);                    // Set the preferred width of the author to 325
		bookAuthor.setWrapText(true);                    // Set the wrap property to true
		delete.setPrefHeight(40);
		delete.setPrefWidth(100);
		delete.setStyle("-fx-background-color: #a30202;" +
				"-fx-text-fill: #f5f6f7");
		edit.setPrefHeight(40);
		edit.setPrefWidth(100);
		edit.setStyle("-fx-background-color: #3264a8;" + 
			    "-fx-text-fill: #f5f6f7;");
		back.setPrefHeight(40);
		back.setPrefWidth(100);
		back.setStyle("-fx-background-color: #3264a8;" + 
				"-fx-text-fill: #f5f6f7;");
		
		// Create the VBox for the title and the author
		VBox nameAndAuthor = new VBox(bookName, bookAuthor);
		nameAndAuthor.setAlignment(Pos.CENTER_LEFT);
		nameAndAuthor.setStyle("-fx-background-color: #3264a8");
		nameAndAuthor.setPrefHeight(150);
		nameAndAuthor.setPrefWidth(400);
		
		// Create a VBox for the series / genre of the book
		VBox seriesLine = new VBox(genreSeries, bookSeries);
		seriesLine.setSpacing(10);
		seriesLine.setAlignment(Pos.CENTER_LEFT);
		
		// Create the VBox for the page count
		VBox pageCount = new VBox(pageLabel, bookPages);
		pageCount.setSpacing(10);
		// Create the VBox for the word count
		VBox wordCount = new VBox(wordLabel, bookWords);
		wordCount.setSpacing(10);
		// Create the HBox that holds the page count and word count
		HBox pageAndWord = new HBox(pageCount, wordCount);
		pageAndWord.setSpacing(50);
		pageAndWord.setAlignment(Pos.CENTER_LEFT);
		
		// Create a VBox for the reading start date
		VBox startDate = new VBox(startLabel, start);
		// Create a VBox for the reading finish date
		VBox endDate = new VBox(endLabel, end);
		// Create an HBox containing the start Date and the finish Date
		HBox startAndEnd = new HBox(startDate, endDate);
		startAndEnd.setSpacing(50);
		startAndEnd.setAlignment(Pos.CENTER_LEFT);
		
		// Create an HBox for edit and delete buttons
		HBox editAndDelete = new HBox(edit, delete);
		editAndDelete.setSpacing(120);
		
		// Create the main view that holds the Books information
		VBox main = new VBox(back, nameAndAuthor, seriesLine, pageAndWord, startAndEnd, editAndDelete);
		main.setSpacing(30);
		main.setTranslateX(60);
		main.setTranslateY(30);
		
		// Get all of the individual rankings of the book
		VBox rankings = getRankings(allBooks, b);
		
		// HBox that holds the menu and the main view
		HBox menuAndDisplay = new HBox(menu, main, rankings);
		
		// Create an on Action event for the delete button
		delete.setOnAction(value -> {
			System.out.println("EVENT: The delete button was pressed");
			allBooks.remove(b);  // Remove the current book from the complete Library
			save(allBooks);      // Update the data file by saving the library to file
			delete.getScene().setRoot(loadMyBooks(allBooks));  // Launch the main view with the updated bookList
		});
		
		// Create an on Action event for the edit button
		edit.setOnAction(value -> {
			System.out.println("EVENT: The edit button was pressed");
			edit.getScene().setRoot(loadAddBook(b));
		});
		
		back.setOnAction(value -> {
			System.out.println("EVENT: Back button pressed. \n... Returning to main page");
			back.getScene().setRoot(loadMyBooks(lib));
		});
		
		// Create the final VBox that holds the search bar, menu, and the main view
		VBox view = new VBox(search, menuAndDisplay);  
		
		// Return the VBox
		return view;
		
	}
	
	/**
	 * Loads the add Book page, which is used when the user presses the add
	 * Book button from the home page.
	 * @param lib The Library object that holds all of the Book data
	 * @param b A Book object. If the book object is empty then prompt will be
	 * shown for each of the text boxes. If the Book is populated then the text boxes
	 * will show the books values.
	 * @return VBox object that holds all of the views for the add Book page
	 */
	public VBox loadAddBook(Book b) {
		
		Library lib = load();    // Load all of the books in the data file so a book can be added
		
		// Create the navigation menu and the search bar
		ToolBar menu = generateNavMenu(lib);
		ToolBar search = generateSearchBar("New Book", lib);
		
		// Create all of the textFields necessary
		TextField bookName = new TextField();
		TextField bookAuthor = new TextField();
		TextField bookSeries = new TextField();
		TextField numPages = new TextField();
		TextField numWords = new TextField();
		DatePicker startDate = new DatePicker();
		DatePicker endDate = new DatePicker();
		
		// Set the TextField attributes
		bookName.setPrefWidth(300);
		bookName.setPromptText("Enter the Book's Title");
		bookAuthor.setPrefWidth(300);
		bookAuthor.setPromptText("Enter the Book's Author");
		bookSeries.setPrefWidth(300);
		bookSeries.setPromptText("Enter Book Series");
		numPages.setPrefWidth(135);
		numPages.setPromptText("Number of Pages");
		numWords.setPrefWidth(135);
		numWords.setPromptText("Number of Words");
		
		Label startLbl = new Label("Date Started:");
		Label endLbl = new Label("Date Finished:");
		
		// Fill in book values if a valid book was passed as an argument
		if(!b.isEmpty()) {
			// Fill in all of the textFields with the appropriate book data
			bookName.setText(b.getTitle());
			bookAuthor.setText(b.getAuthor());
			bookSeries.setText(b.getSeries());
			numPages.setText(String.valueOf(b.getNumPages()));
			numWords.setText(String.valueOf(b.getWordCount()));
			String bookStart = b.getStartDate(); // Stores the books start date as a string
			String bookEnd = b.getEndDate();     // Stores the books end date as a String
			
			// Get the books start Date and end Date
			if(!bookStart.equals("NA")) {
				// Set the start DatePickers value with the books start date
				String[] temp = bookStart.split("-");
				startDate.setValue(LocalDate.of(Integer.parseInt(temp[0]), 
						Integer.parseInt(temp[1]), Integer.parseInt(temp[2])));
			}
			
			if(!bookEnd.equals("NA")) {
				// Set the end DatePickers value with the books end date
				String[] temp =bookEnd.split("-");
				endDate.setValue(LocalDate.of(Integer.parseInt(temp[0]), 
						Integer.parseInt(temp[1]), Integer.parseInt(temp[2])));
			}
		}
		
		// Create button to add to the Library
		Button done = new Button("Done");
		done.setStyle("-fx-background-color: #3264a8; "
				+ "-fx-text-fill: #f5f6f7;");
		done.setPrefWidth(120);
		done.setPrefHeight(40);
		
		// Create an action event for the done button
		done.setOnAction(value -> {
			
			System.out.println("EVENT: Done button pressed.\n...Checking values");
			
			// Get all of the book attributes to add
			String name = bookName.getText();
			String author = bookAuthor.getText();
			String series = bookSeries.getText();
			String pages = numPages.getText();
			String words = numWords.getText();
			String start = "NA";                  // Initialize startDate as NA
			String end = "NA";                    // Initialize endDate as NA
			
			// Check for null in start and end dates
			if(!(startDate.getValue() == null)) {
				start = startDate.getValue().toString();
			}
			
			if(!(endDate.getValue() == null)) {
				end = endDate.getValue().toString();
			}
			
			// Create a Book object
			boolean success = lib.add(name, author, series, pages, words, 
					start, end);
			
			// If add is successful, print success statement and navigate back to home screen
			if(success) {
				System.out.println("PASS: Book added to Library successfully");
				if(!b.isEmpty()) lib.remove(b);            // remove book if b is not empty
				save(lib);                                 // Save the library since a new book was added or edited
				done.getScene().setRoot(loadMyBooks(lib)); // Navigate back to the home screen
			}
			else {
				System.out.println("ERROR: Book was not added successfully to the Library");
			}
			
		});
		
		// Create Two HBox objects to hold the two datePickers and their labels
		HBox pageAndWord = new HBox(numPages, numWords);
		HBox start = new HBox(startLbl, startDate);
		HBox end = new HBox(endLbl, endDate);
		
		pageAndWord.setSpacing(25);
		pageAndWord.setAlignment(Pos.CENTER);
		start.setSpacing(10);
		start.setAlignment(Pos.CENTER);
		end.setSpacing(6);
		end.setAlignment(Pos.CENTER);
		
		// Create a VBox that holds all of the textFields
		VBox bookForm = new VBox(bookName, bookAuthor, bookSeries, pageAndWord, 
				start, end, done);
		
		bookForm.setSpacing(35);                 // Sets the spacing between the TextFields 
		bookForm.setTranslateX(75);              // Shifts the TextFields to the right
		bookForm.setTranslateY(50);
		
		// Create an HBox holding the menu and the form
		HBox menuAndForm = new HBox(menu, bookForm);
		
		// Create a VBox holding the search bar, the menu, and the entire form
		VBox vbox = new VBox(search, menuAndForm);
		// Return the VBox
		return vbox;
	}
	
	/**
	 * Loads the complete View containing all of the sub Views
	 * for the Read navigation button so that the user 
	 * can input data on reading
	 * @return VBox containing the entire view
	 */
	public VBox loadReading() {
		
		Library allBooks = load();                                   // Load all of the books from file
		TreeMap<LocalDate, Double> readingData = loadReadingData();  // Load all of the readingData from file
		
		ToolBar menu = generateNavMenu(allBooks);                 // Generate the Navigation menu
		ToolBar search = generateSearchBar("Reading", allBooks);  // Generate the search bar
		
		// Create the form so that the user can input data
		Label dateLabel = new Label("Enter the Date you read:");
		Label pageLabel = new Label("Pages you read:");
		DatePicker date = new DatePicker();
		TextField pages = new TextField();
		Button done = new Button("Done");
		
		// Set all of the attributes for the done Button
		done.setStyle("-fx-background-color: #3264a8; "
				+ "-fx-text-fill: #f5f6f7;");
		done.setPrefWidth(120);
		done.setPrefHeight(40);
		done.setTranslateY(40);
		
		// Set Label and TextField attributes
		dateLabel.setFont(new Font(18));
		pageLabel.setFont(new Font(18));
		pages.setPrefWidth(150);
		date.setPrefWidth(150);
		
		// Place the reading label and the textField into a VBox
		HBox labels = new HBox(pageLabel, dateLabel);
		labels.setSpacing(90);
		// PLace the date Label and the date picker into a VBox
		HBox fields = new HBox(pages, date);
		fields.setSpacing(60);
		// Place both HBoxes into a VBox with the button
		VBox form = new VBox(labels, fields, done);
		form.setSpacing(25);
		form.setTranslateX(40);
		form.setTranslateY(60);
		form.setAlignment(Pos.TOP_CENTER);
		
		// Place both VBoxes into an HBox so they are side by side
		HBox main = new HBox(menu, form);
		
		// Set on action event for the button 
		done.setOnAction(value -> {
			System.out.println("EVENT: Done button was pressed");
			// Check that the input data is valid
			LocalDate newDate = LocalDate.now();
			
			if(!(date.getValue() == null)) {
				newDate = date.getValue();
			} 
			try {
				
				double pagesRead = Double.parseDouble(pages.getText());  // Get the pages read as a Double
				
				// Add the new data to the readingData
				if(readingData.containsKey(newDate)) {  // Add pagesRead if the radingData already contains this date
					double newVal = readingData.get(newDate) + pagesRead;
					readingData.replace(newDate, newVal);
				} else {                                // Else just append new reading data
					readingData.put(newDate, pagesRead);
				}
				
				boolean success = saveReadData(readingData);
				
				if(success) {
					System.out.println("PASS: reading data has been successfully saved.");
					done.getScene().setRoot(loadMyBooks(allBooks));
				} else {
					System.out.println("ERROR: Reading data was not saved to file.");
				}
				
			} catch(NumberFormatException e) {
				System.out.println("ERROR: The value you entered for pages read is invalid");
			}
		});
		
		// Create the completed view
		VBox view = new VBox(search, main);
		
		// Return the finalized view
		return view;
	}
	
	/**
	 * Creates and returns a VBox object that will hold all of the 
	 * necessary views for the About page
	 * @param lib The Library object that is holding all of the Book data
	 * @return The main View that will be used as an about page
	 */
	public VBox loadAbout() {
		
		Library allBooks = load();
		
		ToolBar menu = generateNavMenu(allBooks);
		ToolBar search = generateSearchBar("About", allBooks);
		
		VBox view = new VBox(search, menu);
		return view;
		
	}
	
	/**
	 * Populates a ListView with Book data from a Library object.
	 * @param lv The ListView to be populated
	 * @param lib The Library that holds the Book objects that will be used to populate
	 * the ListView
	 */
	public void populateListView(ListView lv, ArrayList<Book> lib) {
		
		// Clear the listView
		lv.getItems().clear();
		
		// Cycle through each Book object
		for(int i = 0; i < lib.size(); i++) {
			
			Book current = lib.get(i);  // Get the current book in the list
			
			if(!(current.getTitle() == null)) {  // Check that the Library does not have null values
			
				Label bookNum = new Label((i + 1) + ": ");                    // Start each row with the number of the book
				Label title = new Label(current.getTitle());                  // Get the Title of the book
				Label author = new Label("\tby: " + current.getAuthor());     // Get author of the book
				// Set the font size of each of the labels
				bookNum.setFont(new Font(16));
				title.setFont(new Font(18));
				author.setFont(new Font(15));
		
				VBox bookInfo = new VBox(title, author);  // Store the title and the author in a VBox
			
				HBox row = new HBox(bookNum, bookInfo);   // Store the bookNUm and the VBox in a HBox
		
				// Populate the List View with the new HBox
				lv.getItems().add(row);
			}
			else {
				
				Label invalid = new Label("\t\t\t\t\t\t\t\t\tThe Book you are looking for does not exist."); // Make a label to display
				invalid.setFont(new Font(20));                                             // Set the font of the label
				lv.getItems().add(invalid);                                                // Add the label to the list view
			}
		}
	}
	
	/**
	 * Creates and returns the top search bar.
	 * @param pageName The title of the page
	 * @param lib The Library object that is holding all of the Book data
	 * @return The search bar with the correct title of the page 
	 */
	public ToolBar generateSearchBar(String pageName, Library lib) {
		
		//final Library all = load();
		
		// Create a Text field for searching the list
		TextField searchBar = new TextField();
		searchBar.setPromptText("Search by Book Title");      // Sets the text prompt of the search bar
		searchBar.setTranslateX(170);                         // Shifts the search bar to the right
		searchBar.setMinWidth(400);                           // Sets the size of the search bar
		
		// Create the menuItems for searching
		MenuItem byTitle = new MenuItem("Book Title");     
		MenuItem byAuthor = new MenuItem("Book Author");
		MenuItem bySeries = new MenuItem("Book Series");
		
		Button searchButton = new Button("Search");           // Used to search list
		searchButton.setTranslateX(170);                      // shifts the search button to the right of the search bar
		searchButton.setStyle("-fx-background-color: #3264a8;"
				+ "-fx-text-fill: #f5f6f7;");
		
		// Create a menu bar to change the search type
		MenuButton searchOptions = new MenuButton("Search By:", null, byTitle, byAuthor, bySeries);
		searchOptions.setTranslateX(200);
		searchOptions.setStyle("-fx-background-color: #3264a8");
		
		// Create action events for each of the search options
		byTitle.setOnAction(value -> {
			System.out.println("EVENT: search bar was set to Title");
			searchBar.setPromptText("Search by Book Title");      // Set the Search bar text to Title
		});
				
		byAuthor.setOnAction(value -> {
			System.out.println("EVENT: search bar was set to Author");
			searchBar.setPromptText("Search by Book Author");     // Set the Search bar text to Author
		});
				
		bySeries.setOnAction(value -> {
			System.out.println("EVENT: search bar was set to Series");
			searchBar.setPromptText("Search by Book Series");     // Set the Search bar text to Series
		});
		
		searchButton.setOnAction(value -> {
			System.out.println("EVENT: Search button has been pressed");
			String s = searchBar.getPromptText().substring(15);   // Get the Last word in the search bar prompt
			String toSearch = searchBar.getText();                // Get the value that the user wants to search for
			Library newLib = new Library();                       // Create an empty library object
			
			// Search based on search option picked by user
			switch(s) {
			case "Title":
				newLib.add(lib.searchTitle(toSearch));  // Search by Title and add resulting book to the newLib
				break;
			case "Author":
				newLib.add(lib.searchAuthor(toSearch)); // Search by Author and add the resulting list to newLib
				break;
			case "Series":
				newLib.add(lib.searchSeries(toSearch)); // Search by Series and add the resulting list to newLib
				break;
			}
			
			// Load the main screen using the newLib as a list
			searchButton.getScene().setRoot(loadMyBooks(newLib));
		});
		
		// Create a Header Label to describe the page
		Label label = new Label(pageName);
		label.setFont(new Font(25));        // Increase font size to 25
		label.setPrefWidth(200);            // Keeps the size of the label static, preventing
											// the search bar from moving
		
		//Create the searchBar
		ToolBar search = new ToolBar();
		// Add all of the search items to a ToolBar
		search.getItems().add(label);
		search.getItems().add(searchBar);
		search.getItems().add(searchButton);
		search.getItems().add(searchOptions);
		
		return search;
		
	}
	
	/**
	 * Creates and returns the side navigation menu
	 * @param lib The Library object that is holding all of the Book data
	 * @return The side navigation menu
	 */
	public ToolBar generateNavMenu(Library lib) {
		
		// Create menuBar Buttons
		Button homeButton = new Button("Home");
		Button aboutButton = new Button("About");
		Button statButton = new Button("Statistics");
		Button readButton = new Button("Read");
		homeButton.setPrefWidth(100);                            // Set the width of the Button
		homeButton.setStyle("-fx-background-color: #f5f6f7");    // Sets the background color 
		homeButton.setPadding(new Insets(8));                    // Makes the button bigger
		homeButton.setTranslateX(7);                             // Shifts the button to the right
		homeButton.setTranslateY(10);                            // Shifts the button down
		aboutButton.setPrefWidth(100);                              
		aboutButton.setStyle("-fx-background-color: #f5f6f7");
		aboutButton.setPadding(new Insets(8));
		aboutButton.setTranslateX(7);
		aboutButton.setTranslateY(25);
		statButton.setPrefWidth(100);
		statButton.setStyle("-fx-background-color: #f5f6f7");
		statButton.setPadding(new Insets(8));
		statButton.setTranslateX(7);
		statButton.setTranslateY(15);
		readButton.setPrefWidth(100);
		readButton.setStyle("-fx-background-color: #f5f6f7");
		readButton.setPadding(new Insets(8));
		readButton.setTranslateX(7);
		readButton.setTranslateY(20);
		
		// Create action events for each of the buttons
		homeButton.setOnAction(value -> {
			System.out.println("EVENT: Home button was pressed");
			homeButton.getScene().setRoot(loadMyBooks(lib));
		});
				
		aboutButton.setOnAction(value -> {
			System.out.println("EVENT: About button was pressed");
			aboutButton.getScene().setRoot(loadAbout());  // Navigate to the about screen
		});
				
		statButton.setOnAction(value -> {
			System.out.println("EVENT: Statistics button was pressed");
			statButton.getScene().setRoot(loadStats());  // Navigate to the Statistics screen
		});
		
		readButton.setOnAction(value -> {
			System.out.println("EVENT: Reading button was pressed");
			readButton.getScene().setRoot(loadReading());
		});
		
		// Create the final ToolBar with all of the buttons
		ToolBar menu = new ToolBar(homeButton, statButton, readButton, aboutButton);
		menu.setOrientation(Orientation.VERTICAL);         // Sets the orientation of the menu to Vertical
		menu.setPrefHeight(1250);                          // Sets height so it goes to the bottom of the window 
		menu.setStyle("-fx-background-color: #3264a8");    // Sets the background color
		menu.setPrefWidth(125);                            // Sets the width
		
		// Return the ToolBar
		return menu;
		
	}
	
	/**
	 * Creates a VBox that displays different rankings and 
	 * statistics for an individual book.
	 * @param books The entire collection of books in the users data file
	 * @param b The book to find rankings and statistics for.
	 * @return VBox object to display all of the rankings
	 */
	public VBox getRankings(Library books, Book b) {
		
		long timeToRead = books.getTimeToRead(b);            // Get the total time it took to read the book
		double pagesPerDay = b.getNumPages() / timeToRead;   // Calculate the average number of words per day read
		int pageRanking = 1;                                 // Used to store the total ranking for page count
		int wordRanking = 1;                                 // Used to store the total ranking for word count
		String totalValue = String.valueOf(timeToRead);      // Get the String value of timeToRead
		String perDayValue = String.valueOf(pagesPerDay);    // Get the String vale of pagesPerDay
		
		// Check that timeToRead and pagesPerDay are valid
		if(timeToRead < 0) {
			totalValue = "NA";
			perDayValue = "NA";
		}
		
		// Cycle through each book in the list
		for(Book current : books.getAll()) {
			
			// Compare the current books page and word count with the book b
			if(current.getNumPages() > b.getNumPages()) {
				pageRanking++;  // Increment pageRanking if current has more pages than b
			}
			if(current.getWordCount() > b.getWordCount()) {
				wordRanking++;  // Increment wordRanking if current has more words than b
			}
		}
		
		Label rank = new Label("Rankings:");  // Create a Label for the title
		Label pageRank = new Label("Pages: #" + pageRanking + " Out Of " + 
				books.size());
		Label wordRank = new Label("Number of Words: #" + wordRanking +
				" Out Of " + books.size());
		Label days = new Label("Number of Days to read: " + totalValue);
		Label words = new Label("Average pages per day read: " +
				perDayValue);
		
		rank.setFont(new Font(24));           // Set the Font size for the title
		pageRank.setFont(new Font(20));       // Set the font size for the page row
		wordRank.setFont(new Font(20));       // Set the font size for the word row
		days.setFont(new Font(20));           // Set the font size for the days row
		words.setFont(new Font(20));          // Set the font size for the words row
		
		// Combine the rows into one VBox
		VBox ranking = new VBox(rank, pageRank, wordRank, days, words);
		ranking.setSpacing(25);               // Set the spacing between the different values
		ranking.setTranslateX(140);           // Shift the VBox to the right
		ranking.setTranslateY(90);            // Shift the VBox down
		
		// Return the entire VBox holding all of the ranking details
		return ranking;
		
	}
	
	/**
	 * Calculates the General stats of all of the books in the users
	 * library. Used in the Analytics portion of the navigation menu
	 * @param lib A Library object that holds all of the books in the 
	 * users library
	 * @param readingMap A TreeMap with all of the LocalDate objects as keys
	 * and the number of pages read on each date as values
	 * @return An ArrayList of type String that holds all of the relevant
	 * general statistics
	 */
	public ArrayList<Double> getGenStats(Library lib, TreeMap<LocalDate, Double> readingMap) {
		
		ArrayList<Double> genStats = new ArrayList<Double>();
		double avgPageLength = 0.0;
		double totalPagesRead = 0.0;
		double currentReadingStreak = 0.0;
		int count = 0;
		LocalDate checkDate = LocalDate.now().minusDays(1);
		
		String size = String.valueOf(lib.size());  // Get the total number of books in the library
		genStats.add(Double.parseDouble(size));    // Add total number of books in list
		
		// Get the average book length
		for(Book b : lib.getAll()) {
			
			// Check that current book has a valid page count
			if(b.getNumPages() > 0) {
				// Only add books that have a page count
				avgPageLength += b.getNumPages();
				count++;  // Only count books that have a page count
			}
		}
		
		// Calculate the total pages read
		for(double d : readingMap.values()) {
			totalPagesRead += d;
		}
		
		// Calculate the current reading Streak
		for(int i = 0; i < readingMap.size(); i++) {
			
			// Check that the user has read during the correct date
			if(readingMap.containsKey(checkDate)) {
				currentReadingStreak++;
				checkDate = checkDate.minusDays(1);
			} else if (readingMap.containsKey(LocalDate.now())) {
				currentReadingStreak++;
				break;
			} else {
				break;
			}
		}
		
		// Calculate how many pages the user reads per day based on reading data
		double avgPagesPerDay = totalPagesRead / (readingMap.firstEntry().getKey().until(LocalDate.now()).getDays());
		
		avgPageLength /= count;             // Calculate average book length
		genStats.add(avgPageLength);        // Add the average book length the the list
		genStats.add(totalPagesRead);       // Add the total number of pages read to list
		genStats.add(currentReadingStreak); // Add the current reading streak to the list
		genStats.add(avgPagesPerDay);       // Add the pages read per day for the user
		
		return genStats;  // Return the list
	}
	
	/**
	 * Generates Statistics to be used in the Day section of the Analytics 
	 * section of the Application
	 * @param readingMap A TreeMap object of types LocalDate and Double
	 * representing the Dates the user read and the number of pages they read on
	 * that date.
	 * @return A BarChart that displays all of the days on the x-axis
	 * and the average number of pages read on that day on the y-axis.
	 */
	public BarChart genDayGraph(TreeMap<LocalDate, Double> readingMap) {
		
		// Create a Map object to hold all of the data for each day of the week
		Map<DayOfWeek, Double> dataMap = new HashMap<>(7);
		dataMap.put(DayOfWeek.SUNDAY, 0.0);     // Initiate all values in the map to 0.0
		dataMap.put(DayOfWeek.MONDAY, 0.0);
		dataMap.put(DayOfWeek.TUESDAY, 0.0);
		dataMap.put(DayOfWeek.WEDNESDAY, 0.0);
		dataMap.put(DayOfWeek.THURSDAY, 0.0);
		dataMap.put(DayOfWeek.FRIDAY, 0.0);
		dataMap.put(DayOfWeek.SATURDAY, 0.0);
		
		// Create ArrayList to count number of entries of each day of the week
		ArrayList<Double> dayCounts = new ArrayList<Double>(7);
		for(int i = 0; i < 7; i++) {
			dayCounts.add(0.0);
		}
		
		int pointer;                                     // Used to cycle through each day of the week in dayCount
		LocalDate startingDate = readingMap.firstKey();  // Get the first Date the user read
		LocalDate today = LocalDate.now();               // Used to increment through days from startingDate to today
		
		// calculate pointer based on day of the week of startingDate
		if(startingDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) pointer = 0;
		else if(startingDate.getDayOfWeek().equals(DayOfWeek.TUESDAY)) pointer = 1;
		else if(startingDate.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) pointer = 2;
		else if(startingDate.getDayOfWeek().equals(DayOfWeek.THURSDAY)) pointer = 3;
		else if(startingDate.getDayOfWeek().equals(DayOfWeek.FRIDAY)) pointer = 4;
		else if(startingDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) pointer = 5;
		else pointer = 6;
		
		// Add the value of the startingDate in reading map to dataMap
		dataMap.replace(startingDate.getDayOfWeek(), readingMap.get(startingDate));
		startingDate = startingDate.plusDays(1);  // Increment startingDate by 1 day
		dayCounts.set(pointer, 1.0);              // Add one to the dayCounts at position pointer
		// Set pointer to 0 if it is currently pointing to Saturday, otherwise increment
		if(pointer == dayCounts.size() - 1) pointer = 0;
		else pointer++;
		
		// Cycle through dates until startingDate is tomorrow
		while(startingDate.isBefore(today) || startingDate.equals(today)) {
			
			// Check for a reading value at the current date
			if(readingMap.containsKey(startingDate)) {
				// Add the value at this key to the correct day of the week
				double newVal = readingMap.get(startingDate) + dataMap.get(startingDate.getDayOfWeek());
				dataMap.replace(startingDate.getDayOfWeek(), newVal);
			}
			// Increment the value at pointer in dayCounts by 1
			dayCounts.set(pointer, dayCounts.get(pointer) + 1);
			
			startingDate = startingDate.plusDays(1);  // Increment startingDate for the next round
			// Set pointer to 0 if it is currently pointing to a Saturday, otherwise increment
			if(pointer == dayCounts.size()-1) pointer = 0;
			else pointer++;
		}
		
		pointer = 0;
		for(DayOfWeek d : DayOfWeek.values()) {
			double newVal = dataMap.get(d) / dayCounts.get(pointer++);
			dataMap.replace(d, newVal);
		}
		
		// Create the X and Y axis for the BarChart
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Day Of The Week");
		yAxis.setLabel("Pages Read");
		
		// Create new DataSeries
		XYChart.Series data = new XYChart.Series();
		// Add all of the values to the data
		for(DayOfWeek d : DayOfWeek.values()) {
			data.getData().add(new XYChart.Data(d.toString(), dataMap.get(d)));
		}
		
		// Create the BarChart that holds all of the data
		BarChart barChart = new BarChart(xAxis, yAxis);
		barChart.getData().add(data);
		barChart.setPrefHeight(400);
		barChart.setLegendVisible(false);
		barChart.setTitle("Average Pages per Day of the Week");
		barChart.lookup(".data0.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		barChart.lookup(".data1.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		barChart.lookup(".data2.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		barChart.lookup(".data3.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		barChart.lookup(".data4.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		barChart.lookup(".data5.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		barChart.lookup(".data6.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		
		// Return the BarChart
		return barChart;
	}
	
	/**
	 * Generates the Month graph that is apart of the Analytics tab.
	 * @param books The Library object that contains all of the user's 
	 * Books
	 * @return A BarChart that represents the data
	 */
	public BarChart genMonthGraph(Library books) {
		
		books.sortByEndDate();
		
		ArrayList<Integer> arr = new ArrayList<>(
				Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));  // Create an ArrayList that holds the number of books read each month
		
		// Cycle through all of the books in the Library and update the Array for each book
		for(Book b : books.getAll()) {
			// Skip the Book if it has not yet been finished
			if(!(b.getEndDate().equalsIgnoreCase("NA"))) {
				
				int index = LocalDate.parse(b.getEndDate()).getMonthValue() - 1;  // Get the index of the current month from 0 to 11
				int newVal = arr.get(index) + 1;                                  // Make the value what it was plus 1
				
				arr.set(index, newVal);  // Set the value at the index to the newVal
			}
		}
		
		// Create both the x and y axes
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		// Set the Label of both the axes
		xAxis.setLabel("Month");
		yAxis.setLabel("Books Read");
		
		// Create the data and populate it with the array values above
		XYChart.Series data = new XYChart.Series();
		for(Month m : Month.values()) {
			data.getData().add(new XYChart.Data(m.toString(), arr.get(m.getValue() - 1)));
		}
		
		// Create the barChart and Set the color of each bar to match the theme
		BarChart bc = new BarChart(xAxis, yAxis);
		bc.getData().add(data);
		bc.setPrefHeight(400);
		bc.setLegendVisible(false);
		bc.setTitle("Total Books Read per Month");
		bc.lookup(".data0.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		bc.lookup(".data1.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		bc.lookup(".data2.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		bc.lookup(".data3.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		bc.lookup(".data4.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		bc.lookup(".data5.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		bc.lookup(".data6.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		bc.lookup(".data7.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		bc.lookup(".data8.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		bc.lookup(".data9.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		bc.lookup(".data10.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		bc.lookup(".data11.chart-bar").setStyle("-fx-bar-fill: #3264a8");
		// Return the BarChart
		return bc;
	}
	
	/**
	 * Generates all of the data that is needed for the day section of the 
	 * analytics tab
	 * @param readingData All of the reading Data that the user has entered
	 * up until this date
	 * @return An Array that contains all of the data that represents
	 * days based on the reading data of the user
	 */
	public double[] getDayData(TreeMap<LocalDate, Double> readingData) {
		
		double[] arr = new double[2];           // Create the Array that will hold all of the data
		int today = LocalDate.now().getYear();  // Get the current year as an integer
		
		// Calculate todays reading amount
		if(readingData.containsKey(LocalDate.now())) arr[0] = readingData.get(LocalDate.now());
		
		for(Map.Entry<LocalDate, Double> et : readingData.entrySet()) {
			// check if the current reading data date matches this year
			if(et.getKey().getYear() == today) {
				arr[1] += et.getValue();
			}
		}
		
		return arr;
	}
	
	/**
	 * Generates all of the data that is needed for the month section of the
	 * analytics tab.
	 * @param books A Library object containing all of the users Books
	 * @param readingData A TreeMap containing all of the user's reading data
	 * @return An Array that contains all of the relevant data
	 * that is need for the month section.
	 */
	public double[] getMonthData(Library books, TreeMap<LocalDate, Double> readingData) {
		
		double[] arr = new double[3];       // Create an Array that will hold all of the data
		
		// Cycle through all of the Users books and get the data
		int countBooks = 0;                 // Used to count the number of books the user has read this month
		int countPages = 0;                 // used to count the number of pages the user has read this month
		int countYear = 0;                  // Used to count the number of books the user has read this year
		LocalDate today = LocalDate.now();  // Used to compare this months with each book
		
		for(Book b : books.getAll()) {
			
			if(!(b.getEndDate().equalsIgnoreCase("NA"))) {
				
				// Create a LocalDate object using date that the book was finished
				LocalDate bookDate = LocalDate.parse(b.getEndDate());
				
				// Increment countMonth if the book was read this month
				if(bookDate.getMonth().equals(today.getMonth()) && bookDate.getYear() == today.getYear()) {
					countBooks++;
				}
				
				// Increment countYear if the book was read this year
				if(bookDate.getYear() == today.getYear()) countYear++;
				
			}
		}
		
		// Cycle through all of the users reading data
		for(Map.Entry<LocalDate, Double> et : readingData.entrySet()) {
			// Add page value if the dates month matches this month
			if(today.getMonth().equals(et.getKey().getMonth()) && today.getYear() == et.getKey().getYear()) {
				
				countPages += et.getValue();
			}
		}
		
		arr[0] = countBooks;   // Add the number of books read this month to the array
		arr[1] = countPages;   // Add the number of pages read this month to the array
		arr[2] = countYear;    // Add the number of books read this year to the array
		// Return the array
		return arr;
	}
	
	/**
	 * Saves the current Library of Book objects to the file
	 * defined as apart of Main
	 * @param lib Library object that holds the Book data that is to be saved
	 * to the data file.
	 * @return True if save was successful. False otherwise
	 */
	public boolean save(Library lib) {
		
		lib.sortByTitle();                       // Sort by title before saving
		
		FileWriter fw;                           // Used to Write to file
		BufferedWriter bw;                       // Used with fw to write to file
		StringBuilder sb = new StringBuilder();  // Used to build string before writing to file
		boolean success = true;                  // Flag used to see whether or not save was successful
		
		try {
			
			fw = new FileWriter(this.DATA_FILE);
			bw = new BufferedWriter(fw);
			
			for(int i = 0; i < lib.size(); i++) {
				
				// Get the book at the current position
				Book temp = lib.get(i);
				
				// Append all of the books attributes into the string builder
				sb.append(temp.getTitle());
				sb.append("@!@");
				sb.append(temp.getAuthor());
				sb.append("@!@");
				sb.append(temp.getSeries());
				sb.append("@!@");
				sb.append(temp.getNumPages());
				sb.append("@!@");
				sb.append(temp.getWordCount());
				sb.append("@!@");
				sb.append(temp.getStartDate());
				sb.append("@!@");
				sb.append(temp.getEndDate());
				sb.append("\n");
				
				// Write the stringBuilder to file
				bw.write(sb.toString());
				sb.setLength(0);  // Clear the StringBuilder
			}
			
			bw.close();  // Close the BufferedWriter
			
		}
		catch(IOException e) {
			System.out.println("Error reading from the file: " + this.DATA_FILE);
			System.out.println("Exiting, all data might not have been saved.");
			success = false;
		}
		
		// Print success message if no errors were thrown
		if(success) {
			System.out.println("PASS: Saved data to file successfully");
		}
		// Return success boolean
		return success;

	}  //save()
	
	/**
	 * Loads the Book Data from the file defined as apart of 
	 * the Main object into a Library object and then returns
	 * that Library object
	 * @return Library object containing all of the Book data in the data file.
	 */
	public Library load() {
		
		Library lib = new Library();  // Used to store the books being loaded
		FileReader fr;                // Used to read from the file
		BufferedReader br;            // Used to read from the file
		String line;                  // Used to store the incoming line
		boolean success = true;       // Flag to test whether or not load was successful
		
		try {
			
			// Initialize the FileReader and BufferedReader
			fr = new FileReader(this.DATA_FILE);
			br = new BufferedReader(fr);
			
			// Read from file until file is empty
			while((line = br.readLine()) != null) {
				
				// Split the line by the delimiter
				String[] currentBook = line.split("@!@");
				
				// get all of the relevant attributes for a book
				String title = currentBook[0];
				String author = currentBook[1];
				String series = currentBook[2];
				int numPages = Integer.parseInt(currentBook[3]);
				int wordCount = Integer.parseInt(currentBook[4]);
				String startDate = currentBook[5];
				String endDate = currentBook[6];
				
				// Create a Book object using the above values
				Book newBook = new Book(title, author, series, numPages, wordCount, startDate, endDate);
				// Add the new book to the library
				lib.add(newBook);
			}
			
			br.close();
			
		}
		catch(FileNotFoundException e) {
			System.out.println("ERROR: Data file could not found");
			success = false;
		}
		catch(IOException e) {
			System.out.println("IO ERROR: There was a problem while trying to read from the data file");
			success = false;
		}
		// Print success message if no errors were thrown
		if(success) {
			System.out.println("PASS: Data was loaded successfully");
		}
		
		// return the populated library
		return lib;
		
	}  // load()
	
	/**
	 * Saves the reading data that the user inputs from the 
	 * loadReading view.
	 * @param date A string representing the Date that the
	 * user is entering reading data for.
	 * @param numPages The number of pages that the user read.
	 * @return true if saving was a success. false otherwise.
	 */
	public boolean saveReadData(TreeMap<LocalDate, Double> readingData) {
		
		FileWriter fw;
		BufferedWriter bw;
		StringBuilder sb = new StringBuilder();
		boolean success = true;
		
		try {
			
			fw = new FileWriter(this.READ_FILE);
			bw = new BufferedWriter(fw);
			
			for(Map.Entry<LocalDate, Double> et : readingData.entrySet()) {
				LocalDate key = et.getKey();
				
				sb.append(key.getDayOfWeek());
				sb.append("@!@");
				sb.append(key.getMonth());
				sb.append("@!@");
				sb.append(key.getDayOfMonth());
				sb.append("@!@");
				sb.append(key.getYear());
				sb.append("@!@");
				sb.append(et.getValue());
				sb.append("\n");
				
				bw.write(sb.toString());
				sb.setLength(0);
			}
			
			bw.close();
			
		}
		catch(IOException e) {
			System.out.println("IO ERROR: There was a problem when writing from the data file");
			success = false;
		}
		catch(NumberFormatException e) {
			System.out.println("ERROR: The Date or number of pages written is invalid");
			success = false;
		}
		catch(DateTimeException e) {
			System.out.println("ERROR: Something went wrong while trying to create the date");
			success = false;
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println("ERROR: Use the date picker to pick a date");
			success = false;
		}
		
		return success;  // Return the final boolean value
		
	}  // saveReadData()
	
	/**
	 * Loads the reading data from file and stores it into an ArrayList
	 * @return An ArrayList of type String that holds all of the reading data
	 */
	public TreeMap<LocalDate, Double> loadReadingData() {
		
		// Create the TreeMap to be returned
		TreeMap<LocalDate, Double> readingMap = new TreeMap<LocalDate, Double>();
		
		FileReader fr;       // Used to access the data file
		BufferedReader br;   // Used to read each line from the file
		String line;         // Used to store each line from the file as a String
		
		try {
			
			// Initialize the FileReader and BufferedReader
			fr = new FileReader(this.READ_FILE);
			br = new BufferedReader(fr);
			
			// Read from file until it is empty
			while((line = br.readLine()) != null) {
				// Split the line
				String[] split = line.split("@!@");
				// Create the local Date object
				LocalDate tempDate = LocalDate.of(Integer.parseInt(split[3]), 
						Month.valueOf(split[1]), 
						Integer.parseInt(split[2]));
				
				// Get the pages read for the current line
				double pageVal = Double.parseDouble(split[4]);
				
				// Check if the date is already in the TreeMap
				if(readingMap.containsKey(tempDate)) {
					
					double newVal = readingMap.get(tempDate) + 
							Double.parseDouble(split[4]);       // Calculate new value based on stored val and incoming val
					readingMap.replace(tempDate, newVal);       // Associate date with new val
				} else {
					readingMap.put(tempDate, Double.parseDouble(split[4])); // add date with val if it is not in the map
				}
				
			}
			
		}
		catch(IOException e) {
			System.out.println("ERROR: Something went wrong while trying to read from the data file.");
		}
		
		return readingMap;  // Return the ArrayList
		
	}
	
} // Main extends Application
