import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Spliterators.AbstractDoubleSpliterator;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail, eghere
 */
public class CircuitTracer {
	//ANSI Color Codes
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN = "\u001B[32m";

	private CircuitBoard board;
	private Storage<TraceState> storage;
	private ArrayList<TraceState> bestPaths;
	/** launch the program
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		new CircuitTracer(args); //create this with args
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private void printUsage() {
		System.out.println("Usage: java CircuitTracer [-s | -q] [-c | -g] [filename] [-f delay]");
		System.out.println("-s: stack, -q: queue, -c: console, -g: gui, -f: fancy mode");
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 * @throws InterruptedException
	 */
	public CircuitTracer(String[] args) {
		if (args.length != 3) {
			if (args.length == 5 && args[3].equals("-f")) { //Checks if fancy mode flag is called. 
				CircuitBoard.setFancy(true);
			} else {
				printUsage();
				return;
			} 
		}

		switch (args[0]) {
			case "-s":
				storage = new Storage<TraceState>(Storage.DataStructure.stack);
				break;
			case "-q":
				storage = new Storage<TraceState>(Storage.DataStructure.queue);
				break; 
			default:
				printUsage();
				return;
		}

		switch (args[1]) {
			case "-g":
				System.out.println("GUI is currently unsupported, but check out "
				+ ANSI_GREEN + "fancy mode" + ANSI_RESET + "!");
				return;
			case "-c":
				search(args);
				break;
			default:
				printUsage();
				return;
		}
	}
	/** The main search algorithm for the program.
	 * 
	 * @param arguments passed from command line
	 */
	public void search(String[] args) {
		try {
			board = new CircuitBoard(args[2]);
			bestPaths = new ArrayList<TraceState>();
			int passCount = 0;

			Point startPos = board.getStartingPoint();
			int startX = (int) startPos.getX();
			int startY = (int) startPos.getY();

			//Check right
			if (board.isOpen(startX + 1, startY)) {
				storage.store(new TraceState(board, startX + 1, startY));
			}

			//Check down
			if (board.isOpen(startX, startY - 1)) {
				storage.store(new TraceState(board, startX, startY - 1));
			}

			//Check left
			if (board.isOpen(startX - 1, startY)) {
				storage.store(new TraceState(board, startX - 1, startY));
			}

			//Check up
			if (board.isOpen(startX, startY + 1)) {
				storage.store(new TraceState(board, startX, startY + 1));
			}
			
			while (!storage.isEmpty()) {
				TraceState current = storage.retrieve();
				if (current.isComplete()) {
					passCount++;
					if (bestPaths.isEmpty() || current.pathLength() == bestPaths.get(0).pathLength()) {
						bestPaths.add(current);
					} else if (current.pathLength() < bestPaths.get(0).pathLength()) {
						bestPaths.clear();
						bestPaths.add(current);
					}
				} else {
					//Check right
					if (current.getBoard().isOpen(current.getRow() + 1, current.getCol())) {
						storage.store(new TraceState(current, current.getRow() + 1, current.getCol()));
					}
					if (current.getBoard().isOpen(current.getRow(), current.getCol() - 1)) {
						storage.store(new TraceState(current, current.getRow(), current.getCol() - 1));
					}

					//Check left
					if (current.getBoard().isOpen(current.getRow() - 1, current.getCol())) {
						storage.store(new TraceState(current, current.getRow() - 1, current.getCol()));
					}

					//Check up
					if (current.getBoard().isOpen(current.getRow(), current.getCol() + 1)) {
						storage.store(new TraceState(current, current.getRow(), current.getCol() + 1));
					}	
				}
				
				if (CircuitBoard.getFancy()) { //If the fancy flag is called, make things pretty
					System.out.println("Searching " + args[2] + "...");
					System.out.println(current.getBoard().toString());
					System.out.println("Complete: " + passCount + "\nBest route: " + 
						((!bestPaths.isEmpty()) ? bestPaths.get(0).pathLength() : "N/A"));
					Thread.sleep(Long.parseLong(args[4]));
					System.out.print("\033[H\033[2J");
				}
			}

			//Print results
			for(TraceState path : bestPaths) { 
				System.out.println(path.toString());
			}

			if (CircuitBoard.getFancy()) {
				System.out.println("Found " + bestPaths.size() + " optimal paths" +
				((bestPaths.isEmpty()) ? ". There are no possible solutions."
				: " of length " + bestPaths.get(0).pathLength() + "."));
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
			return;
		} catch (InvalidFileFormatException e) {
			System.out.println(e.toString());
			return;
		} catch (InterruptedException e) {
		 	System.out.println(e.toString());
			return;
		}
	}
} // class CircuitTracer
