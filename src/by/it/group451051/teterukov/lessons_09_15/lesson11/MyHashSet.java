package by.it.group451051.teterukov.lessons_09_15.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    private static class Node<E> {
        E item;
        Node<E> next;
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR    = 0.75f;

    private Node<E>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    private int indexOf(Object o) {
        int h = o == null ? 0 : o.hashCode();
        return Math.abs(h) % buckets.length;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        Node<E>[] old = buckets;
        buckets = new Node[old.length * 2];
        size = 0;
        for (Node<E> head : old) {
            for (Node<E> cur = head; cur != null; cur = cur.next) {
                add(cur.item);
            }
        }
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        buckets = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if ((float) size / buckets.length >= LOAD_FACTOR) rehash();
        int idx = indexOf(e);
        for (Node<E> cur = buckets[idx]; cur != null; cur = cur.next) {
            if (e == null ? cur.item == null : e.equals(cur.item)) return false;
        }
        buckets[idx] = new Node<>(e, buckets[idx]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        Node<E> cur  = buckets[idx];
        Node<E> prev = null;
        while (cur != null) {
            if (o == null ? cur.item == null : o.equals(cur.item)) {
                if (prev == null) buckets[idx] = cur.next;
                else              prev.next    = cur.next;
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
            if (o == null ? cur.item == null : o.equals(cur.item)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> head : buckets) {
            for (Node<E> cur = head; cur != null; cur = cur.next) {
                if (!first) sb.append(", ");
                sb.append(cur.item);
                first = false;
            }
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
        for (Node<E> head : buckets) {
            for (Node<E> cur = head; cur != null; cur = cur.next) {
                if (!c.contains(cur.item)) {
                    remove(cur.item);
                    changed = true;
                }
            }
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int bucket = 0;
            Node<E> cur = null;
            private void advance() {
                while (cur == null && bucket < buckets.length) {
                    cur = buckets[bucket++];
                }
            }

            { advance(); }

            @Override public boolean hasNext() { return cur != null; }
            @Override public E next() {
                E val = cur.item;
                cur = cur.next;
                advance();
                return val;
            }
        };
    }

    @Override public Object[] toArray()        { return toArray(new Object[0]); }
    @Override @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        int i = 0;
        for (Node<E> head : buckets)
            for (Node<E> cur = head; cur != null; cur = cur.next)
                a[i++] = (T) cur.item;
        if (a.length > size) a[size] = null;
        return a;
    }
}