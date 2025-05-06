package by.it.group351051.belsky.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

/*
    Задание на уровень B

    Создайте class MyLinkedList<E>, который реализует интерфейс Deque<E>
    и работает на основе двунаправленного связного списка
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
 */

/**
 * Класс MyLinkedList - реализация двусторонней очереди на основе двунаправленного связного списка.
 * @param <E> тип элементов очереди
 */
public class MyLinkedList<E> implements Deque<E> {

    // Узел двунаправленного связного списка
    private static class Node<E> {
        E data; // Данные, хранимые в узле
        Node<E> next; // Ссылка на следующий узел
        Node<E> prev; // Ссылка на предыдущий узел

        /**
         * Конструктор узла.
         * @param data данные узла
         * @param next ссылка на следующий узел
         * @param prev ссылка на предыдущий узел
         */
        Node(E data, Node<E> next, Node<E> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node<E> head; // Голова списка
    private Node<E> tail; // Хвост списка
    private int size;     // Размер списка

    /**
     * Конструктор по умолчанию.
     * Создает пустой список.
     */
    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление списка.
     * Формат: [элемент1, элемент2, ...].
     * @return строка, представляющая элементы списка
     */
    @Override
    public String toString() {
        // Создаем строку для представления списка
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head; // Начинаем с головы списка
        while (current != null) {
            sb.append(current.data); // Добавляем данные текущего узла
            if (current.next != null) {
                sb.append(", "); // Добавляем запятую, если есть следующий элемент
            }
            current = current.next; // Переходим к следующему узлу
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Добавляет элемент в конец списка.
     * @param element элемент для добавления
     * @return true, если элемент успешно добавлен
     */
    @Override
    public boolean add(E element) {
        addLast(element); // Используем метод addLast для добавления элемента в конец
        return true;
    }

    /**
     * Удаляет элемент по индексу.
     * @param index индекс элемента для удаления
     * @return удалённый элемент
     */
    public E remove(int index) {
        checkIndex(index); // Проверяем корректность индекса
        Node<E> current = getNode(index); // Получаем узел по индексу
        unlink(current); // Удаляем узел
        return current.data; // Возвращаем данные удалённого узла
    }

    /**
     * Удаляет первый найденный элемент, равный указанному.
     * @param o объект для удаления
     * @return true, если элемент был удалён
     */
    @Override
    public boolean remove(Object o) {
        Node<E> current = head; // Начинаем с головы списка
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                unlink(current); // Удаляем узел, если нашли совпадение
                return true;
            }
            current = current.next; // Переходим к следующему узлу
        }
        return false; // Элемент не найден
    }

    /**
     * Возвращает количество элементов в списке.
     * @return размер списка
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Добавляет элемент в начало списка.
     * @param element элемент для добавления
     */
    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element, head, null); // Создаем новый узел
        if (head != null) {
            head.prev = newNode; // Устанавливаем ссылку на новый узел
        } else {
            tail = newNode; // Если список пуст, новый узел становится хвостом
        }
        head = newNode; // Новый узел становится головой
        size++;
    }

    /**
     * Добавляет элемент в конец списка.
     * @param element элемент для добавления
     */
    @Override
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element, null, tail); // Создаем новый узел
        if (tail != null) {
            tail.next = newNode; // Устанавливаем ссылку на новый узел
        } else {
            head = newNode; // Если список пуст, новый узел становится головой
        }
        tail = newNode; // Новый узел становится хвостом
        size++;
    }

    /**
     * Возвращает первый элемент списка без удаления.
     * @return первый элемент списка
     */
    @Override
    public E element() {
        return getFirst(); // Используем метод getFirst
    }

    /**
     * Возвращает первый элемент списка без удаления.
     * @return первый элемент списка
     * @throws IllegalStateException если список пуст
     */
    @Override
    public E getFirst() {
        if (head == null) {
            throw new IllegalStateException("Очередь пуста");
        }
        return head.data;
    }

    /**
     * Возвращает последний элемент списка без удаления.
     * @return последний элемент списка
     * @throws IllegalStateException если список пуст
     */
    @Override
    public E getLast() {
        if (tail == null) {
            throw new IllegalStateException("Очередь пуста");
        }
        return tail.data;
    }

    /**
     * Удаляет и возвращает первый элемент списка.
     * @return первый элемент списка или null, если список пуст
     */
    @Override
    public E poll() {
        return pollFirst(); // Используем метод pollFirst
    }

    /**
     * Удаляет и возвращает первый элемент списка.
     * @return первый элемент списка или null, если список пуст
     */
    @Override
    public E pollFirst() {
        if (head == null) {
            return null; // Если список пуст, возвращаем null
        }
        E data = head.data; // Сохраняем данные головы
        unlink(head); // Удаляем голову
        return data;
    }

    /**
     * Удаляет и возвращает последний элемент списка.
     * @return последний элемент списка или null, если список пуст
     */
    @Override
    public E pollLast() {
        if (tail == null) {
            return null; // Если список пуст, возвращаем null
        }
        E data = tail.data; // Сохраняем данные хвоста
        unlink(tail); // Удаляем хвост
        return data;
    }

    // Удаляет узел из списка
    private void unlink(Node<E> node) {
        if (node.prev != null) {
            node.prev.next = node.next; // Обновляем ссылку предыдущего узла
        } else {
            head = node.next; // Если удаляем голову, обновляем ссылку на новую голову
        }
        if (node.next != null) {
            node.next.prev = node.prev; // Обновляем ссылку следующего узла
        } else {
            tail = node.prev; // Если удаляем хвост, обновляем ссылку на новый хвост
        }
        size--; // Уменьшаем размер списка
    }

    // Возвращает узел по индексу
    private Node<E> getNode(int index) {
        Node<E> current;
        if (index < size / 2) {
            current = head; // Начинаем с головы, если индекс ближе к началу
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail; // Начинаем с хвоста, если индекс ближе к концу
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    // Проверяет корректность индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Остальные методы интерфейса Deque<E> можно оставить нереализованными
    @Override
    public boolean offerFirst(E e) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean offerLast(E e) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E removeFirst() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E removeLast() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E peekFirst() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E peekLast() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean offer(E e) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E remove() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E peek() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public void push(E e) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public E pop() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Метод не реализован");
    }
}
