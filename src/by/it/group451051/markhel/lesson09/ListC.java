package by.it.group451051.markhel.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {

    private class Node {
        E data;
        Node next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node head;
    private int size = 0;

    //==================== ОБЯЗАТЕЛЬНЫЕ ====================

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

    @Override
    public boolean add(E e) {
        Node newNode = new Node(e);

        if (head == null) {
            head = newNode;
        } else {
            Node cur = head;
            while (cur.next != null) {
                cur = cur.next;
            }
            cur.next = newNode;
        }

        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);

        if (index == 0) {
            E val = head.data;
            head = head.next;
            size--;
            return val;
        }

        Node prev = getNode(index - 1);
        Node cur = prev.next;

        prev.next = cur.next;
        size--;

        return cur.data;
    }

    @Override
    public int size() {
        return size;
    }

    //==================== ВСПОМОГАТЕЛЬНЫЕ ====================

    private Node getNode(int index) {
        Node cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
    }

    //==================== ОПЦИОНАЛЬНЫЕ ====================

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();

        Node newNode = new Node(element);

        if (index == 0) {
            newNode.next = head;
            head = newNode;
        } else {
            Node prev = getNode(index - 1);
            newNode.next = prev.next;
            prev.next = newNode;
        }

        size++;
    }

    @Override
    public boolean remove(Object o) {
        if (head == null) return false;

        if (o == null ? head.data == null : o.equals(head.data)) {
            head = head.next;
            size--;
            return true;
        }

        Node cur = head;

        while (cur.next != null) {
            if (o == null ? cur.next.data == null : o.equals(cur.next.data)) {
                cur.next = cur.next.next;
                size--;
                return true;
            }
            cur = cur.next;
        }

        return false;
    }

    @Override
    public E set(int index, E element) {
        Node node = getNode(index);
        E old = node.data;
        node.data = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        Node cur = head;
        int i = 0;

        while (cur != null) {
            if (o == null ? cur.data == null : o.equals(cur.data))
                return i;

            cur = cur.next;
            i++;
        }

        return -1;
    }

    @Override
    public E get(int index) {
        return getNode(index).data;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = -1;
        Node cur = head;
        int i = 0;

        while (cur != null) {
            if (o == null ? cur.data == null : o.equals(cur.data))
                index = i;

            cur = cur.next;
            i++;
        }

        return index;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
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

        while (head != null && !c.contains(head.data)) {
            head = head.next;
            size--;
            changed = true;
        }

        Node cur = head;

        while (cur != null && cur.next != null) {
            if (!c.contains(cur.next.data)) {
                cur.next = cur.next.next;
                size--;
                changed = true;
            } else {
                cur = cur.next;
            }
        }

        return changed;
    }

    //==================== НЕОБЯЗАТЕЛЬНЫЕ ====================

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        ListC<E> res = new ListC<>();
        Node cur = getNode(fromIndex);

        for (int i = fromIndex; i < toIndex; i++) {
            res.add(cur.data);
            cur = cur.next;
        }

        return res;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
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
}
