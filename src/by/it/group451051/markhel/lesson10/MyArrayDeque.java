package by.it.group451051.markhel.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Реализация Deque на основе циклического массива.
 * Поддерживает все обязательные методы для уровня A.
 * Остальные методы интерфейса Deque выбрасывают UnsupportedOperationException.
 *
 * @param <E> тип элементов
 */
public class MyArrayDeque<E> implements Deque<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;
    private int head;
    private int tail;
    private int size;

    /**
     * Конструктор с начальной ёмкостью по умолчанию.
     */
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    /**
     * Увеличивает ёмкость массива в два раза.
     * Копирует элементы в правильном порядке (от head до tail по кругу).
     */
    @SuppressWarnings("unchecked")
    private void grow() {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity * 2;
        E[] newElements = (E[]) new Object[newCapacity];
        if (head <= tail) {
            // нет циклического переноса
            System.arraycopy(elements, head, newElements, 0, size);
        } else {
            // есть перенос: копируем две части
            int firstPart = oldCapacity - head;
            System.arraycopy(elements, head, newElements, 0, firstPart);
            System.arraycopy(elements, 0, newElements, firstPart, tail);
        }
        elements = newElements;
        head = 0;
        tail = size;
    }

    /**
     * Обеспечивает достаточную ёмкость для добавления одного элемента.
     */
    private void ensureCapacity() {
        if (size == elements.length) {
            grow();
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        ensureCapacity();
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        ensureCapacity();
        elements[tail] = e;
        tail = (tail + 1) % elements.length;
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return elements[head];
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int lastIndex = (tail - 1 + elements.length) % elements.length;
        return elements[lastIndex];
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E value = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = (tail - 1 + elements.length) % elements.length;
        E value = elements[tail];
        elements[tail] = null;
        size--;
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        int idx = head;
        for (int i = 0; i < size; i++) {
            sb.append(elements[idx]);
            if (i < size - 1) {
                sb.append(", ");
            }
            idx = (idx + 1) % elements.length;
        }
        sb.append("]");
        return sb.toString();
    }

    // ==================== Остальные методы интерфейса Deque (заглушки) ====================

    @Override
    public boolean offer(E e) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E remove() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E peek() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean offerFirst(E e) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean offerLast(E e) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E removeFirst() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E removeLast() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E peekFirst() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E peekLast() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void push(E e) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E pop() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
