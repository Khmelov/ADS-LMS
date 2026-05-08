package by.it.group451051.kozakov.lesson11;

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // Узел хеш-таблицы
    private static class HashNode<E> {
        final int hash;
        final E value;
        HashNode<E> next;
        LinkedNode<E> linkedNode;

        HashNode(int hash, E value, HashNode<E> next, LinkedNode<E> linkedNode) {
            this.hash = hash;
            this.value = value;
            this.next = next;
            this.linkedNode = linkedNode;
        }
    }

    // Узел двусвязного списка для сохранения порядка добавления
    private static class LinkedNode<E> {
        E value;
        LinkedNode<E> prev;
        LinkedNode<E> next;

        LinkedNode(E value, LinkedNode<E> prev, LinkedNode<E> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private HashNode<E>[] table;
    private int size;
    private float loadFactor;
    private LinkedNode<E> head;
    private LinkedNode<E> tail;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        this.table = (HashNode<E>[]) new HashNode[DEFAULT_CAPACITY];
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        HashNode<E>[] oldTable = table;
        table = (HashNode<E>[]) new HashNode[newCapacity];
        for (HashNode<E> node : oldTable) {
            while (node != null) {
                HashNode<E> next = node.next;
                int index = indexFor(node.hash, newCapacity);
                node.next = table[index];
                table[index] = node;
                node = next;
            }
        }
    }

    @Override
    public boolean add(E e) {
        int hash = hash(e);
        int index = indexFor(hash, table.length);
        HashNode<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && (node.value == e || (e != null && e.equals(node.value)))) {
                return false;
            }
            node = node.next;
        }

        LinkedNode<E> linkedNode = new LinkedNode<>(e, tail, null);
        if (head == null) {
            head = linkedNode;
        } else {
            tail.next = linkedNode;
        }
        tail = linkedNode;

        table[index] = new HashNode<>(hash, e, table[index], linkedNode);
        size++;

        if (size > table.length * loadFactor) {
            resize(2 * table.length);
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);
        HashNode<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && (node.value == o || (o != null && o.equals(node.value)))) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);
        HashNode<E> prev = null;
        HashNode<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && (node.value == o || (o != null && o.equals(node.value)))) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }

                // Удаляем узел из двусвязного списка
                LinkedNode<E> linkedNode = node.linkedNode;
                if (linkedNode.prev != null) {
                    linkedNode.prev.next = linkedNode.next;
                } else {
                    head = linkedNode.next;
                }
                if (linkedNode.next != null) {
                    linkedNode.next.prev = linkedNode.prev;
                } else {
                    tail = linkedNode.prev;
                }

                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
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
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
        head = null;
        tail = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        LinkedNode<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.value);
            first = false;
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        LinkedNode<E> current = head;
        while (current != null) {
            E value = current.value;
            if (!c.contains(value)) {
                remove(value);
                modified = true;
                current = current.next;
            } else {
                current = current.next;
            }
        }
        return modified;
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
}