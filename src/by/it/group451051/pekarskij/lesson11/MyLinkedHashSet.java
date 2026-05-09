package by.it.group451051.pekarskij.lesson11;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class MyLinkedHashSet<E> implements Set<E> {

    // узел: хранит элемент, ссылку на следующий в бакете и на следующий по порядку добавления
    private static class Node<E> {
        E item; Node<E> next, nextInOrder;
        Node(E it, Node<E> n, Node<E> o) { item = it; next = n; nextInOrder = o; }
    }

    private Node<E>[] table;
    private Node<E> head, tail;  // голова и хвост списка порядка добавления
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        head = tail = null;
    }

    // вычисляем индекс в массиве по хеш-коду
    private int index(Object o) {
        return (o == null ? 0 : o.hashCode()) & (table.length - 1);
    }

    @Override
    public String toString() {
        // выводим элементы в порядке добавления (через nextInOrder)
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        boolean first = true;
        while (cur != null) {
            if (!first) sb.append(", ");
            sb.append(cur.item);
            first = false;
            cur = cur.nextInOrder;
        }
        return sb.append("]").toString();
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) table[i] = null;
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean add(E e) {
        int idx = index(e);
        // проверяем, есть ли уже такой элемент
        for (Node<E> cur = table[idx]; cur != null; cur = cur.next) {
            if (e == null ? cur.item == null : e.equals(cur.item)) {
                return false;
            }
        }
        // добавляем в начало цепочки бакета И в конец списка порядка
        Node<E> newNode = new Node<>(e, table[idx], null);
        table[idx] = newNode;
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.nextInOrder = newNode;
            tail = newNode;
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = index(o);
        Node<E> prev = null;
        for (Node<E> cur = table[idx]; cur != null; cur = cur.next) {
            if (o == null ? cur.item == null : o.equals(cur.item)) {
                // удаляем из бакета
                if (prev == null) table[idx] = cur.next;
                else prev.next = cur.next;
                // удаляем из списка порядка
                if (cur == head) head = cur.nextInOrder;
                else {
                    Node<E> p = head;
                    while (p != null && p.nextInOrder != cur) p = p.nextInOrder;
                    if (p != null) p.nextInOrder = cur.nextInOrder;
                }
                if (cur == tail) tail = (head == null) ? null : 
                    (tail == cur) ? findNewTail() : tail;
                size--;
                return true;
            }
            prev = cur;
        }
        return false;
    }

    // вспомогательный: найти новый хвост после удаления
    private Node<E> findNewTail() {
        Node<E> cur = head;
        while (cur != null && cur.nextInOrder != null) cur = cur.nextInOrder;
        return cur;
    }

    @Override
    public boolean contains(Object o) {
        int idx = index(o);
        for (Node<E> cur = table[idx]; cur != null; cur = cur.next) {
            if (o == null ? cur.item == null : o.equals(cur.item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) if (!contains(item)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean mod = false;
        for (E item : c) if (add(item)) mod = true;
        return mod;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean mod = false;
        for (Object item : c) if (remove(item)) mod = true;
        return mod;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean mod = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.nextInOrder;
            if (!c.contains(cur.item)) {
                remove(cur.item);
                mod = true;
            }
            cur = next;
        }
        return mod;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.nextInOrder) {
            arr[i++] = cur.item;
        }
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.nextInOrder) {
            a[i++] = (T) cur.item;
        }
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> cur = head;
            @Override public boolean hasNext() { return cur != null; }
            @Override public E next() {
                if (cur == null) throw new java.util.NoSuchElementException();
                E res = cur.item;
                cur = cur.nextInOrder;
                return res;
            }
        };
    }
}