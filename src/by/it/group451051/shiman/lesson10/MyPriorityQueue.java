package by.it.group451051.shiman.lesson10;

import java.util.*;

public class MyPriorityQueue<E> implements Queue<E>, Collection<E> {

    private static final int DEFAULT_CAPACITY = 11;
    private Object[] heap;
    private int size;
    private int modCount;

    public MyPriorityQueue() {
        heap = new Object[DEFAULT_CAPACITY];
    }

    private void grow() {
        int old = heap.length;
        int newCap = old + (old < 64 ? old + 2 : old >> 1);
        heap = Arrays.copyOf(heap, newCap);
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E e = (E) heap[parent];
            if (((Comparable<? super E>) x).compareTo(e) >= 0) break;
            heap[k] = e;
            k = parent;
        }
        heap[k] = x;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E c = (E) heap[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) c).compareTo((E) heap[right]) > 0)
                c = (E) heap[child = right];
            if (((Comparable<? super E>) x).compareTo(c) <= 0) break;
            heap[k] = c;
            k = child;
        }
        heap[k] = x;
    }

    private E removeAt(int i) {
        modCount++;
        int last = size - 1;
        E removed = (E) heap[i];
        if (i == last) {
            heap[i] = null;
        } else {
            E moved = (E) heap[last];
            heap[last] = null;
            siftDown(i, moved);
            if (heap[i] == moved) {
                siftUp(i, moved);
                if (heap[i] != moved) return moved;
            }
        }
        size--;
        return removed;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(heap[i], o)) return true;
        return false;
    }

    @Override
    public Iterator<E> iterator() { return new Itr(); }

    private class Itr implements Iterator<E> {
        int cursor = 0;
        int lastRet = -1;
        int expected = modCount;

        @Override
        public boolean hasNext() { return cursor < size; }

        @Override
        public E next() {
            if (expected != modCount) throw new ConcurrentModificationException();
            if (cursor >= size) throw new NoSuchElementException();
            lastRet = cursor;
            return (E) heap[cursor++];
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            if (expected != modCount) throw new ConcurrentModificationException();
            removeAt(lastRet);
            if (lastRet < cursor) cursor--;
            lastRet = -1;
            expected = modCount;
        }
    }

    @Override
    public Object[] toArray() { return Arrays.copyOf(heap, size); }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean add(E e) { return offer(e); }

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        if (size >= heap.length) grow();
        modCount++;
        siftUp(size++, e);
        return true;
    }

    @Override
    public E remove() {
        E x = poll();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E poll() {
        if (size == 0) return null;
        modCount++;
        E result = (E) heap[0];
        E moved = (E) heap[--size];
        heap[size] = null;
        if (size > 0) siftDown(0, moved);
        return result;
    }

    @Override
    public E element() {
        E x = peek();
        if (x == null) throw new NoSuchElementException();
        return x;
    }

    @Override
    public E peek() { return size == 0 ? null : (E) heap[0]; }

    @Override
    public void clear() {
        modCount++;
        for (int i = 0; i < size; i++) heap[i] = null;
        size = 0;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(heap[i], o)) { removeAt(i); return true; }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) if (offer(e)) modified = true;
        return modified;
    }

    // Надёжная реализация retainAll через временный массив
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Object[] kept = new Object[size];
        int keptSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                kept[keptSize++] = heap[i];
            } else {
                modified = true;
            }
        }
        if (modified) {
            clear();
            for (int i = 0; i < keptSize; i++) {
                offer((E) kept[i]);
            }
        }
        return modified;
    }

    // Надёжная реализация removeAll через временный массив
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Object[] kept = new Object[size];
        int keptSize = 0;
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                kept[keptSize++] = heap[i];
            } else {
                modified = true;
            }
        }
        if (modified) {
            clear();
            for (int i = 0; i < keptSize; i++) {
                offer((E) kept[i]);
            }
        }
        return modified;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(heap[i]);
        }
        return sb.append("]").toString();
    }
}