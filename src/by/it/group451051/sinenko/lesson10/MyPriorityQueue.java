package by.it.group451051.sinenko.lesson10;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import java.util.Iterator;

public class MyPriorityQueue<E> implements Queue<E> {

    private Object[] heap;   // массив для хранения элементов
    private int size;        // количество элементов
    private static final int DEFAULT_CAPACITY = 11; // начальная ёмкость
    
    // конструктор
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    // возвращает индекс родителя
    private int parent(int index) {
        return (index - 1) / 2;
    }

    // индекс левого ребёнка
    private int leftChild(int index) {
        return 2 * index + 1;
    }

    // правого ребёнка
    private int rightChild(int index) {
        return 2 * index + 2;
    }

    @SuppressWarnings("unchecked")    // подъём элемента вверх при добавлении
    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = parent(index);
            E current = (E) heap[index];
            E parent = (E) heap[parentIndex];
       
            if (((Comparable<E>) current).compareTo(parent) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    // просеивание вниз 
    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        while (leftChild(index) < size) {
            int left = leftChild(index);
            int right = rightChild(index);
            int smallest = left;
            
            // находит наименьшего ребёнка
            if (right < size && 
                ((Comparable<E>) heap[right]).compareTo((E) heap[left]) < 0) {
                smallest = right;
            }
        
            if (((Comparable<E>) heap[index]).compareTo((E) heap[smallest]) <= 0) {
                break;
            }
            
            // Меняем местами с наименьшим ребёнком
            swap(index, smallest);
            index = smallest;
        }
    }

    // меняет местами два элемента в куче
    private void swap(int i, int j) {
        Object temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // расширение массива при необходимости
    @SuppressWarnings("unchecked")
    private void grow() {
        int newCapacity = heap.length * 2;
        heap = Arrays.copyOf(heap, newCapacity);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(heap[i]);
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
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        return offer(e);
    }

    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        
        // проверка нужно ли расширить массив
        if (size >= heap.length) {
            grow();
        }
        
        heap[size] = e;
        siftUp(size);
        size++;
        
        return true;
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        return (E) heap[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return (E) heap[0];
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        
        E result = (E) heap[0];
        
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        
        if (size > 0) {
            siftDown(0);
        }
        
        return result;
    }

    @Override
    public E remove() {
        E result = poll();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(heap[i], o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E item : c) {
            if (add(item)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Object[] temp = new Object[heap.length];
        int newSize = 0;
        
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            } else {
                changed = true;
            }
        }
        
        if (changed) {
            heap = temp;
            size = newSize;
            for (int i = size / 2; i >= 0; i--) {
                siftDown(i);
            }
        }
        
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Object[] temp = new Object[heap.length];
        int newSize = 0;
        
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                temp[newSize++] = heap[i];
            } else {
                changed = true;
            }
        }
        
        if (changed) {
            heap = temp;
            size = newSize;
            for (int i = size / 2; i >= 0; i--) {
                siftDown(i);
            }
        }
        
        return changed;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(heap, size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(heap, size, a.getClass());
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        // заглушка возвращает пустой итератор
        return new Iterator<E>() {
            private int current = 0;
            
            @Override
            public boolean hasNext() {
                return current < size;
            }
            
            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (E) heap[current++];
            }
        };
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(heap[i], o)) {
                heap[i] = heap[size - 1];
                heap[size - 1] = null;
                size--;
                if (i < size) {
                    siftDown(i);
                    siftUp(i);
                }
                return true;
            }
        }
        return false;
    }
}
