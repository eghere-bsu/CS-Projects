package cs321.search;

import cs321.btree.BTree;
import cs321.btree.TreeObject;
import cs321.common.Cache;
import cs321.common.ParseArgumentException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static cs321.create.SequenceUtils.DNAStringToLong;
import static cs321.create.SequenceUtils.getComplement;

//import javax.swing.plaf.basic.BasicTreeUI.TreeCancelEditingAction;
//import cs321.common.ParseArgumentUtils;
//import cs321.create.GeneBankCreateBTreeArguments;

/**
 * The driver class for searching through the GeneBank. Given a BTree 
 * binary file, this class will search through the BTree to find 
 * repeating sequences of DNA strings. 
 */
public class GeneBankSearchBTree
{
	//Variables
	Cache cache;

	private static boolean useCache = false;
	private static int degree = 0;
	private static String btreeFileName = "";
	private static int subsequenceLength = -1;
	private static String queryFileName = "";
	private static int cacheSize = 0;
	private static int debugLevel = 0;

	public static void main(String[] args) throws Exception
	{
		if (args.length < 5) {
			return;
		}
		
		//System.out.println("Hello world from cs321.search.GeneBankSearchBTree.main");
		parseArgumentsAndHandleExceptions(args);
		//Cache cache = new Cache(cacheSize);
		/*
		 * Reminders for driver
		 * if hit on search, remove object then add back
		 * if not found, add it
		 */

		search();


		//Should have two methods: one for reading the query file for each line, another that takes in that line
		// for parameter and searches the btree file for that specific sequence
		// Also need cache stuff, if specified in command line args. If using cache, will search cache first, if not in
		// cache, then search btree file, and add to cache

		//When searching, you will have to put the results in a file, examples of this are found in results/query-results
		//What each line will be is the sequence searched for and its frequency in the btree file

		/*
		FROM README
		Note that our BTree stores only one side of the DNA strand but there is a complementary strand as well.
		This implies that when we search for a subsequence, we also need to search for its complement to get the correct answer.
		For example: the DNA sequence: AATGC actually represents two sequences: AATGC and its complement TTACG
		(replace A by T, T by A, C by G and G by C). To search for AATGC, we will search for both AATGC and its complement
		TTACG to get the result.
		 */
	}

	public static void search() {
		BufferedReader brQFile;
		try {
			BTree bt = new BTree(btreeFileName, cacheSize);
			brQFile = new BufferedReader(new FileReader(queryFileName));
			String line;
			while((line=brQFile.readLine())!=null) {
				line = line.toLowerCase();
				TreeObject temp = new TreeObject(DNAStringToLong(line));
				TreeObject temp2 = new TreeObject(getComplement(DNAStringToLong(line), subsequenceLength));
				int total = bt.searchTree(bt.getRoot(), temp).getFrequency() + bt.searchTree(bt.getRoot(), temp2).getFrequency();
				System.out.println(line + " " + total);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static GeneBankSearchBTreeArguments parseArgumentsAndHandleExceptions(String[] args) throws Exception
	{
		GeneBankSearchBTreeArguments geneBankSearchBTreeArguments = null;
		try
		{
			geneBankSearchBTreeArguments = parseArguments(args);
		}
		catch (ParseArgumentException e)
		{
			printUsageAndExit(e.getMessage());
		}
		return geneBankSearchBTreeArguments;
	}

	private static void printUsageAndExit(String errorMessage)
	{

		System.err.println(errorMessage);
		System.err.println("Usage: java GeneBankSearchBTree --cache=<cache> --degree=<degree> --gbkfile=<btree file> --length=<sequence length> --queryfile=<query file> [--cachesize=<cache size>] [--debug=<debuglevel>]");
		System.err.println("<cache>: 0/1 (no cache/cache)");
		System.err.println("<degree>: degree of the BTree (0 for default)");
		System.err.println("<btree file>: BTree file");
		System.err.println("<sequence length>: 1-31");
		System.err.println("<Query file>: Query file");
		System.err.println("[<cache size>]: size of cache, optional");
		System.err.println("[<debug level>]: 0/1, optional");
		System.exit(1);
	}

	private static void printUsage() {
		System.err.println("Usage: java GeneBankSearchBTree --cache=<cache> --degree=<degree> --gbkfile=<btree file> --length=<sequence length> --queryfile=<query file> [--cachesize=<cache size>] [--debug=<debuglevel>]");
		System.err.println("<cache>: 0/1 (no cache/cache)");
		System.err.println("<degree>: degree of the BTree (0 for default)");
		System.err.println("<btree file>: BTree file");
		System.err.println("<sequence length>: 1-31");
		System.err.println("<Query file>: Query file");
		System.err.println("[<cache size>]: size of cache, optional");
		System.err.println("[<debug level>]: 0/1, optional");
		System.exit(1);
	}

	public static GeneBankSearchBTreeArguments parseArguments(String[] args) throws Exception
	{
		useCache = false;
		degree = 0;
		btreeFileName = "";
		subsequenceLength = -1;
		queryFileName = "";
		cacheSize = 0;
		debugLevel = 0;

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
				subsequenceLength = Integer.parseInt(arg.substring(arg.indexOf('=') + 1));
			}
			if (arg.contains("--cachesize=")) {
				cacheSize = Integer.parseInt(arg.substring(arg.indexOf('=') + 1));
			}
			if (arg.contains("--debug=")) {
				debugLevel = Integer.parseInt(arg.substring(arg.indexOf('=') + 1));
			}
			if (arg.contains("--btreefile=")) {
				btreeFileName = arg.substring(arg.indexOf('=')+1);
			}
			if (arg.contains("--queryfile=")) {
				queryFileName = arg.substring(arg.indexOf('=')+1);
			}

		}
		/* Check arguments */
		if (useCache && (cacheSize < 100 || cacheSize > 5000)) {
			printUsage();
			throw new Exception("Invalid cache size");
		}
		if (degree < 0) {
			printUsage();
			throw new Exception("Invalid degree value");
		}
		if (subsequenceLength < 1 || subsequenceLength > 31) {
			printUsage();
			throw new Exception("Invalid length value");
		}
		if (btreeFileName==null) {
			printUsage();
			throw new Exception("Invalid btreefile name");
		}
		if (queryFileName==null) {
			printUsage();
			throw new Exception("Invalid queryfile name");
		}

		//Returns object with command line argument values, so that when testing can create the same object with the arguments that are expected to be in it. Then, compare with what is generated below.
		return new GeneBankSearchBTreeArguments(useCache, degree, btreeFileName, subsequenceLength, queryFileName, cacheSize, debugLevel);
	}



}

