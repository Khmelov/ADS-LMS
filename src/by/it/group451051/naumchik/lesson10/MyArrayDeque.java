package by.it.group451051.naumchik.lesson10;

import java.util.Deque;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {

    // Приватный массив для хранения элементов
    private Object[] elements;
    // Индексы головы и хвоста
    private int head;
    private int tail;
    // Размер дека
    private int size;

    // Начальная ёмкость по умолчанию
    private static final int DEFAULT_CAPACITY = 16;

    // Конструктор по умолчанию
    public MyArrayDeque() {
        elements = new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    // Конструктор с указанием начальной ёмкости
    public MyArrayDeque(int initialCapacity) {
        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1; // ближайшая степень двойки
        }
        elements = new Object[capacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    // Вспомогательный метод для увеличения ёмкости в 2 раза
    private void doubleCapacity() {
        int newCapacity = elements.length << 1;
        Object[] newElements = new Object[newCapacity];
        // Копируем элементы от head до конца массива
        for (int i = head; i < elements.length; i++) {
            newElements[i - head] = elements[i];
        }
        // Копируем элементы от начала до tail
        for (int i = 0; i < tail; i++) {
            newElements[i + (elements.length - head)] = elements[i];
        }
        elements = newElements;
        head = 0;
        tail = size;
    }

    // Вспомогательный метод для проверки пустоты
    private void checkNotEmpty() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        int current = head;
        for (int i = 0; i < size; i++) {
            sb.append(elements[current]);
            if (i < size - 1) sb.append(", ");
            current = (current + 1) % elements.length;
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
        return false;
    }

    // Добавление элемента в конец (по умолчанию)
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public void addFirst(E element) {
        if (element == null) throw new NullPointerException();
        if (size == elements.length) {
            doubleCapacity();
        }
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = element;
        size++;
    }

    @Override
    public void addLast(E element) {
        if (element == null) throw new NullPointerException();
        if (size == elements.length) {
            doubleCapacity();
        }
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        checkNotEmpty();
        return (E) elements[head];
    }

    @Override
    public E getLast() {
        checkNotEmpty();
        int lastIndex = (tail - 1 + elements.length) % elements.length;
        return (E) elements[lastIndex];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        E result = (E) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E result = (E) elements[tail];
        elements[tail] = null;
        size--;
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    // Заглушки для остальных методов интерфейса Deque,
    // чтобы класс компилировался.
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        checkNotEmpty();
        return pollFirst();
    }

    @Override
    public E removeLast() {
        checkNotEmpty();
        return pollLast();
    }

    @Override
    public E peekFirst() {
        return (size == 0) ? null : (E) elements[head];
    }

    @Override
    public E peekLast() {
        return (size == 0) ? null : (E) elements[(tail - 1 + elements.length) % elements.length];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

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
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();
    }
}