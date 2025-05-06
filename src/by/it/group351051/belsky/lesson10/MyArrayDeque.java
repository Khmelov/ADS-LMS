package by.it.group351051.belsky.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

/*
    Задание на уровень А

    Создайте class MyArrayDeque<E>, который реализует интерфейс Deque<E>
    и работает на основе приватного массива типа E[]
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
 */

/**
 * Класс MyArrayDeque - реализация двусторонней очереди на основе массива.
 *
 * Особенности:
 * - Используется кольцевой буфер для эффективного использования памяти.
 * - Индексы head и tail указывают на начало и конец очереди соответственно.
 * - При переполнении массива его размер увеличивается в два раза.
 *
 * @param <E> тип элементов очереди
 */
public class MyArrayDeque<E> implements Deque<E> {

    // Константа для начальной емкости массива
    private static final int DEFAULT_CAPACITY = 10;

    // Массив для хранения элементов
    private E[] elements;

    // Текущий размер очереди
    private int size;

    // Индекс начала очереди (head) указывает на первый элемент.
    // При удалении элемента head сдвигается вперед с учетом кольцевой структуры.
    private int head;

    // Индекс конца очереди (tail) указывает на следующую свободную позицию.
    // При добавлении элемента tail сдвигается вперед с учетом кольцевой структуры.
    private int tail;

    /**
     * Конструктор по умолчанию, инициализирует массив с начальной емкостью.
     */
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
        head = 0;
        tail = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление очереди.
     * Элементы выводятся в порядке их расположения в очереди.
     *
     * @return строка, представляющая элементы очереди
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Возвращает размер очереди.
     *
     * @return количество элементов в очереди
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Добавляет элемент в конец очереди.
     *
     * @param element элемент для добавления
     * @return true, если элемент успешно добавлен
     */
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    /**
     * Добавляет элемент в начало очереди.
     * Индекс head уменьшается, а при достижении начала массива
     * "перематывается" на конец массива.
     *
     * @param element элемент для добавления
     */
    @Override
    public void addFirst(E element) {
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    /**
     * Добавляет элемент в конец очереди.
     * Индекс tail увеличивается, а при достижении конца массива
     * "перематывается" на начало массива.
     *
     * @param element элемент для добавления
     */
    @Override
    public void addLast(E element) {
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    /**
     * Возвращает первый элемент очереди без удаления.
     *
     * @return первый элемент очереди
     */
    @Override
    public E element() {
        return getFirst();
    }

    /**
     * Возвращает первый элемент очереди без удаления.
     * Если очередь пуста, выбрасывается исключение.
     *
     * @return первый элемент очереди
     */
    @Override
    public E getFirst() {
        if (size == 0) {
            throw new IllegalStateException("Очередь пуста");
        }
        return elements[head];
    }

    /**
     * Возвращает последний элемент очереди без удаления.
     * Если очередь пуста, выбрасывается исключение.
     *
     * @return последний элемент очереди
     */
    @Override
    public E getLast() {
        if (size == 0) {
            throw new IllegalStateException("Очередь пуста");
        }
        return elements[(tail - 1 + elements.length) % elements.length];
    }

    /**
     * Удаляет и возвращает первый элемент очереди.
     *
     * @return первый элемент очереди или null, если очередь пуста
     */
    @Override
    public E poll() {
        return pollFirst();
    }

    /**
     * Удаляет и возвращает первый элемент очереди.
     * Индекс head сдвигается вперед с учетом кольцевой структуры.
     *
     * @return первый элемент очереди или null, если очередь пуста
     */
    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E element = elements[head];
        elements[head] = null; // Удаляем ссылку для предотвращения утечки памяти
        head = (head + 1) % elements.length;
        size--;
        return element;
    }

    /**
     * Удаляет и возвращает последний элемент очереди.
     * Индекс tail сдвигается назад с учетом кольцевой структуры.
     *
     * @return последний элемент очереди или null, если очередь пуста
     */
    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = (tail - 1 + elements.length) % elements.length;
        E element = elements[tail];
        elements[tail] = null; // Удаляем ссылку для предотвращения утечки памяти
        size--;
        return element;
    }

    /**
     * Увеличивает емкость массива при необходимости.
     * Новый массив создается в два раза больше текущего.
     * Элементы копируются с учетом кольцевой структуры.
     */
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newElements = (E[]) new Object[elements.length * 2];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[(head + i) % elements.length];
            }
            elements = newElements;
            head = 0;
            tail = size;
        }
    }

    // Остальные методы интерфейса Deque<E> можно оставить нереализованными
    @Override
    public boolean offerFirst(E e) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean offerLast(E e) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E removeFirst() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E removeLast() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E peekFirst() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E peekLast() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean offer(E e) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E remove() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E peek() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public void push(E e) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E pop() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean isEmpty() {
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
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Метод не реализован");
    }
}