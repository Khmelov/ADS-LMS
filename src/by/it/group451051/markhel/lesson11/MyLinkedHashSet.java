package by.it.group451051.markhel.lesson11;

import java.util.*;

/**
 * Реализация интерфейса Set на основе хеш-таблицы с односвязными списками для разрешения коллизий.
 * Сохраняет порядок добавления элементов (LinkedHashSet).
 * Без использования стандартных коллекций.
 *
 * @param <E> тип элементов множества
 */
public class MyLinkedHashSet<E> implements Set<E> {

    // Узел: хранит ключ, ссылки для цепочки коллизий и для порядка добавления
    private static class Node<E> {
        final E key;
        Node<E> next;          // следующая коллизия
        Node<E> orderPrev;     // предыдущий в порядке добавления
        Node<E> orderNext;     // следующий в порядке добавления

        Node(E key) {
            this.key = key;
        }
    }

    private Node<E>[] table;          // массив корзин
    private int size;                 // количество элементов
    private int threshold;            // порог для расширения
    private final float loadFactor;   // коэффициент загрузки
    private Node<E> head;             // первый элемент в порядке добавления
    private Node<E> tail;             // последний элемент в порядке добавления

    // Конструктор по умолчанию
    public MyLinkedHashSet() {
        this(16, 0.75f);
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0 || loadFactor <= 0) {
            throw new IllegalArgumentException();
        }
        this.loadFactor = loadFactor;
        this.table = new Node[initialCapacity];
        this.threshold = (int) (initialCapacity * loadFactor);
        this.head = this.tail = null;
    }

    // Хеш-функция для null возвращает 0
    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    // Вычисление индекса в массиве
    private int indexFor(int hash, int length) {
        return (hash & 0x7FFFFFFF) % length;
    }

    // Поиск узла по ключу
    private Node<E> findNode(Object key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        Node<E> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    // Добавление узла в конец списка порядка
    private void linkLast(Node<E> node) {
        if (tail == null) {
            head = tail = node;
            node.orderPrev = null;
            node.orderNext = null;
        } else {
            tail.orderNext = node;
            node.orderPrev = tail;
            node.orderNext = null;
            tail = node;
        }
    }

    // Удаление узла из списка порядка
    private void unlinkOrder(Node<E> node) {
        Node<E> prev = node.orderPrev;
        Node<E> next = node.orderNext;
        if (prev == null) {
            head = next;
        } else {
            prev.orderNext = next;
        }
        if (next == null) {
            tail = prev;
        } else {
            next.orderPrev = prev;
        }
        node.orderPrev = node.orderNext = null;
    }

    // Расширение таблицы с сохранением порядка
    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = table.length;
        int newCapacity = oldCapacity * 2;
        Node<E>[] newTable = new Node[newCapacity];
        // Перехеширование в порядке добавления (сохраняем порядок)
        Node<E> current = head;
        while (current != null) {
            int newIndex = indexFor(hash(current.key), newCapacity);
            // Вставляем в начало цепочки коллизий
            current.next = newTable[newIndex];
            newTable[newIndex] = current;
            current = current.orderNext;
        }
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    // ==================== Основные методы Set ====================

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
        return findNode(o) != null;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        Node<E> newNode = new Node<>(e);
        int hash = hash(e);
        int index = indexFor(hash, table.length);
        // Добавление в цепочку коллизий (в начало)
        newNode.next = table[index];
        table[index] = newNode;
        // Добавление в порядок
        linkLast(newNode);
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
                // Удаляем из цепочки коллизий
                if (prev == null) {
                    table[index] = curr.next;
                } else {
                    prev.next = curr.next;
                }
                // Удаляем из порядка
                unlinkOrder(curr);
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
        head = tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;
        while (current != null) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(current.key);
            current = current.orderNext;
        }
        sb.append("]");
        return sb.toString();
    }

    // ==================== Методы работы с коллекциями ====================

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
        // Проходим по порядку добавления, удаляя элементы, не входящие в c
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.orderNext;
            if (!c.contains(current.key)) {
                remove(current.key);
                modified = true;
            }
            current = next;
        }
        return modified;
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
}
