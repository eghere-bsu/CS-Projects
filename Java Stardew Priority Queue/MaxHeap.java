/**
 * This class is an implementation of a max heap data structure that
 * holds Task objets. A max heap uses an array to hold all of its data;
 * in it, the root node contains children which are of lesser value
 * than the parent nodes.
 */
public class MaxHeap {
    
    protected Task[] heap;
    protected int heapSize;

    public MaxHeap() {
        heap = new Task[2];
        heapSize = 0;
    }

    public MaxHeap(int size) {
        heap = new Task[size];
        heapSize = 0;
    }

    public MaxHeap(int size, Task[] task) {
        heap = task;
        heapSize = size;
        buildMaxHeap();
    }

    /**
     * @return true if maxHeap is empty; false otherwise
     */
    public boolean isEmpty() {
        if (heapSize == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Doubles the size of the array if it runs out of space.
     */
    public void increaseSize() {
        Task[] newHeap = new Task[heap.length * 2];
        for (int i = 1; i <= heapSize; i++) {
            newHeap[i] = heap[i]; 
        }
        
        heap = newHeap;
    }

    /*
     * Maintains the descending value proptery of the max heap. 
     */
    public void heapify(int index) {
        int left = 2 * index;
        int right = (2 * index) + 1;
        int largest;

        if (left <= heapSize && heap[left].compareTo(heap[index]) > 0) {
            largest = left;
        } else {
            largest = index;
        }
        if (right <= heapSize && heap[right].compareTo(heap[largest]) > 0) {
            largest = right;
        } if (largest != index) {
            Task temp = heap[largest];
            heap[largest] = heap[index];
            heap[index] = temp;
            heapify(largest);
        }
    }


    /**
     * @return the maximum value of the heap (the root node).
     */
    public Task max() {
        return heap[1];
    }

    /**
     * Extracts the max value (root node) from the heap.
     * @return the max value of the heap.
     */
    public Task extractMax() {
        Task max = max();
        heap[1] = heap[heapSize];
        heapSize--;
        heapify(1);
        return max;
    }

    /**
     * Inserts a task at the end of the heap structure.
     * @param task to be added to the heap.
     */
    public void insert(Task task) {
        if ((heapSize + 1) >= heap.length) {
            increaseSize();
        }

        heapSize++;
        heap[heapSize] = task;
        
        increaseKey(heapSize);
    }

    /**
     * Increases the key of the node at a current index
     * @param index to increase
     */
    public void increaseKey(int index) {
        while (index > 1 && (heap[index].compareTo(heap[index/2]) > 0)) {
            Task temp = heap[index];
            heap[index] = heap[index / 2];
            heap[index / 2] = temp;
            index /= 2;
        }
    }

    /**
     * Builds the max heap.
     */
    public void buildMaxHeap() {
        for (int i = heapSize / 2; i >= 1; i--) {
            heapify(i);
        }
    }
}
