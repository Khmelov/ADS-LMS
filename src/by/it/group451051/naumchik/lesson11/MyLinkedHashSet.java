package by.it.group451051.naumchik.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.lang.reflect.Array;

/**
 * Реализация Set<E> на основе хеш-таблицы с сохранением порядка вставки.
 * Коллизии разрешаются односвязным списком (цепочки в корзинах).
 * Порядок обхода (toString, итератор) соответствует порядку добавления элементов.
 * Не использует стандартные классы коллекций (кроме интерфейсов).
 *
 * @param <E> тип элементов множества
 */
public class MyLinkedHashSet<E> implements Set<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    /** Узел элемента: участвует в цепочке корзины (next) и в списке порядка (after/before). */
    private static class Node<E> {
        final int hash;
        final E key;
        Node<E> next;      // следующая запись в цепочке корзины
        Node<E> after;     // следующая запись в порядке добавления
        Node<E> before;    // предыдущая запись в порядке добавления

        Node(int hash, E key, Node<E> next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
        }
    }

    private Node<E>[] table;   // массив корзин
    private int size;          // количество элементов

    private Node<E> head;      // голова двусвязного списка порядка вставки
    private Node<E> tail;      // хвост двусвязного списка порядка вставки

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    // ================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ==================

    /**
     * Возвращает строку вида [элемент1, элемент2, ...] в порядке добавления.
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        Node<E> node = head;
        while (node != null) {
            if (node != head) {
                sb.append(", ");
            }
            // Специальный случай: если элемент — сама коллекция, избегаем рекурсии
            sb.append(node.key == this ? "(this Collection)" : node.key);
            node = node.after;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        table = (Node<E>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Добавляет элемент в множество, если он отсутствует.
     * При попытке добавить уже существующий элемент порядок не меняется.
     * @param element добавляемый элемент (допускается null)
     * @return true, если элемент был добавлен (отсутствовал)
     */
    @Override
    public boolean add(E element) {
        int hash = hash(element);
        int index = indexFor(hash, table.length);

        // Проверяем, нет ли уже такого элемента
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && eq(element, node.key)) {
                return false; // уже существует, не меняем порядок
            }
        }

        // Создаём узел и вставляем в начало цепочки корзины
        Node<E> newNode = new Node<>(hash, element, table[index]);
        table[index] = newNode;
        linkLast(newNode);   // добавляем в конец двусвязного списка порядка
        size++;

        // Расширение при превышении порога загрузки
        if (size > table.length * LOAD_FACTOR) {
            resize(table.length * 2);
        }
        return true;
    }

    /**
     * Удаляет элемент из множества.
     * @param o элемент для удаления
     * @return true, если элемент был найден и удалён
     */
    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> prev = null;
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && eq(o, node.key)) {
                // Удаляем из цепочки корзины
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                // Удаляем из списка порядка
                unlink(node);
                size--;
                return true;
            }
            prev = node;
        }
        return false;
    }

    /**
     * Проверяет, содержится ли элемент в множестве.
     * @param o искомый элемент (может быть null)
     * @return true, если элемент присутствует
     */
    @Override
    public boolean contains(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && eq(o, node.key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        // Обходим элементы в порядке вставки, сохраняя следующий узел
        Node<E> node = head;
        while (node != null) {
            Node<E> nextNode = node.after;
            if (c.contains(node.key)) {
                remove(node.key); // это изменит связи, но nextNode уже сохранён
                changed = true;
            }
            node = nextNode;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> node = head;
        while (node != null) {
            Node<E> nextNode = node.after;
            if (!c.contains(node.key)) {
                remove(node.key);
                changed = true;
            }
            node = nextNode;
        }
        return changed;
    }

    // ========== МЕТОДЫ ИНТЕРФЕЙСА SET / COLLECTION (для компиляции) ==========

    /**
     * Итератор, следующий в порядке вставки элементов.
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;
            private Node<E> lastReturned;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                lastReturned = current;
                current = current.after;
                return lastReturned.key;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove в итераторе не поддерживается");
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> node = head; node != null; node = node.after) {
            result[i++] = node.key;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            T[] newArray = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
            a = newArray;
        }
        int i = 0;
        for (Node<E> node = head; node != null; node = node.after) {
            a[i++] = (T) node.key;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    // ================== ПРИВАТНЫЕ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==================

    /** Хеш объекта (0 для null). */
    private static int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    /** Сравнение двух ключей с учётом null. */
    private static boolean eq(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    /** Индекс корзины для хеша. */
    private static int indexFor(int hash, int length) {
        return (hash & 0x7fffffff) % length;
    }

    /** Присоединяет узел в конец списка порядка вставки. */
    private void linkLast(Node<E> node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
    }

    /** Удаляет узел из списка порядка вставки. */
    private void unlink(Node<E> node) {
        if (node.before == null) {
            head = node.after;
        } else {
            node.before.after = node.after;
        }
        if (node.after == null) {
            tail = node.before;
        } else {
            node.after.before = node.before;
        }
        node.after = null;
        node.before = null;
    }

    /** Увеличение размера хеш-таблицы и перераспределение элементов. */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Node<E>[] oldTable = table;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
        // Переносим узлы, порядок остаётся прежним (head/tail не меняются)
        for (Node<E> node = head; node != null; node = node.after) {
            int index = indexFor(node.hash, newCapacity);
            // Вставляем в начало цепочки новой корзины
            node.next = newTable[index];
            newTable[index] = node;
        }
        table = newTable;
    }
}