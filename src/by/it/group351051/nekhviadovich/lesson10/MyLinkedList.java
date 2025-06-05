package by.it.group351051.nekhviadovich.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

//lesson 10 - 2
public class MyLinkedList<E> implements Deque<E> {

    // Внутренний класс Node для представления узлов списка
    private static class Node<E> {
        E element;
        Node<E> next;
        Node<E> prev;

        Node(E element, Node<E> prev, Node<E> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<E> head; // Первый элемент списка
    private Node<E> tail; // Последний элемент списка
    private int size;     // Размер списка

    // Конструктор
    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // Метод для получения строкового представления списка
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
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

    // Добавление элемента в конец списка
    @Override
    public boolean add(E element) {
        addLast(element);
        return true;
    }

    // Добавление элемента в начало списка
    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element, null, head);
        if (head != null) {
            head.prev = newNode;
        } else {
            tail = newNode;
        }
        head = newNode;
        size++;
    }

    // Добавление элемента в конец списка
    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element, tail, null);
        if (tail != null) {
            tail.next = newNode;
        } else {
            head = newNode;
        }
        tail = newNode;
        size++;
    }

    // Удаление и возврат первого элемента списка
    @Override
    public E remove() {
        if (head == null) {
            throw new NoSuchElementException("The deque is empty");
        }
        return removeFirst();
    }

    // Удаление элемента по индексу (вспомогательный метод, не из Deque<E>)
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> current = getNode(index);
        return unlink(current);
    }

    // Удаление первого вхождения указанного элемента
    @Override
    public boolean remove(Object element) {
        Node<E> current = head;
        while (current != null) {
            if (element.equals(current.element)) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Внутренний метод для удаления узла
    private E unlink(Node<E> node) {
        E element = node.element;
        Node<E> next = node.next;
        Node<E> prev = node.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.element = null;
        size--;
        return element;
    }

    // Получение первого элемента без удаления
    @Override
    public E element() {
        return getFirst();
    }

    // Получение первого элемента
    @Override
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.element;
    }

    // Получение последнего элемента
    @Override
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail.element;
    }

    // Удаление и возврат первого элемента (или null, если список пуст)
    @Override
    public E poll() {
        return pollFirst();
    }

    // Удаление и возврат первого элемента (или null, если список пуст)
    @Override
    public E pollFirst() {
        if (head == null) {
            return null;
        }
        return removeFirst();
    }

    // Удаление и возврат последнего элемента (или null, если список пуст)
    @Override
    public E pollLast() {
        if (tail == null) {
            return null;
        }
        return removeLast();
    }

    // Удаление и возврат первого элемента
    @Override
    public E removeFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return unlink(head);
    }

    // Удаление и возврат последнего элемента
    @Override
    public E removeLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return unlink(tail);
    }

    // Возврат размера списка
    @Override
    public int size() {
        return size;
    }

    // Внутренний метод для получения узла по индексу
    private Node<E> getNode(int index) {
        if (index < (size >> 1)) {
            Node<E> current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {
            Node<E> current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }

    // Остальные методы интерфейса Deque<E>

    @Override
    public boolean offer(E e) {
        return add(e);
    }

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
    public E peek() {
        return peekFirst();
    }

    @Override
    public E peekFirst() {
        return (head == null) ? null : head.element;
    }

    @Override
    public E peekLast() {
        return (tail == null) ? null : tail.element;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> current = tail;
        while (current != null) {
            if (o.equals(current.element)) {
                unlink(current);
                return true;
            }
            current = current.prev;
        }
        return false;
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
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E element = current.element;
                current = current.next;
                return element;
            }
        };
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private Node<E> current = tail;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E element = current.element;
                current = current.prev;
                return element;
            }
        };
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for (Node<E> current = head; current != null; current = current.next) {
            array[i++] = current.element;
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (Node<E> current = head; current != null; current = current.next) {
            a[i++] = (T) current.element;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        boolean modified = false;
        for (Object e : c) {
            while (contains(e)) {
                remove(e);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
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
        for (Node<E> current = head; current != null; ) {
            Node<E> next = current.next;
            current.element = null;
            current.next = null;
            current.prev = null;
            current = next;
        }
        head = tail = null;
        size = 0;
    }

    // Внутренний метод для поиска индекса элемента
    private int indexOf(Object o) {
        int index = 0;
        for (Node<E> current = head; current != null; current = current.next) {
            if (o.equals(current.element)) {
                return index;
            }
            index++;
        }
        return -1;
    }
}