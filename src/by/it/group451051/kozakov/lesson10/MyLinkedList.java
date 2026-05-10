package by.it.group451051.kozakov.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

public class MyLinkedList<E> implements Deque<E> {

    private class Node {
        E data;
        Node prev;
        Node next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node first;
    private Node last;
    private int size;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node current = first;

        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        Node current;

        if (index < size / 2) {
            current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }

        E result = current.data;

        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            first = current.next;
        }

        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            last = current.prev;
        }

        size--;
        return result;
    }

    @Override
    public boolean remove(Object o) {
        Node current = first;

        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    first = current.next;
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    last = current.prev;
                }

                size--;
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        Node node = new Node(element);

        if (size == 0) {
            first = node;
            last = node;
        } else {
            node.next = first;
            first.prev = node;
            first = node;
        }

        size++;
    }

    @Override
    public void addLast(E element) {
        Node node = new Node(element);

        if (size == 0) {
            first = node;
            last = node;
        } else {
            last.next = node;
            node.prev = last;
            last = node;
        }

        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        return first == null ? null : first.data;
    }

    @Override
    public E getLast() {
        return last == null ? null : last.data;
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

        E result = first.data;

        if (first == last) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }

        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }

        E result = last.data;

        if (first == last) {
            first = null;
            last = null;
        } else {
            last = last.prev;
            last.next = null;
        }

        size--;
        return result;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean offerFirst(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offerLast(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E removeFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E removeLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peekFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peekLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offer(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E peek() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void push(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pop() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> descendingIterator() {
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
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }
}