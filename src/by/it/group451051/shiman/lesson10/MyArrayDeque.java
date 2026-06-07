package by.it.group451051.shiman.lesson10;

import java.util.*;

public class MyArrayDeque<E> implements Collection<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int head;
    private int tail;
    private int size;

    public MyArrayDeque() {
        elements = new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    // Увеличение ёмкости
    private void ensureCapacity(int needed) {
        if (elements.length >= needed) return;
        int newCap = elements.length * 2;
        if (newCap < needed) newCap = needed;
        Object[] newArr = new Object[newCap];
        for (int i = 0; i < size; i++) {
            newArr[i] = elements[(head + i) % elements.length];
        }
        elements = newArr;
        head = 0;
        tail = size;
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
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[(head + i) % elements.length], o))
                return true;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        private int cursor = 0;
        private int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastRet = cursor;
            return (E) elements[(head + cursor++) % elements.length];
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            MyArrayDeque.this.removeAt(lastRet);
            if (lastRet < cursor) cursor--;
            lastRet = -1;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = elements[(head + i) % elements.length];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T) elements[(head + i) % elements.length];
        }
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    // Методы Deque
    public void addFirst(E e) {
        ensureCapacity(size + 1);
        head = (head - 1 + elements.length) % elements.length;
        elements[head] = e;
        size++;
    }

    public void addLast(E e) {
        ensureCapacity(size + 1);
        elements[tail] = e;
        tail = (tail + 1) % elements.length;
        size++;
    }

    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return (E) elements[head];
    }

    public E getFirst() {
        return element();
    }

    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return (E) elements[(tail - 1 + elements.length) % elements.length];
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        if (size == 0) return null;
        E result = (E) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        size--;
        return result;
    }

    public E pollLast() {
        if (size == 0) return null;
        tail = (tail - 1 + elements.length) % elements.length;
        E result = (E) elements[tail];
        elements[tail] = null;
        size--;
        return result;
    }

    // Вспомогательный метод для удаления по внутреннему индексу (используется итератором)
    private void removeAt(int idx) {
        // Сдвигаем элементы слева направо, начиная с idx
        for (int i = idx; i < size - 1; i++) {
            int cur = (head + i) % elements.length;
            int next = (head + i + 1) % elements.length;
            elements[cur] = elements[next];
        }
        tail = (tail - 1 + elements.length) % elements.length;
        elements[tail] = null;
        size--;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            int idx = (head + i) % elements.length;
            if (Objects.equals(elements[idx], o)) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[(head + i) % elements.length] = null;
        }
        head = tail = 0;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements[(head + i) % elements.length]);
        }
        sb.append("]");
        return sb.toString();
    }
}