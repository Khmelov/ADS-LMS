package by.it.group451051.sinenko.lesson10;

import java.util.*;

public class MyLinkedList<E> implements Deque<E> {

    // Каждый узел хранит данные, ссылку на предыдущий узел и на следующий узел
    private static class Node<E> {
        E item;        // данные
        Node<E> prev;  // ссылка на предыдущий элемент
        Node<E> next;  // ссылка на следующий элемент
        
        // Конструктор узла
        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.prev = prev;
            this.next = next;
        }
    }
    
    private Node<E> first;  // ссылка на первый узел 
    private Node<E> last;   // ссылка на последний узел 
    private int size;       // количество элементов

    public MyLinkedList() {
        first = null;   // пока нет элементов
        last = null;    // пока нет элементов
        size = 0;       // размер 0
    }
    
    @Override
    public int size() {
        return size;    // возвращаем количество элементов
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = first;  // начало с первого узла
        int index = 0;
        
        while (current != null) {  // пока не дошло до конца
            if (index++ > 0) {
                sb.append(", ");
            }
            sb.append(current.item);  // плюсданные узла
            current = current.next;   // переход к следующему узлу
        }
        
        sb.append("]");
        return sb.toString();
    }

    @Override
    public void addFirst(E e) {
        Node<E> newNode = new Node<>(null, e, first);
        
        if (first == null) {
            // если список был пуст то новый узел становится первым и последним
            last = newNode;
        } else {
            // если  не пуст, у бывшего первого узла prev указывает на новый
            first.prev = newNode;
        }
        
        first = newNode;  // новый узел становится первым
        size++;           // увеличивает размер
    }

    @Override
    public void addLast(E e) {
        Node<E> newNode = new Node<>(last, e, null);
        
        if (last == null) {
            // если список был пуст то новый узел становится первым и последним
            first = newNode;
        } else {
            // если  не пуст, у бывшего первого узла prev указывает на новый
            last.next = newNode;
        }
        
        last = newNode;   // новый узел становится последним
        size++;           // увеличивает размер
    }

    @Override
    public boolean add(E e) {
        addLast(e);  //добавляет элемент в конец
        return true;
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return first.item;
    }

    @Override
    public E getLast() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
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
        E result = first.item;
        first = first.next;
        if (first == null) {
            last = null;
        } else {
            first.prev = null;
        }
        size--;
        return result;
    }

    @Override
    public E pollLast() {
        if (size == 0) {
            return null;
        }
        E result = last.item;
        last = last.prev;
        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }
        size--;
        return result;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }

    @Override
    public E remove() {
        return pollFirst();
    }

    @Override
    public E peek() {
        return size == 0 ? null : first.item;
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
    public E removeFirst() {
        return pollFirst();
    }

    @Override
    public E removeLast() {
        return pollLast();
    }

    @Override
    public E peekFirst() {
        return size == 0 ? null : first.item;
    }

    @Override
    public E peekLast() {
        return size == 0 ? null : last.item;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        Node<E> current = first;
        while (current != null) {
            if (Objects.equals(current.item, o)) {
                removeNode(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Node<E> current = last;
        while (current != null) {
            if (Objects.equals(current.item, o)) {
                removeNode(current);
                return true;
            }
            current = current.prev;
        }
        return false;
    }

    private void removeNode(Node<E> node) {
        Node<E> prev = node.prev;
        Node<E> next = node.next;
        
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
        }
        
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
        }
        
        size--;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> current = first;
        while (current != null) {
            if (Objects.equals(current.item, o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E item : c) {
            if (add(item)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object item : c) {
            if (remove(item)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Node<E> current = first;
        while (current != null) {
            Node<E> next = current.next;
            if (!c.contains(current.item)) {
                removeNode(current);
                changed = true;
            }
            current = next;
        }
        return changed;
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        Node<E> current = first;
        int i = 0;
        while (current != null) {
            result[i++] = current.item;
            current = current.next;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(toArray(), size, a.getClass());
        }
        System.arraycopy(toArray(), 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return pollFirst();
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> nodeToRemove;
        
        // ищем узел с нужным индексом
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
        E result = nodeToRemove.item;
        removeNode(nodeToRemove);
        return result;
    }
}
