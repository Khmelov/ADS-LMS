package by.it.group451051.pekarskij.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyHashSet<E> implements Set<E> {

    // узел односвязного списка для обработки коллизий
    private static class Node<E> {
        E item; Node<E> next;
        Node(E it, Node<E> n) { item = it; next = n; }
    }

    private Node<E>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    // вычисляем индекс в массиве по хеш-коду
    private int index(Object o) {
        return (o == null ? 0 : o.hashCode()) & (table.length - 1);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> bucket : table) {
            for (Node<E> cur = bucket; cur != null; cur = cur.next) {
                if (!first) sb.append(", ");
                sb.append(cur.item);
                first = false;
            }
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        int idx = index(e);
        // проверяем, есть ли уже такой элемент в цепочке
        for (Node<E> cur = table[idx]; cur != null; cur = cur.next) {
            if (e == null ? cur.item == null : e.equals(cur.item)) {
                return false;
            }
        }
        // добавляем в начало цепочки
        table[idx] = new Node<>(e, table[idx]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> prev = null;
        for (Node<E> cur = table[idx]; cur != null; cur = cur.next) {
            if (o == null ? cur.item == null : o.equals(cur.item)) {
                if (prev == null) {
                    table[idx] = cur.next;
                } else {
                    prev.next = cur.next;
                }
                size--;
                return true;
            }
            prev = cur;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        for (Node<E> cur = table[idx]; cur != null; cur = cur.next) {
            if (o == null ? cur.item == null : o.equals(cur.item)) {
                return true;
            }
        }
        return false;
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
    public boolean retainAll(Collection<?> c) {
        boolean mod = false;
        for (int i = table.length - 1; i >= 0; i--) {
            Node<E> prev = null;
            Node<E> cur = table[i];
            while (cur != null) {
                Node<E> next = cur.next;
                if (!c.contains(cur.item)) {
                    if (prev == null) table[i] = next;
                    else prev.next = next;
                    size--;
                    mod = true;
                } else {
                    prev = cur;
                }
                cur = next;
            }
        }
        return mod;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean mod = false;
        for (Object item : c) if (remove(item)) mod = true;
        return mod;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Node<E> bucket : table) {
            for (Node<E> cur = bucket; cur != null; cur = cur.next) {
                arr[i++] = cur.item;
            }
        }
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        for (Node<E> bucket : table) {
            for (Node<E> cur = bucket; cur != null; cur = cur.next) {
                a[i++] = (T) cur.item;
            }
        }
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucket = 0;
            private Node<E> cur = null;

            private void findNext() {
                while (cur == null && bucket < table.length) {
                    cur = table[bucket++];
                }
            }

            @Override
            public boolean hasNext() {
                if (cur == null) findNext();
                return cur != null;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new java.util.NoSuchElementException();
                E res = cur.item;
                cur = cur.next;
                return res;
            }
        };
    }
}