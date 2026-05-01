package by.it.group451051.markhel.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Реализация очереди с приоритетом на основе min-кучи.
 * Элементы должны быть сравнимы (реализовывать Comparable).
 *
 * @param <E> тип элементов
 */
public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 11;
    private Object[] heap;
    private int size;

    public MyPriorityQueue() {
        heap = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    // ==================== Вспомогательные методы для кучи ====================

    @SuppressWarnings("unchecked")
    private E elementAt(int index) {
        return (E) heap[index];
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            int newCapacity = heap.length * 2 + 1;
            Object[] newHeap = new Object[newCapacity];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    /**
     * Поднимает элемент вверх по куче.
     */
    @SuppressWarnings("unchecked")
    private void siftUp(int k, E item) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E parentVal = (E) heap[parent];
            if (((Comparable<? super E>) item).compareTo(parentVal) >= 0) {
                break;
            }
            heap[k] = parentVal;
            k = parent;
        }
        heap[k] = item;
    }

    /**
     * Опускает элемент вниз по куче.
     */
    @SuppressWarnings("unchecked")
    private void siftDown(int k, E item) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            E childVal = (E) heap[child];
            int right = child + 1;
            if (right < size && ((Comparable<? super E>) childVal).compareTo((E) heap[right]) > 0) {
                childVal = (E) heap[child = right];
            }
            if (((Comparable<? super E>) item).compareTo(childVal) <= 0) {
                break;
            }
            heap[k] = childVal;
            k = child;
        }
        heap[k] = item;
    }

    /**
     * Перестраивает кучу из массива (heapify).
     */
    @SuppressWarnings("unchecked")
    private void heapify() {
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, elementAt(i));
        }
    }

    // ==================== Методы интерфейса Queue ====================

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        ensureCapacity();
        heap[size] = e;
        siftUp(size, e);
        size++;
        return true;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        E item = poll();
        if (item == null) {
            throw new NoSuchElementException();
        }
        return item;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = elementAt(0);
        size--;
        E last = elementAt(size);
        heap[size] = null;
        if (size > 0) {
            siftDown(0, last);
        }
        return result;
    }

    @Override
    public E element() {
        E item = peek();
        if (item == null) {
            throw new NoSuchElementException();
        }
        return item;
    }

    @Override
    public E peek() {
        return (size == 0) ? null : elementAt(0);
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
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (o.equals(heap[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
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
        if (c == null || c.isEmpty()) {
            return false;
        }
        Object[] newHeap = new Object[heap.length];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            Object elem = heap[i];
            if (!c.contains(elem)) {
                newHeap[newSize++] = elem;
            }
        }
        if (newSize == size) {
            return false;
        }
        heap = newHeap;
        size = newSize;
        heapify();
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            return false;
        }
        Object[] newHeap = new Object[heap.length];
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            Object elem = heap[i];
            if (c.contains(elem)) {
                newHeap[newSize++] = elem;
            }
        }
        if (newSize == size) {
            return false;
        }
        heap = newHeap;
        size = newSize;
        heapify();
        return true;
    }

    // ==================== Методы, не поддерживаемые очередью ====================

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not supported");
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
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    // ==================== Метод toString ====================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
