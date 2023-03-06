import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Integer;
import java.util.Random;
import java.util.Date;
import java.util.Scanner;

/**
 * This is the driver class for the HashTest project. The user will specify through command
 * line arguments the method by which keys will be inserted into two hash tables. The two
 * hash tables will be analyzed and compared against one another once finished.
 * @author eghere
 */
public class HashTest {
   private int inputType, tableSize, debugLevel;
   private double loadFactor;
   
   private static boolean fancyTerm;
   private final int LOWER_PRIME_BOUND = 95500;
   private final int UPPER_PRIME_BOUND = 96000;

   /**
    * Prints usage.
    */
   public void printUsage() {
      System.out.println(
            "Usage: java HashTest.java " +
               "<input-type> " +
               "<load-factor> " +
               "<debug-level>");
      System.exit(1);
   }

   /**
    * Processes command line arguments to prepare for
    * the hash table testing.
    * @param args
    */
   private void runTest(String[] args) {
      //I use ANSI formatting in the terminal output. This checks if the current terminal supports it.
      fancyTerm = (System.console() != null && System.getenv().get("TERM") != null) ? true : false;

      //Find twin primes to use for testing
      TwinPrimeGenerator tpg = new TwinPrimeGenerator();
      tableSize = tpg.findTwinPrimes(LOWER_PRIME_BOUND, UPPER_PRIME_BOUND);
      System.out.println("A good table size is found: " + ((fancyTerm) ? "\u001b[1m" + tpg.getPrime() + "\u001b[0m" : tpg.getPrime()));
      
      inputType = Integer.parseInt(args[0]);
      if (1 > inputType || inputType > 3) throw new IllegalArgumentException("Illegal argument: Input type must be 1, 2, or 3.");
      
      loadFactor = Double.parseDouble(args[1]);
      if (0 > loadFactor || loadFactor > 1) throw new IllegalArgumentException("Illegal argument: Load factor must be in range [0,1].");

      debugLevel = Integer.parseInt(args[2]);
      if (debugLevel != 0 && debugLevel != 1) throw new IllegalAccessError("Illegal argument: Debug level must be 0 or 1");

      switch (inputType) {
         case 1:   //Random Integer
            System.out.println("Data source type: " + ((fancyTerm) ? "\u001b[1m" + "Random Integer" + "\u001b[0m" : "Random Integer"));
            HashTable<Integer> randLinear = new LinearProbing<Integer>(tableSize);
            HashTable<Integer> randDouble = new DoubleHashing<Integer>(tableSize); 

            Random rand = new Random();
            int inputNum;
            // Fill tables until target load factor is reached
            while ((((double)(randLinear.getSpotsFilled()) / (double)(tableSize)) < loadFactor)
                  && (double)(randDouble.getSpotsFilled() / (double)(tableSize)) < loadFactor) {
               inputNum = rand.nextInt();
               
               randLinear.insert(inputNum);
               randDouble.insert(inputNum);
            }
            
            System.out.println("Using " + ((fancyTerm) ? "\u001b[1m" + "Linear Hashing..." + "\u001b[0m" : "Linear Hashing..."));
            randLinear.printSummary(loadFactor, fancyTerm);
            System.out.println("Using " + ((fancyTerm) ? "\u001b[1m" + "Double Hashing..." + "\u001b[0m" : "Double Hashing..."));
            randDouble.printSummary(loadFactor, fancyTerm);

            if (debugLevel == 1) {
               randLinear.dump("linear-dump");
               randDouble.dump("double-dump");
            }
            break;
         case 2:   //Date
            System.out.println("Data source type: " + ((fancyTerm) ? "\u001b[1m" + "System Time" + "\u001b[0m" : "System Time"));
            
            long current = new Date().getTime();
            HashTable<Date> dateLinear = new LinearProbing<Date>(tableSize);
            HashTable<Date> dateDouble = new DoubleHashing<Date>(tableSize); 

            while (((double)(dateLinear.getSpotsFilled()) / (double)(tableSize) < loadFactor)
               && (double)(dateDouble.getSpotsFilled() / (double)(tableSize)) < loadFactor) {
                  current++;
                  Date date = new Date(current);
                  
                  dateLinear.insert(date);
                  dateDouble.insert(date);
            }

            System.out.println("Using " + ((fancyTerm) ? "\u001b[1m" + "Linear Hashing..." + "\u001b[0m" : "Linear Hashing..."));
            dateLinear.printSummary(loadFactor, fancyTerm);
            System.out.println("Using " + ((fancyTerm) ? "\u001b[1m" + "Double Hashing..." + "\u001b[0m" : "Double Hashing..."));
            dateDouble.printSummary(loadFactor, fancyTerm);

            if (debugLevel == 1) {
               dateLinear.dump("linear-dump");
               dateDouble.dump("double-dump");
            }
            break;
         case 3:   //From word-list file
            System.out.println("Data source type: " + ((fancyTerm) ? "\u001b[1m" + "word-list" + "\u001b[0m" : "word-list"));
            Scanner scan;
            try {
               scan = new Scanner(new File("word-list"));

               HashTable<String> wordLinear = new LinearProbing<String>(tableSize);
               HashTable<String> wordDouble = new DoubleHashing<String>(tableSize);

               while ((((double)(wordLinear.getSpotsFilled()) / (double)(tableSize)) < loadFactor)
                  && (double)(wordDouble.getSpotsFilled() / (double)(tableSize)) < loadFactor) {
                     String word = scan.nextLine();
                     
                     wordLinear.insert(word);
                     wordDouble.insert(word);
               }
               
               System.out.println("Using " + ((fancyTerm) ? "\u001b[1m" + "Linear Hashing..." + "\u001b[0m" : "Linear Hashing..."));
               wordLinear.printSummary(loadFactor, fancyTerm);
               System.out.println("Using " + ((fancyTerm) ? "\u001b[1m" + "Double Hashing..." + "\u001b[0m" : "Double Hashing..."));
               wordDouble.printSummary(loadFactor, fancyTerm); 

               if (debugLevel == 1) {
                  wordLinear.dump("linear-dump");
                  wordDouble.dump("double-dump");
               }
            } catch (FileNotFoundException e) {
               e.printStackTrace();
            } 
            break;
      }
   }

   /**
    * Main method for the program.
    */
   public static void main(String[] args) {
      HashTest hash = new HashTest();
      if (args.length != 3) {
         hash.printUsage();
         System.exit(1);
      }
      hash.runTest(args);
   }
}
