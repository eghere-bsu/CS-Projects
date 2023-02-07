package cs321.create;

import cs321.btree.BTree;
import cs321.btree.TreeObject;
import cs321.common.Cache;

import java.io.*;
import java.sql.*;
import java.util.LinkedList;

import static cs321.create.SequenceUtils.longToDNAString;

/**
 * This is the driver class for GeneBankCreateBTree. Based on command line
 * arguments, this program will construct a BTree from a given .gbk file. 
 */
public class GeneBankCreateBTree {

	private static boolean useCache = false;
	private static int degree = 0;
	private static int seqLength = -1;
	private static int cacheSize = -1;
	private static int debug = 0;
	private static String fileName= "";
	private static BTree tree;
	private static File dnaFile;

	/**
	 * The main method 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 4) {
			printUsageAndExit("Incorrect number of arguments provided");
		}
		parseArgumentsAndHandleExceptions(args);
		tree = new BTree(degree, fileName + ".btree.data." + seqLength + "." + degree, cacheSize);

		fileParse(dnaFile, tree, seqLength);
	}

	/**
	 * Parses the arguments from the command line
	 * @param args from the command line 
	 * @return the BTree arguments
	 * @throws Exception if there are invalid arguments
	 */
	public static GeneBankCreateBTreeArguments parseArguments(String[] args) throws Exception {
		useCache = false;
		degree = 0;
		seqLength = -1;
		cacheSize = 0;
		debug = 0;
		fileName = "";

		for (String arg : args) {
			if (arg.contains("--cache=")) {
				if (arg.charAt(arg.indexOf('=') + 1) == '1') {
					useCache = true;
				}
			}
			if (arg.contains("--degree=")) {
				degree = Integer.parseInt(arg.substring(arg.indexOf('=') + 1));
			}
			if (arg.contains("--length=")) {
				seqLength = Integer.parseInt(arg.substring(arg.indexOf('=') + 1));
			}
			if (arg.contains("--cachesize=")) {
				cacheSize = Integer.parseInt(arg.substring(arg.indexOf('=') + 1));
			}
			if (arg.contains("--debug=")) {
				debug = Integer.parseInt(arg.substring(arg.indexOf('=') + 1));
			}
			if (arg.contains("--gbkfile=")) {
				dnaFile = new File(arg.substring(arg.indexOf('=')+1));
			}
		}
		/* Check arguments */
		if (useCache && (cacheSize < 100 || cacheSize > 5000)) {
			throw new Exception("Invalid cache size");
		}
		if (degree < 0) {
			throw new Exception("Invalid degree value");
		}
		if (seqLength < 1 || seqLength > 31) {
			throw new Exception("Invalid length value");
		}
		if (!dnaFile.exists()) {
			throw new FileNotFoundException("File does not exist");
		}
		fileName = dnaFile.getName();

		return new GeneBankCreateBTreeArguments(useCache, degree, fileName, seqLength, cacheSize, debug);
	}

	/**
	 * Calls the argument parsing method and handles any exceptions,
	 * if any.
	 * @param args from the command line
	 * @return the BTreeArguments 
	 * @throws IOException if file is invalid.
	 */
	public static GeneBankCreateBTreeArguments parseArgumentsAndHandleExceptions(String[] args) throws IOException {
		GeneBankCreateBTreeArguments geneBankCreateBTreeArguments = null;
		try
		{
			geneBankCreateBTreeArguments = parseArguments(args);
		}
		catch (Exception e)
		{
			printUsageAndExit(e.getMessage());
		}
		return geneBankCreateBTreeArguments;
	}

	/**
	 * Parses the given file.
	 * @param dnaFile of a .gbk type
	 * @param tree BTree to parse
	 * @param seqLength length of the DNA sequences
	 * @throws IOException if file is invalid.
	 */
	public static void fileParse(File dnaFile, BTree tree, int seqLength) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(dnaFile));

		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			if (line.startsWith("ORIGIN")) {
				dnaSeqInsert(reader, tree, seqLength);
			}
		}

		if (debug == 1) {
			tree.dump("dump", seqLength); //creates dump file with all inserts
		} else if (debug == 2) {
			createNewDatabase(tree.inOrderTraverse(tree.getRoot(), seqLength), "bTree.db");
		}
		if (useCache) { //write cache to disk
			tree.writeFromCache();
		}
		tree.finishUp(); //close random access file
	}

	/**
	 * Inserts the DNA sequences into the BTree.
	 * @param scanner
	 * @param tree
	 * @param seqLength
	 * @throws IOException if file is invalid.
	 */
	public static void dnaSeqInsert(BufferedReader scanner, BTree tree, int seqLength) throws IOException {
		int currentSeqLength = 0;
		int num = 0;
		String currentLine = "";
		long window = 0;
		long mask = 0;
		for (int i = 1; i <= seqLength; i++) {
			mask = mask << 2;
			mask += 3;
		}

		while (!currentLine.contains("//")) {
			currentLine = scanner.readLine();
			char[] characters = currentLine.toCharArray();
			for (char character : characters) {
				boolean insert = false;
				if (character == 'a') {
					window = window<<2;
					insert = true;
					currentSeqLength++;
				}
				if (character == 'c') {
					window = window<<2;
					window += 1;
					insert = true;
					currentSeqLength++;
				}
				if (character == 't') {
					window = window<<2;
					window += 3;
					insert = true;
					currentSeqLength++;
				}
				if (character == 'g') {
					window = window<<2;
					window += 2;
					insert = true;
					currentSeqLength++;
				}
				if (character == 'n') {
					window = 0;
					currentSeqLength = 0;
				}
				if (insert && currentSeqLength >= seqLength) {
					tree.insert(new TreeObject(window & mask, 1));
				}
			}
		}
	}

	/**
	 * Inserts needed values into the database. 
	 * @param sequence of DNA string
	 * @param frequency of DNA string 
	 */
	public static void insertIntoDatabase(String sequence, int frequency){
		try(Connection conn = DriverManager.getConnection("jdbc:sqlite:bTree.db")){
			if(conn != null){
				Statement statement = conn.createStatement();
				statement.executeUpdate("insert into dna values('"+sequence+"','"+frequency+"')");
			}
		}catch (SQLException e){
			System.err.print(e.getMessage());
		}
	}

	/**
	 * Creates a new database with tables for the BTree.
	 * @param fileName 
	 */
	public static void createNewDatabase(LinkedList<TreeObject> s, String fileName){
		//TODO: in order traversal in main
		String url = "jdbc:sqlite:" + fileName;
		TreeObject temp;

		try(Connection conn = DriverManager.getConnection(url)){
			if (conn != null){
				Statement statement = conn.createStatement();
				statement.executeUpdate("drop table if exists dna");
				statement.executeUpdate("create table dna (sequence varchar(255), frequency int)");

//				//get data from dump file
//				File dump = new File("dump");
//				Scanner s = new Scanner(dump);
				//for loop to insert all values
				while(!s.isEmpty()){
					temp = s.removeFirst();
					String seq = longToDNAString(temp.getSubstring(), seqLength);
					seq=seq.toLowerCase();
					int freq = temp.getFrequency();
					insertIntoDatabase(seq, freq);
				}

				ResultSet rs = statement.executeQuery("select * from dna");
				while(rs.next()){
	//				System.out.println(rs.getString("sequence") +" "+ rs.getInt("frequency"));
				}
			}
		} catch (Exception e){
			System.err.println(e.getMessage());
		}

	}

	/**
	 * Prints usage and exits.
	 */
	private static void printUsageAndExit(String errorMessage) {
		System.err.println(errorMessage);
		System.err.println("Usage: java GeneBankCreateBTree --cache=<cache> --degree=<degree> --gbkfile=<gbk file> --length=<sequence length> [--cachesize=<cache size>] [--debug=<debuglevel>]");
		System.err.println("<cache>: 0/1 (no cache/cache)");
		System.err.println("<degree>: degree of the BTree (0 for default)");
		System.err.println("<gbk file>: GeneBank file");
		System.err.println("<sequence length>: 1-31");
		System.err.println("[<cache size>]: size of cache, optional");
		System.err.println("[<debug level>]: 0/1/2");
		System.exit(1);
	}

}
