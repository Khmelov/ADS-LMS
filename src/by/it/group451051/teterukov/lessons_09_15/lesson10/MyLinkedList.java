package by.it.group451051.teterukov.lessons_09_15.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E item, Node<E> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    private Node<E> nodeAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> cur;
        if (index < size / 2) {
            cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
        } else {
            cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
        }
        return cur;
    }

    private E unlink(Node<E> node) {
        E val = node.item;
        Node<E> p = node.prev;
        Node<E> n = node.next;
        if (p == null) head = n; else { p.next = n; node.prev = null; }
        if (n == null) tail = p; else { n.prev = p; node.next = null; }
        node.item = null;
        size--;
        return val;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.item);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public E remove(int index) {
        return unlink(nodeAt(index));
    }

    @Override
    public boolean remove(Object o) {
        for (Node<E> cur = head; cur != null; cur = cur.next) {
            if (o == null ? cur.item == null : o.equals(cur.item)) {
                unlink(cur);
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E e) {
        Node<E> node = new Node<>(null, e, head);
        if (head == null) tail = node;
        else head.prev = node;
        head = node;
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> node = new Node<>(tail, e, null);
        if (tail == null) head = node;
        else tail.next = node;
        tail = node;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return tail.item;
    }
    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        return head == null ? null : unlink(head);
    }

    @Override
    public E pollLast() {
        return tail == null ? null : unlink(tail);
    }

    @Override public boolean isEmpty()       { return size == 0; }

    @Override public void push(E e)          { addFirst(e); }
    @Override public E pop()                 { return removeFirst(); }
    @Override public E peek()                { return peekFirst(); }
    @Override public E peekFirst()           { return head == null ? null : head.item; }
    @Override public E peekLast()            { return tail == null ? null : tail.item; }

    @Override public E remove()              { return removeFirst(); }
    @Override public E removeFirst()         { if (head == null) throw new NoSuchElementException(); return unlink(head); }
    @Override public E removeLast()          { if (tail == null) throw new NoSuchElementException(); return unlink(tail); }

    @Override public boolean offer(E e)      { return offerLast(e); }
    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e)  { addLast(e); return true; }

    @Override public boolean contains(Object o) {
        for (Node<E> c = head; c != null; c = c.next)
            if (o == null ? c.item == null : o.equals(c.item)) return true;
        return false;
    }

    @Override public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override public boolean removeLastOccurrence(Object o) {
        for (Node<E> c = tail; c != null; c = c.prev)
            if (o == null ? c.item == null : o.equals(c.item)) { unlink(c); return true; }
        return false;
    }

    @Override public void clear() {
        for (Node<E> c = head; c != null; ) {
            Node<E> next = c.next;
            c.item = null; c.prev = null; c.next = null;
            c = next;
        }
        head = tail = null;
        size = 0;
    }

    @Override public boolean addAll(Collection<? extends E> c) {
        for (E e : c) addLast(e);
        return !c.isEmpty();
    }
    @Override public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }
    @Override public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (c.contains(cur.item)) { unlink(cur); changed = true; }
            cur = next;
        }
        return changed;
    }
    @Override public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (!c.contains(cur.item)) { unlink(cur); changed = true; }
            cur = next;
        }
        return changed;
    }

    @Override public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Node<E> c = head; c != null; c = c.next) arr[i++] = c.item;
        return arr;
    }
    @Override @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        for (Node<E> c = head; c != null; c = c.next) a[i++] = (T) c.item;
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
                E val = cur.item; cur = cur.next; return val;
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
                E val = cur.item; cur = cur.prev; return val;
            }
        };
    }
}
