package by.it.group351051.burdo.lesson09;

import java.util.*;

/**
 * Класс, представляющий собой реализацию списка, аналогичного ArrayList, без использования стандартных классов библиотеки.
 * Этот список поддерживает операции добавления, удаления, получения элементов, а также управления размером.
 *
 * @param <E> Тип элементов, хранимых в списке.
 */
public class ListA<E> implements List<E> {
    // Константа, определяющая начальную емкость списка.
    private static final int DEFAULT_CAPACITY = 10;
    // Массив для хранения элементов списка.
    private Object[] elements;
    // Текущий размер списка (количество элементов).
    private int size;

    /**
     * Конструктор класса ListA.
     * Инициализирует список с заданной начальной емкостью.
     */
    public ListA() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Переопределенный метод toString для вывода списка в виде строки.
     *
     * @return строковое представление списка.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Добавляет элемент в конец списка.
     * Если массив, в который добавляются элементы, заполнен, то он увеличивается в два раза.
     *
     * @param e элемент, который нужно добавить.
     * @return true, если элемент успешно добавлен.
     */
    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            increaseCapacity();
        }
        elements[size++] = e;
        return true;
    }

    /**
     * Удаляет элемент по индексу.
     * Все элементы, следовавшие за удаленным, сдвигаются на одну позицию влево.
     *
     * @param index индекс элемента, который нужно удалить.
     * @return удаленный элемент.
     * @throws IndexOutOfBoundsException если индекс вне допустимого диапазона.
     */
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index];

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }

        elements[--size] = null;
        return oldValue;
    }

    /**
     * Возвращает текущий размер списка (количество элементов).
     *
     * @return размер списка.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Увеличивает емкость массива, в котором хранятся элементы.
     * Новый размер массива в два раза больше текущего.
     */
    private void increaseCapacity() {
        int newCapacity = elements.length * 2;
        Object[] newElements = new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Вставляет элемент в список по заданному индексу.
     * Все элементы сдвигаются на одну позицию вправо.
     *
     * @param index индекс, на который нужно вставить элемент.
     * @param element элемент, который нужно вставить.
     * @throws IndexOutOfBoundsException если индекс вне допустимого диапазона.
     */
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (size == elements.length) {
            increaseCapacity();
        }

        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    /**
     * Удаляет первый встретившийся элемент, равный переданному объекту.
     *
     * @param o элемент, который нужно удалить.
     * @return true, если элемент был удален.
     */
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    /**
     * Заменяет элемент по заданному индексу на новый.
     *
     * @param index индекс элемента, который нужно заменить.
     * @param element новый элемент.
     * @return старое значение элемента.
     * @throws IndexOutOfBoundsException если индекс вне допустимого диапазона.
     */
    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index];
        elements[index] = element;
        return oldValue;
    }

    /**
     * Проверяет, пуст ли список.
     *
     * @return true, если список пуст, иначе false.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Очищает список, удаляя все элементы.
     */
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /**
     * Находит индекс первого вхождения элемента в список.
     *
     * @param o элемент для поиска.
     * @return индекс элемента, или -1, если элемент не найден.
     */
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Получает элемент по заданному индексу.
     *
     * @param index индекс элемента.
     * @return элемент по индексу.
     * @throws IndexOutOfBoundsException если индекс вне допустимого диапазона.
     */
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        @SuppressWarnings("unchecked")
        E element = (E) elements[index];
        return element;
    }

    /**
     * Проверяет, содержит ли список указанный элемент.
     *
     * @param o элемент для проверки.
     * @return true, если элемент найден в списке, иначе false.
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * Находит индекс последнего вхождения элемента в список.
     *
     * @param o элемент для поиска.
     * @return индекс последнего вхождения элемента, или -1, если элемент не найден.
     */
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return List.of();
    }

    /**
     * Метод для преобразования списка в массив.
     *
     * @return массив, содержащий все элементы списка.
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(elements, 0, array, 0, size);
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает итератор для списка.
     * В данной реализации метод не поддерживается.
     *
     * @return null.
     */
    @Override
    public Iterator<E> iterator() {
        return null;
    }
}
