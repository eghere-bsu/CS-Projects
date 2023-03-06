import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double-linked node implementation of IndexedUnsortedList.
 * 
 * @author ethanghere
 * 
 * @param <T> type to store
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head, tail;
	private int size;
	private int modCount;
	
	/** Creates an empty list */
	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		if (size == 0) {
            head = tail = new Node<T>(element);
        } else {
            Node<T> node = new Node<T>(element); 
            node.setNext(head);
			node.setPrevious(null);
            head = node;
        } 

        size++;
        modCount++;
	}

	@Override
	public void addToRear(T element) {
		if (size == 0) {
            head = tail = new Node<T>(element);
        } else {
            Node<T> node = new Node<T>(element);
            tail.setNext(node);
            node.setPrevious(tail);
			tail = node;
        }

        size++;
        modCount++;
	}

	@Override
	public void add(T element) {
		if (size == 0) {
            head = tail = new Node<T>(element);
        } else {
            Node<T> node = new Node<T>(element);
            tail.setNext(node);
			node.setPrevious(tail);
            tail = node;
        }

        size++;
        modCount++;
	}

	@Override
	public void addAfter(T element, T target) {
        Node<T> targetNode = head;
        while (targetNode != null && !targetNode.getElement().equals(target)) {
            targetNode = targetNode.getNext();
        }

		if (targetNode == null) {
            throw new NoSuchElementException();
        }

        Node<T> newNode = new Node<T>(element);
        newNode.setNext(targetNode.getNext());
		targetNode.setNext(newNode);
		targetNode.getNext().setPrevious(newNode);
		newNode.setPrevious(targetNode);
        

        if (targetNode == tail) {
            tail = newNode;
        }

        size++;
        modCount++;
	}

	@Override
	public void add(int index, T element) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		
		if (size == 1) {
			add(element);
		} else {
			Node<T> newNode = new Node<T>(element);
			
			if (index == 0 && size == 0) {
				newNode.setNext(head);
				newNode.setPrevious(null);
				head = tail = newNode;
			} else if (index == 0) {
				newNode.setNext(head);
				newNode.setPrevious(null);
				head = newNode;
			} else {
				Node<T> indexNode = head;
				for (int i = 0; i < index; i++) {
					indexNode = indexNode.getNext();
				}

				newNode.setNext(indexNode);
				newNode.setPrevious(indexNode.getPrevious());
				indexNode.setPrevious(newNode);
				indexNode.getPrevious().setNext(newNode);
		
				if (indexNode == tail) {
					tail = newNode;
				}
			}
		}
		
		
		size++;
        modCount++;
	}

	@Override
	public T removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		if (size == 1) {
			tail = null;
		}

        Node<T> oldHead = head;
		head = head.getNext();


		size--;
		modCount++;
		return oldHead.getElement();
	}

	@Override
	public T removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		T retVal = null;
		if (head == tail) {
			retVal = head.getElement();
			head = tail = null;
		} else {
			Node<T> targetNode = head;
			for (int i = 1; i < size - 1; i++) {
				targetNode = targetNode.getNext();
			}
			retVal = tail.getElement();
			tail = targetNode;
			targetNode.setNext(null);
		}

		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		boolean found = false;
		Node<T> previous = null;
		Node<T> current = head;
		
		while (current != null && !found) {
			if (element.equals(current.getElement())) {
				found = true;
			} else {
				previous = current;
				current = current.getNext();
			}
		}
		
		if (!found) {
			throw new NoSuchElementException();
		}
		
		if (size() == 1) { //only node
			head = tail = null;
		} else if (current == head) { //first node
			head = current.getNext();
		} else if (current == tail) { //last node
			tail = previous;
			tail.setNext(null);
		} else { //somewhere in the middle
			previous.setNext(current.getNext());
			current.getNext().setPrevious(previous);
		}
		
		size--;
		modCount++;
		
		return current.getElement();
	}

	@Override
	public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

		if (index == 0) {
			return removeFirst();
		} else if (index == size - 1) {
			return removeLast();
		}
		
		Node<T> origNode;
		Node<T> indexNode = head;
		for (int i = 0; i < index - 1; i++) {
			indexNode = indexNode.getNext();
		}

		origNode = indexNode.getNext();
		indexNode.setNext(indexNode.getNext().getNext());
		
		size--;
		modCount++;
		return origNode.getElement();

	}

	@Override
	public void set(int index, T element) {
		if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
		
		Node<T> indexNode = head;
		for (int i = 0; i < index; i++) {
			indexNode = indexNode.getNext();
		}
		
		indexNode.setElement(element);
		modCount++;
	}

	@Override
	public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
		
		Node<T> indexNode = head;
		for (int i = 0; i < index; i++) {
			indexNode = indexNode.getNext();
		}
		
		return indexNode.getElement();
	}

	@Override
	public int indexOf(T element) {
		Node<T> targetNode = head;
		int index = 0;
		while (targetNode != null) {
			if (targetNode.getElement().equals(element)) {
				return index;
			}
			targetNode = targetNode.getNext();
			index++;
		}
		return -1;
	}

	@Override
	public T first() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		return head.getElement();
	}

	@Override
	public T last() { 
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		return (indexOf(target) != -1);
	}

	@Override
	public boolean isEmpty() {
		return (size == 0);
	}

	@Override
	public int size() {
		return size;
	}

	/** 
	 * Returns a formatted string of the DoubleLinkedList
	 * @returns string
	 */
	public String toString() {
		StringBuilder stringBuild = new StringBuilder();
		stringBuild.append("[");
		Node<T> trackerNode = head;

		while (trackerNode != null) {
			stringBuild.append(trackerNode.getElement() + ", ");
			trackerNode = trackerNode.getNext();
		}
		if (!isEmpty()) {
			stringBuild.delete(stringBuild.length() - 2, stringBuild.length());
		}
		stringBuild.append("]");

		return stringBuild.toString();
	}

	@Override
	public Iterator<T> iterator() {
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLLIterator(startingIndex);
	}

	/** Iterator for IUDoubleLinkedList */
	private class DLLIterator implements ListIterator<T> {
		private Node<T> nextNode, previousNode, lastReturned;
		private int iterModCount;
		private boolean canRemove;
		
		/** Creates a new iterator for the list */
		public DLLIterator() {
			nextNode = head;
			iterModCount = modCount;
		}

		public DLLIterator(int startingIndex) {
			for (int i = 0; i < startingIndex; i++) {

			}
			nextNode = head;
			iterModCount = modCount;
			previousNode = null;
		}

		@Override
		public boolean hasNext() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}

			return (nextNode != null) ? true : false;
		}

		@Override
		public boolean hasPrevious() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}

			return (previousNode != null) ? true : false;
		}

		@Override
		public T next() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}

			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			lastReturned = nextNode;
			previousNode = nextNode;
			T retVal = nextNode.getElement();
			nextNode = nextNode.getNext();
			canRemove = true;

			return retVal;
		}

		@Override
		public T previous() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}

			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}

			lastReturned = previousNode;
			nextNode = previousNode;
			T retVal = previousNode.getElement();
			previousNode = previousNode.getPrevious();

			return retVal;
		}
		
		@Override
		public void remove() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}

			if (!canRemove) {
				throw new IllegalStateException();
			}
			canRemove = false;

			if (size == 1) {
				head = tail = null;
			} else if (lastReturned == head) {
				lastReturned.getNext().setPrevious(null);
				head = lastReturned.getNext();
			} else if (lastReturned == tail) {
				lastReturned.getPrevious().setNext(null);
				tail = lastReturned.getPrevious();
			} else {
				lastReturned.getPrevious().setNext(lastReturned.getNext());
				lastReturned.getNext().setPrevious(lastReturned.getPrevious());
			}

			size--;
			modCount++;
			iterModCount++;
		}

		@Override
		public void add(T element) {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}

			Node<T> newNode = new Node<T>(element);

			if (isEmpty()) {
				head = tail = nextNode = newNode;
			} else if (previousNode == null) {
				nextNode = newNode;
				newNode.setNext(head);
				head.setPrevious(newNode);
				head = newNode;
			} else if (nextNode == null) {
				nextNode = newNode;

			} else {
				nextNode.setPrevious(newNode);
				previousNode.setNext(newNode);
				newNode.setNext(nextNode);
				newNode.setPrevious(previousNode);
			}
			
			lastReturned = newNode;

			modCount++;
			iterModCount++;
		}

		

		@Override
		public int nextIndex() {
			// TODO Auto-generated method stub
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();

			}

			if (lastReturned == null) {
				throw new IllegalStateException();
			} else {

			}
			
			return 0;
		}

		@Override
		public int previousIndex() {
			// TODO Auto-generated method stub
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			
			if (lastReturned == null) {
				throw new IllegalStateException();
			} else {

			}
			
			return 0;
		}

		@Override
		public void set(T element) {
			// TODO Auto-generated method stub
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}

			Node<T> newNode = new Node<T>(element);
			if (lastReturned == null) {
				throw new IllegalStateException();
			} else {
				lastReturned = newNode;
			}

			modCount++;
			iterModCount++;
		}
	}
}