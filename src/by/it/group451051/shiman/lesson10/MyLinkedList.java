package by.it.group451051.shiman.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Collection<E> {

    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    // ========== Основные методы Deque ==========

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    public void addFirst(E e) {
        Node<E> f = first;
        Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null) {
            last = newNode;
        } else {
            f.prev = newNode;
        }
        size++;
    }

    public void addLast(E e) {
        Node<E> l = last;
        Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    public E element() {
        if (size == 0) throw new NoSuchElementException();
        return first.item;
    }

    public E getFirst() {
        return element();
    }

    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return last.item;
    }

    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        if (size == 0) return null;
        E result = first.item;
        Node<E> next = first.next;
        first.item = null;
        first.next = null;
        first = next;
        if (next == null) {
            last = null;
        } else {
            next.prev = null;
        }
        size--;
        return result;
    }

    public E pollLast() {
        if (size == 0) return null;
        E result = last.item;
        Node<E> prev = last.prev;
        last.item = null;
        last.prev = null;
        last = prev;
        if (prev == null) {
            first = null;
        } else {
            prev.next = null;
        }
        size--;
        return result;
    }

    // ========== Дополнительные методы удаления ==========

    public boolean remove(Object o) {
        Node<E> x = first;
        while (x != null) {
            if (Objects.equals(x.item, o)) {
                unlink(x);
                return true;
            }
            x = x.next;
        }
        return false;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> x = nodeAt(index);
        E result = x.item;
        unlink(x);
        return result;
    }

    private Node<E> nodeAt(int index) {
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++) x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--) x = x.prev;
            return x;
        }
    }

    private void unlink(Node<E> x) {
        Node<E> prev = x.prev;
        Node<E> next = x.next;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }
        x.item = null;
        size--;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> x = first;
        int i = 0;
        while (x != null) {
            if (i++ > 0) sb.append(", ");
            sb.append(x.item);
            x = x.next;
        }
        sb.append("]");
        return sb.toString();
    }

    // ========== Остальные методы Collection ==========

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> x = first;
        while (x != null) {
            if (Objects.equals(x.item, o)) return true;
            x = x.next;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        private Node<E> cursor = first;
        private Node<E> lastRet = null;
        private int expectedSize = size;

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        public E next() {
            if (expectedSize != size) throw new ConcurrentModificationException();
            if (cursor == null) throw new NoSuchElementException();
            lastRet = cursor;
            E result = cursor.item;
            cursor = cursor.next;
            return result;
        }

        @Override
        public void remove() {
            if (lastRet == null) throw new IllegalStateException();
            if (expectedSize != size) throw new ConcurrentModificationException();
            MyLinkedList.this.unlink(lastRet);
            expectedSize--;
            lastRet = null;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next) {
            result[i++] = x.item;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> x = first; x != null; x = x.next) {
            a[i++] = (T) x.item;
        }
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.item = null;
            x.prev = null;
            x.next = null;
            x = next;
        }
        first = last = null;
        size = 0;
    }
}