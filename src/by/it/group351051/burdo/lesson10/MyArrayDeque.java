package by.it.group351051.burdo.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Deque<E> {
    // Константа, определяющая начальную емкость для deque
    private static final int DEFAULT_CAPACITY = 10;

    // Массив для хранения элементов deque
    private E[] elements;
    // Индекс первого элемента в deque
    private int head;
    // Индекс последнего элемента в deque
    private int tail;
    // Текущее количество элементов в deque
    private int size;

    // Конструктор по умолчанию, инициализирует deque с начальной емкостью
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY]; // Инициализация массива с дефолтной емкостью
        head = 0; // Изначально head указывает на начало
        tail = 0; // Изначально tail указывает на конец
        size = 0; // Множество элементов пустое
    }

    // Метод для преобразования deque в строку
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]"; // Если deque пуст, возвращаем пустой список
        }

        StringBuilder sb = new StringBuilder("["); // Строим строку с элементами deque
        for (int i = 0; i < size; i++) {
            sb.append(elements[(head + i) % elements.length]); // Добавляем каждый элемент с учетом кольцевого массива
            if (i < size - 1) {
                sb.append(", "); // Разделяем элементы запятой
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Метод для получения текущего размера deque
    @Override
    public int size() {
        return size; // Возвращаем текущее количество элементов
    }

    // Метод для получения итератора по элементам deque (не реализован)
    @Override
    public Iterator<E> iterator() {
        return null; // Возвращаем null, так как метод не реализован
    }

    // Метод для получения итератора по элементам в обратном порядке (не реализован)
    @Override
    public Iterator<E> descendingIterator() {
        return null; // Возвращаем null, так как метод не реализован
    }

    // Метод для добавления элемента в конец deque
    @Override
    public boolean add(E element) {
        addLast(element); // Используем метод добавления в конец
        return true;
    }

    // Метод для добавления элемента в начало deque
    @Override
    public void addFirst(E element) {
        if (element == null) {
            throw new NullPointerException(); // Исключение для null элементов
        }
        ensureCapacity(); // Проверяем и при необходимости увеличиваем емкость
        head = (head - 1 + elements.length) % elements.length; // Перемещаем head в начало массива
        elements[head] = element; // Добавляем элемент в начало
        size++; // Увеличиваем размер
    }

    // Метод для добавления элемента в конец deque
    @Override
    public void addLast(E element) {
        if (element == null) {
            throw new NullPointerException(); // Исключение для null элементов
        }
        ensureCapacity(); // Проверяем и при необходимости увеличиваем емкость
        elements[tail] = element; // Добавляем элемент в конец
        tail = (tail + 1) % elements.length; // Перемещаем tail на следующий индекс
        size++; // Увеличиваем размер
    }

    // Метод для получения первого элемента deque
    @Override
    public E element() {
        return getFirst(); // Возвращаем первый элемент с помощью метода getFirst
    }

    // Метод для получения первого элемента deque
    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException(); // Исключение, если deque пуст
        }
        return elements[head]; // Возвращаем элемент на позиции head
    }

    // Метод для получения последнего элемента deque
    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException(); // Исключение, если deque пуст
        }
        return elements[(tail - 1 + elements.length) % elements.length]; // Возвращаем последний элемент с учетом кольцевого массива
    }

    // Метод для извлечения и удаления первого элемента deque
    @Override
    public E poll() {
        return pollFirst(); // Используем метод pollFirst для извлечения первого элемента
    }

    // Метод для извлечения и удаления первого элемента deque
    @Override
    public E pollFirst() {
        if (isEmpty()) {
            return null; // Если deque пуст, возвращаем null
        }
        E element = elements[head]; // Сохраняем первый элемент
        elements[head] = null; // Убираем ссылку на элемент
        head = (head + 1) % elements.length; // Перемещаем head на следующий элемент
        size--; // Уменьшаем размер
        return element; // Возвращаем удаленный элемент
    }

    // Метод для извлечения и удаления последнего элемента deque
    @Override
    public E pollLast() {
        if (isEmpty()) {
            return null; // Если deque пуст, возвращаем null
        }
        tail = (tail - 1 + elements.length) % elements.length; // Перемещаем tail на предыдущую позицию
        E element = elements[tail]; // Сохраняем последний элемент
        elements[tail] = null; // Убираем ссылку на элемент
        size--; // Уменьшаем размер
        return element; // Возвращаем удаленный элемент
    }

    // Метод для проверки, пуст ли deque
    @Override
    public boolean isEmpty() {
        return size == 0; // Если размер 0, то deque пуст
    }

    // Метод для увеличения емкости массива, если он полностью заполнен
    private void ensureCapacity() {
        if (size == elements.length) { // Если массив заполнен
            @SuppressWarnings("unchecked")
            E[] newElements = (E[]) new Object[elements.length * 2]; // Создаем новый массив с удвоенной емкостью

            for (int i = 0; i < size; i++) {
                newElements[i] = elements[(head + i) % elements.length]; // Копируем элементы в новый массив с учетом кольцевой структуры
            }

            elements = newElements; // Переназначаем массив
            head = 0; // Устанавливаем head в начало
            tail = size; // Устанавливаем tail в конец
        }
    }

    // Все методы ниже выбрасывают UnsupportedOperationException, так как они не реализованы

    @Override public boolean offer(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerFirst(E e) { throw new UnsupportedOperationException(); }
    @Override public boolean offerLast(E e) { throw new UnsupportedOperationException(); }
    @Override public E remove() { throw new UnsupportedOperationException(); }
    @Override public E removeFirst() { throw new UnsupportedOperationException(); }
    @Override public E removeLast() { throw new UnsupportedOperationException(); }
    @Override public E peek() { throw new UnsupportedOperationException(); }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false; // Этот метод не реализован
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false; // Этот метод не реализован
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false; // Этот метод не реализован
    }

    @Override public E peekFirst() { throw new UnsupportedOperationException(); }
    @Override public E peekLast() { throw new UnsupportedOperationException(); }
    @Override public boolean remove(Object o) { throw new UnsupportedOperationException(); }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false; // Этот метод не реализован
    }

    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public void push(E e) { throw new UnsupportedOperationException(); }
    @Override public E pop() { throw new UnsupportedOperationException(); }
    @Override public boolean removeFirstOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public boolean removeLastOccurrence(Object o) { throw new UnsupportedOperationException(); }
    @Override public void clear() { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
}
