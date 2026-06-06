package by.it.group451051.teterukov.lessons_09_15.lesson11;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private Object[] data;
    private int size;
    private final Comparator<? super E> comparator;

    public MyTreeSet() {
        this(null);
    }

    public MyTreeSet(Comparator<? super E> comparator) {
        this.data       = new Object[DEFAULT_CAPACITY];
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    private int compare(Object a, Object b) {
        if (comparator != null)
            return comparator.compare((E) a, (E) b);
        return ((Comparable<Object>) a).compareTo(b);
    }

    private int binarySearch(Object o) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = compare(data[mid], o);
            if      (cmp < 0) lo = mid + 1;
            else if (cmp > 0) hi = mid - 1;
            else              return mid;
        }
        return -(lo + 1);
    }

    private void ensureCapacity() {
        if (size < data.length) return;
        Object[] newData = new Object[data.length * 2];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    private void insertAt(int idx, Object o) {
        ensureCapacity();
        System.arraycopy(data, idx, data, idx + 1, size - idx);
        data[idx] = o;
        size++;
    }

    private void removeAt(int idx) {
        System.arraycopy(data, idx + 1, data, idx, size - idx - 1);
        data[--size] = null;
    }

    @Override public int     size()    { return size; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public boolean add(E e) {
        int idx = binarySearch(e);
        if (idx >= 0) return false;
        insertAt(-(idx + 1), e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = binarySearch(o);
        if (idx < 0) return false;
        removeAt(idx);
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return binarySearch(o) >= 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) if (add(e)) changed = true;
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) if (remove(o)) changed = true;
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(data[i])) {
                removeAt(i);
                changed = true;
            }
        }
        return changed;
    }
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int cursor = 0;
            @Override public boolean hasNext() { return cursor < size; }
            @Override @SuppressWarnings("unchecked")
            public E next() { return (E) data[cursor++]; }
        };
    }

    @Override public Object[] toArray() { return toArray(new Object[0]); }
    @Override @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        System.arraycopy(data, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }
}