package by.it.group451052.nasonova.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {

    private static class Node<E> {
        E data;
        Node<E> next;       // цепочка в корзине
        Node<E> orderPrev;  // предыдущий по порядку вставки
        Node<E> orderNext;  // следующий по порядку вставки

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node<E>[] table;
    private int size;
    private Node<E> firstAdded;
    private Node<E> lastAdded;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[16];
        size = 0;
        firstAdded = null;
        lastAdded = null;
    }

    private int getHash(Object o) {
        return (o == null ? 0 : o.hashCode()) & 0x7fffffff;
    }

    private int getIndex(Object o, int length) {
        return getHash(o) % length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[oldTable.length * 2];

        Node<E> current = firstAdded;
        while (current != null) {
            int index = getIndex(current.data, table.length);
            current.next = table[index];
            table[index] = current;
            current = current.orderNext;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = firstAdded;

        while (current != null) {
            sb.append(current.data);
            if (current.orderNext != null) {
                sb.append(", ");
            }
            current = current.orderNext;
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
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
        firstAdded = null;
        lastAdded = null;
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

        if (size >= table.length * 0.75) {
            resize();
        }

        int index = getIndex(e, table.length);
        Node<E> newNode = new Node<>(e, table[index]);
        table[index] = newNode;

        if (firstAdded == null) {
            firstAdded = newNode;
            lastAdded = newNode;
        } else {
            lastAdded.orderNext = newNode;
            newNode.orderPrev = lastAdded;
            lastAdded = newNode;
        }

        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o, table.length);
        Node<E> current = table[index];
        Node<E> previous = null;

        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {

                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                if (current.orderPrev == null) {
                    firstAdded = current.orderNext;
                } else {
                    current.orderPrev.orderNext = current.orderNext;
                }

                if (current.orderNext == null) {
                    lastAdded = current.orderPrev;
                } else {
                    current.orderNext.orderPrev = current.orderPrev;
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
        int index = getIndex(o, table.length);
        Node<E> current = table[index];

        while (current != null) {
            if (o == null ? current.data == null : o.equals(current.data)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E element : c) {
            if (add(element)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = firstAdded;

        while (current != null) {
            Node<E> next = current.orderNext;
            if (c.contains(current.data)) {
                remove(current.data);
                changed = true;
            }
            current = next;
        }

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = firstAdded;

        while (current != null) {
            Node<E> next = current.orderNext;
            if (!c.contains(current.data)) {
                remove(current.data);
                changed = true;
            }
            current = next;
        }

        return changed;
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