public class LinearProbing<T> extends HashTable<T> {
   public LinearProbing(int tableSize) {
      super(tableSize);
   }

   @Override
   protected int probe(T key, int tableSize, int attempts) { //attempts = i
      int h1 = positiveMod(key.hashCode(), tableSize);
      int baseIndex = h1 + attempts; 
      return positiveMod(baseIndex, tableSize);
   }
}