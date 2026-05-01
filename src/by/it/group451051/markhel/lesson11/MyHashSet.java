package by.it.group451051.markhel.lesson11;

import java.util.*;

/**
 * Реализация интерфейса Set на основе хеш-таблицы с односвязными списками для разрешения коллизий.
 * Без использования стандартных коллекций.
 *
 * @param <E> тип элементов множества
 */
public class MyHashSet<E> implements Set<E> {

    // Внутренний узел односвязного списка
    private static class Node<E> {
        final E key;
        Node<E> next;

        Node(E key, Node<E> next) {
            this.key = key;
            this.next = next;
        }
    }

    private Node<E>[] table;          // массив корзин
    private int size;                 // количество элементов
    private int threshold;            // порог для расширения
    private final float loadFactor;   // коэффициент загрузки

    // Конструктор по умолчанию: начальная ёмкость 16, коэффициент загрузки 0.75
    public MyHashSet() {
        this(16, 0.75f);
    }

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0 || loadFactor <= 0) {
            throw new IllegalArgumentException();
        }
        this.loadFactor = loadFactor;
        this.table = new Node[initialCapacity];
        this.threshold = (int) (initialCapacity * loadFactor);
    }

    // Хеш-функция для null возвращает 0
    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    // Вычисление индекса в массиве
    private int indexFor(int hash, int length) {
        return (hash & 0x7FFFFFFF) % length;
    }

    // Расширение таблицы при превышении порога
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity * 2;
        Node<E>[] newTable = new Node[newCapacity];
        for (Node<E> node : oldTable) {
            while (node != null) {
                Node<E> next = node.next;
                int newIndex = indexFor(hash(node.key), newCapacity);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    // ==================== Обязательные методы ====================

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
        int hash = hash(o);
        int index = indexFor(hash, table.length);
        Node<E> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        int hash = hash(e);
        int index = indexFor(hash, table.length);
        Node<E> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, e)) {
                return false; // элемент уже существует
            }
            current = current.next;
        }
        // вставка в начало списка
        table[index] = new Node<>(e, table[index]);
        size++;
        if (size >= threshold) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);
        Node<E> prev = null;
        Node<E> curr = table[index];
        while (curr != null) {
            if (Objects.equals(curr.key, o)) {
                if (prev == null) {
                    table[index] = curr.next;
                } else {
                    prev.next = curr.next;
                }
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> node : table) {
            while (node != null) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(node.key);
                node = node.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // ==================== Заглушки для остальных методов Set ====================

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
}
