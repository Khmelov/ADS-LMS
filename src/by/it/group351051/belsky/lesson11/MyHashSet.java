package by.it.group351051.belsky.lesson11;

import java.util.Iterator;
import java.util.Set;

/*
    Задание на уровень А

    Создайте class MyHashSet<E>, который реализует интерфейс Set<E>
    и работает на основе массива с адресацией по хеш-коду
    и односвязным списком для элементов с коллизиями
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    Метод toString() может выводить элементы в произвольном порядке
    Формат вывода: скобки (квадратные) и разделитель (запятая с пробелом) должны
    быть такими же как в методе toString() обычной коллекции
 */

/**
 * Реализация множества MyHashSet на основе массива с адресацией по хеш-коду
 * и односвязным списком для элементов с коллизиями.
 *
 * Особенности:
 * - Элементы хранятся в массиве, индекс которого определяется хеш-кодом элемента.
 * - Для разрешения коллизий используется односвязный список.
 * - Методы реализованы без использования стандартных коллекций.
 *
 * @param <E> тип элементов множества
 */
public class MyHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16; // Начальная емкость массива
    private Node<E>[] table; // Хэш-таблица для хранения элементов
    private int size; // Количество элементов в множестве

    /**
     * Узел односвязного списка для хранения элементов с коллизиями.
     * Содержит ключ, хеш-код и ссылку на следующий узел.
     *
     * @param <E> тип элемента
     */
    private static class Node<E> {
        final E key; // Значение элемента
        final int hash; // Хеш-код элемента
        Node<E> next; // Ссылка на следующий узел

        Node(E key, int hash, Node<E> next) {
            this.key = key;
            this.hash = hash;
            this.next = next;
        }
    }

    /**
     * Конструктор, инициализирующий множество с начальной емкостью.
     * Создает пустую хэш-таблицу с фиксированным размером.
     */
    public MyHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        // Формируем строку с элементами в произвольном порядке
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> node : table) {
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.key);
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
    public void clear() {
        // Очищает таблицу, создавая новый массив, и сбрасывает размер
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException("Элемент не может быть null");
        int hash = hash(e);
        int index = indexFor(hash);
        Node<E> current = table[index];

        // Проверяем, существует ли уже элемент с таким значением
        while (current != null) {
            if (current.hash == hash && current.key.equals(e)) {
                return false; // Элемент уже существует
            }
            current = current.next;
        }

        // Добавляем новый узел в начало цепочки
        table[index] = new Node<>(e, hash, table[index]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        int hash = hash(o);
        int index = indexFor(hash);
        Node<E> current = table[index];
        Node<E> prev = null;

        // Ищем элемент в цепочке коллизий
        while (current != null) {
            if (current.hash == hash && current.key.equals(o)) {
                // Удаляем элемент, корректируя ссылки
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        int hash = hash(o);
        int index = indexFor(hash);
        Node<E> current = table[index];

        // Проверяем наличие элемента в цепочке
        while (current != null) {
            if (current.hash == hash && current.key.equals(o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Вычисляет хеш-код для элемента.
     * Используется метод XOR для уменьшения коллизий.
     *
     * @param key элемент
     * @return хеш-код
     */
    private int hash(Object key) {
        int h = key.hashCode();
        return h ^ (h >>> 16); // Уменьшение коллизий за счет смешивания битов
    }

    /**
     * Вычисляет индекс в массиве для заданного хеш-кода.
     * Используется побитовая операция для получения индекса в пределах массива.
     *
     * @param hash хеш-код
     * @return индекс в массиве
     */
    private int indexFor(int hash) {
        return (table.length - 1) & hash;
    }

    // Остальные методы интерфейса Set<E> можно оставить нереализованными
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Метод не реализован");
    }
}