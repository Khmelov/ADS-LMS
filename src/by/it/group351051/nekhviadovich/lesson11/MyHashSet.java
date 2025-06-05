package by.it.group351051.nekhviadovich.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
//lesson 11 - 1
public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private Node<E>[] table;
    private int size;

    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private static class Node<E> {
        E element;
        Node<E> next;

        Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    private int getIndex(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        while (current != null) {
            if (Objects.equals(current.element, o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);
        Node<E> current = table[index];
        while (current != null) {
            if (Objects.equals(current.element, e)) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }
        table[index] = new Node<>(e, table[index]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> previous = null;
        while (current != null) {
            if (Objects.equals(current.element, o)) {
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> node : table) {
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.element);
                first = false;
                node = node.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
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
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
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
}