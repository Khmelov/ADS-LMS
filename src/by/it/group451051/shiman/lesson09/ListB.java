package by.it.group451051.shiman.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    // ================= ДОБАВЛЕННЫЕ ПОЛЯ =================
    // Внутреннее хранилище элементов списка
    private Object[] elements;
    // Текущее количество элементов в списке
    private int size;

    // ================= ДОБАВЛЕННЫЙ КОНСТРУКТОР =================
    // Конструктор по умолчанию: создаём массив начальной ёмкости 10
    public ListB() {
        this.elements = new Object[10];
        this.size = 0;
    }

    // ================= ДОБАВЛЕННЫЙ ВСПОМОГАТЕЛЬНЫЙ МЕТОД =================
    // Увеличивает ёмкость массива, если места недостаточно
    private void grow() {
        if (size == elements.length) {
            // Увеличиваем размер массива в 1.5 раза
            int newCapacity = elements.length + (elements.length >> 1);
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }

    // ================= РЕАЛИЗАЦИЯ ОБЯЗАТЕЛЬНЫХ МЕТОДОВ =================

    @Override
    public String toString() {
        // Возвращает строковое представление списка в виде [элемент1, элемент2, ...]
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        // Добавляет элемент в конец списка
        grow();
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        // Удаляет элемент по индексу и возвращает его
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

    @Override
    public int size() {
        // Возвращает количество элементов в списке
        return size;
    }

    @Override
    public void add(int index, E element) {
        // Вставляет элемент на указанную позицию
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        grow();
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        // Удаляет первое вхождение указанного объекта
        int index = indexOf(o);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        // Заменяет элемент в указанной позиции
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index];
        elements[index] = element;
        return oldValue;
    }

    @Override
    public boolean isEmpty() {
        // Проверяет, пуст ли список
        return size == 0;
    }

    @Override
    public void clear() {
        // Очищает список, удаляя все элементы
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        // Возвращает индекс первого вхождения объекта или -1
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        // Возвращает элемент по индексу
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        @SuppressWarnings("unchecked")
        E result = (E) elements[index];
        return result;
    }

    @Override
    public boolean contains(Object o) {
        // Проверяет наличие объекта в списке
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        // Возвращает индекс последнего вхождения объекта или -1
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    // ================= ОПЦИОНАЛЬНЫЕ МЕТОДЫ (реализованы минимально) =================

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяет, содержит ли список все элементы коллекции
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // Добавляет все элементы коллекции в конец списка
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // Вставляет все элементы коллекции, начиная с указанной позиции
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        int numNew = c.size();
        if (numNew == 0) return false;
        // Убеждаемся, что хватит места
        while (size + numNew > elements.length) {
            grow();
        }
        // Сдвигаем элементы, освобождая место
        System.arraycopy(elements, index, elements, index + numNew, size - index);
        // Вставляем элементы из коллекции
        int i = index;
        for (E e : c) {
            elements[i++] = e;
        }
        size += numNew;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // Удаляет все элементы, содержащиеся в коллекции
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (c.contains(elements[i])) {
                remove(i);
                i--;
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // Оставляет только те элементы, которые содержатся в коллекции
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(i);
                i--;
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // Не реализован, так как требует создания представления списка
        throw new UnsupportedOperationException("subList not implemented");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        // Не реализован, возвращаем null
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        // Не реализован, возвращаем null
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // Преобразует список в массив типа T
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public Object[] toArray() {
        // Преобразует список в массив Object
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    // ================= ИТЕРАТОР (необязательный, но добавлен для отладки) =================
    @Override
    public Iterator<E> iterator() {
        // Простейшая реализация итератора
        return new Iterator<E>() {
            private int cursor = 0;
            @Override
            public boolean hasNext() {
                return cursor < size;
            }
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                @SuppressWarnings("unchecked")
                E result = (E) elements[cursor++];
                return result;
            }
        };
    }
}