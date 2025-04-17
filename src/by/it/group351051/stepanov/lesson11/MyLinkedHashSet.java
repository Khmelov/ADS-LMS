package by.it.group351051.stepanov.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16;
    private Node<E>[] table;
    private LinkedNode<E> head;
    private LinkedNode<E> tail;
    private int size;

    public MyLinkedHashSet() {
        table = new Node[DEFAULT_CAPACITY];
        head = null;
        tail = null;
        size = 0;
    }

    private static class Node<E> {
        E element;
        Node<E> next;
        LinkedNode<E> linkedNode;

        Node(E element, Node<E> next, LinkedNode<E> linkedNode) {
            this.element = element;
            this.next = next;
            this.linkedNode = linkedNode;
        }
    }

    private static class LinkedNode<E> {
        E element;
        LinkedNode<E> prev;
        LinkedNode<E> next;

        LinkedNode(E element) {
            this.element = element;
        }
    }

    private int getIndex(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public boolean add(E e) {
        int index = getIndex(e);
        Node<E> current = table[index];

        while (current != null) {
            if (Objects.equals(current.element, e)) {
                return false;
            }
            current = current.next;
        }

        LinkedNode<E> newLinkedNode = new LinkedNode<>(e);
        if (tail == null) {
            head = tail = newLinkedNode;
        } else {
            tail.next = newLinkedNode;
            newLinkedNode.prev = tail;
            tail = newLinkedNode;
        }

        table[index] = new Node<>(e, table[index], newLinkedNode);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];
        Node<E> previous = null;

        while (current != null) {
            if (Objects.equals(current.element, o)) {
                LinkedNode<E> linkedNode = current.linkedNode;
                if (linkedNode.prev != null) {
                    linkedNode.prev.next = linkedNode.next;
                } else {
                    head = linkedNode.next;
                }
                if (linkedNode.next != null) {
                    linkedNode.next.prev = linkedNode.prev;
                } else {
                    tail = linkedNode.prev;
                }

                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);
        Node<E> current = table[index];

        while (current != null) {
            if (Objects.equals(current.element, o)) {
                return true;
            }
            current = current.next;
        }

        return false;
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
        table = new Node[DEFAULT_CAPACITY];
        head = tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        LinkedNode<E> current = head;
        while (current != null) {
            sb.append(current.element);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        LinkedNode<E> current = head;

        while (current != null) {
            if (!c.contains(current.element)) {
                remove(current.element);
                modified = true;
            }
            current = current.next;
        }

        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}