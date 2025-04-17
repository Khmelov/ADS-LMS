package by.it.group351051.burdo.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyHashSet<E> implements Set<E> {

    // Константа, определяющая начальный размер хеш-таблицы.
    private static final int DEFAULT_CAPACITY = 16;

    // Коэффициент загрузки для хеш-таблицы. Когда размер набора превышает 75% от текущего объема, таблица будет расширена.
    private static final float LOAD_FACTOR = 0.75f;

    // Хеш-таблица для хранения элементов.
    private Node<E>[] table;

    // Текущий размер набора (количество элементов).
    private int size;

    // Конструктор для создания нового пустого MyHashSet с начальной емкостью.
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY]; // Инициализация таблицы.
        size = 0; // Изначально размер набора 0.
    }

    // Внутренний класс, представляющий узел для хранения элементов и обработки коллизий.
    private static class Node<E> {
        final E element; // Элемент, хранящийся в узле.
        final int hash;  // Хеш элемента.
        Node<E> next;    // Ссылка на следующий узел (для разрешения коллизий).

        // Конструктор для создания нового узла с элементом, хешом и ссылкой на следующий узел.
        Node(E element, int hash, Node<E> next) {
            this.element = element;
            this.hash = hash;
            this.next = next;
        }
    }

    // Переопределение метода toString для удобного вывода набора.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        // Перебор всех элементов хеш-таблицы.
        for (Node<E> node : table) {
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.element); // Добавление элемента в строковое представление.
                first = false;
                node = node.next; // Переход к следующему узлу в цепочке.
            }
        }

        sb.append("]");
        return sb.toString();
    }

    // Возвращает количество элементов в наборе.
    @Override
    public int size() {
        return size;
    }

    // Проверяет, пуст ли набор.
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очищает набор, удаляя все элементы.
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null; // Удаление всех узлов в таблице.
        }
        size = 0; // Сброс размера набора.
    }

    // Проверяет, содержит ли набор указанный элемент.
    @Override
    public boolean contains(Object o) {
        if (o == null) return false; // Если элемент null, возвращаем false.

        int hash = hash(o); // Вычисляем хеш элемента.
        int index = (table.length - 1) & hash; // Индекс в таблице.

        // Перебор всех узлов в цепочке на данном индексе.
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && o.equals(node.element)) {
                return true; // Элемент найден.
            }
        }

        return false; // Элемент не найден.
    }

    // Добавляет новый элемент в набор.
    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException(); // Невозможно добавить null.

        int hash = hash(e); // Вычисление хеша элемента.
        int index = (table.length - 1) & hash; // Индекс для вставки в таблицу.

        // Проверяем, существует ли уже элемент в наборе.
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && e.equals(node.element)) {
                return false; // Элемент уже есть в наборе.
            }
        }

        // Добавляем новый элемент в начало цепочки на соответствующем индексе.
        table[index] = new Node<>(e, hash, table[index]);
        size++;

        // Проверка, нужно ли расширить таблицу.
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }

        return true; // Элемент успешно добавлен.
    }

    // Удаляет элемент из набора.
    @Override
    public boolean remove(Object o) {
        if (o == null) return false; // Если элемент null, возвращаем false.

        int hash = hash(o); // Вычисляем хеш элемента.
        int index = (table.length - 1) & hash; // Индекс для поиска элемента в таблице.
        Node<E> prev = null;

        // Перебор всех узлов на заданном индексе.
        for (Node<E> node = table[index]; node != null; prev = node, node = node.next) {
            if (node.hash == hash && o.equals(node.element)) {
                // Если элемент найден, удаляем его из цепочки.
                if (prev == null) {
                    table[index] = node.next; // Удаляем первый элемент в цепочке.
                } else {
                    prev.next = node.next; // Удаляем элемент, который не первый.
                }
                size--; // Уменьшаем размер набора.
                return true; // Элемент успешно удален.
            }
        }

        return false; // Элемент не найден.
    }

    // Возвращает итератор для набора. В данном случае не поддерживается.
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    // Преобразует набор в массив. Не поддерживается в данной реализации.
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    // Преобразует набор в массив с заданным типом. Не поддерживается в данной реализации.
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    // Проверяет, содержат ли все элементы коллекции c элементы текущего набора. Не поддерживается.
    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    // Добавляет все элементы из коллекции c в текущий набор. Не поддерживается.
    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    // Удаляет все элементы из коллекции c из текущего набора. Не поддерживается.
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    // Сохраняет только те элементы, которые содержатся в коллекции c. Не поддерживается.
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    // Вспомогательный метод для вычисления хеша элемента.
    private int hash(Object o) {
        int h = o.hashCode(); // Получаем хеш-код элемента.
        return h ^ (h >>> 16); // Убираем часть битов, улучшая распределение хешей.
    }

    // Метод для расширения таблицы (увеличение ее размера и перераспределение элементов).
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table; // Сохраняем старую таблицу.
        int oldCapacity = oldTable.length; // Старый размер таблицы.
        int newCapacity = oldCapacity << 1; // Новый размер таблицы (удвоение).

        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity]; // Создаем новую таблицу.
        table = newTable;

        // Перехешируем все элементы из старой таблицы в новую.
        for (int i = 0; i < oldCapacity; i++) {
            Node<E> node = oldTable[i];
            while (node != null) {
                Node<E> next = node.next;
                int index = (newCapacity - 1) & node.hash; // Новый индекс для элемента.
                node.next = newTable[index];
                newTable[index] = node; // Вставка в новую таблицу.
                node = next;
            }
        }
    }
}
