package by.it.group351051.burdo.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<E>[] table;
    private Node<E> head; // первый добавленный элемент
    private Node<E> tail; // последний добавленный элемент
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    // узел для хранения элементов и поддержания порядка добавления
    private static class Node<E> {
        final E element;
        final int hash;
        Node<E> next; // следующий элемент в цепочке коллизий
        Node<E> before, after; // ссылки для поддержания порядка добавления

        Node(E element, int hash, Node<E> next) {
            this.element = element;
            this.hash = hash;
            this.next = next;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;

        while (current != null) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.element);
            first = false;
            current = current.after;
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
        head = tail = null;
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

        //проверяем, есть ли уже такой элемент
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && e.equals(node.element)) {
                return false;
            }
        }

        // добавляем новый элемент
        Node<E> newNode = new Node<>(e, hash, table[index]);
        table[index] = newNode;

        // добавляем в конец списка порядка добавления
        linkNodeLast(newNode);

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
                // удаляем из цепочки коллизий
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }

                // удаляем из списка порядка добавления
                unlinkNode(node);

                size--;
                return true;
            }
        }

        return false;
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
            while (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;

        while (current != null) {
            Node<E> next = current.after;
            if (!c.contains(current.element)) {
                remove(current.element);
                modified = true;
            }
            current = next;
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

    private int hash(Object o) {
        int h = o.hashCode();
        return h ^ (h >>> 16); // улучшаем распределение хешей
    }

    private void linkNodeLast(Node<E> node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    private void unlinkNode(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;

        if (before == null) {
            head = after;
        } else {
            before.after = after;
        }

        if (after == null) {
            tail = before;
        } else {
            after.before = before;
        }

        node.before = node.after = null;
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