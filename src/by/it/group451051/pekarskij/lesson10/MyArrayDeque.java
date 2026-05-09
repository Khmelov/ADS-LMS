package by.it.group451051.pekarskij.lesson10;

import java.util.Deque;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int head;
    private int tail;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0; tail = 0; size = 0;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newArray = (E[]) new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newArray[i] = elements[(head + i) % elements.length];
            }
            elements = newArray;
            head = 0; tail = size;
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[(head + i) % elements.length] = null;
        }
        head = 0; tail = 0; size = 0;
    }

    @Override
    public boolean add(E e) { addLast(e); return true; }

    @Override
    public void addFirst(E e) {
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        ensureCapacity();
        elements[tail] = e;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public boolean offer(E e) { return add(e); }
    @Override
    public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override
    public boolean offerLast(E e) { addLast(e); return true; }

    @Override
    public E remove() { return removeFirst(); }
    @Override
    public E removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        return pollFirst();
    }
    @Override
    public E removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        return pollLast();
    }

    @Override
    public E poll() { return pollFirst(); }
    @Override
    public E pollFirst() {
        if (isEmpty()) return null;
        E res = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return res;
    }
    @Override
    public E pollLast() {
        if (isEmpty()) return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E res = elements[tail];
        elements[tail] = null;
        size--;
        return res;
    }

    @Override
    public E element() { return getFirst(); }
    @Override
    public E getFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        return elements[head];
    }
    @Override
    public E getLast() {
        if (isEmpty()) throw new NoSuchElementException();
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E peek() { return peekFirst(); }
    @Override
    public E peekFirst() { return isEmpty() ? null : elements[head]; }
    @Override
    public E peekLast() { return isEmpty() ? null : elements[(tail - 1 + elements.length) % elements.length]; }

    @Override
    public void push(E e) { addFirst(e); }
    @Override
    public E pop() { return removeFirst(); }

    @Override
    public boolean contains(Object o) { return indexOf(o) != -1; }
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) if (!contains(item)) return false;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx == -1) return false;
        removeAt(idx);
        return true;
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean mod = false;
        for (Object item : c) while (remove(item)) mod = true;
        return mod;
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean mod = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(getAt(i))) { removeAt(i); mod = true; }
        }
        return mod;
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean mod = false;
        for (E item : c) { addLast(item); mod = true; }
        return mod;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override
    public boolean removeLastOccurrence(Object o) {
        int idx = lastIndexOf(o);
        if (idx == -1) return false;
        removeAt(idx);
        return true;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = elements[(head + i) % elements.length];
        return arr;
    }
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) a[i] = (T) elements[(head + i) % elements.length];
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cur = head, cnt = 0;
            @Override public boolean hasNext() { return cnt < size; }
            @Override public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E res = elements[cur];
                cur = (cur + 1) % elements.length;
                cnt++;
                return res;
            }
        };
    }
    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private int cur = (tail - 1 + elements.length) % elements.length, cnt = 0;
            @Override public boolean hasNext() { return cnt < size; }
            @Override public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E res = elements[cur];
                cur = (cur - 1 + elements.length) % elements.length;
                cnt++;
                return res;
            }
        };
    }

    // вспомогательные методы для поиска и удаления
    private int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            E el = elements[(head + i) % elements.length];
            if (o == null ? el == null : o.equals(el)) return i;
        }
        return -1;
    }
    private int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            E el = elements[(head + i) % elements.length];
            if (o == null ? el == null : o.equals(el)) return i;
        }
        return -1;
    }
    private E getAt(int i) { return elements[(head + i) % elements.length]; }

    private void removeAt(int i) {
        if (i < head) {
            for (int j = i; j < head; j++) {
                int next = (j + elements.length) % elements.length;
                int prev = ((j - 1) + elements.length) % elements.length;
                elements[next] = elements[prev];
            }
            head = (head - 1 + elements.length) % elements.length;
        } else {
            for (int j = i; j < size - 1; j++) {
                elements[(head + j) % elements.length] = elements[(head + j + 1) % elements.length];
            }
            tail = (tail - 1 + elements.length) % elements.length;
            elements[tail] = null;
        }
        size--;
    }
}