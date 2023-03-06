public class DoubleHashing<T> extends HashTable<T> {
   public DoubleHashing(int tableSize) {
      super(tableSize);
   }

   @Override
   protected int probe(T key, int tableSize, int attempts) {
      int h1 = positiveMod(key.hashCode(), tableSize);
      int h2 = 1 + positiveMod(key.hashCode(), tableSize - 2); 
      return positiveMod((h1 + (attempts * h2)), tableSize); 
   } 
}
