package by.it.group351051.burdo.lesson10;

import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

// Класс MyLinkedList реализует интерфейс Deque для создания двусвязного списка.
public class MyLinkedList<E> implements Deque<E> {

    // Вложенный класс Node представляет собой элемент списка с указателями на предыдущий и следующий элементы.
    private static class Node<E> {
        E item;           // Содержит значение элемента.
        Node<E> next;     // Ссылка на следующий элемент.
        Node<E> prev;     // Ссылка на предыдущий элемент.

        // Конструктор для инициализации нового узла.
        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    // Указатели на первый и последний элементы списка.
    private Node<E> first;
    private Node<E> last;

    // Размер списка (количество элементов).
    private int size;

    // Конструктор, инициализирующий пустой список.
    public MyLinkedList() {
        first = null;  // Нет первого элемента.
        last = null;   // Нет последнего элемента.
        size = 0;      // Список пустой.
    }

    // Метод для преобразования списка в строку.
    @Override
    public String toString() {
        if (size == 0) {  // Если список пустой, возвращаем "[]".
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node<E> current = first;
        while (current != null) {  // Проходим по всем элементам списка.
            sb.append(current.item);  // Добавляем значение текущего элемента.
            if (current.next != null) {
                sb.append(", ");  // Если не последний элемент, добавляем запятую.
            }
            current = current.next;  // Переходим к следующему элементу.
        }
        sb.append("]");  // Закрываем строку.
        return sb.toString();
    }

    // Метод для добавления элемента в конец списка.
    @Override
    public boolean add(E element) {
        addLast(element);  // Используем метод addLast для добавления в конец.
        return true;
    }

    @Override
    public boolean offer(E e) {
        return false;  // Данный метод не поддерживается в нашем классе.
    }

    @Override
    public E remove() {
        return null;  // Этот метод не реализован в текущей версии.
    }

    // Метод для удаления элемента по индексу.
    public E remove(int index) {
        checkElementIndex(index);  // Проверяем корректность индекса.
        return unlink(node(index));  // Удаляем элемент по индексу.
    }

    // Метод для удаления элемента по его значению.
    @Override
    public boolean remove(Object element) {
        if (element == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {  // Если элемент равен null, удаляем его.
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (element.equals(x.item)) {  // Если элемент найден, удаляем его.
                    unlink(x);
                    return true;
                }
            }
        }
        return false;  // Если элемент не найден, возвращаем false.
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;  // Данный метод не поддерживается.
    }

    @Override
    public boolean contains(Object o) {
        return false;  // Данный метод не поддерживается.
    }

    @Override
    public int size() {
        return size;  // Возвращаем размер списка.
    }

    @Override
    public boolean isEmpty() {
        return size == 0;  // Проверяем, пуст ли список.
    }

    @Override
    public Iterator<E> iterator() {
        return null;  // Этот метод не реализован.
    }

    @Override
    public Object[] toArray() {
        return new Object[0];  // Возвращаем пустой массив.
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;  // Этот метод не реализован.
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;  // Этот метод не реализован.
    }

    // Метод для добавления элемента в начало списка.
    @Override
    public void addFirst(E element) {
        linkFirst(element);  // Добавляем элемент в начало списка.
    }

    // Метод для добавления элемента в конец списка.
    @Override
    public void addLast(E element) {
        linkLast(element);  // Добавляем элемент в конец списка.
    }

    @Override
    public boolean offerFirst(E e) {
        return false;  // Этот метод не поддерживается.
    }

    @Override
    public boolean offerLast(E e) {
        return false;  // Этот метод не поддерживается.
    }

    @Override
    public E removeFirst() {
        return null;  // Этот метод не реализован.
    }

    @Override
    public E removeLast() {
        return null;  // Этот метод не реализован.
    }

    @Override
    public E element() {
        return getFirst();  // Возвращаем первый элемент.
    }

    @Override
    public E peek() {
        return null;  // Этот метод не реализован.
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;  // Этот метод не поддерживается.
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;  // Этот метод не поддерживается.
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;  // Этот метод не поддерживается.
    }

    @Override
    public void clear() {
        first = null;  // Очищаем список.
        last = null;
        size = 0;
    }

    @Override
    public void push(E e) {
        // Этот метод не реализован.
    }

    @Override
    public E pop() {
        return null;  // Этот метод не реализован.
    }

    // Метод для получения первого элемента.
    @Override
    public E getFirst() {
        if (first == null) {
            throw new NoSuchElementException();  // Если список пуст, выбрасываем исключение.
        }
        return first.item;  // Возвращаем значение первого элемента.
    }

    // Метод для получения последнего элемента.
    @Override
    public E getLast() {
        if (last == null) {
            throw new NoSuchElementException();  // Если список пуст, выбрасываем исключение.
        }
        return last.item;  // Возвращаем значение последнего элемента.
    }

    @Override
    public E peekFirst() {
        return null;  // Этот метод не реализован.
    }

    @Override
    public E peekLast() {
        return null;  // Этот метод не реализован.
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;  // Этот метод не поддерживается.
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;  // Этот метод не поддерживается.
    }

    @Override
    public E poll() {
        return pollFirst();  // Возвращаем первый элемент или null, если список пуст.
    }

    @Override
    public E pollFirst() {
        if (first == null) {
            return null;  // Если список пуст, возвращаем null.
        }
        return unlinkFirst();  // Удаляем и возвращаем первый элемент.
    }

    @Override
    public E pollLast() {
        if (last == null) {
            return null;  // Если список пуст, возвращаем null.
        }
        return unlinkLast();  // Удаляем и возвращаем последний элемент.
    }

    // Вспомогательные методы для манипуляций с элементами списка.

    // Метод для добавления элемента в начало списка.
    private void linkFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f);  // Создаем новый узел.
        first = newNode;  // Новый узел становится первым элементом.
        if (f == null) {
            last = newNode;  // Если список был пуст, новый узел становится и последним.
        } else {
            f.prev = newNode;  // Обновляем ссылку на предыдущий элемент.
        }
        size++;  // Увеличиваем размер списка.
    }

    // Метод для добавления элемента в конец списка.
    private void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);  // Создаем новый узел.
        last = newNode;  // Новый узел становится последним элементом.
        if (l == null) {
            first = newNode;  // Если список был пуст, новый узел становится и первым.
        } else {
            l.next = newNode;  // Обновляем ссылку на следующий элемент.
        }
        size++;  // Увеличиваем размер списка.
    }

    // Метод для удаления первого элемента.
    private E unlinkFirst() {
        final Node<E> f = first;
        if (f == null) {
            return null;  // Если список пуст, возвращаем null.
        }

        final E element = f.item;  // Сохраняем значение первого элемента.
        final Node<E> next = f.next;  // Получаем следующий элемент.
        f.item = null;  // Очищаем ссылки на элемент.
        f.next = null;  // Очищаем ссылку на следующий элемент.
        first = next;  // Новый первый элемент.
        if (next == null) {
            last = null;  // Если следующий элемент null, то список пуст.
        } else {
            next.prev = null;  // Обновляем ссылку на предыдущий элемент.
        }
        size--;  // Уменьшаем размер списка.
        return element;  // Возвращаем удаленный элемент.
    }

    // Метод для удаления последнего элемента.
    private E unlinkLast() {
        final Node<E> l = last;
        if (l == null) {
            return null;  // Если список пуст, возвращаем null.
        }

        final E element = l.item;  // Сохраняем значение последнего элемента.
        final Node<E> prev = l.prev;  // Получаем предыдущий элемент.
        l.item = null;  // Очищаем ссылки на элемент.
        l.prev = null;  // Очищаем ссылку на предыдущий элемент.
        last = prev;  // Новый последний элемент.
        if (prev == null) {
            first = null;  // Если предыдущий элемент null, то список пуст.
        } else {
            prev.next = null;  // Обновляем ссылку на следующий элемент.
        }
        size--;  // Уменьшаем размер списка.
        return element;  // Возвращаем удаленный элемент.
    }

    // Метод для удаления конкретного элемента.
    private E unlink(Node<E> x) {
        final E element = x.item;  // Сохраняем значение элемента.
        final Node<E> next = x.next;  // Получаем следующий элемент.
        final Node<E> prev = x.prev;  // Получаем предыдущий элемент.

        if (prev == null) {
            first = next;  // Если элемент был первым, обновляем первый элемент.
        } else {
            prev.next = next;  // Обновляем ссылку на следующий элемент.
            x.prev = null;
        }

        if (next == null) {
            last = prev;  // Если элемент был последним, обновляем последний элемент.
        } else {
            next.prev = prev;  // Обновляем ссылку на предыдущий элемент.
            x.next = null;
        }

        x.item = null;  // Очищаем ссылки на элемент.
        size--;  // Уменьшаем размер списка.
        return element;  // Возвращаем удаленный элемент.
    }

    // Метод для получения узла по индексу.
    private Node<E> node(int index) {
        if (index < (size >> 1)) {  // Если индекс меньше половины списка, ищем с начала.
            Node<E> x = first;
            for (int i = 0; i < index; i++) {
                x = x.next;  // Проходим по элементам списка.
            }
            return x;
        } else {  // Если индекс больше половины списка, ищем с конца.
            Node<E> x = last;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;  // Проходим по элементам списка.
            }
            return x;
        }
    }

    // Метод для проверки корректности индекса.
    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {  // Если индекс выходит за пределы списка.
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}
