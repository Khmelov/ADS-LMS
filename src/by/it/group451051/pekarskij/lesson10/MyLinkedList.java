package by.it.group451051.pekarskij.lesson10;

import java.util.Deque;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E item; Node<E> prev, next;
        Node(Node<E> p, E it, Node<E> n) { prev = p; item = it; next = n; }
    }

    private Node<E> head, tail;
    private int size;

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        for (int i = 0; i < size; i++) {
            sb.append(cur.item);
            if (i < size - 1) sb.append(", ");
            cur = cur.next;
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() { head = tail = null; size = 0; }

    @Override
    public boolean add(E e) { addLast(e); return true; }

    @Override
    public void addFirst(E e) {
        head = new Node<>(null, e, head);
        if (tail == null) tail = head; else head.next.prev = head;
        size++;
    }

    @Override
    public void addLast(E e) {
        tail = new Node<>(tail, e, null);
        if (head == null) head = tail; else tail.prev.next = tail;
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
        E res = head.item;
        head = head.next;
        if (head == null) tail = null; else head.prev = null;
        size--;
        return res;
    }
    @Override
    public E removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        E res = tail.item;
        tail = tail.prev;
        if (tail == null) head = null; else tail.next = null;
        size--;
        return res;
    }

    @Override
    public E poll() { return pollFirst(); }
    @Override
    public E pollFirst() { return isEmpty() ? null : removeFirst(); }
    @Override
    public E pollLast() { return isEmpty() ? null : removeLast(); }

    @Override
    public E element() { return getFirst(); }
    @Override
    public E getFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        return head.item;
    }
    @Override
    public E getLast() {
        if (isEmpty()) throw new NoSuchElementException();
        return tail.item;
    }

    @Override
    public E peek() { return peekFirst(); }
    @Override
    public E peekFirst() { return isEmpty() ? null : head.item; }
    @Override
    public E peekLast() { return isEmpty() ? null : tail.item; }

    @Override
    public void push(E e) { addFirst(e); }
    @Override
    public E pop() { return removeFirst(); }

    // этот метод НЕ из интерфейса Deque, поэтому БЕЗ @Override
    public boolean remove(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if (o == null ? cur.item == null : o.equals(cur.item)) {
                unlink(cur);
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    // этот метод НЕ из интерфейса Deque, поэтому БЕЗ @Override
    public E remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> node = nodeAt(index);
        E res = node.item;
        unlink(node);
        return res;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> cur = head;
        while (cur != null) {
            if (o == null ? cur.item == null : o.equals(cur.item)) return true;
            cur = cur.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) if (!contains(item)) return false;
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
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (!c.contains(cur.item)) { unlink(cur); mod = true; }
            cur = next;
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
        Node<E> cur = tail;
        while (cur != null) {
            if (o == null ? cur.item == null : o.equals(cur.item)) {
                unlink(cur);
                return true;
            }
            cur = cur.prev;
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        Node<E> cur = head;
        for (int i = 0; i < size; i++) { arr[i] = cur.item; cur = cur.next; }
        return arr;
    }
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        Node<E> cur = head;
        for (int i = 0; i < size; i++) { a[i] = (T) cur.item; cur = cur.next; }
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> cur = head;
            @Override public boolean hasNext() { return cur != null; }
            @Override public E next() {
                if (cur == null) throw new NoSuchElementException();
                E res = cur.item; cur = cur.next; return res;
            }
        };
    }
    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            Node<E> cur = tail;
            @Override public boolean hasNext() { return cur != null; }
            @Override public E next() {
                if (cur == null) throw new NoSuchElementException();
                E res = cur.item; cur = cur.prev; return res;
            }
        };
    }

    private Node<E> nodeAt(int index) {
        if (index < size / 2) {
            Node<E> cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
            return cur;
        } else {
            Node<E> cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
            return cur;
        }
    }

    private void unlink(Node<E> node) {
        if (node.prev != null) node.prev.next = node.next; else head = node.next;
        if (node.next != null) node.next.prev = node.prev; else tail = node.prev;
        node.item = null; node.prev = node.next = null;
        size--;
    }
}