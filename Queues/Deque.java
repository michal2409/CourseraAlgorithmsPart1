import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private static final String FIRST = "first";
    private static final String LAST = "last";
    private Node first, last;
    private int size;

    private class Node {
        private Item key;
        private Node next, prev;
    }

    public Deque() { // construct an empty deque
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() { // is the deque empty?
        return size == 0;
    }

    public int size() { // return the number of items on the deque
        return size;
    }

    private Node createNode(Item item) {
        Node newEl = new Node();
        newEl.key = item;
        newEl.next = null;
        newEl.prev = null;
        return newEl;
    }

    private void addEl(String addLocation, Item item) {
        if (item == null)
            throw new java.lang.IllegalArgumentException();
        Node newEl = createNode(item);
        if (addLocation.equals(FIRST)) {
            newEl.next = first;
            if (first != null)
                first.prev = newEl;
            first = newEl;
            if (last == null)
                last = first;
        } else {
            newEl.prev = last;
            if (last != null)
                last.next = newEl;
            last = newEl;
            if (first == null)
                first = last;
        }
        size++;
    }

    public void addFirst(Item item) { // add the item to the front
        addEl(FIRST, item);
    }

    public void addLast(Item item) { // add the item to the end
        addEl(LAST, item);
    }

    private Item remove(String removeLocation) {
        if (isEmpty())
            throw new java.util.NoSuchElementException();
        Item item = (removeLocation.equals(FIRST)) ? first.key : last.key;
        if (removeLocation.equals(FIRST)) {
            first = first.next;
            if (first != null)
                first.prev = null;
            else
                last = null;
        } else {
            last = last.prev;
            if (last != null)
                last.next = null;
            else
                first = null;
        }
        size--;
        return item;
    }

    public Item removeFirst() { // remove and return the item from the front
        return remove(FIRST);
    }

    public Item removeLast() { // remove and return the item from the end
        return remove(LAST);
    }

    public Iterator<Item> iterator() { // return an iterator over items in order from front to end
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (current == null)
                throw new java.util.NoSuchElementException();
            Item item = current.key;
            current = current.next;
            return item;
        }
    }
    public static void main(String[] args) {
		Deque<Integer> deque = new Deque<Integer>();
		         deque.addFirst(0);
		         deque.addFirst(1);
		         deque.removeLast()  ; 
		         deque.removeLast();
    }
}
	
  


    
