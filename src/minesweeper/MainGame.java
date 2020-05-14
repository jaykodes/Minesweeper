/* Minesweeper in the Java Console
By Jay, Abdul, Kalapan
Mr. Krnic
April 5, 2019 */

package minesweeper;
import java.util.*;
import java.io.*;

/* Here is how everything works in the game:
 * 2D Array: used for the display grid. The display grid is a 2d array that displays the users's action on the screen
 * 2D Array: used for the item grid. The itemGrid is a 2d array that has the contains the mines and the numbers of mines bordering a square
 * 2D Array: used for the mine grid. the mineGrid is a 2d array that contains the mines and the user's first position
 * Used all three 2d array to find interact throughout the game
 * 1D Array: used to get and store multiple return value. Useful in terms of keeping the number of functions down
 * Arrays are constantly updating
 * Scanner is used to get the users option in menus
 * File IO is used for the user registration, sign users up, look at user account, and leaderboard
 * Read file to login, get leaderboard score, and user info
 * Write file to add user to system, write new scores
 * read from the file during the log in, reads from the file during the leaderboard and account actions
 * repetition is used to go through 2D array, update file, etc
 * selection is used to check if spots contain mine, flags, etc
 * numerous repetitions and selections
 */

public class MainGame {
	
	/************************************
	 * Array that holds the master file *
	 ************************************/
	public static String[][] masterFile = {};//holds all of the users
	/***********************************
	 * Array that holds an empty value *
	 ***********************************/
	public static final String[][] tempEmpty = {};//an empty array
	public static String username = "";//the user's name
	public static String password = "";//the user's password
	public static int indexPosition = 0;//the index position
	//variables that are the essentials and change how the game is randomized
	//height and width are the dimensions of the game
	//numberMines is the amount of mines that will be in the grid
	public static int height, width, numberMines;
	/**********************************************
	 * Array that displays the grid on the screen *
	 **********************************************/
	//the grid that the user sees
	public static String[][] gameGrid = {};
	/******************************************
	 * Array that holds all mines and numbers *
	 ******************************************/
	//the grid that checks user's input
	public static String[][] itemGrid = {};
	/************************************************
	 * Array that holds user's first move and mines *
	 ************************************************/
	//the grid that holds the mine, the user's first move, and the blocks around the user's first move
	public static String[][] mineGrid = {};
	//a variable that shows if the user can continue playing or not
	public static boolean alive;
	//a variable that holds the start time
	public static long startTime;
	//a variable that holds the difficulty the user has chose
	public static String difficulty;
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//This function will clear the console window
	public static void clearScreen(){
		//Create try-catch get the system property
	    try {
	    	/*************************
	    	 * Selection to check os *
	    	 *************************/
	        if (System.getProperty("os.name").contains("Windows"))//Simple way to check if it's running on a Windows machine, which would mean CMD.
	            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();//This sends a "cls" command to CMD, which clears the screen.
	        else
	            Runtime.getRuntime().exec("clear");//If it's not on Windows, try clearing it a different way.
	    } catch (IOException | InterruptedException ex) {};
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//function that is the beginning and where all of the resets will happen
	public static void beginning() {
		/***********************************
		 * Scanner to ask user for options *
		 ***********************************/
		//scanner item used to get input from the user of which action they want to take
		Scanner scan = new Scanner(System.in);
		//a string that holds the user value
		String userOption = "";
		
		/************************************
		 * Repetition that asks for options *
		 ************************************/
		//loops while true
		while (true) {
			//clear screen
			clearScreen();
			//displays title
			title();
			//user login
			System.out.println("Enter 1 to Log In");
			//user sign up
			System.out.println("Enter 2 to Sign-Up");
			
			//gets the user input
			userOption = scan.nextLine();
			
			/*****************************
			 * Selection of user options *
			 *****************************/
			//if user clicks 1 then have them log in
			if (userOption.equals("1")) {
				loginSystem();
				break;
			}
			//if user clicks 2 then have them sign up
			else if (userOption.equals("2")) {
				signUpSystem();
				break;
			}
			//user did not choose one or two
			else {
				System.out.println("Not an Option");
				//clear the screen
			}
		}
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//function to allow only registered members to play the game
	public static void loginSystem() {
		/***********************************
		 * Scanner to ask user for options *
		 ***********************************/
		//scanner item used to get input from the user of which action they want to take
		Scanner scan = new Scanner(System.in);
		//boolean that holds if the person has been found
		boolean personFound = false;
		
		/******************
		 * Updating array *
		 ******************/
		//masterFile gets the most up to date version of itself
		masterFile = readDataFile(tempEmpty);
		//ask for username
		System.out.println("Enter your username: ");
		//hold it in the variable
		username = scan.nextLine();
		//ask for password
		System.out.println("Enter your password: ");
		//hold it in the variable
		password = scan.nextLine();
		
		/*******************************************
		 * Repetition that goes through a 2D Array *
		 *******************************************/
		//goes through the 2d array of masterFile
		for (int i = 0; i < masterFile.length; i++) {
			/*****************************************************
			 * Selection to check is username and password match *
			 *****************************************************/
			//if the password and username match then set the variable equals to the data
			if (username.equals(masterFile[i][0]) && password.equals(masterFile[i][1])) {
				//assigns username
				username = masterFile[i][0];
				//assigns password
				password = masterFile[i][1];
				//which position the person is
				indexPosition = i;
				//personFound is now true
				personFound = true;
				//can break out of loop
				break;
			}
		}
		/******************************************
		 * Selection to check if person signed up *
		 ******************************************/
		//if person is not found wrong credentials
		if (!personFound) {
			System.out.println("Sorry but invalid credentials.");
			personFound = false;
			beginning();
		}
		//if person is have them go to the main menu
		else {
			mainMenu();
		}
	}

	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//function that allows user to sign up and join the system
	public static void signUpSystem() {
		/***********************************
		 * Scanner to ask user for options *
		 ***********************************/
		//scanner item used to get input from the user of which action they want to take
		Scanner scan = new Scanner(System.in);
		//a string that holds two items together
		String line = "";
		//boolean that holds if the person has been found
		boolean personFound = false;
		
		/******************
		 * Updating array *
		 ******************/
		//masterFile gets the most up to date version of itself
		masterFile = readDataFile(tempEmpty);
		//ask for username
		System.out.println("What would you like your username to be? ");
		//hold it in the variable
		username = scan.nextLine();
		
		/*******************************************
		 * Repetition that goes through a 2D array *
		 *******************************************/
		//goes through the 2d array of masterFile
		for (int i = 0; i < masterFile.length; i++) {
			/******************************************
			 * Selection to check is username matches *
			 ******************************************/
			//if the username is found then the person has already signed up with the same username
			if (username.equals(masterFile[i][0])) {
				//personFound is now true
				personFound = true;
				//can break out of loop
				break;
			}
		}
		
		/***************************************************
		 * Seleciton to check if username is already taken *
		 ***************************************************/
		//if person is found username taken
		if (personFound) {
			System.out.println("Sorry but the username has already been taken.");
			beginning();
		}
		//if person not found username not taken
		else {
			//ask for password
			System.out.println("What would you like your password to be? ");
			//hold it in the variable
			password = scan.nextLine();
			//line that joins username and password
			line = username + ":" + password;
			//adds line to the masterFile
			updateMasterFile(line);
			//updates the data file
			updateDataFile();
			//makes an unique file for the user
			userFileMaker();
			//call the main menu function
			mainMenu();
		}
	}
	
	/*******************************************************
	 * Function that takes a parameter and returns a value *
	 *******************************************************/
	//reads the data file so that MasterFile is up to date
	//the parameter is the 2D an empty array which can be manipulated
	//the return value is the 2D array with all of the data inside. MasterFile is set to the return value
	public static String[][] readDataFile(String[][] tempArray) {
		//variable that holds a line in the file
		String line = "";
		//counter for how many name in the file
		int count = 0;
		
		//Create try-catch loop to read the file
		try {
			/***************************************************
			 * File IO that reads the text file with all users *
			 ***************************************************/
			//Create buffered reader and file reader to open and read file
			BufferedReader br = new BufferedReader(new FileReader("users.txt"));
			/**************************************
			 * File IO: Read a line from txt file *
			 **************************************/
			//take out the first line of the file
			line = br.readLine();
			
			/***********************************************
			 * Repetition that read every line in txt file *
			 ***********************************************/
			//Create do while loop to loop through the file
			do {
				/**************************************
				 * File IO: Read a line from txt file *
				 **************************************/
				//Store each line in the file in the variable 'line'
				line = br.readLine();
				/**************************************
				 * Selection to check is line is null *
				 **************************************/
				//If the line is not empty or null keep looping and increase counter
				if (line != null) {
					count++;
				}
				
			} 
			//Keep looping until line reads null
			while(line != null);
			/*************************
			 * File IO: close a file *
			 *************************/
			//Close File
			br.close();
			
			/******************
			 * Updating array *
			 ******************/
			//Add 2 elements to String 2d array
			tempArray = new String[count][2];
			
			/***************************************************
			 * File IO that reads the text file with all users *
			 ***************************************************/
			//Create buffered reader and file reader to open and read file
			br = new BufferedReader(new FileReader("users.txt"));
			//take out the first line of the file
			/**************************************
			 * File IO: Read a line from txt file *
			 **************************************/
			line = br.readLine();
			//Reset count variable
			count = 0;
			
			/********************************************
			 * Repetition that read each line in a file *
			 ********************************************/
			//Create do while loop, to loop through the file
			do {
				/**************************************
				 * File IO: Read a line from txt file *
				 **************************************/
				//Store each line in the file in the variable 'line'
				line = br.readLine();
				/**************************************
				 * Selection that checks if line null *
				 **************************************/
				//If line does not equal null split the following lines in the file with ':' to read each segment in each line
				if (line != null) {
					/******************
					 * Updating array *
					 ******************/
					tempArray[count][0]= line.split(":")[0];//username
					tempArray[count][1]= line.split(":")[1];//password
					//increase counter
					count++;
				}
			} 
			//Keep looping until line reads null
			while(line != null);
			/***********************
			 * File IO: close file *
			 ***********************/
			//Close File
			br.close();
		} 
		// Catch error to prevent crashes
		catch(Exception ex){System.out.println("Error:"+ex.getMessage());}
		
		//return array for later use
		return tempArray;
	}
	
	/*******************************************************
	 * Function that takes parameters but returns no value *
	 *******************************************************/
	//updates the masterFile
	//the parameter is the new string that needs to be updated in the 2D array
	public static void updateMasterFile(String newLine) {
		//current size of the masterFile
		int currentSize = masterFile.length;
		//new size of the masterFile
		int newSize = currentSize + 1;
		/******************
		 * Updating array *
		 ******************/
		//temporary array with the new size
		String[][] tempArray = new String[newSize][2];
		
		/********************************************
		 * Repetition that iterates though 2D array *
		 ********************************************/
		//iterates through 2d array masterFile
		for (int i = 0; i < currentSize; i++) {
			for (int j = 0; j < 2; j++) {
				/*****************
				 * Copying Array *
				 *****************/
				//copies whatever is in masterFile into tempArray
				tempArray[i][j] = masterFile[i][j];
			}
		}
		
		/******************
		 * Updating array *
		 ******************/
		//can add the new info due to the increase in size
		tempArray[newSize-1][0] = newLine.split(":")[0];
		tempArray[newSize-1][1] = newLine.split(":")[1];
		
		/******************
		 * Updating array *
		 ******************/
		//have masterFile equal to the tempArray
		masterFile = tempArray;
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//function that updates the data file when a user signs up
	public static void updateDataFile() {
		//string that joins two items
		String sentence = "";
		//variable that is the first line of
		String firstLine = "Name:Password";
		
		//Create try-catch loop to write to the file
		try {
			/***************************************************
			 * File IO that writes text to file with all users *
			 ***************************************************/
			//Create a buffered writer and file writer to write to the file; erases file beforehand
			BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt", false));
			/**************************************
			 * File IO: Writes a line to txt file *
			 **************************************/
			//write the first line into the file
			bw.write(firstLine);
			
			/*******************************************
			 * Repetition that goes through a 2D array *
			 *******************************************/
			//iterates through the masterFile
			for (int i = 0; i < masterFile.length; i++) {
				//sentence formats the line before being written to the data file
				sentence = masterFile[i][0] + ":" + masterFile[i][1];
				/**************************************
				 * File IO: Writes a line to txt file *
				 **************************************/
				//write data to file
				bw.write("\r\n" + sentence);
				//reset sentence
				sentence = "";
			}
			
			/***********************
			 * File IO: close file *
			 ***********************/
			//close file
			bw.close();
			
		} 
		// Catch error to prevent crashes
		catch(Exception ex){System.out.println("Error:"+ex.getMessage());}
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//function that makes each users a txt value to hold scores
	public static void userFileMaker() {
		//Create try-catch loop to write to the file
		try {
			/************************************************************
			 * File IO that writes text to file with all user specifics *
			 ************************************************************/
			//Create a buffered writer and file writer to write to the file
			BufferedWriter bw = new BufferedWriter(new FileWriter(username + ".txt", true));
			/**************************************
			 * File IO: Read a line from txt file *
			 **************************************/
			bw.write(username);
			/***********************
			 * File IO: close file *
			 ***********************/
			//close file
			bw.close();
		} 
		// Catch error to prevent crashes
		catch(Exception ex){System.out.println("Error:"+ex.getMessage());}
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//function that holds the main menu
	public static void mainMenu() {

		/***********************************
		 * Scanner to ask user for options *
		 ***********************************/
		//scanner item used to get input from the user of which action they want to take
		Scanner scan = new Scanner(System.in);
		//a string that holds the user value
		String userOption = "";
		
		//Clear Screen
		clearScreen();
		//display title
		title();
		
		//greet user
		System.out.println("Hello " + username);
		System.out.println();//line break
		
		/*******************************************
		 * Repetition until user selects an option *
		 *******************************************/
		//infinite loop
		while (true) {
			//ask the user to complete on of the five actions on the main menu
			//the user wants an play
			System.out.println("Enter 1 to Play");
			//the user wants look at leaderboard
			System.out.println("Enter 2 to see Leaderboard");
			//the user wants to see account
			System.out.println("Enter 3 to see Account");
			//the user wants to read the instructions
			System.out.println("Enter 4 to read Instructions");
			//the user wants to quit
			System.out.println("Enter 5 to Quit");
			//gets the user input
			userOption = scan.nextLine();
			
			/*****************************
			 * Selection for user option *
			 *****************************/
			if (userOption.equals("1")) {
				difficultyMenu();
				break;
			}
			else if (userOption.equals("2")) {
				leaderboard();
				break;
			}
			else if (userOption.equals("3")) {
				account();
				break;
			}
			else if (userOption.equals("4")) {
				instructions();
				break;
			}
			else if (userOption.equals("5")) {
				beginning();
				break;
			}
			else {
				System.out.println("Not an Option");
			}
		}
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//functions that prints out the title of the game
	//prints the title "Minesweepers" in a block formation
	public static void title() {
		/*
					_                                                       
			  /\/\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __ ___ 
			 /    \| | '_ \ / _ \/ __\ \ /\ / / _ \/ _ \ '_ \ / _ \ '__/ __|
			/ /\/\ \ | | | |  __/\__ \\ V  V /  __/  __/ |_) |  __/ |  \__ \
			\/    \/_|_| |_|\___||___/ \_/\_/ \___|\___| .__/ \___|_|  |___/
			                                           |_|                  
		 */
		
		
		System.out.println("        _                                                       ");
		System.out.println("  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __ ___ ");
		System.out.println(" /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__/ __|");
		System.out.println("/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |  \\__ \\");
		System.out.println("\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|  |___/");
		System.out.println("                                           |_|                  ");
		System.out.println();
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//function to print out difficulty menu
	//user gets to choose difficulty
	public static void difficultyMenu() {
		/***********************************
		 * Scanner to ask user for options *
		 ***********************************/
		//scanner item used to get input from the user of which action they want to take
		Scanner scan = new Scanner(System.in);
		//a string that holds the user value
		String userOption = "";
		
		/****************************************
		 * Repetition until user chooses option *
		 ****************************************/
		//infinite loop
		while (true) {
			//clear screen
			clearScreen();
			//ask the user to complete on of the four actions on the difficulty menu
			System.out.println("Choose Difficulty");
			System.out.println();//line break
			//the user wants an easy difficulty
			System.out.println("Enter 1 to Choose an Easy Map");
			//the user wants a medium difficulty
			System.out.println("Enter 2 to Choose a Medium Map");
			//the user wants a hard difficulty
			System.out.println("Enter 3 to Choose a Hard Map");
			//the user quits
			System.out.println("Enter anything to go Back");
			//gets the user input
			userOption = scan.nextLine();
			
			/*****************************
			 * Selection for user option *
			 *****************************/
			//if the user chose '1' then call the play the game with easy level modifications to the grid
			if (userOption.equals("1")) {
				height = 8;
				width = 10;
				numberMines = 10;
				alive = true;
				difficulty = "easy";
				playGame();
				break;
			}
			//if the user chose '1' then call the play the game with medium level modifications to the grid
			else if (userOption.equals("2")) {
				height = 14;
				width = 18;
				numberMines = 40;
				alive = true;
				difficulty = "medium";
				playGame();
				break;
			}
			//if the user chose '1' then call the play the game with hard level modifications to the grid
			else if (userOption.equals("3")) {
				height = 20;
				width = 24;
				numberMines = 99;
				alive = true;
				difficulty = "hard";
				playGame();
				break;
			}
			//else the user's input does not match and they will need to try again
			else {
				mainMenu();
				break;
			}
		}
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//function to allow users to see the high scores
	public static void leaderboard() {
		/***********************************
		 * Scanner to ask user for options *
		 ***********************************/
		//scanner item used to get input from the user of which action they want to take
		Scanner scan = new Scanner(System.in);
		//a string that holds the user value
		String userOption = "";
		
		/****************************************
		 * Repetition until user chooses option *
		 ****************************************/
		while (true) {
			//clear screen
			clearScreen();
			//ask the user to complete on of the three actions on the main menu
			//the user wants see easy leaderboard
			System.out.println("Enter 1 to Easy Leaderboard");
			//the user wants see medium leaderboard
			System.out.println("Enter 2 to Medium Leaderboard");
			//the user wants see hard leaderboard
			System.out.println("Enter 3 to Hard Leaderboard");
			//the user wants to go back
			System.out.println("Enter anything to go back");
			//gets the user input
			userOption = scan.nextLine();
			
			/*****************************
			 * Selection for user option *
			 *****************************/
			if (userOption.equals("1")) {
				leaderboardChoose("easy");
				break;
			}
			else if (userOption.equals("2")) {
				leaderboardChoose("medium");
				break;
			}
			else if (userOption.equals("3")) {
				leaderboardChoose("hard");
				break;
			}
			else {
				mainMenu();
			}
		}
	}
	
	/*******************************************************
	 * Function that takes parameters but returns no value *
	 *******************************************************/
	//user gets to see who has the best scores in the each category
	//the parameter is the game difficulty which will help the function restrict certain possibilities
	public static void leaderboardChoose(String gameDifficulty) {
		//variable that holds a line in the file
		String line = "", lineDifficulty = "";
		//counter for how many name in the file
		int count = 0;
		String[][] leaderboardArray = {};
		
		//Create try-catch loop to read the file
		try {
			/*******************************************
			 * File IO: reads the leaderboard txt file *
			 *******************************************/
			//Create buffered reader and file reader to open and read file
			BufferedReader br = new BufferedReader(new FileReader("leaderboard.txt"));
			/***********************
			 * File IO: reads file *
			 ***********************/
			//take out the first line of the file
			line = br.readLine();
			
			/************************************************************
			 * Repetition that goes through the entire leaderboard file *
			 ************************************************************/
			//Create do while loop to loop through the file
			do {
				/***********************
				 * File IO: reads file *
				 ***********************/
				//Store each line in the file in the variable 'line'
				line = br.readLine();
				/**************************************
				 * Selection to check if line is null *
				 **************************************/
				if (line != null) {
					lineDifficulty = line.split(":")[0];
					//If the line is not empty or null keep looping and increase counter
					/*******************************************
					 * Selection to check if proper difficulty *
					 *******************************************/
					if (gameDifficulty.equals(lineDifficulty)) {
						count++;
					}
				}
			} 
			//Keep looping until line reads null
			while(line != null);
			/***********************
			 * File IO: close file *
			 ***********************/
			//Close File
			br.close();
			
			/****************
			 * Update Array *
			 ****************/
			//Add 2 elements to String 2d array
			leaderboardArray = new String[count][2];
			/*******************************************
			 * File IO: reads the leaderboard txt file *
			 *******************************************/
			//Create buffered reader and file reader to open and read file
			br = new BufferedReader(new FileReader("leaderboard.txt"));
			/***********************
			 * File IO: reads file *
			 ***********************/
			//take out the first line of the file
			line = br.readLine();
			//Reset count variable
			count = 0;
			
			/************************************************************
			 * Repetition that goes through the entire leaderboard file *
			 ************************************************************/
			//Create do while loop, to loop through the file
			do {
				/***********************
				 * File IO: reads file *
				 ***********************/
				//Store each line in the file in the variable 'line'
				line = br.readLine();
				/**************************************
				 * Selection to check if line is null *
				 **************************************/
				if (line != null) {
					/*******************************************
					 * Selection to check if proper difficulty *
					 *******************************************/
					//If line does not equal null split the following lines in the file with ':' to read each segment in each line
					if (line.split(":")[0].equals(gameDifficulty)) {
						leaderboardArray[count][0]= line.split(":")[1];//name
						leaderboardArray[count][1]= line.split(":")[2];//score
						//increase counter
						count++;
					}
				}
			} 
			//Keep looping until line reads null
			while(line != null);
			/***********************
			 * File IO: Close file *
			 ***********************/
			//Close File
			br.close();
		}
		// Catch error to prevent crashes
		catch(Exception ex){System.out.println("Error:"+ex.getMessage());}
		
		//leaderboard call function to get sorted
		leaderboardArray = sort(leaderboardArray);
		
		System.out.println("Person : Time (Sec)");
		/************************************************************************************************
		 * Repetition goes through length of array and displays the user's score from greatest to least *
		 ************************************************************************************************/
		
		//clear screen
		clearScreen();
		for (int i = 1; i <= leaderboardArray.length; i++) {
			System.out.println(i + ". " + leaderboardArray[i - 1][0] + " : " + leaderboardArray[i - 1][1]);
		}
		afterGameAction();
	}
	
	/*******************************************************
	 * Function that takes a parameter and returns a value *
	 *******************************************************/
	//sort the leaderboard
	//takes in an unsorted array
	//return a sorted array from least to greatest
	public static String[][] sort(String[][] sortArray) {
		int n = sortArray.length;
		/********************************************************
		 * Repetition that goes through the length of the array *
		 ********************************************************/
		for (int i = 0; i < n-1; i++) {
			int min = i;
			/***********************************************************************************
			 * Repetition that goes through the length of array from different start positions *
			 ***********************************************************************************/
			for (int j = i + 1; j < n; j++) {
				/****************************************************************************************
				 * Selection that checks if current number in array is smaller than the smallest before *
				 ****************************************************************************************/
				if (Integer.parseInt(sortArray[j][1]) < Integer.parseInt(sortArray[min][1])) {
					min = j;
				}
			}
			
			/******************
			 * Updating Array *
			 ******************/
			String[] temp = sortArray[min];
			sortArray[min] = sortArray[i];
			sortArray[i] = temp;
		}
		
		return sortArray;
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//functions that shows the user's info
	public static void account() {		
		//clear screen
		clearScreen();
		//prints name
		System.out.println("Name: " + username);
		//prints the number of wins the user has in each game mode
		System.out.println("Number of Wins Easy: " + accountStuff("easy"));
		System.out.println("Number of Wins Medium: " + accountStuff("medium"));
		System.out.println("Number of Wins Hard: " + accountStuff("hard"));
		afterGameAction();
	}
	
	/*******************************************************
	 * Function that takes a parameter and returns a value *
	 *******************************************************/
	//a function that returns the amount of each difficulty
	//the parameter is the game difficulty which will help the function restrict certain possibilities
	public static String accountStuff(String gameDifficulty) {
		//variable that holds a line in the file
		String line = "", lineDifficulty = "";
		//counter for how many of each difficulty in the file
		int count = 0;
		try {
			/***************************************************
			 * File IO that read text to file with leaderboard *
			 ***************************************************/
			//Create buffered reader and file reader to open and read file
			BufferedReader br = new BufferedReader(new FileReader(username + ".txt"));
			/***********************
			 * File IO read a line *
			 ***********************/
			//take out the first line of the file
			line = br.readLine();
			/***********************************************
			 * Repetition that read every line in txt file *
			 ***********************************************/
			//Create do while loop to loop through the file
			do {
				/***********************
				 * File IO read a line *
				 ***********************/
				//Store each line in the file in the variable 'line'
				line = br.readLine();
				/**************************************
				 * Selection to check if line is null *
				 **************************************/
				if (line != null) {
					lineDifficulty = line.split(":")[0];
					/*******************************************
					 * Selection to check if proper difficulty *
					 *******************************************/
					//If the line is not empty or null keep looping and increase counter
					if (gameDifficulty.equals(lineDifficulty)) {
						count++;
					}
				}
			} while (line != null);
			/***********************
			 * File IO: Close file *
			 ***********************/
			//close file
			br.close();
		}
		// Catch error to prevent crashes
		catch(Exception ex){System.out.println("Error:"+ex.getMessage());}
		
		return count + " ";
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//a function that prints out the instructions
	public static void instructions() {
		//clear screen
		clearScreen();
		System.out.println("In order to win the game, you must reveal all the squares that DO NOT contain a mine and \n"
				+ "must flag (P) on each mine. If a mine is revealed, the player loses. \n" + 
				"Type in any coordinate to begin. Hopefully, a large portion of the grid will be revealed. \n"
				+ "If it isn't, just keep trying until it does or until you lose, in which case start a new game (or ragequit). \n" + 
				"The number displayed in each square represents how many mines are adjacent to that square. \n"
				+ "There are a total of 8 adjacent squares per square (left, right, up, down and one at each corner). \n" + 
				"Grey squares surrounded by ones are always the easiest :D .The two grey squares below the flag may be clicked safely. \n" + 
				"Go through the game taking into account how many mines may be adjacent to the squares and try to unravel all the squares \n"
				+ "without clicking the mines to win. If you suspect a mine anywhere on the grid you must place a flag(P) to indicate there is a mine.\n" + 
				"");
		afterGameAction();
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//play function that runs the game
	public static void playGame() {
		/***********************************
		 * Scanner to ask user for options *
		 ***********************************/
		//scanner item used to get input from the user of which action they want to take
		Scanner scan = new Scanner(System.in);
		//a string that holds the user value
		String userOption = "";
		//a boolean that checks if the user has won the game
		boolean win = false;
		//long variables that hold the total amount of time spent on the round
		long timePlayed;
		
		/*********************
		 * Update array size *
		 *********************/
		//set up the grid width and height
		gameGrid = new String[height][width];
		itemGrid = new String[height][width];
		mineGrid = new String[height][width];
		
		//clear screen
		clearScreen();
		//calls the first move function
		firstMove();
		
		/*******************************
		 * Repetition while user alive *
		 *******************************/
		//loops while the user is able to play
		while (alive) {
			//clear screen
			clearScreen();
			//update the grid so the most up to date grid can be shown
			updateGrid();
			
			//check if the user has won
			win = checkWin();
			/**********************************
			 * Selection to check if user won *
			 **********************************/
			//if the user has won the user can exit the game and the program will come out of the loop
			if (win) {
				//alive is set to false so the loop can die
				alive = false;
				//break out of the loop
				break;
			}
			
			//ask the user to complete one of the three actions
			System.out.println();
			//the user wants to clear a block
			System.out.println("Enter 1 to Clear a Block");
			//the user wants to place down or replace a flag
			System.out.println("Enter 2 to Place/Remove a Flag");
			//the user quits
			System.out.println("Enter 3 to Quit");
			//gets the user input
			userOption = scan.nextLine();
			
			/*****************************
			 * Selection for user option *
			 *****************************/
			//if the user chose '1' then call the clearBlock function
			if (userOption.equals("1")) {
				clearBlock();
			}
			//if the user chose '2' then call the placeFlag function
			else if (userOption.equals("2")) {
				placeFlag();
			}
			//if the user chose '3' then call let alive be false
			else if (userOption.equals("3")) {
				alive = false;
				//break out of the loop
				break;
			}
			//else the user's input does not match and they will need to try again
			else {
				System.out.println("Not an Option");
			}
		}
		
		System.out.println();//line break
		//total amount in seconds
		timePlayed = (System.currentTimeMillis() - startTime) / 1000;
		
		/**********************************
		 * Selection to check if user won *
		 **********************************/
		//user won
		if (win) {
			//clear screen
			clearScreen();
			//print that they win
			System.out.println("You win");
			//print their time
			System.out.println("Time: " + timePlayed + " seconds");
			//add to personal file
			personalFile(username, timePlayed);
			//add to leaderboard file
			leaderboardFile(username, timePlayed);
			//have the user choose what they want to do next
			afterGameAction();
		}
		
		//user lost
		else {
			//clear screen
			clearScreen();
			//print that they lose
			System.out.println("You Lose");
			afterGameAction();
		}
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//function that allows user to go back to the main menu
	public static void afterGameAction() {
		/***********************************
		 * Scanner to ask user for options *
		 ***********************************/
		//scanner item used to get input from the user of which action they want to take
		Scanner scan = new Scanner(System.in);
		//a string that holds the user value
		String userOption = "";
		
		//ask the user to complete one of the three actions
		System.out.println();
		//the user wants to go back to menu
		System.out.println("Enter anything to go to Main Menu");
		//the user wants to quit
		System.out.println("Enter 2 to Quit");
		//gets the user input
		userOption = scan.nextLine();
		
		/*************************************
		 * Selection to check if user option *
		 *************************************/
		if (userOption.equals("2")) {
			clearScreen();
			beginning();
		}
		else {
			clearScreen();
			mainMenu();
		}
	}
	
	/*******************************************************
	 * Function that takes parameters but returns no value *
	 *******************************************************/
	//function that adds the user's time to their own file
	//the parameters properly input the user's name and score
	public static void personalFile(String user, long score) {
		//Create try-catch loop to write to the file
		try {
			/*************************************************
			 * File IO that makes a unique txt file for user *
			 *************************************************/
			//Create a buffered writer and file writer to write to the file
			BufferedWriter bw = new BufferedWriter(new FileWriter(user + ".txt", true));
			/***********************
			 * File IO: write line *
			 ***********************/
			bw.write("\r\n" + difficulty + ":" + score);
			/***********************
			 * File IO: close file *
			 ***********************/
			//close file
			bw.close();
		} 
		// Catch error to prevent crashes
		catch(Exception ex){System.out.println("Error:"+ex.getMessage());}
	}
	
	/*******************************************************
	 * Function that takes parameters but returns no value *
	 *******************************************************/
	//function that adds the user's score to their own file
	//the parameters properly input the user's name and score
	public static void leaderboardFile(String user, long score) {
		//Create try-catch loop to write to the file
		try {
			/***********************************************
			 * File IO that writes to the leaderboard file *
			 ***********************************************/
			//Create a buffered writer and file writer to write to the file
			BufferedWriter bw = new BufferedWriter(new FileWriter("leaderboard.txt", true));
			/***********************
			 * File IO: write line *
			 ***********************/
			bw.write("\r\n" + difficulty + ":" + user + ":" + score);
			/***********************
			 * File IO: close file *
			 ***********************/
			//close file
			bw.close();
		} 
		// Catch error to prevent crashes
		catch(Exception ex){System.out.println("Error:"+ex.getMessage());}
	}

	/****************************************************
	 * Function that returns a value with no parameters *
	 ****************************************************/
	//function that checks if the player win
	//returns if the user has won or not
	public static boolean checkWin() {
		/******************
		 * Make new array *
		 ******************/
		//two 2d arrays that represent the two cases
		String[][] caseOne = new String[height][width], caseTwo = new String[height][width];
		//boolean that holds if the person has won or not
		boolean win = false;
		
		/*******************************************
		 * Repetition that goes through a 2D array *
		 *******************************************/
		//goes through every position in itemGrid
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				/*************************************************
				 * Selection to check is current position is "M" *
				 *************************************************/
				//if position in itemGrid is a mine turn
				if (itemGrid[i][j].equals("[M]")) {
					/****************
					 * Update Array *
					 ****************/
					//for case one the mine turns into a "?"
					caseOne[i][j] = "[?]";
					//for case two the mine turns into a "P" (flag)
					caseTwo[i][j] = "[P]";
				}
				//if not a mine both case take the value of the current position in itemGrid
				else {
					/****************
					 * Update Array *
					 ****************/
					caseOne[i][j] = itemGrid[i][j];
					caseTwo[i][j] = itemGrid[i][j];
				}
			}
		}
		
		/*********************************************
		 * Selection to check if arrays are the same *
		 *********************************************/
		//if the the display grid equals to one of the case then the user wins
		if (Arrays.deepEquals(gameGrid, caseOne) || Arrays.deepEquals(gameGrid, caseTwo)) {
			//turn win into true
			win  = true;
		}
		
		return win;//return win value
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//the first move in mine sweeper is different than all the other moves as it is the only move 
	//where you cannot die
	//function that executes the first move of the game
	public static void firstMove() {
		/********************************
		 * 1D array to get return value *
		 ********************************/
		//an array that holds the return values
		String[] arrayReturner = {};
		//a string that acts like a boolean to tell is user input is valid
		String possibleMove = "";
		//two integers that hold user's block y and x positions
		int gridY = 0, gridX = 0;
		
		//create the base game
		createGameGrid();
		//display the most up to date grid
		updateGrid();
		
		/*****************************************************************************
		 * Repetition that keeps asking user for coordinates until properly inputted *
		 *****************************************************************************/
		//loop until user satisfies the input parameters
		do {
			//calls function and stores data in variable
			arrayReturner = askUserInput();
			//the first element of the return value will tell if user has properly inputed their first block
			possibleMove = arrayReturner[0];
			//clear screen
			clearScreen();
			updateGrid();
		} 
		//continues iterating while 'false'
		while (!possibleMove.equals("true"));
		
		//holds the y position of the user's first choice
		gridY = (Integer.parseInt(arrayReturner[1])) - 1;
		//holds the x position of the user's first choice
		gridX = (arrayReturner[2].toUpperCase().charAt(0)) - 65;
		//creates the item grid making sure no mine goes in the user's first block
		createItemGrid(gridY, gridX);
		//the timer has officially started
		startTime = System.currentTimeMillis();
		//finds the zeroes and numbers bordering the zeros and displays them
		findZero(gridY, gridX);
	}
	
	/*******************************************************
	 * Function that takes parameters but returns no value *
	 *******************************************************/
	//checks if the user's first choice has any block bordering on the row above
	//the parameter is the current position of the block. Both the y and x position
	public static void firstMoveTop(int userFirstY, int userFirstX) {
		//[x-1][   x  ][x+1]
		//     [actual]
		
		/*****************************************
		 * Selection to check if top row in grid *
		 *****************************************/
		//checks if the row on top is not in the grid
		if ((userFirstY - 1) >= 0) {
			/********************************************
			 * Selection to check is going left in grid *
			 ********************************************/
			//checks if [x-1] is in the grid, if so it is bordering the first user block
			if ((userFirstX - 1) >= 0) {
				mineGrid[userFirstY - 1][userFirstX - 1] = "[F1]";
				itemGrid[userFirstY - 1][userFirstX - 1] = "[F1]";
			}
			//[x] is bordering the first user block
			mineGrid[userFirstY - 1][userFirstX] = "[F1]";
			itemGrid[userFirstY - 1][userFirstX] = "[F1]";
			/*********************************************
			 * Selection to check is going right in grid *
			 *********************************************/
			//checks if [x+1] is in the grid, if so it is bordering the first user block
			if((userFirstX + 1) < width) {
				mineGrid[userFirstY - 1][userFirstX + 1] = "[F1]";
				itemGrid[userFirstY - 1][userFirstX + 1] = "[F1]";
			}
		}
	}
	
	/*******************************************************
	 * Function that takes parameters but returns no value *
	 *******************************************************/
	//checks if the user's first choice has any block bordering on the same row
	//the parameter is the current position of the block. Both the y and x position
	public static void firstMoveMiddle(int userFirstY, int userFirstX) {
		//[x-1][actual][x+1]
		
		/********************************************
		 * Selection to check is going left in grid *
		 ********************************************/
		//checks if [x-1] is in the grid, if so it is bordering the first user block
		if ((userFirstX - 1) >= 0) {
			mineGrid[userFirstY][userFirstX - 1] = "[F1]";
			itemGrid[userFirstY][userFirstX - 1] = "[F1]";
		}
		/*********************************************
		 * Selection to check is going right in grid *
		 *********************************************/
		//checks if [x+1] is in the grid, if so it is bordering the first user block
		if((userFirstX + 1) < width) {
			mineGrid[userFirstY][userFirstX + 1] = "[F1]";
			itemGrid[userFirstY][userFirstX + 1] = "[F1]";
		}
	}
	
	/*******************************************************
	 * Function that takes parameters but returns no value *
	 *******************************************************/
	//checks if the user's first choice has any block bordering on the row below
	//the parameter is the current position of the block. Both the y and x position
	public static void firstMoveBottom(int userFirstY, int userFirstX) {
		//	   [actual]
		//[x-1][   x  ][x+1]
		
		/********************************************
		 * Selection to check if bottom row in grid *
		 ********************************************/
		//checks if the row below is not in the grid
		if ((userFirstY + 1) < height) {
			/********************************************
			 * Selection to check is going left in grid *
			 ********************************************/
			//checks if [x-1] is in the grid, if so it is bordering the first user block
			if ((userFirstX - 1) >= 0) {
				mineGrid[userFirstY + 1][userFirstX - 1] = "[F1]";
				itemGrid[userFirstY + 1][userFirstX - 1] = "[F1]";
			}
			//[x] is bordering the first user block
			mineGrid[userFirstY + 1][userFirstX] = "[F1]";
			itemGrid[userFirstY + 1][userFirstX] = "[F1]";
			//checks if [x+1] is in the grid, if so it is bordering the first user block
			/*********************************************
			 * Selection to check is going right in grid *
			 *********************************************/
			if((userFirstX + 1) < width) {
				mineGrid[userFirstY + 1][userFirstX + 1] = "[F1]";
				itemGrid[userFirstY + 1][userFirstX + 1] = "[F1]";
			}
		}
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//function that dictates what will happen when the user picks a block
	public static void clearBlock() {
		/************************************
		 * 1D array that keeps return value *
		 ************************************/
		//an array that holds the return values
		String[] arrayReturner = {};
		//a string that holds the user block value
		String userBlock = "";
		//a string that acts like a boolean to tell is user input is valid
		String possibleMove = "";
		//two integers that hold user's block y and x positions
		int gridY = 0, gridX = 0;
		
		//clear screen
		clearScreen();
		updateGrid();
		/*****************************************************************************
		 * Repetition that keeps asking user for coordinates until properly inputted *
		 *****************************************************************************/
		//loop until user satisfies the input parameters
		do {
			//calls function and stores data in variable
			arrayReturner = askUserInput();
			//the first element of the return value will tell if user has properly inputed their first block
			possibleMove = arrayReturner[0];
			//clear screen
			clearScreen();
			updateGrid();
		}
		//continues iterating while 'false'
		while (!possibleMove.equals("true"));
		
		//holds the y position of the user's first choice
		gridY = (Integer.parseInt(arrayReturner[1])) - 1;
		//holds the x position of the user's first choice
		gridX = (arrayReturner[2].toUpperCase().charAt(0)) - 65;
		//holds the value of position gridY and gridX on itemGrid
		userBlock = itemGrid[gridY][gridX];
		
		/****************************************************
		 * Selection to check what type of block user chose *
		 ****************************************************/
		//if on the display grid the coordinate is a 'P' then there is a flag
		if (gameGrid[gridY][gridX].equals("[P]")) {
			//remind user there is a flag
			System.out.println("You placed a flag there remember");
		}
		//if there is a 'M' on the coordinate then find display all the mines and user dies
		else if (userBlock.equals("[M]")) {
			findMine();
			updateGrid();
			alive = false;
		}
		//if there is a '0' on the coordinate then find all of the zeros surrounding and the number bordering each zero
		else if (userBlock.equals("[0]")) {
			findZero(gridY, gridX);
		}
		//else is just a regular number and display it on the screen
		else {
			gameGrid[gridY][gridX] = userBlock;
		}
	}
	
	/*******************************************************
	 * Function that takes parameters but returns no value *
	 *******************************************************/
	//find all bordering zeros and the numbers bordering those zeros
	//the parameter is the current position of the zero block. Both the y and x position
	public static void findZero(int zeroY, int zeroX) {
		
		//Top Row     [x-1][   x  ][x+1]
		//Middle Row  [x-1][actual][x+1]
		//Bottom Row  [x-1][   x  ][x+1]
		
		/***************************************
		 * Selection to check is block in grid *
		 ***************************************/
		//checks if the block is in the grid
		if (zeroY >= 0 && zeroY < height && zeroX >= 0 && zeroX < width) {
			/************************************************************************
			 * Selection to check if block is zero and has not been checked already *
			 ************************************************************************/
			//checks if the block is zero and has not already been chosen
			if (itemGrid[zeroY][zeroX].equals("[0]") && !(gameGrid[zeroY][zeroX].equals("[0]"))) {
				//add it to the game grid to be displayed
				gameGrid[zeroY][zeroX] = "[0]";
				findZero((zeroY - 1), zeroX);//call the function again for the top [x] block
				findZero((zeroY + 1), zeroX);//call the function again for the bottom [x] block
				findZero(zeroY, (zeroX + 1));//call the function again for the middle [x-1] block
				findZero(zeroY, (zeroX - 1));//call the function again for the middle [x+1] block
				findZero((zeroY - 1), (zeroX + 1));//call the function again for the top [x-1] block
				findZero((zeroY - 1), (zeroX - 1));//call the function again for the top [x+1] block
				findZero((zeroY + 1), (zeroX + 1));//call the function again for the bottom [x-1] block
				findZero((zeroY + 1), (zeroX - 1));//call the function again for the bottom [x+1] block
			}
			//if the block is not a zero add it to the game grid to be displayed but do not call the function again
			else {
				//add it to the game block to be displayed
				gameGrid[zeroY][zeroX] = itemGrid[zeroY][zeroX];
			}
		}
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//find all the mine placements and check if the user put any flags in the wrong spot
	public static void findMine() {
		/*****************************************
		 * Repetition that goes through 2D array *
		 *****************************************/
		//goes through the 2d array
		for (int i = 0; i < height; i++) {
			for (int  j = 0; j < width; j++) {
				/*************************************************
				 * Selection to check if current position is "M" *
				 *************************************************/
				//checks if current position in the grid is a mine
				if (mineGrid[i][j].equals("[M]")) {
					//add it to the display grid
					gameGrid[i][j] = mineGrid[i][j];
				}
				/***************************************************************************
				 * Selection to check if current position is "P" and not "M" in other grid *
				 ***************************************************************************/
				if (gameGrid[i][j].equals("[P]") && !(itemGrid[i][j].equals("[M]"))) {
					gameGrid[i][j] = "[X]";
				}
			}
		}
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//function that allows user to place or remove flag
	public static void placeFlag() {
		/*********************************
		 * 1D array to keep return value *
		 *********************************/
		//an array that holds the return values
		String[] arrayReturner = {};
		//a string that acts like a boolean to tell is user input is valid
		String possibleMove = "";
		//two integers that hold user's flag y and x positions
		int gridY = 0, gridX = 0;
		
		//clear screen
		clearScreen();
		updateGrid();
		/*****************************************************************************
		 * Repetition that keeps asking user for coordinates until properly inputted *
		 *****************************************************************************/
		//loop until user satisfies the input parameters
		do {
			//calls function and stores data in variable
			arrayReturner = askUserInput();
			//the first element of the return value will tell if user has properly inputed their first block
			possibleMove = arrayReturner[0];
			//clear screen
			clearScreen();
			updateGrid();
		}
		//continues iterating while 'false'
		while (!possibleMove.equals("true"));
		
		//holds the y position of the user's first choice
		gridY = (Integer.parseInt(arrayReturner[1])) - 1;
		//holds the x position of the user's first choice
		gridX = (arrayReturner[2].toUpperCase().charAt(0)) - 65;
		
		/**************************************************
		 * Selection to check if user already placed flag *
		 **************************************************/
		//if the coordinate already has a flag on it remove the flag
		if (gameGrid[gridY][gridX].equals("[P]")) {
			//replace flag with question mark
			gameGrid[gridY][gridX] = "[?]";
		}
		//else there is no flag on the coordinate
		else {
			//add flag to the display grid
			gameGrid[gridY][gridX] = "[P]";
		}
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//function that fills the game grid with "?"
	public static void createGameGrid() {
		/*****************************************
		 * Repetition that goes through 2D array *
		 *****************************************/
		//goes through the 2d array
		for (int i = 0; i < (height); i++) {
			for (int j = 0; j < (width); j++) {
				//assigns the current position of the game grid to a '?'
				gameGrid[i][j] = "[?]";
			}
		}
	}
	
	/*******************************************************
	 * Function that takes parameters but returns no value *
	 *******************************************************/
	//function that gets the numbers and mines on the itemGrid
	//the parameter is the current position of the user's first choice. Both the y and x position
	public static void createItemGrid(int userFirstY, int userFirstX) {
		//random function to randomize mine position
		Random randomCoor = new Random();
		//boolean to check if square has mine or the mine cannot be placed in that specific square
		boolean taken = true;
		//integers that holds the mine x and y position
		int mineX = 0, mineY = 0;
		//integer that is a counter for the total amount of mines to be around a specific square
		int mineAround = 8;
		
		/*****************************************
		 * Repetition that goes through 2D array *
		 *****************************************/
		//goes through all of the elements in the mineGrid and put '[ ]' to represent empty
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				//assigns the current position of the game grid to an empty
				mineGrid[i][j] = "[ ]";
			}
		}
		
		//assigns "F" to a position in the grid to the user's first choice
		mineGrid[userFirstY][userFirstX] = "[F]";
		itemGrid[userFirstY][userFirstX] = "[F]";
		//assigns "F1" to the positions bordering the user's first choice
		firstMoveTop(userFirstY, userFirstX);//squares on the row above
		firstMoveMiddle(userFirstY, userFirstX);//squares on the same row
		firstMoveBottom(userFirstY, userFirstX);//squares on the row below
		
		/********************************************
		 * Repetition that goes the number of mines *
		 ********************************************/
		//puts mine into mineGrid randomly
		for (int i = 0; i < numberMines; i++) {
			/********************************************************************************
			 * Repetition repeats while there is a mine or too close to user's first choice *
			 ********************************************************************************/
			//loops while the mines cannot be placed
			while (taken) {
				//mine's random y position
				mineY = randomCoor.nextInt(height);
				//mine's random x position
				mineX = randomCoor.nextInt(width);
				
				/********************************************************************************
				 * Selection to check if mine already there or too close to user's first choice *
				 ********************************************************************************/
				//if the mine is not in the same place as the user's first choice
				//is not bordering the user's first choice
				//or not in the same position as another mine then a possible place for the mine
				if (itemGrid[mineY][mineX] != "[F1]" && itemGrid[mineY][mineX] != "[F]" && itemGrid[mineY][mineX] != "[M]") {
					taken = false;
				}
			}
			
			/****************
			 * Update Array *
			 ****************/
			//put the mine in the position the position
			itemGrid[mineY][mineX] = "[M]";
			mineGrid[mineY][mineX] = "[M]";
			//resets the taken value
			taken = true;
		}
		
		/*****************************************
		 * Repetition that goes through 2D array *
		 *****************************************/
		//iterates through each position in the 2D array and checks how many mines are bordering the square
		for (int i = 0; i < height; i++) {
			for (int  j = 0; j < width; j++) {
				/****************************************
				 * Checks if current position is a mine *
				 ****************************************/
				//if there is no mine in the position check if any bordering mines subtract from total possibility
				if (mineGrid[i][j] != "[F]" && mineGrid[i][j] != "[M]") {
					//checks if there are any mines in the top row
					mineAround -= checkTopRow(i, j);
					//checks if there are any mines in the middle row
					mineAround -= checkMiddleRow(i, j);
					//checks if there are any mines in the bottom row
					mineAround -= checkBottomRow(i, j);
					//add it to the item grid
					itemGrid[i][j] = "[" + mineAround + "]";
					//reset mine count
					mineAround = 8;
				}
			}
		}
		
		/****************
		 * Update Array *
		 ****************/
		//has the user's items to be a "0"
		itemGrid[userFirstY][userFirstX] = "[0]";
	}
	
	/*******************************************************
	 * Function that takes a parameter and returns a value *
	 *******************************************************/
	//checks if the block has any mine bordering on the row above
	//the parameter is the current position of the block. Both the y and x position
	public static int checkTopRow(int yPos, int xPos) {
		int decrease = 0;//holds the total amount to decrease
		
		//[x-1][   x  ][x+1]
		//     [actual]
		
		/*****************************************
		 * Selection to check if top row in grid *
		 *****************************************/
		//if the row on top is not in the grid add three to the decrease counter
		if ((yPos - 1) < 0) {
			decrease += 3;
		}
		//the row on top is in the grid
		else {
			/**************************************************************************
			 * Selection to check if left possible or if close to user's first choice *
			 **************************************************************************/
			//if [x-1] is not in the grid or does not hold a mine therefore decrease counter increase
			if ((xPos - 1) < 0 || mineGrid[yPos - 1][xPos - 1] == "[ ]" || mineGrid[yPos - 1][xPos - 1] == "[F]" || mineGrid[yPos - 1][xPos - 1] == "[F1]") {
				decrease += 1;
			}
			/****************************************************************************
			 * Selection to check if center possible or if close to user's first choice *
			 ***************************************************************************/
			//if [x] does not hold a mine therefore decrease counter increase
			if (mineGrid[yPos - 1][xPos] == "[ ]" || mineGrid[yPos - 1][xPos] == "[F]" || mineGrid[yPos - 1][xPos] == "[F1]") {
				decrease += 1;
			}
			/***************************************************************************
			 * Selection to check if right possible or if close to user's first choice *
			 ***************************************************************************/
			//if [x+1] is not in the grid or does not hold a mine therefore decrease counter increase
			if ((xPos + 1) >= width || mineGrid[yPos - 1][xPos + 1] == "[ ]" || mineGrid[yPos - 1][xPos + 1] == "[F]"  || mineGrid[yPos - 1][xPos + 1] == "[F1]") {
				decrease += 1;
			}
		}
		
		return decrease;//return total amount of empty square around
	}
	
	/*******************************************************
	 * Function that takes a parameter and returns a value *
	 *******************************************************/
	//checks if the block has any mine bordering on the same row
	//the parameter is the current position of the block. Both the y and x position
	public static int checkMiddleRow(int yPos, int xPos) {
		int decrease = 0;//holds the total amount to decrease
		
		//[x-1][actual][x+1]
		
		/**************************************************************************
		 * Selection to check if left possible or if close to user's first choice *
		 **************************************************************************/
		//if [x-1] is not in the grid or does not hold a mine therefore decrease counter increase
		if ((xPos - 1) < 0 || mineGrid[yPos][xPos - 1] == "[ ]" || mineGrid[yPos][xPos - 1] == "[F]" || mineGrid[yPos][xPos - 1] == "[F1]") {
			decrease += 1;
		}
		/***************************************************************************
		 * Selection to check if right possible or if close to user's first choice *
		 ***************************************************************************/
		//if [x+1] is not in the grid or does not hold a mine therefore decrease counter increase
		if ((xPos + 1) >= width || mineGrid[yPos][xPos + 1] == "[ ]" || mineGrid[yPos][xPos + 1] == "[F]" || mineGrid[yPos][xPos + 1] == "[F1]") {
			decrease += 1;
		}
		
		return decrease;//return total amount of empty square around
	}
	
	/*******************************************************
	 * Function that takes a parameter and returns a value *
	 *******************************************************/
	//checks if the block has any mine bordering on the row below
	//the parameter is the current position of the block. Both the y and x position
	public static int checkBottomRow(int yPos, int xPos) {
		int decrease = 0;//holds the total amount to decrease
		
		//	   [actual]
		//[x-1][   x  ][x+1]
		
		/********************************************
		 * Selection to check if bottom row in grid *
		 ********************************************/
		//if the row below is not in the grid add three to the decrease counter
		if ((yPos + 1) >= height) {
			decrease += 3;
		}
		else {
			/**************************************************************************
			 * Selection to check if left possible or if close to user's first choice *
			 **************************************************************************/
			//if [x-1] is not in the grid or does not hold a mine therefore decrease counter increase
			if ((xPos - 1) < 0 || mineGrid[yPos + 1][xPos - 1] == "[ ]" || mineGrid[yPos + 1][xPos - 1] == "[F]"  || mineGrid[yPos + 1][xPos - 1] == "[F1]") {
				decrease += 1;
			}
			/****************************************************************************
			 * Selection to check if center possible or if close to user's first choice *
			 ****************************************************************************/
			//if [x] does not hold a mine therefore decrease counter increase
			if (mineGrid[yPos + 1][xPos] == "[ ]" || mineGrid[yPos + 1][xPos] == "[F]"  || mineGrid[yPos + 1][xPos] == "[F1]") {
				decrease += 1;
			}
			/***************************************************************************
			 * Selection to check if right possible or if close to user's first choice *
			 ***************************************************************************/
			//if [x+1] is not in the grid or does not hold a mine therefore decrease counter increase
			if ((xPos + 1) >= width || mineGrid[yPos + 1][xPos + 1] == "[ ]" || mineGrid[yPos + 1][xPos + 1] == "[F]" || mineGrid[yPos + 1][xPos + 1] == "[F1]") {
				decrease += 1;
			}
		}
		
		return decrease;
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//update the grid so it displays
	public static void updateGrid() {
		//a string that holds a line
		String gridLine = "";
		//a count variable
		int count = 0;
		
		System.out.println();//line print
		createAlphabet();//prints the x coordinate part
		System.out.println();//line break
		
		/*******************************************
		 * Repetition that goes through a 2D array *
		 *******************************************/
		//goes through each row of the 2D array
		for (int i = 0; i < height; i++) {
			//holds the row number
			gridLine = " " + (i + 1);
			
			/*********************************************
			 * Repetition that adds space until length 4 *
			 *********************************************/
			//adds the necessary spaces so everything is in line
			while (gridLine.length() <= 4) {
				//adds a space until the length is four
				gridLine += " ";
				//count variable increases
				count++;
			}
			
			/*****************************************************
			 * Repetition that goes through the width of the row *
			 *****************************************************/
			//a loop that goes through width of the row
			for (int j = 0; j < width; j++) {
				//adds the position to the line
				gridLine += gameGrid[i][j];
			}
			
			/**************************************************
			 * Repetition that adds space until same as count *
			 **************************************************/
			//adds the pace at the end
			for (int j = 0; j < count; j++) {
				gridLine += " ";
			}
			//add final spaces
			gridLine += (i + 1) + " ";
			//print data
			System.out.println(gridLine);
			
			//reset count value
			count = 0;
		}
		
		System.out.println();//line print
		createAlphabet();//prints the x coordinate part
	}
	
	/***************************************************
	 * Function with no return value and no parameters *
	 ***************************************************/
	//create the alphabet coordinate system
	public static void createAlphabet() {
		String alphabetLine = "";//holds a line
		
		/*********************************************
		 * Repetition that adds space until length 4 *
		 *********************************************/
		//adds the necessary spacing so in line with the grid
		while (alphabetLine.length() <= 4) {
			alphabetLine += " ";
		}
		
		/*********************************************************************************
		 * Repetition that goes through the width of the row and give letter of alphabet *
		 *********************************************************************************/
		//add one letter of the alphabet to the coordinate system according to the width
		for (int i = 65; i < (65 + width); i++) {
			alphabetLine += " " + String.valueOf((char)i) + " ";
		}
		
		//add more spacing and print the line
		alphabetLine += "    ";
		System.out.println(alphabetLine);
	}
	
	/*******************************************************
	 * Function that takes a parameter and returns a value *
	 *******************************************************/
	//functions that asks the user for their input within the game
	//returns a string of the user's input. the first value being if user's input is valid or not
	//the second and third value being the user's y and x input
	public static String[] askUserInput() {
		/******************************
		 * Scanner to get user option *
		 ******************************/
		//scanner item to get the data from the user
		Scanner scan = new Scanner(System.in);
		//holds the x and y input from the user
		String userYInput = "", userXInput = "";
		/************************************
		 * 1D array that returns base value *
		 ************************************/
		//the array that will be return if user does not enter the data properly
		String[] returnArray = {"false", "", ""};
		//boolean that holds if the user's input is valid
		boolean possible = false;
		
		//line break
		System.out.println();
		//asks the user for the row number of the block
		System.out.println("Enter a row number (1 - " + height + ")");
		//get the user input and puts it in the variable
		userYInput = scan.nextLine();
		
		//calls the function to check if a user's input is a possible y answer
		possible = checkUserY(userYInput);
		/**********************************
		 * Selection to check if possible *
		 **********************************/
		//if a possible y answer then ask the user for an x input as well
		if (possible) {
			//asks the user for the column letter of the block
			System.out.println("Enter a coloumn number (A - " + String.valueOf((char)(64 + width)) + ")");//asks for column number
			//get the user input and puts it in the variable
			userXInput = scan.nextLine();
			
			/**********************************
			 * Selection to check if possible *
			 **********************************/
			//calls the function to check if a user's input is a possible y answer
			possible = checkUserX(userXInput);
			//if a possible x answer then reassigns the array so it returns a different set of data
			if (possible) {
				/****************
				 * Update array *
				 ****************/
				//makes the first element true
				returnArray[0] = "true";
				//makes the second element the y position guess
				returnArray[1] = userYInput;
				//makes the third element the x position guess
				returnArray[2] = userXInput.toUpperCase();
			}
			//not a possible x answer
			else {
				System.out.println("Keep it in the range");
			}
		}
		//not a possible y answer
		else {
			System.out.println("Keep it in the range");
		}
		
		return returnArray;//return the array
	}
		
	/*******************************************************
	 * Function that takes a parameter and returns a value *
	 *******************************************************/
	//function that checks if the user's y input is valid
	//the parameter is what the user inputted
	//the return value is whether or not the the input is valid
	public static boolean checkUserY(String userYPos) {
		boolean possibleMoveY = false;//holds if user's move is a possible move
		
		//try catch so when converting to the user's input into an integer there is no error
		try {
			//changes to user's input into an integer value
			int tempInt = Integer.parseInt(userYPos);
			
			/************************************************************
			 * Selection to check if number is between the height value *
			 ************************************************************/
			//checks if between 0 and the height of the grid and if so it is a possible move 
			if (tempInt > 0 && tempInt <= height) {
				possibleMoveY = true;
			}
			//user is out of the range of the grid and therefore not possible
			else {
				possibleMoveY = false;
			}
		}
		//user did not enter an integer and therefore not possible
		catch(NumberFormatException e) {
			possibleMoveY = false;
		}
		
		return possibleMoveY;//return value
	}
	
	/*******************************************************
	 * Function that takes a parameter and returns a value *
	 *******************************************************/
	//function that checks if the user's x input is valid
	//the parameter is what the user inputted
	//the return value is whether or not the the input is valid
	public static boolean checkUserX(String userXPos) {
		boolean possibleMoveX = false;//holds if user's move is a possible move
		int tempLength = userXPos.length();//the length of the user's input
		
		/***************************************
		 * Selection to check if length is one *
		 ***************************************/
		//checks if the user input has a length of one and is therefore a single character
		if (tempLength == 1) {
			//try catch so when converting to the user's input into an integer there is no error
			try {
				//gets first letter from the user's input, turns it into a capital letter, and converts to an integer
				int tempInt = userXPos.toUpperCase().charAt(0);
				
				/************************************************************
				 * Selection to check if number is between the height value *
				 ************************************************************/
				//checks if between A and the width of the gird and if so it is a possible move  
				if (tempInt >= 64 && tempInt <= (64 + width)) {
					possibleMoveX = true;
				}
				//user is out of the range of the grid and therefore not possible
				else {
					possibleMoveX = false;
				}
			}
			//user's input cannot be converted into integer and therefore not possible
			catch(NumberFormatException e) {
				possibleMoveX = false;
			}
		}
		//user did not enter a single character and therefore not possible
		else {
			possibleMoveX = false;
		}
		
		return possibleMoveX;//returns value
	}
	
	
	//main function
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//runs the beginning function
		beginning();
	}

}
