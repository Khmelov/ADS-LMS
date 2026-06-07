package by.it.group451051.shiman.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Реализация множества на основе отсортированного массива.
 * Элементы хранятся в порядке возрастания (с поддержкой null).
 *
 * @param <E> тип элементов
 */
public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private Object[] elements;
    private int size;
    private final java.util.Comparator<? super E> comparator;

    // Конструкторы
    public MyTreeSet() {
        this(null);
    }

    public MyTreeSet(java.util.Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Вспомогательный метод сравнения двух элементов (с учётом null и компаратора)
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (a == b) return 0;
        if (a == null) return -1;  // null считается наименьшим
        if (b == null) return 1;
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return ((Comparable<? super E>) a).compareTo(b);
        }
    }

    // Бинарный поиск позиции для элемента (или место вставки)
    // Возвращает индекс, где находится элемент, или -insertionPoint-1, если не найден
    private int binarySearch(E e) {
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            @SuppressWarnings("unchecked")
            E midVal = (E) elements[mid];
            int cmp = compare(e, midVal);
            if (cmp < 0) {
                right = mid - 1;
            } else if (cmp > 0) {
                left = mid + 1;
            } else {
                return mid;
            }
        }
        return -left - 1;
    }

    // Расширение массива
    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    // === Методы интерфейса Set ===

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
        if (o == null) {
            // Проверяем, есть ли null
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return true;
            }
            return false;
        }
        try {
            @SuppressWarnings("unchecked")
            E e = (E) o;
            int index = binarySearch(e);
            return index >= 0;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public boolean add(E e) {
        // Поиск позиции
        int index = binarySearch(e);
        if (index >= 0) {
            return false; // уже есть
        }
        int insertionPoint = -index - 1;
        ensureCapacity();
        // Сдвигаем элементы вправо
        System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);
        elements[insertionPoint] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    System.arraycopy(elements, i + 1, elements, i, size - i - 1);
                    elements[--size] = null;
                    return true;
                }
            }
            return false;
        }
        try {
            @SuppressWarnings("unchecked")
            E e = (E) o;
            int index = binarySearch(e);
            if (index < 0) return false;
            System.arraycopy(elements, index + 1, elements, index, size - index - 1);
            elements[--size] = null;
            return true;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!contains(obj)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object obj : c) {
            if (remove(obj)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (!c.contains(e)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    // === Итератор ===
    private class TreeSetIterator implements Iterator<E> {
        private int cursor = 0;
        private int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastRet = cursor;
            return (E) elements[cursor++];
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            MyTreeSet.this.remove(elements[lastRet]);
            // После удаления элементы сдвинулись, корректируем курсор
            if (lastRet < cursor) cursor--;
            lastRet = -1;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new TreeSetIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    // === equals и hashCode как у Set ===
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;
        Set<?> other = (Set<?>) o;
        if (other.size() != size()) return false;
        return containsAll(other);
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (int i = 0; i < size; i++) {
            E e = (E) elements[i];
            h += (e == null ? 0 : e.hashCode());
        }
        return h;
    }

    // toString в порядке возрастания (массив уже отсортирован)
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i] == this ? "(this Collection)" : elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}