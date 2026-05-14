package by.it.group451051.sinenko.lesson11;

import java.util.*;

public class MyLinkedHashSet<E> implements Set<E> {

    // узел с ссылками для порядка добавления
    private static class Node<E> {
        E item;
        Node<E> next;        // для связи в корзине
        Node<E> linkPrev;    // для порядка добавления
        Node<E> linkNext;    // для порядка добавления
        
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    private Node<E>[] buckets;
    private int size;
    private Node<E> head;  // первый элемент в порядке добавления
    private Node<E> tail;  // последний элемент в порядке добавления
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        buckets = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        head = null;
        tail = null;
    }

    // индекс корзины по хеш-коду
    private int getBucketIndex(Object o) {
        if (o == null) return 0;
        return Math.abs(o.hashCode()) % buckets.length;
    }

    // добавляет узел в конец списка порядка 
    private void addToLinks(Node<E> node) {
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.linkNext = node;
            node.linkPrev = tail;
            tail = node;
        }
    }

    // удаляет узел из списка порядка
    private void removeFromLinks(Node<E> node) {
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
    }

    // расширение массива корзин с сохранением порядка
    @SuppressWarnings("unchecked")
    private void grow() {
        Node<E>[] oldBuckets = buckets;
        buckets = (Node<E>[]) new Node[oldBuckets.length * 2];
        
        Node<E> oldHead = head;

        size = 0;
        head = null;
        tail = null;

        Node<E> current = oldHead;
        while (current != null) {
            add(current.item);
            current = current.linkNext;
        }
    }

    // проверка нужно ли расширять массив
    private void checkAndGrow() {
        if ((double) size / buckets.length > LOAD_FACTOR) {
            grow();
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
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        } 
        
        checkAndGrow();
        
        int index = getBucketIndex(e);
        Node<E> newNode = new Node<>(e, buckets[index]);
        buckets[index] = newNode;
        addToLinks(newNode);
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
                removeFromLinks(current);
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

    // выводит элементы в порядке добавления 
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) sb.append(", ");
            sb.append(current.item);
            first = false;
            current = current.linkNext;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        Node<E> current = head;
        int i = 0;
        while (current != null) {
            result[i++] = current.item;
            current = current.linkNext;
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
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.linkNext;
            if (!c.contains(current.item)) {
                remove(current.item);
                changed = true;
            }
            current = next;
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
