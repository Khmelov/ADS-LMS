package by.it.group451051.teterukov.lessons_09_15.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> before;
        Node<E> after;

        Node(E item) {
            this.item = item;
        }
    }

    private static final int   DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR      = 0.75f;

    private Node<E>[] buckets;
    private int size;

    private final Node<E> head;
    private final Node<E> tail;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        buckets  = new Node[DEFAULT_CAPACITY];
        head     = new Node<>(null);
        tail     = new Node<>(null);
        head.after  = tail;
        tail.before = head;
    }

    private int indexOf(Object o) {
        int h = (o == null) ? 0 : o.hashCode();
        return Math.abs(h) % buckets.length;
    }

    private void linkLast(Node<E> node) {
        Node<E> prev = tail.before;
        prev.after   = node;
        node.before  = prev;
        node.after   = tail;
        tail.before  = node;
    }

    private void unlink(Node<E> node) {
        node.before.after = node.after;
        node.after.before = node.before;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        Node<E>[] old = buckets;
        buckets = new Node[old.length * 2];
        size    = 0;

        for (Node<E> cur = head.after; cur != tail; cur = cur.after) {
            cur.next = null;
            int idx = indexOf(cur.item);
            cur.next    = buckets[idx];
            buckets[idx] = cur;
            size++;
        }
    }

    @Override public int     size()    { return size; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        buckets     = new Node[DEFAULT_CAPACITY];
        head.after  = tail;
        tail.before = head;
        size        = 0;
    }

    @Override
    public boolean add(E e) {
        if ((float) size / buckets.length >= LOAD_FACTOR) rehash();
        int idx = indexOf(e);
        for (Node<E> cur = buckets[idx]; cur != null; cur = cur.next) {
            if (e == null ? cur.item == null : e.equals(cur.item))
                return false;
        }
        Node<E> node = new Node<>(e);
        node.next    = buckets[idx];
        buckets[idx] = node;
        linkLast(node);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx      = indexOf(o);
        Node<E> cur  = buckets[idx];
        Node<E> prev = null;
        while (cur != null) {
            if (o == null ? cur.item == null : o.equals(cur.item)) {
                if (prev == null) buckets[idx] = cur.next;
                else              prev.next    = cur.next;
                unlink(cur);
                size--;
                return true;
            }
            prev = cur;
            cur  = cur.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int idx = indexOf(o);
        for (Node<E> cur = buckets[idx]; cur != null; cur = cur.next) {
            if (o == null ? cur.item == null : o.equals(cur.item))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head.after;
        while (cur != tail) {
            sb.append(cur.item);
            cur = cur.after;
            if (cur != tail) sb.append(", ");
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
        Node<E> cur = head.after;
        while (cur != tail) {
            Node<E> next = cur.after;
            if (!c.contains(cur.item)) {
                remove(cur.item);
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> cur = head.after;
            @Override public boolean hasNext() { return cur != tail; }
            @Override public E next() {
                E val = cur.item;
                cur   = cur.after;
                return val;
            }
        };
    }

    @Override public Object[] toArray() { return toArray(new Object[0]); }
    @Override @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        int i = 0;
        for (Node<E> cur = head.after; cur != tail; cur = cur.after)
            a[i++] = (T) cur.item;
        if (a.length > size) a[size] = null;
        return a;
    }
}