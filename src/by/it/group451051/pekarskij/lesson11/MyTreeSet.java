package by.it.group451051.pekarskij.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyTreeSet<E extends Comparable<E>> implements Set<E> {

    private E[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newArray = (E[]) new Comparable[elements.length * 2];
            for (int i = 0; i < size; i++) newArray[i] = elements[i];
            elements = newArray;
        }
    }

    // бинарный поиск: возвращает индекс элемента или -(точка вставки)-1
    private int binarySearch(E key) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = key.compareTo(elements[mid]);
            if (cmp == 0) return mid;
            if (cmp < 0) hi = mid - 1; else lo = mid + 1;
        }
        return -lo - 1;
    }

    @Override
    public String toString() {
        // элементы уже отсортированы, просто выводим их
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) elements[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        int pos = binarySearch(e);
        if (pos >= 0) return false; // уже есть
        ensureCapacity();
        int insertPos = -pos - 1;
        // сдвигаем элементы вправо
        for (int i = size; i > insertPos; i--) {
            elements[i] = elements[i - 1];
        }
        elements[insertPos] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        E key = (E) o;
        int pos = binarySearch(key);
        if (pos < 0) return false; // не найдено
        // сдвигаем элементы влево
        for (int i = pos; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        @SuppressWarnings("unchecked")
        E key = (E) o;
        return binarySearch(key) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) if (!contains(item)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean mod = false;
        for (E item : c) if (add(item)) mod = true;
        return mod;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean mod = false;
        for (Object item : c) if (remove(item)) mod = true;
        return mod;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean mod = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                mod = true;
            }
        }
        return mod;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = elements[i];
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) a[i] = (T) elements[i];
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            @Override public boolean hasNext() { return cursor < size; }
            @Override public E next() {
                if (cursor >= size) throw new java.util.NoSuchElementException();
                return elements[cursor++];
            }
        };
    }
}