package by.it.group351051.burdo.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    // узел для хранения элементов и обработки коллизий
    private static class Node<E> {
        final E element;
        final int hash;
        Node<E> next;

        Node(E element, int hash, Node<E> next) {
            this.element = element;
            this.hash = hash;
            this.next = next;
        }
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
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;

        int hash = hash(o);
        int index = (table.length - 1) & hash;

        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && o.equals(node.element)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();

        int hash = hash(e);
        int index = (table.length - 1) & hash;

        // проверяем, есть ли уже такой элемент
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && e.equals(node.element)) {
                return false;
            }
        }

        // добавляем новый элемент в начало цепочки
        table[index] = new Node<>(e, hash, table[index]);
        size++;

        // проверяем необходимость расширения таблицы
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }

        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;

        int hash = hash(o);
        int index = (table.length - 1) & hash;
        Node<E> prev = null;

        for (Node<E> node = table[index]; node != null; prev = node, node = node.next) {
            if (node.hash == hash && o.equals(node.element)) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return true;
            }
        }

        return false;
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
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    private int hash(Object o) {
        int h = o.hashCode();
        return h ^ (h >>> 16); // улучшаем распределение хешей
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1; // удваиваем размер

        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
        table = newTable;

        // перехешируем все элементы
        for (int i = 0; i < oldCapacity; i++) {
            Node<E> node = oldTable[i];
            while (node != null) {
                Node<E> next = node.next;
                int index = (newCapacity - 1) & node.hash;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
    }
}