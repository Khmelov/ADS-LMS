package by.it.group451051.teterukov.lessons_09_15.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private Object[] data;
    private int size;

    public MyPriorityQueue() {
        data = new Object[16];
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? data[i] == null : o.equals(data[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    private void grow() {
        Object[] bigger = new Object[data.length * 2];
        for (int i = 0; i < size; i++) bigger[i] = data[i];
        data = bigger;
    }
    @SuppressWarnings("unchecked")
    private int cmp(Object a, Object b) {
        return ((Comparable<E>) a).compareTo((E) b);
    }

    private void swap(int i, int j) {
        Object tmp = data[i]; data[i] = data[j]; data[j] = tmp;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (cmp(data[i], data[parent]) < 0) {
                swap(i, parent);
                i = parent;
            } else break;
        }
    }

    private void siftDown(int i) {
        while (true) {
            int left  = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (left  < size && cmp(data[left],  data[smallest]) < 0) smallest = left;
            if (right < size && cmp(data[right], data[smallest]) < 0) smallest = right;

            if (smallest == i) break;
            swap(i, smallest);
            i = smallest;
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        if (size == data.length) grow();
        data[size] = e;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public boolean offer(E e) { return add(e); }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) return null;
        E min = (E) data[0];
        data[0] = data[--size];
        data[size] = null;
        if (size > 0) siftDown(0);
        return min;
    }

    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();
        return poll();
    }
    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        return size == 0 ? null : (E) data[0];
    }

    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return peek();
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? data[i] == null : o.equals(data[i])) return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) add(e);
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Object[] tmp = new Object[size];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(data[i])) {
                tmp[newSize++] = data[i];
            } else {
                changed = true;
            }
        }
        rebuild(tmp, newSize);
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Object[] tmp = new Object[size];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(data[i])) {
                tmp[newSize++] = data[i];
            } else {
                changed = true;
            }
        }
        rebuild(tmp, newSize);
        return changed;
    }

    private void rebuild(Object[] src, int n) {
        for (int i = 0; i < size; i++) data[i] = null;
        for (int i = 0; i < n; i++) data[i] = src[i];
        size = n;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    private void removeAt(int i) {
        size--;
        if (i == size) {
            data[size] = null;
        } else {
            data[i] = data[size];
            data[size] = null;
            siftDown(i);
            siftUp(i);
        }
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = data[i];
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) a[i] = (T) data[i];
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cursor = 0;
            @Override public boolean hasNext() { return cursor < size; }
            @Override @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (E) data[cursor++];
            }
        };
    }
}