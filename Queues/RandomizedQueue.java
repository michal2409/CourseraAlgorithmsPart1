import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] arr;
    private int idx;

    public RandomizedQueue() { // construct an empty randomized queue
        idx = 0;
        arr = (Item[]) new Object[1];
    }

    public boolean isEmpty() { // is the randomized queue empty?
        return idx == 0;
    }

    public int size() { // return the number of items on the randomized queue
        return idx;
    }

    private void resize(int newSize) {
        Item[] copy = (Item[]) new Object[newSize];
        System.arraycopy(arr, 0, copy, 0, idx);
        arr = copy;
    }

    public void enqueue(Item item) { // add the item
        if (item == null)
            throw new java.lang.IllegalArgumentException();
        if (idx == arr.length)
            resize(2 * idx);
        arr[idx++] = item;
    }

    public Item dequeue() { // remove and return a random item
        if (isEmpty())
            throw new java.util.NoSuchElementException();
        if (idx > 0 && idx == arr.length / 4)
            resize(2 * idx);
        int idxToDel = StdRandom.uniform(0, idx);
        Item item = arr[idxToDel];
        arr[idxToDel] = arr[--idx]; // filling the gap with last element in array
        arr[idx] = null;
        return item;
    }

    public Item sample()  { // return a random item (but do not remove it)
        if (isEmpty())
            throw new java.util.NoSuchElementException();
        return arr[StdRandom.uniform(0, idx)];
    }

    public Iterator<Item> iterator() { // return an independent iterator over items in random order
        return new RandQueueIterator();
    }

    private class RandQueueIterator implements Iterator<Item> {
        private Item[] arrCpy;
        private int idxCpy;

        public RandQueueIterator() {
            arrCpy = (Item[]) new Object[idx];
            System.arraycopy(arr, 0, arrCpy, 0, idx);
            idxCpy = 0;
            StdRandom.shuffle(arrCpy);
        }

        public boolean hasNext() {
            return idxCpy < idx;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (idxCpy == idx)
                throw new java.util.NoSuchElementException();
            return arrCpy[idxCpy++];
        }
    }
}