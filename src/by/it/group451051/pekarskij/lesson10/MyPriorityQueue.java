package by.it.group451051.pekarskij.lesson10;

import java.util.Queue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E extends Comparable<E>> implements Queue<E> {

    private E[] heap;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == heap.length) {
            E[] newArray = (E[]) new Comparable[heap.length * 2];
            for (int i = 0; i < size; i++) newArray[i] = heap[i];
            heap = newArray;
        }
    }

    @Override
    public String toString() {
        // выводим элементы в порядке хранения в массиве (без null)
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (int i = 0; i < size; i++) {
            if (heap[i] != null) {
                if (!first) sb.append(", ");
                sb.append(heap[i]);
                first = false;
            }
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        ensureCapacity();
        heap[size] = e;
        siftUp(size++);
        return true;
    }

    @Override
    public boolean offer(E e) { return add(e); }

    @Override
    public E remove() {
        E res = poll();
        if (res == null) throw new NoSuchElementException();
        return res;
    }

    @Override
    public E poll() {
        if (isEmpty()) return null;
        E res = heap[0];
        heap[0] = heap[--size];
        heap[size] = null;
        if (size > 0) siftDown(0);
        return res;
    }

    @Override
    public E peek() { return isEmpty() ? null : heap[0]; }

    @Override
    public E element() {
        E res = peek();
        if (res == null) throw new NoSuchElementException();
        return res;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) if (!contains(item)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean mod = false;
        for (E item : c) { add(item); mod = true; }
        return mod;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // эффективное удаление: проходим по куче один раз
        boolean mod = false;
        int writeIdx = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[writeIdx++] = heap[i];
            } else {
                heap[i] = null;
                mod = true;
            }
        }
        int removed = size - writeIdx;
        size = writeIdx;
        // перестраиваем кучу
        if (size > 0) {
            for (int i = size / 2 - 1; i >= 0; i--) siftDown(i);
        }
        return mod;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // эффективное сохранение только нужных элементов
        boolean mod = false;
        int writeIdx = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[writeIdx++] = heap[i];
            } else {
                heap[i] = null;
                mod = true;
            }
        }
        int removed = size - writeIdx;
        size = writeIdx;
        // перестраиваем кучу
        if (size > 0) {
            for (int i = size / 2 - 1; i >= 0; i--) siftDown(i);
        }
        return mod;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = heap[i];
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) a[i] = (T) heap[i];
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            @Override public boolean hasNext() { return cursor < size; }
            @Override public E next() {
                if (cursor >= size) throw new NoSuchElementException();
                return heap[cursor++];
            }
        };
    }

    private void siftUp(int i) {
        while (i > 0) {
            int p = (i - 1) / 2;
            if (heap[i].compareTo(heap[p]) >= 0) break;
            E tmp = heap[i]; heap[i] = heap[p]; heap[p] = tmp;
            i = p;
        }
    }

    private void siftDown(int i) {
        while (true) {
            int l = 2 * i + 1, r = 2 * i + 2, min = i;
            if (l < size && heap[l].compareTo(heap[min]) < 0) min = l;
            if (r < size && heap[r].compareTo(heap[min]) < 0) min = r;
            if (min == i) break;
            E tmp = heap[i]; heap[i] = heap[min]; heap[min] = tmp;
            i = min;
        }
    }

    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) {
                heap[i] = heap[--size];
                heap[size] = null;
                if (size > 0 && i < size) {
                    siftDown(i);
                    siftUp(i);
                }
                return true;
            }
        }
        return false;
    }
}