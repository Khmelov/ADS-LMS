package by.it.group451051.zhelubovskaya.lesson10;

import java.util.*;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;
    private int head;
    private int tail;
    private int size;

    public MyArrayDeque() {
        elements = (E[]) new Object[10];
        head = 0;
        tail = 0;
        size = 0;
    }

    private void grow() {
        if (size == elements.length) {
            E[] newArray = (E[]) new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newArray[i] = elements[(head + i) % elements.length];
            }
            elements = newArray;
            head = 0;
            tail = size;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        grow();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        grow();
        elements[tail] = e;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) return null;
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) return null;
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E removed = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return removed;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E removed = elements[tail];
        elements[tail] = null;
        size--;
        return removed;
    }

    // заглушки
    @Override
    public boolean offer(E e) { return false; }
    @Override
    public E remove() { return null; }
    @Override
    public E peek() { return null; }
    @Override
    public boolean offerFirst(E e) { return false; }
    @Override
    public boolean offerLast(E e) { return false; }
    @Override
    public E removeFirst() { return null; }
    @Override
    public E removeLast() { return null; }
    @Override
    public E peekFirst() { return null; }
    @Override
    public E peekLast() { return null; }
    @Override
    public boolean removeFirstOccurrence(Object o) { return false; }
    @Override
    public boolean removeLastOccurrence(Object o) { return false; }
    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }
    @Override
    public void push(E e) {}
    @Override
    public E pop() { return null; }
    @Override
    public boolean remove(Object o) { return false; }
    @Override
    public boolean contains(Object o) { return false; }
    @Override
    public boolean containsAll(Collection<?> c) { return false; }
    @Override
    public boolean removeAll(Collection<?> c) { return false; }
    @Override
    public boolean retainAll(Collection<?> c) { return false; }
    @Override
    public void clear() {}
    @Override
    public boolean isEmpty() { return size == 0; }
    @Override
    public Iterator<E> iterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
    @Override
    public Iterator<E> descendingIterator() { return null; }
}