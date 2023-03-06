/**
 * A driver class to test the max heap implementation.
 * 
 * @author eghere
 */
public class MaxHeapTest {
    public static void main(String[] args) {
        MaxHeap maxHeap = new MaxHeap();
        int lastPriorityChecked = 9999;

        for (int i = 0; i <= 100; i++) {
            Task task = new Task(i, i);
            maxHeap.insert(task);
        }

        while (!maxHeap.isEmpty()) { //This is basically heap sort
            Task extract = maxHeap.extractMax();
            System.out.println("priority: " + extract.getPriority());

            if (extract.getPriority() > lastPriorityChecked) {
                throw new RuntimeException("MaxHeap is out of order");
            } else {
                lastPriorityChecked = extract.getPriority();
            }
        }

        System.out.println("MaxHeap class is working"); 
    }
}
