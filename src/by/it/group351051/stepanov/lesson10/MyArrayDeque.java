package by.it.group351051.stepanov.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Collection<E> {
    private E[] array;
    private int size;
    private int head;
    private int tail;

    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        array = (E[]) new Object[16];
        size = 0;
        head = 0;
        tail = 0;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        int index = head;
        for (int i = 0; i < size; i++) {
            sb.append(array[index]);
            if (i < size - 1) {
                sb.append(", ");
            }
            index = (index + 1) % array.length;
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
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (array[(head + i) % array.length] == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(array[(head + i) % array.length])) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = head;
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E element = array[index];
                index = (index + 1) % array.length;
                count++;
                return element;
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int index = head;
        for (int i = 0; i < size; i++) {
            result[i] = array[index];
            index = (index + 1) % array.length;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int index = head;
        for (int i = 0; i < size; i++) {
            a[i] = (T) array[index];
            index = (index + 1) % array.length;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (o.equals(iterator.next())) {
                iterator.remove();
                return true;
            }
        }
        return false;
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
        for (E e : c) {
            add(e);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (c.contains(iterator.next())) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            if (!c.contains(iterator.next())) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }
        size = 0;
        head = 0;
        tail = 0;
    }

    public void addFirst(E element) {
        if (size == array.length) {
            resize();
        }
        head = (head - 1 + array.length) % array.length;
        array[head] = element;
        size++;
    }

    public void addLast(E element) {
        if (size == array.length) {
            resize();
        }
        array[tail] = element;
        tail = (tail + 1) % array.length;
        size++;
    }

    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return array[head];
    }

    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return array[head];
    }

    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return array[(tail - 1 + array.length) % array.length];
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E element = array[head];
        array[head] = null;
        head = (head + 1) % array.length;
        size--;
        return element;
    }

    public E pollLast() {
        if (size == 0) {
            return null;
        }
        tail = (tail - 1 + array.length) % array.length;
        E element = array[tail];
        array[tail] = null;
        size--;
        return element;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        E[] newArray = (E[]) new Object[array.length * 2];
        int index = head;
        for (int i = 0; i < size; i++) {
            newArray[i] = array[index];
            index = (index + 1) % array.length;
        }
        array = newArray;
        head = 0;
        tail = size;
    }
}
