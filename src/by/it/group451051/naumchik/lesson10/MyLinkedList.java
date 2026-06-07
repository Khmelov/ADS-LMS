package by.it.group451051.naumchik.lesson10;

import java.util.*;

/**
 * Реализация Deque на базе двусвязного списка.
 * Не использует готовые коллекции стандартной библиотеки.
 */
public class MyLinkedList<E> implements Deque<E> {

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

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // -------------------------------------------------------------------
    // Обязательные методы
    // -------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.item);
            if (curr.next != null) sb.append(", ");
            curr = curr.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> node;
        if (index < size / 2) {
            node = head;
            for (int i = 0; i < index; i++) node = node.next;
        } else {
            node = tail;
            for (int i = size - 1; i > index; i--) node = node.prev;
        }
        final E item = node.item;   // сохраняем до удаления
        unlink(node);
        return item;
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(E element) {
        final Node<E> f = head;
        final Node<E> newNode = new Node<>(null, element, f);
        head = newNode;
        if (f == null) {
            tail = newNode;
        } else {
            f.prev = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        final Node<E> l = tail;
        final Node<E> newNode = new Node<>(l, element, null);
        tail = newNode;
        if (l == null) {
            head = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return head.item;
    }

    @Override
    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return tail.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        final Node<E> f = head;
        return (f == null) ? null : unlinkFirst(f);
    }

    @Override
    public E pollLast() {
        final Node<E> l = tail;
        return (l == null) ? null : unlinkLast(l);
    }

    // -------------------------------------------------------------------
    // Остальные методы Deque
    // -------------------------------------------------------------------

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if (head == null) throw new NoSuchElementException();
        return unlinkFirst(head);
    }

    @Override
    public E removeLast() {
        if (tail == null) throw new NoSuchElementException();
        return unlinkLast(tail);
    }

    @Override
    public E peekFirst() {
        return (head == null) ? null : head.item;
    }

    @Override
    public E peekLast() {
        return (tail == null) ? null : tail.item;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (Node<E> x = head; x != null; x = x.next) {
            if (objectsEqual(o, x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Node<E> x = tail; x != null; x = x.prev) {
            if (objectsEqual(o, x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean contains(Object o) {
        for (Node<E> x = head; x != null; x = x.next) {
            if (objectsEqual(o, x.item)) return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DescendingItr();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = head; x != null; x = x.next) {
            result[i++] = x.item;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        }
        int i = 0;
        Object[] result = a;
        for (Node<E> x = head; x != null; x = x.next) {
            result[i++] = x.item;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
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
        // Защита от бесконечного цикла при c == this
        Object[] a = c.toArray();
        for (Object e : a) {
            add((E) e);
        }
        return a.length > 0;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Защита от c == this
        Object[] a = c.toArray();
        boolean modified = false;
        for (Object e : a) {
            while (remove(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Защита от c == this
        Object[] a = c.toArray();
        boolean modified = false;
        for (Iterator<E> it = iterator(); it.hasNext(); ) {
            if (!containsInArray(a, it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    private boolean containsInArray(Object[] a, Object o) {
        for (Object e : a) {
            if (objectsEqual(o, e)) return true;
        }
        return false;
    }

    @Override
    public void clear() {
        for (Node<E> x = head; x != null; ) {
            Node<E> next = x.next;
            x.item = null;
            x.prev = null;
            x.next = null;
            x = next;
        }
        head = tail = null;
        size = 0;
    }

    // -------------------------------------------------------------------
    // Вспомогательные методы
    // -------------------------------------------------------------------

    private void unlink(Node<E> x) {
        final Node<E> prev = x.prev;
        final Node<E> next = x.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;   // помощь GC
        size--;
    }

    private E unlinkFirst(Node<E> f) {
        final E element = f.item;
        final Node<E> next = f.next;
        f.item = null;
        f.next = null;
        head = next;
        if (next == null) {
            tail = null;
        } else {
            next.prev = null;
        }
        size--;
        return element;
    }

    private E unlinkLast(Node<E> l) {
        final E element = l.item;
        final Node<E> prev = l.prev;
        l.item = null;
        l.prev = null;
        tail = prev;
        if (prev == null) {
            head = null;
        } else {
            prev.next = null;
        }
        size--;
        return element;
    }

    private static boolean objectsEqual(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    // -------------------------------------------------------------------
    // Итераторы
    // -------------------------------------------------------------------

    private class Itr implements Iterator<E> {
        private Node<E> lastReturned = null;
        private Node<E> next = head;

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastReturned = next;
            next = next.next;
            return lastReturned.item;
        }

        @Override
        public void remove() {
            if (lastReturned == null) throw new IllegalStateException();
            Node<E> lastNext = lastReturned.next;
            unlink(lastReturned);
            if (next == lastReturned) {
                next = lastNext;
            }
            lastReturned = null;
        }
    }

    private class DescendingItr implements Iterator<E> {
        private Node<E> lastReturned = null;
        private Node<E> next = tail;

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastReturned = next;
            next = next.prev;
            return lastReturned.item;
        }

        @Override
        public void remove() {
            if (lastReturned == null) throw new IllegalStateException();
            Node<E> lastPrev = lastReturned.prev;
            unlink(lastReturned);
            if (next == lastReturned) {
                next = lastPrev;
            }
            lastReturned = null;
        }
    }
}
