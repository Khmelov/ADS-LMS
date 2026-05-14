package by.it.group451051.sinenko.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {
    // каждый узел хранит данные и ссылку на следующий узел
    private static class Node<E> {
        E item;        // данные
        Node<E> next;  // ссылка на следующий узел
        
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    private Node<E>[] buckets;   // массив корзин
    private int size;            // количество элементов
    private static final int DEFAULT_CAPACITY = 16;  // начальный размер массива
    private static final double LOAD_FACTOR = 0.75;   // коэффициент загрузки

    // конструктор
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        buckets = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }
    
    // индекс корзины для элемента
    private int getBucketIndex(Object o) {
        if (o == null) return 0; 
        return Math.abs(o.hashCode()) % buckets.length; // берёт модуль от хеш-кода чтобы получить индекс в массиве
    }
    
    // проверка нужно ли расширять массив
    private void checkAndGrow() {
        if ((double) size / buckets.length > LOAD_FACTOR) {
            grow();
        }
    }
    
    // расширение массива корзин в 2 раза
    @SuppressWarnings("unchecked")
    private void grow() {
        Node<E>[] oldBuckets = buckets;
        buckets = (Node<E>[]) new Node[oldBuckets.length * 2];
        size = 0;
        
        for (Node<E> node : oldBuckets) {
            Node<E> current = node;
            while (current != null) {
                add(current.item);
                current = current.next;
            }
        }
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
        // очистка всех корзин
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E e) {
        // проверка нет ли уже такого элемента
        if (contains(e)) {
            return false;  // элемент уже есть
        }
        
        // проверка нужно ли расширение
        checkAndGrow();

        int index = getBucketIndex(e);
        buckets[index] = new Node<>(e, buckets[index]);
        size++;
        
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getBucketIndex(o);
        Node<E> current = buckets[index];
        Node<E> prev = null;
        
        while (current != null) {
            if (Objects.equals(current.item, o)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        
        return false; 
    }

    @Override
    public boolean contains(Object o) {
        int index = getBucketIndex(o);
        Node<E> current = buckets[index];
        
        while (current != null) {
            if (Objects.equals(current.item, o)) {
                return true;
            }
            current = current.next;
        }
        
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        
        for (Node<E> bucket : buckets) {
            Node<E> current = bucket;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.item);
                first = false;
                current = current.next;
            }
        }
        
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> bucket : buckets) {
            Node<E> current = bucket;
            while (current != null) {
                result[i++] = current.item;
                current = current.next;
            }
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(toArray(), size, a.getClass());
        }
        System.arraycopy(toArray(), 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
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
        for (Object item : c) {
            if (remove(item)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < buckets.length; i++) {
            Node<E> current = buckets[i];
            Node<E> newBucket = null;
            Node<E> newBucketTail = null;
            
            while (current != null) {
                if (c.contains(current.item)) {
                    Node<E> newNode = new Node<>(current.item, null);
                    if (newBucket == null) {
                        newBucket = newNode;
                        newBucketTail = newNode;
                    } else {
                        newBucketTail.next = newNode;
                        newBucketTail = newNode;
                    }
                } else {
                    changed = true;
                }
                current = current.next;
            }
            buckets[i] = newBucket;
        }
        
        if (changed) {
            size = 0;
            for (Node<E> bucket : buckets) {
                Node<E> current = bucket;
                while (current != null) {
                    size++;
                    current = current.next;
                }
            }
        }
        
        return changed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }
}
