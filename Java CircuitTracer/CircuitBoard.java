import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.chrono.ThaiBuddhistEra;
import java.util.Scanner;
import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * Represents a 2D circuit board as read from an input file.
 *  
 * @author mvail, eghere
 */
public class CircuitBoard {
	//ANSI Color codes
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_CYAN = "\u001B[36m";

	public static boolean isFancy = false;

	/** current contents of the board */
	private char[][] board;
	/** location of row,col for '1' */
	private Point startingPoint;
	/** location of row,col for '2' */
	private Point endingPoint;

	//constants you may find useful
	private final int ROWS; //initialized in constructor
	private final int COLS; //initialized in constructor
	private final char OPEN = 'O'; //capital 'o'
	private final char CLOSED = 'X';
	private final char TRACE = 'T';
	private final char START = '1';
	private final char END = '2';
	private final String ALLOWED_CHARS = "OXT12";

	/** Construct a CircuitBoard from a given board input file, where the first
	 * line contains the number of rows and columns as ints and each subsequent
	 * line is one row of characters representing the contents of that position.
	 * Valid characters are as follows:
	 *  'O' an open position
	 *  'X' an occupied, unavailable position
	 *  '1' first of two components needing to be connected
	 *  '2' second of two components needing to be connected
	 *  'T' is not expected in input files - represents part of the trace
	 *   connecting components 1 and 2 in the solution
	 * 
	 * @param filename
	 * 		file containing a grid of characters
	 * @throws FileNotFoundException if Scanner cannot read the file
	 * @throws InvalidFileFormatException for any other format or content issue that prevents reading a valid input file
	 */
	public CircuitBoard(String filename) throws FileNotFoundException {
		//TODO: parse the given file to populate the char[][]
		// throw FileNotFoundException if Scanner cannot read the file
		// throw InvalidFileFormatException if any formatting or parsing issues are encountered
		Scanner fileScan = new Scanner(new File(filename));
		int rows = -1;
		int cols = -1;
		startingPoint = new Point();
		endingPoint = new Point();

		String[] line = fileScan.nextLine().split(" ");
		if (line.length != 2) {
			throw new InvalidFileFormatException("Invalid board metadata");
		}
		
		try {
			rows = Integer.parseInt(line[0]);
			cols = Integer.parseInt(line[1]);
		} catch (NumberFormatException e) {
			throw new InvalidFileFormatException("Invalid board metadata");
		}

		board = new char[rows][cols];
		boolean hasStart = false, hasEnd = false;
		Scanner lineScan;
		char current;

		//Check the contents of file for correct formatting
		for (int i = 0 ; i < rows; i++) {
			lineScan = new Scanner(fileScan.nextLine());
			for (int j = 0; j < cols; j++) {
				if (!lineScan.hasNext()) {
					lineScan.close();
					throw new InvalidFileFormatException("Incorrect row length");
				}
				
				current = lineScan.next().charAt(0);

				if (hasStart && current == START) {
					lineScan.close();
					throw new InvalidFileFormatException("Too many start points");
				} else if (current == START) {
					hasStart = true;
					startingPoint.setLocation(i,j);
				}

				if (hasEnd && current == END) {
					lineScan.close();
					throw new InvalidFileFormatException("Too many end points");
				} else if (current == END) {
					hasEnd = true;
					endingPoint.setLocation(i,j);
				}

        		if (ALLOWED_CHARS.indexOf(current) != -1) {
        			board[i][j] = current;
        		} else {
        			lineScan.close();
        			throw new InvalidFileFormatException("Invalid character: " + current);
        		}
			}

			if (lineScan.hasNext()) {
				lineScan.close();
				throw new InvalidFileFormatException("Incorrect row length");
			}
			lineScan.close();
		}

		if (!hasStart || !hasEnd) {
			throw new InvalidFileFormatException("No start or end point");
		}

		if (fileScan.hasNext()) {
			if (!fileScan.next().equals("")) {
				throw new InvalidFileFormatException("Incorrect column length");
			}
		}

		fileScan.close();
		ROWS = rows;
		COLS = cols;
		//System.out.println(this.toString());
	}

	/** Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	/** utility method for copy constructor
	 * @return copy of board array */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}
	
	/** Return the char at board position x,y
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}
	
	/** Return whether given board position is open
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open 
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}
	
	/** Set given position to be a 'T'
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}
	
	/** @return starting Point(row,col) */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}
	
	/** @return ending Point(row,col) */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}
	
	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}
	
	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}

	static public void setFancy(boolean fancy){
		isFancy = fancy;
	}

	public static boolean getFancy() {
		return isFancy;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (isFancy) {
					switch (board[row][col]) { //Color codes
						case TRACE:
							str.append(ANSI_GREEN + board[row][col] + ANSI_RESET + " ");
							break;
						case START:
							str.append(ANSI_CYAN + board[row][col] + ANSI_RESET + " ");
							break;
						case END:
							str.append(ANSI_YELLOW + board[row][col] + ANSI_RESET + " ");
							break;
						default:
							str.append(board[row][col] + " "); 
							break;
					}
				} else { 
						str.append(board[row][col] + " "); 
				}
				
			}
			str.append("\n");
		}
		return str.toString();
	}
	
}// class CircuitBoard
