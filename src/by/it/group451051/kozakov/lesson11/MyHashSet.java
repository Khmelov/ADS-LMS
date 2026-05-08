package by.it.group451051.kozakov.lesson11;

import java.util.Set;

public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // Узел односвязного списка
    private static class Node<E> {
        final int hash;
        final E value;
        Node<E> next;

        Node(int hash, E value, Node<E> next) {
            this.hash = hash;
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private float loadFactor;

    // Конструктор по умолчанию
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        this.table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.size = 0;
    }

    // Метод для вычисления хеша
    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    // Метод для вычисления индекса корзины
    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    // Метод для изменения размера массива
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[newCapacity];
        for (Node<E> node : oldTable) {
            while (node != null) {
                Node<E> next = node.next;
                int index = indexFor(node.hash, newCapacity);
                node.next = table[index];
                table[index] = node;
                node = next;
            }
        }
    }

    // Метод для добавления элемента
    @Override
    public boolean add(E e) {
        int hash = hash(e);
        int index = indexFor(hash, table.length);
        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && (node.value == e || (e != null && e.equals(node.value)))) {
                return false;
            }
            node = node.next;
        }
        table[index] = new Node<>(hash, e, table[index]);
        size++;
        if (size > table.length * loadFactor) {
            resize(2 * table.length);
        }
        return true;
    }

    // Метод для проверки наличия элемента
    @Override
    public boolean contains(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);
        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && (node.value == o || (o != null && o.equals(node.value)))) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    // Метод для удаления элемента
    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);
        Node<E> prev = null;
        Node<E> node = table[index];
        while (node != null) {
            if (node.hash == hash && (node.value == o || (o != null && o.equals(node.value)))) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    // Метод для получения размера множества
    @Override
    public int size() {
        return size;
    }

    // Метод для проверки пустоты множества
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Метод для очистки множества
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    // Метод для строкового представления множества
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> node : table) {
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.value);
                first = false;
                node = node.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Необязательные методы интерфейса Set<E>
    @Override
    public java.util.Iterator<E> iterator() {
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
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}