package by.it.group451051.markhel.lesson11;

import java.util.*;

/**
 * Реализация интерфейса Set на основе отсортированного массива.
 * Элементы должны быть сравнимы (Comparable), порядок - естественный.
 * Без использования стандартных коллекций.
 *
 * @param <E> тип элементов множества (должен реализовывать Comparable)
 */
public class MyTreeSet<E> implements Set<E> {

    private Object[] elements;          // отсортированный массив элементов
    private int size;                   // текущее количество элементов
    private static final int DEFAULT_CAPACITY = 10;

    public MyTreeSet() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Вспомогательный метод для приведения к Comparable
    @SuppressWarnings("unchecked")
    private Comparable<? super E> comparable(Object obj) {
        return (Comparable<? super E>) obj;
    }

    // Бинарный поиск индекса элемента (если не найден, возвращает -(insertionPoint + 1))
    private int binarySearch(Object key) {
        int left = 0;
        int right = size - 1;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            @SuppressWarnings("unchecked")
            int cmp = comparable(elements[mid]).compareTo((E) key);
            if (cmp < 0) {
                left = mid + 1;
            } else if (cmp > 0) {
                right = mid - 1;
            } else {
                return mid;
            }
        }
        return -(left + 1);
    }

    // Расширение массива
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length + (elements.length >> 1); // увеличиваем в 1.5 раза
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    // ==================== Обязательные методы ====================

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
        if (o == null) throw new NullPointerException(); // TreeSet не допускает null
        return binarySearch(o) >= 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        int pos = binarySearch(e);
        if (pos >= 0) {
            return false; // уже есть
        }
        int insertPoint = -pos - 1;
        ensureCapacity(size + 1);
        System.arraycopy(elements, insertPoint, elements, insertPoint + 1, size - insertPoint);
        elements[insertPoint] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) throw new NullPointerException();
        int pos = binarySearch(o);
        if (pos < 0) {
            return false;
        }
        int numMoved = size - pos - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, pos + 1, elements, pos, numMoved);
        }
        elements[--size] = null;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[i]);
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
        // Собираем элементы, которые нужно сохранить
        Object[] newElements = new Object[elements.length];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            Object elem = elements[i];
            if (c.contains(elem)) {
                newElements[newSize++] = elem;
            } else {
                modified = true;
            }
        }
        if (modified) {
            elements = newElements;
            size = newSize;
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
