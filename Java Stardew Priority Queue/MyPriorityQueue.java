/**
 * This class is an implementation of the priority queue.
 * The Priority Queue data satrcture acts like a queue where each
 * item in the queue also has a priority value. The higher the priority,
 * the closer it will be to the front of the queue.
 */
public class MyPriorityQueue extends MaxHeap implements PriorityQueueInterface{

    public MyPriorityQueue() {
        super();
    }

    //write about this change in the readme
    @Override
    public void enqueue(Task task) {
        super.insert(task);
    }

    @Override
    public Task dequeue() {
        return super.extractMax();
    }

    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public void update(int timeToIncrementPriority, int maxPriority) {
        for (int i = 1; i <= super.heapSize; i++) {
            Task t = super.heap[i];
            t.incrementWaitingTime();
            if (t.getWaitingTime() >= timeToIncrementPriority) {
                t.resetWaitingTime();
                if (t.getPriority() < maxPriority) {
                    t.setPriority(t.getPriority() + 1);
                    super.increaseKey(i);
                }
            }
        }
    }
    
}
