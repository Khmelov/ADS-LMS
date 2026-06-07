package by.it.group451051.naumchik.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.lang.reflect.Array;

/**
 * Реализация хеш-множества на основе массива с адресацией по хеш-коду
 * и односвязными списками для разрешения коллизий.
 * Не использует стандартные классы коллекций.
 *
 * @param <E> тип элементов множества
 */
public class MyHashSet<E> implements Set<E> {

    // Начальная ёмкость по умолчанию
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    // Коэффициент загрузки
    private static final float LOAD_FACTOR = 0.75f;

    // Массив корзин (списков)
    private Node<E>[] table;

    // Количество элементов в множестве
    private int size;

    /**
     * Узел односвязного списка.
     */
    private static class Node<E> {
        final int hash;   // хеш элемента (для ускорения сравнения)
        final E key;      // сам элемент
        Node<E> next;     // следующий элемент в цепочке

        Node(int hash, E key, Node<E> next) {
            this.hash = hash;
            this.key = key;
            this.next = next;
        }
    }

    // ================== Конструкторы ==================

    /** Создаёт пустое множество с начальной ёмкостью 16. */
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    // ================== Обязательные методы ==================

    /**
     * Возвращает количество элементов в множестве.
     * @return текущий размер
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Удаляет все элементы из множества.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        // Просто создаём новую пустую таблицу
        table = (Node<E>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Проверяет, пусто ли множество.
     * @return true, если множество не содержит элементов
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Добавляет элемент в множество, если он отсутствовал.
     * @param element добавляемый элемент (может быть null)
     * @return true, если элемент был добавлен (т.е. его не было)
     */
    @Override
    public boolean add(E element) {
        int hash = hash(element);
        int index = indexFor(hash, table.length);

        // Проверяем, нет ли уже такого элемента
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && eq(element, node.key)) {
                return false; // уже есть
            }
        }

        // Добавляем новый узел в начало цепочки
        Node<E> newNode = new Node<>(hash, element, table[index]);
        table[index] = newNode;
        size++;

        // Проверяем необходимость расширения
        if (size > table.length * LOAD_FACTOR) {
            resize(table.length * 2);
        }
        return true;
    }

    /**
     * Удаляет указанный элемент из множества, если он присутствует.
     * @param o элемент для удаления (может быть null)
     * @return true, если элемент был найден и удалён
     */
    @Override
    public boolean remove(Object o) {
        int hash = hash(o);
        int index = indexFor(hash, table.length);

        Node<E> prev = null;
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && eq(o, node.key)) {
                if (prev == null) {
                    table[index] = node.next; // удаляем первый
                } else {
                    prev.next = node.next;    // удаляем в середине/конце
                }
                size--;
                return true;
            }
            prev = node;
        }
        return false;
    }

    /**
     * Проверяет, содержится ли указанный элемент в множестве.
     * @param o элемент для поиска (может быть null)
     * @return true, если элемент найден
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

    /**
     * Возвращает строковое представление множества.
     * Формат: [элемент1, элемент2, ...], порядок не гарантирован.
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> node : table) {
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.key == this ? "(this Collection)" : node.key);
                first = false;
                node = node.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // ========== Остальные методы интерфейса Set/Collection ==========

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int bucketIndex = 0;
            private Node<E> currentNode = null;
            private Node<E> nextNode = advance();

            private Node<E> advance() {
                // Если есть продолжение текущей цепочки – берём следующий
                if (currentNode != null && currentNode.next != null) {
                    return currentNode.next;
                }
                // Ищем следующий непустой bucket
                while (bucketIndex < table.length) {
                    Node<E> node = table[bucketIndex++];
                    if (node != null) {
                        return node;
                    }
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public E next() {
                if (nextNode == null) {
                    throw new NoSuchElementException();
                }
                currentNode = nextNode;
                nextNode = advance();
                return currentNode.key;
            }

            @Override
            public void remove() {
                // Не будем поддерживать для простоты
                throw new UnsupportedOperationException("remove не поддерживается в итераторе");
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> node : table) {
            while (node != null) {
                result[i++] = node.key;
                node = node.next;
            }
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
        for (Node<E> node : table) {
            while (node != null) {
                a[i++] = (T) node.key;
                node = node.next;
            }
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
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
        for (Object e : c) {
            if (remove(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < table.length; i++) {
            Node<E> prev = null;
            Node<E> node = table[i];
            while (node != null) {
                if (!c.contains(node.key)) {
                    // Удаляем узел
                    if (prev == null) {
                        table[i] = node.next;
                    } else {
                        prev.next = node.next;
                    }
                    size--;
                    modified = true;
                } else {
                    prev = node;
                }
                node = (prev == null) ? table[i] : prev.next;
            }
        }
        return modified;
    }

    // ================== Вспомогательные методы ==================

    /** Вычисление хеш-кода с обработкой null (0). */
    private static int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    /** Сравнение двух ключей (включая null). */
    private static boolean eq(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    /** Получение индекса корзины по хешу и длине массива. */
    private static int indexFor(int hash, int length) {
        return (hash & 0x7fffffff) % length;
    }

    /** Расширение массива корзин. */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Node<E>[] oldTable = table;
        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
        for (Node<E> node : oldTable) {
            while (node != null) {
                Node<E> next = node.next;
                int index = indexFor(node.hash, newCapacity);
                // Вставляем в начало нового списка
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
    }
}
