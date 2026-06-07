package by.it.group451051.belousova.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListA<E> implements List<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    // Конструкторы
    public ListA() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public ListA(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be negative");
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }

    // Вспомогательный метод для увеличения ёмкости
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length + (elements.length >> 1); // увеличиваем на 50%
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    // Вспомогательный метод для проверки индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Вспомогательный метод для проверки индекса при вставке
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    // Обязательные методы
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E removed = (E) elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // позволяем GC очистить
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    /////////////////////////////////////////////////////////////////////////
    // Опциональные методы
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        checkIndex(index);
        E oldValue = (E) elements[index];
        elements[index] = element;
        return oldValue;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

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
        if (c == null || c.isEmpty()) {
            return false;
        }
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        if (c == null || c.isEmpty()) {
            return false;
        }
        int numNew = c.size();
        ensureCapacity(size + numNew);
        System.arraycopy(elements, index, elements, index + numNew, size - index);
        int i = index;
        for (E e : c) {
            elements[i++] = e;
        }
        size += numNew;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(elements[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        ListA<E> sublist = new ListA<>(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) elements[i];
            sublist.add(element);
        }
        return sublist;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIterator<E>() {
            private int cursor = index;
            private int lastReturned = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = cursor;
                return (E) elements[cursor++];
            }

            @Override
            public boolean hasPrevious() {
                return cursor > 0;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E previous() {
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                lastReturned = --cursor;
                return (E) elements[cursor];
            }

            @Override
            public int nextIndex() {
                return cursor;
            }

            @Override
            public int previousIndex() {
                return cursor - 1;
            }

            @Override
            public void remove() {
                if (lastReturned < 0) {
                    throw new IllegalStateException();
                }
                ListA.this.remove(lastReturned);
                if (lastReturned < cursor) {
                    cursor--;
                }
                lastReturned = -1;
            }

            @Override
            public void set(E e) {
                if (lastReturned < 0) {
                    throw new IllegalStateException();
                }
                ListA.this.set(lastReturned, e);
            }

            @Override
            public void add(E e) {
                ListA.this.add(cursor++, e);
                lastReturned = -1;
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            private int lastReturned = -1;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = cursor;
                return (E) elements[cursor++];
            }

            @Override
            public void remove() {
                if (lastReturned < 0) {
                    throw new IllegalStateException();
                }
                ListA.this.remove(lastReturned);
                cursor = lastReturned;
                lastReturned = -1;
            }
        };
    }
}