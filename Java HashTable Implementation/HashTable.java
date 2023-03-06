import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public abstract class HashTable <T> {
   protected int size;
   protected int spotsFilled;
   protected int totalProbes;
   protected long totalDuplicates;
   private HashObject<T>[] table;

   /**
    * Constructor for the HashTable
    */
   @SuppressWarnings("unchecked")
   public HashTable (int tableSize) {
      this.size = tableSize;
      this.spotsFilled = 0;
      this.totalProbes = 0;
      this.totalDuplicates = 0L;
      table = new HashObject[size];
   }

   protected void insert(T key){
      HashObject<T> obj = new HashObject<T>(key); 
      int i = 0;
      while (i != size) {
         int j = probe(key, size, i);
         if (table[j] == null) { //Finding avialable space
            table[j] = obj;
            table[j].incrementProbeCount();
            totalProbes += (i + 1);
            spotsFilled++;
            return;
         } else if (table[j].equals(obj)) {
            table[j].incrementDuplicateCount();
            totalDuplicates++;
            return;
         } else {
            obj.incrementProbeCount();
            i++;
         }
      }
   }

   protected int positiveMod(int baseIndex, int tableSize) {
      int value = baseIndex % tableSize;
      if (value < 0) {
         value += tableSize;
      }
      return value;
   }
   
   /**
    * outputs the contents of the table to a text file named "filename"
    * @param filename
    */
   public void dump(String filename) {
      PrintStream ps;
      try {
         ps = new PrintStream(new File(filename));
         for (int i = 0; i < size; i++) {
            if (table[i] != null) {
               ps.append("table[" + String.valueOf(i) + "]: " + table[i].toString());
               ps.append("\n");
            }
         }
         System.setOut(ps);
         ps.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }

   public double getSpotsFilled() {
      return spotsFilled;
   }

   public void printSummary(double loadFactor, boolean fancyTerm) {
      System.out.println(((fancyTerm) ? (
               "Input " + "\u001b[1m" + (spotsFilled + totalDuplicates) + "\u001b[0m"  + " elements, of which " +
               "\u001b[1m" + totalDuplicates +  "\u001b[0m" + " duplicates.\n" +
               "Load factor = " +  "\u001b[1m" + loadFactor + "\u001b[0m" +  
               ", Avg. no. of probes " + "\u001b[1m" + ((double)(totalProbes) / spotsFilled) + "\u001b[0m"
            ) : (
               "Input " + (spotsFilled + totalDuplicates) + " elements, of which " + totalDuplicates + " duplicates.\n" +
               "Load factor = " + loadFactor +  ", Avg. no. of probes " + ((double)(totalProbes) / spotsFilled)
            )
         )
      );
   }

   /**
    * The probing algorithm to be defined for either linear probing or double hashing.
    * @param key
    * @param tableSize
    * @param attempts
    * @return
    */
   protected abstract int probe(T key, int tableSize, int attempts);   
}
