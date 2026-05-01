package by.it.group451051.markhel.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class ListA<E> implements List<E> {

    private class Node {
        E data;
        Node prev;
        Node next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int size = 0;

    //==================== ОСНОВНЫЕ ====================

    @Override
    public boolean add(E e) {
        Node n = new Node(e);

        if (tail == null) {
            head = tail = n;
        } else {
            tail.next = n;
            n.prev = tail;
            tail = n;
        }

        size++;
        return true;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return getNode(index).data;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);

        Node cur = getNode(index);
        return removeNode(cur);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node cur = head;

        while (cur != null) {
            sb.append(cur.data);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }

        sb.append("]");
        return sb.toString();
    }

    //==================== ВСПОМОГАТЕЛЬНЫЕ ====================

    private Node getNode(int index) {
        Node cur;

        if (index < size / 2) {
            cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
        } else {
            cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
        }

        return cur;
    }

    private E removeNode(Node cur) {

        E val = cur.data;

        if (cur.prev != null) cur.prev.next = cur.next;
        else head = cur.next;

        if (cur.next != null) cur.next.prev = cur.prev;
        else tail = cur.prev;

        size--;
        return val;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
    }

    //==================== ОБЯЗАТЕЛЬНЫЕ ====================

    @Override
    public void add(int index, E element) {
        if (index == size) {
            add(element);
            return;
        }

        checkIndex(index);

        Node next = getNode(index);
        Node prev = next.prev;
        Node n = new Node(element);

        n.next = next;
        n.prev = prev;
        next.prev = n;

        if (prev == null) head = n;
        else prev.next = n;

        size++;
    }

    @Override
    public boolean remove(Object o) {
        Node cur = head;

        while (cur != null) {
            if (Objects.equals(cur.data, o)) {
                removeNode(cur);
                return true;
            }
            cur = cur.next;
        }

        return false;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);

        Node n = getNode(index);
        E old = n.data;
        n.data = element;

        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        Node cur = head;
        int i = 0;

        while (cur != null) {
            if (Objects.equals(cur.data, o)) return i;
            cur = cur.next;
            i++;
        }

        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node cur = tail;
        int i = size - 1;

        while (cur != null) {
            if (Objects.equals(cur.data, o)) return i;
            cur = cur.prev;
            i--;
        }

        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!contains(e)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            add(e);
            changed = true;
        }
        return changed;
    }

    // ✔ ВОТ ЭТО БЫЛА ТВОЯ ОШИБКА
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E e : c) {
            add(index++, e);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object e : c) {
            while (remove(e)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;

        Node cur = head;
        while (cur != null) {
            Node next = cur.next;
            if (!c.contains(cur.data)) {
                remove(cur.data);
                changed = true;
            }
            cur = next;
        }

        return changed;
    }

    //==================== ОСТАЛЬНОЕ ====================

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        ListA<E> res = new ListA<>();
        Node cur = getNode(fromIndex);

        for (int i = fromIndex; i < toIndex; i++) {
            res.add(cur.data);
            cur = cur.next;
        }

        return res;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        Node cur = head;

        for (int i = 0; i < size; i++) {
            arr[i] = cur.data;
            cur = cur.next;
        }

        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) toArray();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node cur = head;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public E next() {
                E val = cur.data;
                cur = cur.next;
                return val;
            }
        };
    }

    @Override public ListIterator<E> listIterator() { throw new UnsupportedOperationException(); }
    @Override public ListIterator<E> listIterator(int index) { throw new UnsupportedOperationException(); }
}