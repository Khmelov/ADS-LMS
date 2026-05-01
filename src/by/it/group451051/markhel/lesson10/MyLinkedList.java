package by.it.group451051.markhel.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Реализация Deque на основе двусвязного списка.
 * Поддерживает все обязательные методы для уровня B.
 * Остальные методы интерфейса Deque выбрасывают UnsupportedOperationException.
 *
 * @param <E> тип элементов
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

    private Node<E> first;
    private Node<E> last;
    private int size;

    public MyLinkedList() {
        first = null;
        last = null;
        size = 0;
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
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void addFirst(E e) {
        Node<E> newFirst = new Node<>(null, e, first);
        if (first == null) {
            first = last = newFirst;
        } else {
            first.prev = newFirst;
            first = newFirst;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> newLast = new Node<>(last, e, null);
        if (last == null) {
            first = last = newLast;
        } else {
            last.next = newLast;
            last = newLast;
        }
        size++;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return first.item;
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return last.item;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E value = first.item;
        Node<E> next = first.next;
        first.item = null;
        first.next = null;
        if (next != null) {
            next.prev = null;
        }
        first = next;
        if (first == null) {
            last = null;
        }
        size--;
        return value;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        E value = last.item;
        Node<E> prev = last.prev;
        last.item = null;
        last.prev = null;
        if (prev != null) {
            prev.next = null;
        }
        last = prev;
        if (last == null) {
            first = null;
        }
        size--;
        return value;
    }

    // Метод remove(int index) – удаление по индексу
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> nodeToRemove;
        if (index < size / 2) {
            nodeToRemove = first;
            for (int i = 0; i < index; i++) {
                nodeToRemove = nodeToRemove.next;
            }
        } else {
            nodeToRemove = last;
            for (int i = size - 1; i > index; i--) {
                nodeToRemove = nodeToRemove.prev;
            }
        }
        return unlink(nodeToRemove);
    }

    // Метод remove(Object o) – удаление по значению (первое вхождение)
    public boolean remove(Object o) {
        Node<E> current = first;
        while (current != null) {
            if (Objects.equals(o, current.item)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Вспомогательный метод удаления узла
    private E unlink(Node<E> node) {
        E element = node.item;
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.item = null;
        size--;
        return element;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = first;
        int index = 0;
        while (current != null) {
            sb.append(current.item);
            if (++index < size) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    // ==================== Остальные методы интерфейса Deque (заглушки) ====================

    @Override
    public boolean offer(E e) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E remove() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E peek() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean offerFirst(E e) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean offerLast(E e) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E removeFirst() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E removeLast() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E peekFirst() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E peekLast() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void push(E e) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public E pop() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Method not implemented");
    }

    // Утилитарный класс для сравнения объектов (аналог Objects.equals)
    private static class Objects {
        static boolean equals(Object a, Object b) {
            return (a == b) || (a != null && a.equals(b));
        }
    }
}
