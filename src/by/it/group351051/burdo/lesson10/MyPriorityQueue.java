package by.it.group351051.burdo.lesson10;
import java.util.Iterator;

import java.util.Collection;
import java.util.Queue;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(heap[i]);
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
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return poll();
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (element.equals(heap[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();

        if (size >= heap.length) {
            resize();
        }

        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) return null;

        E result = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        siftDown(0);
        return result;
    }

    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return heap[0];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Object[] newHeap = new Object[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                newHeap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        if (modified) {
            System.arraycopy(newHeap, 0, heap, 0, newSize);
            for (int i = newSize; i < size; i++) {
                heap[i] = null;
            }
            size = newSize;

            for (int i = (size >>> 1) - 1; i >= 0; i--) {
                siftDown(i);
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            } else {
                modified = true;
            }
        }

        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }

        size = newSize;

        if (modified) {
            for (int i = (size >>> 1) - 1; i >= 0; i--) {
                siftDown(i);
            }
        }

        return modified;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    private void resize() {
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Object[heap.length * 2];
        System.arraycopy(heap, 0, newHeap, 0, heap.length);
        heap = newHeap;
    }

    private void siftUp(int index) {
        E element = heap[index];
        while (index > 0) {
            int parentIndex = (index - 1) >>> 1;
            E parent = heap[parentIndex];
            if (compare(element, parent) >= 0) {
                break;
            }
            heap[index] = parent;
            index = parentIndex;
        }
        heap[index] = element;
    }

    private void siftDown(int index) {
        E element = heap[index];
        int half = size >>> 1;
        while (index < half) {
            int childIndex = (index << 1) + 1;
            E child = heap[childIndex];
            int rightIndex = childIndex + 1;
            if (rightIndex < size && compare(child, heap[rightIndex]) > 0) {
                childIndex = rightIndex;
                child = heap[rightIndex];
            }
            if (compare(element, child) <= 0) {
                break;
            }
            heap[index] = child;
            index = childIndex;
        }
        heap[index] = element;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void removeAt(int index) {
        size--;
        if (size == index) {
            heap[index] = null;
        } else {
            E moved = heap[size];
            heap[size] = null;
            heap[index] = moved;
            siftDown(index);
            if (heap[index] == moved) {
                siftUp(index);
            }
        }
    }
}