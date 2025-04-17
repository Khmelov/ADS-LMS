package by.it.group351051.stepanov.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] heap;
    private int size;
    private Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(heap[i], o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return heap[currentIndex++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove operation is not supported");
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(heap, 0, array, 0, size);
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            @SuppressWarnings("unchecked")
            T[] newArray = (T[]) new Object[size];
            a = newArray;
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(heap[i], o)) {
                removeAt(i);
                return true;
            }
        }
        return false;
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
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        E[] newHeap = (E[]) new Object[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            E element = heap[i];
            if (!c.contains(element)) {
                newHeap[newSize] = element;
                newSize++;
            }
        }

        if (newSize != size) {
            heap = newHeap;
            size = newSize;
            for (int i = (size / 2) - 1; i >= 0; i--) {
                siftDown(i);
            }
            modified = true;
        }

        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        E[] newHeap = (E[]) new Object[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            E element = heap[i];
            if (c.contains(element)) {
                newHeap[newSize] = element;
                newSize++;
            }
        }

        if (newSize != size) {
            heap = newHeap;
            size = newSize;
            for (int i = (size / 2) - 1; i >= 0; i--) {
                siftDown(i);
            }
            modified = true;
        }

        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException("Null elements are not allowed");
        }
        if (size >= heap.length) {
            resize();
        }
        heap[size] = e;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return poll();
    }

    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        E result = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        siftDown(0);
        return result;
    }

    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return heap[0];
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return heap[0];
    }

    private void resize() {
        int newCapacity = heap.length * 2;
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
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

    private int compare(E a, E b) {
        if (a == null && b == null) {
            return 0;
        }
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            @SuppressWarnings("unchecked")
            Comparable<? super E> comparable = (Comparable<? super E>) a;
            return comparable.compareTo(b);
        }
    }

    private void removeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        heap[index] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (index > 0 && compare(heap[index], heap[(index - 1) / 2]) < 0) {
            siftUp(index);
        } else {
            siftDown(index);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}