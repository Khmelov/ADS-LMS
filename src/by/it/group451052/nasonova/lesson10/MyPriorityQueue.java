package by.it.group451052.nasonova.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] data;
    private int size;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        data = (E[]) new Object[10];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size >= data.length) {
            E[] newData = (E[]) new Object[data.length * 2];
            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compare(data[index], data[parent]) < 0) {
                E temp = data[index];
                data[index] = data[parent];
                data[parent] = temp;
                index = parent;
            } else {
                break;
            }
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int smallest = index;

            if (left < size && compare(data[left], data[smallest]) < 0) {
                smallest = left;
            }

            if (right < size && compare(data[right], data[smallest]) < 0) {
                smallest = right;
            }

            if (smallest != index) {
                E temp = data[index];
                data[index] = data[smallest];
                data[smallest] = temp;
                index = smallest;
            } else {
                break;
            }
        }
    }

    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
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
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        offer(element);
        return true;
    }

    @Override
    public E remove() {
        E result = poll();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public boolean offer(E element) {
        ensureCapacity();
        data[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        E result = data[0];
        data[0] = data[size - 1];
        data[size - 1] = null;
        size--;

        if (size > 0) {
            siftDown(0);
        }

        return result;
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        return data[0];
    }

    @Override
    public E element() {
        E result = peek();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null) {
                if (data[i] == null) {
                    return true;
                }
            } else {
                if (o.equals(data[i])) {
                    return true;
                }
            }
        }
        return false;
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
        boolean changed = false;
        for (E element : c) {
            offer(element);
            changed = true;
        }
        return changed;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        int newSize = 0;
        boolean changed = false;

        for (int i = 0; i < size; i++) {
            if (!c.contains(data[i])) {
                data[newSize] = data[i];
                newSize++;
            } else {
                changed = true;
            }
        }

        for (int i = newSize; i < size; i++) {
            data[i] = null;
        }

        size = newSize;

        if (changed) {
            heapify();
        }

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int newSize = 0;
        boolean changed = false;

        for (int i = 0; i < size; i++) {
            if (c.contains(data[i])) {
                data[newSize] = data[i];
                newSize++;
            } else {
                changed = true;
            }
        }

        for (int i = newSize; i < size; i++) {
            data[i] = null;
        }

        size = newSize;

        if (changed) {
            heapify();
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

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }
}