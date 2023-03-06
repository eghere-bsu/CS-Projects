package cs321.search;

import cs321.common.ParseArgumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

import static cs321.create.SequenceUtils.*;

/**
 * This class will search the created database for matches of a specified 
 * sequence. It returns the frequency of each pattern specified.
 */
public class GeneBankSearchDatabase
{
    private static String databaseFile = "";
    private static String queryFileName = "";
    private static int debugLevel = 0;
    private static File queryFile;

    public static void main(String[] args) throws Exception
    {
        if (args.length < 2) {
            return;
        }
        parseArgumentsAndHandleExceptions(args);
        Scanner s = new Scanner(queryFile); //buffer scan instead?
        String sequence;
        while(s.hasNext()){
            sequence = s.next(); //get next line in query
            long temp = DNAStringToLong(sequence);
            long comp = getComplement(temp, sequence.length());
            String compliment = longToDNAString(comp, sequence.length());
            compliment = compliment.toLowerCase();
            int frequency = searchDatabase(sequence.toLowerCase());
            int frequency2 = searchDatabase(compliment);
            frequency = frequency + frequency2;
            System.out.println(sequence + " "+ frequency);
        }
    }

    /*
    Search entire database for matches to given sequence
    Returns: frequency of sequence in database
     */
    public static int searchDatabase(String sequence) {
        String url = "jdbc:sqlite:bTree.db";
        int freq1 = 0;
        int frequency = 0;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("select * from dna where sequence = '"+ sequence +"';");
                while (rs.next()) {
                        frequency += rs.getInt("frequency");
                    }
                }
            }
        catch (SQLException e) {
           System.out.println(e.getMessage());
        }
        return frequency;
    }


    private static GeneBankSearchDatabaseArguments parseArgumentsAndHandleExceptions(String[] args)
    {
        GeneBankSearchDatabaseArguments geneBankSearchDatabaseArguments = null;
        try
        {
            geneBankSearchDatabaseArguments = parseArguments(args);
        }
        catch (ParseArgumentException | FileNotFoundException e)
        {
            printUsageAndExit(e.getMessage());
        }
        return geneBankSearchDatabaseArguments;
    }
    private static void printUsageAndExit(String errorMessage) {
        System.err.println(errorMessage);
        System.err.println("Usage: java GeneBankSearchDatabase <database file path> <query file> [<debuglevel>]");
        System.err.println("<database file path>: Database file path");
        System.err.println("<Query file>: Query file");
        System.err.println("[<debug level>]: 0/1, optional");
        System.exit(1);
    }
    private static void printUsage() {
        System.err.println("Usage: java GeneBankSearchDatabase <database file path> <query file> [<debuglevel>]");
        System.err.println("<database file path>: Database file path");
        System.err.println("<Query file>: Query file");
        System.err.println("[<debug level>]: 0/1, optional");
        System.exit(1);
    }
    public static GeneBankSearchDatabaseArguments parseArguments(String[] args) throws ParseArgumentException, FileNotFoundException {
        databaseFile = "";
        queryFileName = "";
        debugLevel = 0;
        for(String arg: args) {
            if (arg.contains("--queryfile=")) {
                queryFileName = (arg.substring(arg.indexOf('=') + 1));
                queryFile = new File(queryFileName);
            }
            if (arg.contains("--debug=")) {
                debugLevel = Integer.parseInt(arg.substring(arg.indexOf('=') + 1));
            }
        }
            /* Check arguments */
            if (!queryFile.exists()) {
                printUsage();
                throw new FileNotFoundException("File does not exist");
            }
            if (debugLevel!=0) {
                printUsage();
            }
        //Returns object with command line argument values, so that when testing can create the same object with the arguments that are expected to be in it. Then, compare with what is generated below.
        return new GeneBankSearchDatabaseArguments(databaseFile, queryFileName, debugLevel);
    }
}
