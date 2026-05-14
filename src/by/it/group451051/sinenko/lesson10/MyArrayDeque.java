package by.it.group451051.sinenko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;
import java.util.Arrays;

public class MyArrayDeque<E> implements Deque<E> {

    private E[] elements;      // массив для хранения элементов
    private int head;          // индекс первого элемента
    private int tail;          // индекс куда вставлять следующий
    private int size;          // сколько элементов сейчас

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[10];  
        head = 0;
        tail = 0;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0, idx = head; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[idx]);
            idx = (idx + 1) % elements.length;
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

    @SuppressWarnings("unchecked")
    private void expandArray() {
        int newCapacity = elements.length * 2;
        E[] newElements = (E[]) new Object[newCapacity];
    
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[(head + i) % elements.length];
        }
        elements = newElements;
        head = 0;
        tail = size;
    }
    
    @Override
    public void addFirst(E e) {
        if (size == elements.length) {
            expandArray();
        }
        head = (head - 1 + elements.length) % elements.length;  // двигаем head назад
        elements[head] = e;
        size++;
    }
    
    @Override
    public void addLast(E e) {
        if (size == elements.length) {
            expandArray();
        }
        elements[tail] = e;           // кладём элемент
        tail = (tail + 1) % elements.length;  // двигаем tail
        size++;
    }
    
    @Override
    public E element() {
        return getFirst();
    }
    
    @Override
    public E getFirst() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return elements[head];
    }
    
    @Override
    public E getLast() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        int lastIndex = (tail - 1 + elements.length) % elements.length;
        return elements[lastIndex];
    }
    
    @Override
    public E poll() {
        return pollFirst();
    }
    
    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E result = elements[head];
        elements[head] = null;  
        head = (head + 1) % elements.length;
        size--;
        return result;
    }
    
    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        int lastIndex = (tail - 1 + elements.length) % elements.length;
        E result = elements[lastIndex];
        elements[lastIndex] = null;
        tail = lastIndex;
        size--;
        return result;
    }
    
    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            int idx = (head + i) % elements.length;
            if (Objects.equals(elements[idx], o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        return pollFirst();
    }

    @Override
    public E peek() {
        return size == 0 ? null : elements[head];
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        return pollFirst();
    }

    @Override
    public E removeLast() {
        return pollLast();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : elements[head];
    }

    @Override
    public E peekLast() {
        if (size == 0) return null;
        int lastIndex = (tail - 1 + elements.length) % elements.length;
        return elements[lastIndex];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (int i = 0; i < size; i++) {
            int idx = (head + i) % elements.length;
            if (Objects.equals(elements[idx], o)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            int idx = (head + i) % elements.length;
            if (Objects.equals(elements[idx], o)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    private void removeAt(int index) {
        for (int i = index; i < size - 1; i++) {
            int curr = (head + i) % elements.length;
            int next = (head + i + 1) % elements.length;
            elements[curr] = elements[next];
        }
        tail = (tail - 1 + elements.length) % elements.length;
        elements[tail] = null;
        size--;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E item : c) {
            if (add(item)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object item : c) {
            if (remove(item)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {
            int idx = (head + i) % elements.length;
            if (!c.contains(elements[idx])) {
                removeAt(i);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            int idx = (head + i) % elements.length;
            elements[idx] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements[(head + i) % elements.length];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(toArray(), size, a.getClass());
        }
        System.arraycopy(toArray(), 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override 
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return pollFirst();
    }
}
