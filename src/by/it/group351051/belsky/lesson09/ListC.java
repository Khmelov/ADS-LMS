package by.it.group351051.belsky.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Класс ListC - реализация списка без использования стандартных библиотек.
 * @param <E> тип элементов списка
 */
public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    // Константа для начальной емкости массива
    private static final int DEFAULT_CAPACITY = 10;

    // Массив для хранения элементов
    private Object[] elements;

    // Текущий размер списка
    private int size;

    /**
     * Конструктор по умолчанию, инициализирует массив с начальной емкостью.
     */
    public ListC() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление списка.
     * @return строка, представляющая элементы списка
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
     * @param e элемент для добавления
     * @return true, если элемент успешно добавлен
     */
    @Override
    public boolean add(E e) {
        ensureCapacity();
        elements[size++] = e;
        return true;
    }

    /**
     * Удаляет элемент по индексу.
     * @param index индекс элемента для удаления
     * @return удалённый элемент
     */
    @Override
    public E remove(int index) {
        checkIndex(index);
        E removedElement = (E) elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
        return removedElement;
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
     * Добавляет элемент в список по указанному индексу.
     * @param index индекс, по которому будет добавлен элемент
     * @param element элемент для добавления
     */
    @Override
    public void add(int index, E element) {
        checkIndexForAdd(index);
        ensureCapacity();
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    /**
     * Удаляет первый найденный элемент, равный указанному.
     * @param o объект для удаления
     * @return true, если элемент был удалён
     */
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(o)) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Заменяет элемент по указанному индексу.
     * @param index индекс заменяемого элемента
     * @param element новый элемент
     * @return старый элемент, который был заменён
     */
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E oldElement = (E) elements[index];
        elements[index] = element;
        return oldElement;
    }

    /**
     * Проверяет, пуст ли список.
     * @return true, если список пуст
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
     * Возвращает индекс первого вхождения указанного элемента.
     * @param o объект для поиска
     * @return индекс элемента или -1, если элемент не найден
     */
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Возвращает элемент по указанному индексу.
     * @param index индекс элемента
     * @return элемент по указанному индексу
     */
    @Override
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    /**
     * Проверяет, содержится ли указанный элемент в списке.
     * @param o объект для проверки
     * @return true, если элемент содержится в списке
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * Возвращает индекс последнего вхождения указанного элемента.
     * @param o объект для поиска
     * @return индекс элемента или -1, если элемент не найден
     */
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (elements[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Проверяет, содержатся ли все элементы указанной коллекции в списке.
     * @param c коллекция для проверки
     * @return true, если все элементы содержатся в списке
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Добавляет все элементы указанной коллекции в конец списка.
     * @param c коллекция для добавления
     * @return true, если список изменился
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E item : c) {
            add(item);
        }
        return true;
    }

    /**
     * Добавляет все элементы указанной коллекции, начиная с указанного индекса.
     * @param index индекс, с которого начинается добавление
     * @param c коллекция для добавления
     * @return true, если список изменился
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkIndexForAdd(index);
        for (E item : c) {
            add(index++, item);
        }
        return true;
    }

    /**
     * Удаляет из списка все элементы, содержащиеся в указанной коллекции.
     * @param c коллекция элементов для удаления
     * @return true, если список изменился
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object item : c) {
            while (remove(item)) {
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Удаляет из списка все элементы, не содержащиеся в указанной коллекции.
     * @param c коллекция элементов для сохранения
     * @return true, если список изменился
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i--);
                modified = true;
            }
        }
        return modified;
    }

    // Увеличивает емкость массива при необходимости
    private void ensureCapacity() {
        if (size == elements.length) {
            Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, elements.length);
            elements = newElements;
        }
    }

    // Проверяет индекс на корректность
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Проверяет индекс для добавления
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
