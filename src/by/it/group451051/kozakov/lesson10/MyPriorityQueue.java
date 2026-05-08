package by.it.group451051.kozakov.lesson10;

import java.util.Collection;
import java.util.Queue;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        this.heap = (E[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (a == null && b == null) {
            return 0;
        }
        if (a == null) {
            return 1;
        }
        if (b == null) {
            return -1;
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null) {
                if (heap[i] == null) {
                    return true;
                }
            } else {
                if (o.equals(heap[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        ensureCapacity();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            return false;
        }
        ensureCapacity();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        E result = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) {
            siftDown(0);
        }
        return result;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (heap[i] == null) {
                    fastRemove(i);
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(heap[i])) {
                    fastRemove(i);
                    return true;
                }
            }
        }
        return false;
    }

    private void fastRemove(int i) {
        heap[i] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (i < size) {
            siftDown(i);
        }
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) {
            siftDown(0);
        }
        return result;
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return heap[0];
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        return heap[0];
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == heap.length) {
            E[] newHeap = (E[]) new Object[heap.length * 2];
            for (int i = 0; i < size; i++) {
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (compare(heap[index], heap[parentIndex]) >= 0) {
                break;
            }
            swap(index, parentIndex);
            index = parentIndex;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int leftChildIndex = 2 * index + 1;
            int rightChildIndex = 2 * index + 2;
            int smallestChildIndex = index;

            if (leftChildIndex < size && compare(heap[leftChildIndex], heap[smallestChildIndex]) < 0) {
                smallestChildIndex = leftChildIndex;
            }
            if (rightChildIndex < size && compare(heap[rightChildIndex], heap[smallestChildIndex]) < 0) {
                smallestChildIndex = rightChildIndex;
            }
            if (smallestChildIndex == index) {
                break;
            }
            swap(index, smallestChildIndex);
            index = smallestChildIndex;
        }
    }

    private void swap(int i, int j) {
        E temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int newSize = 0;
        boolean changed = false;

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize] = heap[i];
                newSize++;
            } else {
                changed = true;
            }
        }

        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }

        size = newSize;

        if (changed) {
            for (int i = size / 2 - 1; i >= 0; i--) {
                siftDown(i);
            }
        }

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int newSize = 0;
        boolean changed = false;

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize] = heap[i];
                newSize++;
            } else {
                changed = true;
            }
        }

        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }

        size = newSize;

        if (changed) {
            for (int i = size / 2 - 1; i >= 0; i--) {
                siftDown(i);
            }
        }

        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}