package by.it.group451051.zhelubovskaya.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;
        Node<E> linkPrev;
        Node<E> linkNext;
        
        Node(E value, Node<E> next, Node<E> linkPrev, Node<E> linkNext) {
            this.value = value;
            this.next = next;
            this.linkPrev = linkPrev;
            this.linkNext = linkNext;
        }
    }

    private Node<E>[] buckets;
    private int size = 0;
    private Node<E> head;
    private Node<E> tail;
    
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        buckets = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        head = tail = null;
    }

    private int getIndex(Object o) {
        if (o == null) return 0;
        return Math.abs(o.hashCode()) % buckets.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        if (size < buckets.length * LOAD_FACTOR) return;
        
        Node<E>[] oldBuckets = buckets;
        buckets = (Node<E>[]) new Node[oldBuckets.length * 2];
        
        // Сохраняем порядок
        Node<E> current = head;
        head = tail = null;
        size = 0;
        
        while (current != null) {
            add(current.value);
            current = current.linkNext;
        }
    }

    private void addToOrder(Node<E> node) {
        if (head == null) {
            head = tail = node;
        } else {
            tail.linkNext = node;
            node.linkPrev = tail;
            tail = node;
        }
    }

    private void removeFromOrder(Node<E> node) {
        if (node.linkPrev != null) {
            node.linkPrev.linkNext = node.linkNext;
        } else {
            head = node.linkNext;
        }
        
        if (node.linkNext != null) {
            node.linkNext.linkPrev = node.linkPrev;
        } else {
            tail = node.linkPrev;
        }
        
        node.linkPrev = node.linkNext = null;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            sb.append(current.value);
            if (i < size - 1) sb.append(", ");
            current = current.linkNext;
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
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        int index = getIndex(e);
        Node<E> newNode = new Node<>(e, buckets[index], null, null);
        buckets[index] = newNode;
        addToOrder(newNode);
        size++;
        resize();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!contains(o)) {
            return false;
        }
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
                removeFromOrder(current);
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
            if (add(item)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object item : c) {
            while (contains(item)) {
                remove(item);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.linkNext;
            if (!c.contains(current.value)) {
                remove(current.value);
                changed = true;
            }
            current = next;
        }
        return changed;
    }

    @Override
    public Iterator<E> iterator() { return null; }
    @Override
    public Object[] toArray() { return new Object[0]; }
    @Override
    public <T> T[] toArray(T[] a) { return null; }
}