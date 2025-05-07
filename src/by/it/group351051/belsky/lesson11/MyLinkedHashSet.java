package by.it.group351051.belsky.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/*
    Задание на уровень B

    Создайте class MyLinkedHashSet<E>, который реализует интерфейс Set<E>
    и работает на основе массива с адресацией по хеш-коду
    и односвязным списком для элементов с коллизиями
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    Метод toString() должен выводить элементы в порядке их добавления в коллекцию
    Формат вывода: скобки (квадратные) и разделитель (запятая с пробелом) должны
    быть такими же как в методе toString() обычной коллекции
 */

/**
 * Реализация множества MyLinkedHashSet на основе массива с адресацией по хеш-коду
 * и односвязным списком для элементов с коллизиями.
 * Элементы выводятся в порядке их добавления.
 *
 * @param <E> тип элементов множества
 */
public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16; // Начальная емкость массива
    private Node<E>[] table; // Хэш-таблица для хранения элементов
    private Node<E> head; // Указатель на первый добавленный элемент
    private Node<E> tail; // Указатель на последний добавленный элемент
    private int size; // Количество элементов в множестве

    /**
     * Узел односвязного списка для хранения элементов с коллизиями.
     * Также содержит ссылки для поддержания порядка добавления.
     *
     * @param <E> тип элемента
     */
    private static class Node<E> {
        final E key; // Значение элемента
        final int hash; // Хэш-код элемента
        Node<E> next; // Ссылка на следующий узел в цепочке коллизий
        Node<E> prevOrder; // Ссылка на предыдущий узел в порядке добавления
        Node<E> nextOrder; // Ссылка на следующий узел в порядке добавления

        Node(E key, int hash, Node<E> next) {
            this.key = key;
            this.hash = hash;
            this.next = next;
        }
    }

    /**
     * Конструктор, инициализирующий множество с начальной емкостью.
     */
    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
        head = null;
        tail = null;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;

        // Формируем строку с элементами в порядке добавления
        while (current != null) {
            sb.append(current.key);
            if (current.nextOrder != null) {
                sb.append(", ");
            }
            current = current.nextOrder;
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
        // Очищаем таблицу и сбрасываем ссылки на порядок добавления
        table = new Node[DEFAULT_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
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

        // Добавляем новый узел в хэш-таблицу
        Node<E> newNode = new Node<>(e, hash, table[index]);
        table[index] = newNode;

        // Обновляем порядок добавления
        if (tail == null) {
            head = newNode; // Первый элемент
        } else {
            tail.nextOrder = newNode;
            newNode.prevOrder = tail;
        }
        tail = newNode;
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
                // Удаляем элемент из хэш-таблицы
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                // Удаляем элемент из связного списка порядка добавления
                if (current.prevOrder != null) {
                    current.prevOrder.nextOrder = current.nextOrder;
                } else {
                    head = current.nextOrder;
                }
                if (current.nextOrder != null) {
                    current.nextOrder.prevOrder = current.prevOrder;
                } else {
                    tail = current.prevOrder;
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

        // Проверяем наличие элемента в цепочке коллизий
        while (current != null) {
            if (current.hash == hash && current.key.equals(o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем, содержатся ли все элементы из коллекции
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
        // Добавляем все элементы из коллекции
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
        // Удаляем все элементы, которые содержатся в коллекции
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
        Node<E> current = head;

        // Удаляем элементы, которые не содержатся в коллекции
        while (current != null) {
            Node<E> next = current.nextOrder;
            if (!c.contains(current.key)) {
                remove(current.key);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    private int hash(Object key) {
        // Вычисляем хэш-код с использованием метода XOR для уменьшения коллизий
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private int indexFor(int hash) {
        // Вычисляем индекс в массиве на основе хэш-кода
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
}