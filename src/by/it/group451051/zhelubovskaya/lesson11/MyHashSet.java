package by.it.group451051.zhelubovskaya.lesson11;

import java.util.*;

public class MyHashSet<E> implements Set<E> {

    // Узел для односвязного списка (цепочки)
    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<E>[] buckets;  // массив цепочек
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        buckets = (Node<E>[]) new Node[DEFAULT_CAPACITY];
    }

    // Хеш-функция
    private int getIndex(Object o) {
        if (o == null) return 0;
        return Math.abs(o.hashCode()) % buckets.length;
    }

    // Расширение таблицы при переполнении
    @SuppressWarnings("unchecked")
    private void resize() {
        if (size < buckets.length * LOAD_FACTOR) return;
        
        Node<E>[] oldBuckets = buckets;
        buckets = (Node<E>[]) new Node[oldBuckets.length * 2];
        size = 0;
        
        for (Node<E> node : oldBuckets) {
            while (node != null) {
                add(node.value);
                node = node.next;
            }
        }
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Node<E> bucket : buckets) {
            Node<E> node = bucket;
            while (node != null) {
                if (!first) sb.append(", ");
                sb.append(node.value);
                first = false;
                node = node.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);
        Node<E> current = buckets[index];
        
        // Проверяем, есть ли уже такой элемент
        while (current != null) {
            if (Objects.equals(current.value, e)) {
                return false;
            }
            current = current.next;
        }
        
        // Добавляем в начало цепочки
        buckets[index] = new Node<>(e, buckets[index]);
        size++;
        resize();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        Node<E> current = buckets[index];
        Node<E> prev = null;
        
        while (current != null) {
            if (Objects.equals(current.value, o)) {
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
        int index = getIndex(o);
        Node<E> current = buckets[index];
        while (current != null) {
            if (Objects.equals(current.value, o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // заглушки
    @Override
    public Iterator<E> iterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
    @Override
    public boolean containsAll(Collection<?> c) { return false; }
    @Override
    public boolean addAll(Collection<? extends E> c) { return false; }
    @Override
    public boolean retainAll(Collection<?> c) { return false; }
    @Override
    public boolean removeAll(Collection<?> c) { return false; }
}