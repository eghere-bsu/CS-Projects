import java.util.Random;

/**
 * This class finds twin primes within a specified range.
 * @author eghere
 */
public class TwinPrimeGenerator {
   private int prime;

   /**
    * Checks if an integer is prime. 
    * @param base number
    * @param exponent
    * @return true if prime, false otherwise
    */
   public boolean isPrime(Integer exp) {
      String str[] = Integer.toBinaryString(exp - 1).split("");
      Random rand = new Random();
      long result = rand.nextInt(exp - 2) + 2;
      long a = result;
      
      for (int i = 1; i < str.length; i++) {
         result *= (str[i].equals("1") ? result * a : result);
         result = result % exp;
      }
      
      if (result % exp == 1) {
         return true;
      }
     
      return false;
   }

   /**
    * Finds the lowest twin prime value in a range.
    * The number returned is the greater of the two twin primes.
    * @param lower bound of the list
    * @param upper bound of the list
    * @return the lowest twin prime value
    */
   public int findTwinPrimes(int lower, int upper) {
      if (lower % 2 == 0) lower++;
      if (upper % 2 == 0) upper--;

      for (int i = lower; i < upper; i += 2) {
         if (isPrime(i) && isPrime(i) && isPrime(i + 2) && isPrime (i + 2)) { 
            prime = i + 2;
            return i + 2; 
         } 
      }
      return 0; //Returns 0 if no twin primes are found.
   }

   /**
    * @return the greater of the twin primes found by the generator.
    */
   public int getPrime() {
      return prime;
   }
}
