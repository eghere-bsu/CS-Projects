/**
 * This class represents a HashObject which is to be used by
 * the HashTable class.
 * @param <T> generic object data type
 */
public class HashObject<T> {
   private T keyObject;
   private int duplicateCount;
   private int probeCount;

   /**
    * The default HashObject constructor.
    * @param key object type to be used
    */
   public HashObject (T keyObject) {
      this.keyObject = keyObject;
      duplicateCount = 0;
      probeCount = 0;
   }

   /**
    * Determines if this HashObject and another are equal.
    * @param object to compare to this HashObject
    * @return true if equal, false otherwise.
    */
   public boolean equals(HashObject<T> object) { 
      return keyObject.equals(object.getKeyObject()); 
   }

   /**
    * Increases duplicate count by 1.
    */
   public void incrementDuplicateCount() { 
      duplicateCount++; 
   }

   /**
    * Increases probe count by 1.
    */
   public void incrementProbeCount() { 
      probeCount++; 
   }
   
   /**
    * @return the hash code of the HashObject's key.
    */
   public int getKey() { 
      return keyObject.hashCode(); 
   }

   /**
    * @return the key object of the HashObject.
    */   
   public T getKeyObject() { 
      return keyObject; 
   }

   /**
    * @return the duplicate count.
    */
   public int getDuplicateCount() { 
      return duplicateCount; 
   }

   /**
    * @return the probe count.
    */
   public int getProbeCount() { 
      return probeCount; 
   }

   /**
    * @return a string that contains keyObject.toString(), the duplicate count, and the probe count.
    */
   @Override
   public String toString() { 
      return (keyObject.toString() + " " + getDuplicateCount() + " " + getProbeCount()); 
   }
}