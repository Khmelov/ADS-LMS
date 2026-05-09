package by.it.group451051.pekarskij.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    // внутреннее поле для хранения элементов
    private E[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    // конструктор инициализирует массив нужной ёмкости
    @SuppressWarnings("unchecked")
    public ListA() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    // увеличиваем массив в 2 раза, если место закончилось
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == elements.length) {
            E[] newArray = (E[]) new Object[elements.length * 2];
            System.arraycopy(elements, 0, newArray, 0, size);
            elements = newArray;
        }
    }

    @Override
    public String toString() {
        // формируем строковое представление списка в формате [elem1, elem2, ...]
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        // добавляем элемент в конец списка, при необходимости расширяя массив
        ensureCapacity();
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        // удаляем элемент по индексу и сдвигаем оставшиеся влево
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        E removed = elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null; // help GC
        return removed;
    }

    @Override
    public int size() {
        // возвращаем текущее количество элементов
        return size;
    }

    @Override
    public void add(int index, E element) {
        // вставляем элемент по указанному индексу со сдвигом последующих
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        ensureCapacity();
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    @Override
    public boolean remove(Object o) {
        // удаляем первое вхождение объекта, используя indexOf
        int index = indexOf(o);
        if (index == -1) return false;
        remove(index);
        return true;
    }

    @Override
    public E set(int index, E element) {
        // заменяем элемент по индексу и возвращаем старое значение
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    @Override
    public boolean isEmpty() {
        // проверяем, пустой ли список
        return size == 0;
    }

    @Override
    public void clear() {
        // очищаем список, обнуляя ссылки для GC
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {
        // ищем индекс первого вхождения объекта с учётом null
        for (int i = 0; i < size; i++) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        // возвращаем элемент по индексу с проверкой границ
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return elements[index];
    }

    @Override
    public boolean contains(Object o) {
        // проверяем наличие объекта через indexOf
        return indexOf(o) != -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        // ищем индекс последнего вхождения объекта, идя с конца
        for (int i = size - 1; i >= 0; i--) {
            if (o == null ? elements[i] == null : o.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // проверяем, что все элементы коллекции присутствуют в списке
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // добавляем все элементы коллекции в конец списка
        boolean modified = false;
        for (E item : c) {
            add(item);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // добавляем элементы коллекции по указанному индексу со сдвигом
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        boolean modified = false;
        for (E item : c) {
            add(index++, item);
            modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // удаляем все элементы, которые есть в переданной коллекции
        boolean modified = false;
        for (Object item : c) {
            while (remove(item)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // оставляем только те элементы, которые есть в переданной коллекции
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elements[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // создаём новый список с элементами в заданном диапазоне [from, to)
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        ListA<E> subList = new ListA<>();
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(elements[i]);
        }
        return subList;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        // возвращаем двунаправленный итератор с указанной позиции
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        return new ListIterator<E>() {
            private int cursor = index;
            private int lastRet = -1;

            @Override
            public boolean hasNext() { return cursor < size; }
            @Override
            public E next() {
                if (cursor >= size) throw new java.util.NoSuchElementException();
                return elements[cursor++];
            }
            @Override
            public boolean hasPrevious() { return cursor > 0; }
            @Override
            public E previous() {
                if (cursor <= 0) throw new java.util.NoSuchElementException();
                return elements[--cursor];
            }
            @Override
            public int nextIndex() { return cursor; }
            @Override
            public int previousIndex() { return cursor - 1; }
            @Override
            public void remove() {
                if (lastRet < 0) throw new IllegalStateException();
                ListA.this.remove(lastRet);
                if (cursor > lastRet) cursor--;
                lastRet = -1;
            }
            @Override
            public void set(E e) {
                if (lastRet < 0) throw new IllegalStateException();
                ListA.this.set(lastRet, e);
            }
            @Override
            public void add(E e) {
                ListA.this.add(cursor++, e);
                lastRet = -1;
            }
        };
    }

    @Override
    public ListIterator<E> listIterator() {
        // возвращаем итератор с начала списка
        return listIterator(0);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // копируем элементы в переданный массив или создаём новый нужного типа
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(elements, 0, a, 0, size);
        if (a.length > size) a[size] = null;
        return a;
    }

    @Override
    public Object[] toArray() {
        // возвращаем копию элементов в виде массива Object[]
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        // возвращаем простой однонаправленный итератор
        return new Iterator<E>() {
            private int cursor = 0;
            private boolean canRemove = false;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (cursor >= size) throw new java.util.NoSuchElementException();
                canRemove = true;
                return elements[cursor++];
            }

            @Override
            public void remove() {
                if (!canRemove) throw new IllegalStateException();
                ListA.this.remove(--cursor);
                canRemove = false;
            }
        };
    }
}