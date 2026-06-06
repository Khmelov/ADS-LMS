package by.it.group451051.teterukov.lessons_09_15.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E item, Node<E> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    private Node<E> nodeAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<E> cur;
        if (index < size / 2) {
            cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
        } else {
            cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
        }
        return cur;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.item);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        return sb.append("]").toString();
    }

    @Override
    public boolean add(E e) {
        Node<E> last = tail;
        Node<E> node = new Node<>(last, e, null);
        tail = node;
        if (last == null) head = node;
        else last.next = node;
        size++;
        return true;
    }

    @Override
    public E remove(int index) {
        Node<E> node = nodeAt(index);
        E val = node.item;
        Node<E> p = node.prev;
        Node<E> n = node.next;
        if (p == null) head = n;
        else { p.next = n; node.prev = null; }
        if (n == null) tail = p;
        else { n.prev = p; node.next = null; }
        node.item = null;
        size--;
        return val;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (index == size) {
            add(element);
        } else {
            Node<E> succ = nodeAt(index);
            Node<E> pred = succ.prev;
            Node<E> node = new Node<>(pred, element, succ);
            succ.prev = node;
            if (pred == null) head = node;
            else pred.next = node;
            size++;
        }
    }

    @Override
    public boolean remove(Object o) {
        for (Node<E> cur = head; cur != null; cur = cur.next) {
            if (o == null ? cur.item == null : o.equals(cur.item)) {
                Node<E> p = cur.prev, n = cur.next;
                if (p == null) head = n; else p.next = n;
                if (n == null) tail = p; else n.prev = p;
                cur.item = null; cur.prev = null; cur.next = null;
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        Node<E> node = nodeAt(index);
        E old = node.item;
        node.item = element;
        return old;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for (Node<E> cur = head; cur != null; ) {
            Node<E> next = cur.next;
            cur.item = null; cur.prev = null; cur.next = null;
            cur = next;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next, i++) {
            if (o == null ? cur.item == null : o.equals(cur.item)) return i;
        }
        return -1;
    }

    @Override
    public E get(int index) {
        return nodeAt(index).item;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        int i = size - 1;
        for (Node<E> cur = tail; cur != null; cur = cur.prev, i--) {
            if (o == null ? cur.item == null : o.equals(cur.item)) return i;
        }
        return -1;
    }


    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) add(e);
        return !c.isEmpty();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        int i = index;
        for (E e : c) add(i++, e);
        return !c.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (c.contains(cur.item)) {
                Node<E> p = cur.prev, n = cur.next;
                if (p == null) head = n; else p.next = n;
                if (n == null) tail = p; else n.prev = p;
                cur.item = null; cur.prev = null; cur.next = null;
                size--;
                changed = true;
            }
            cur = next;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> cur = head;
        while (cur != null) {
            Node<E> next = cur.next;
            if (!c.contains(cur.item)) {
                Node<E> p = cur.prev, n = cur.next;
                if (p == null) head = n; else p.next = n;
                if (n == null) tail = p; else n.prev = p;
                cur.item = null; cur.prev = null; cur.next = null;
                size--;
                changed = true;
            }
            cur = next;
        }
        return changed;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next) a[i++] = (T) cur.item;
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Node<E> cur = head; cur != null; cur = cur.next) arr[i++] = cur.item;
        return arr;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> cur = head;
            @Override public boolean hasNext() { return cur != null; }
            @Override public E next() { E val = cur.item; cur = cur.next; return val; }
        };
    }

}
