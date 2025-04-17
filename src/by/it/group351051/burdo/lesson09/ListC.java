package by.it.group351051.burdo.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListC<E> implements List<E> {
    // Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private static final int DEFAULT_CAPACITY = 10; // Начальный размер массива
    private Object[] elements; // Массив для хранения элементов списка
    private int size; // Количество элементов в списке

    // Конструктор по умолчанию, создающий пустой список с начальной ёмкостью
    public ListC() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Метод для вывода списка в строковом представлении
    @Override
    public String toString() {
        if (size == 0) {
            return "[]"; // Если список пуст, возвращаем пустые скобки
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size - 1; i++) { // Проходим по всем элементам, кроме последнего
            sb.append(elements[i]).append(", ");
        }
        sb.append(elements[size - 1]).append("]"); // Добавляем последний элемент
        return sb.toString();
    }

    // Метод для добавления элемента в конец списка
    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1); // Обеспечиваем достаточную ёмкость для нового элемента
        elements[size++] = e; // Добавляем элемент и увеличиваем размер
        return true;
    }

    // Метод для удаления элемента по индексу
    @Override
    public E remove(int index) {
        checkIndex(index); // Проверяем, что индекс валиден

        @SuppressWarnings("unchecked")
        E removed = (E) elements[index]; // Сохраняем удалённый элемент

        // Сдвигаем все элементы после удалённого на одну позицию влево
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[--size] = null; // Очищаем последний элемент, чтобы освободить память
        return removed; // Возвращаем удалённый элемент
    }

    // Метод для получения размера списка
    @Override
    public int size() {
        return size; // Возвращаем количество элементов в списке
    }

    // Метод для добавления элемента по заданному индексу
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) { // Проверяем, что индекс валиден
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        ensureCapacity(size + 1); // Обеспечиваем достаточную ёмкость

        // Сдвигаем все элементы после индекса на одну позицию вправо
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element; // Вставляем новый элемент
        size++; // Увеличиваем размер
    }

    // Метод для удаления первого вхождения объекта из списка
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o); // Находим индекс первого вхождения объекта
        if (index >= 0) { // Если объект найден
            remove(index); // Удаляем элемент по индексу
            return true;
        }
        return false; // Если объект не найден, возвращаем false
    }

    // Метод для замены элемента по индексу
    @Override
    public E set(int index, E element) {
        checkIndex(index); // Проверяем валидность индекса

        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index]; // Сохраняем старое значение
        elements[index] = element; // Заменяем элемент
        return oldValue; // Возвращаем старое значение
    }

    // Метод для проверки, пуст ли список
    @Override
    public boolean isEmpty() {
        return size == 0; // Возвращаем true, если список пуст
    }

    // Метод для очистки списка
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null; // Очищаем все элементы
        }
        size = 0; // Устанавливаем размер в 0
    }

    // Метод для поиска индекса первого вхождения объекта в список
    @Override
    public int indexOf(Object o) {
        if (o == null) { // Если объект null, ищем все элементы, равные null
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i; // Возвращаем индекс первого вхождения
                }
            }
        } else { // Если объект не null, ищем равенство через метод equals
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    return i; // Возвращаем индекс первого вхождения
                }
            }
        }
        return -1; // Если объект не найден, возвращаем -1
    }

    // Метод для получения элемента по индексу
    @Override
    public E get(int index) {
        checkIndex(index); // Проверяем, что индекс валиден
        @SuppressWarnings("unchecked")
        E element = (E) elements[index]; // Получаем элемент по индексу
        return element;
    }

    // Метод для проверки, содержит ли список указанный объект
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0; // Если индекс >= 0, то объект найден
    }

    // Метод для поиска индекса последнего вхождения объекта
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) { // Если объект null, ищем все элементы, равные null, с конца
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) {
                    return i; // Возвращаем индекс последнего вхождения
                }
            }
        } else { // Если объект не null, ищем равенство через метод equals
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) {
                    return i; // Возвращаем индекс последнего вхождения
                }
            }
        }
        return -1; // Если объект не найден, возвращаем -1
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

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Дополнительные методы для работы со списком ///////
    /////////////////////////////////////////////////////////////////////////

    // Метод для проверки, содержат ли все элементы из коллекции текущий список
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) { // Если хотя бы один элемент не содержится, возвращаем false
                return false;
            }
        }
        return true; // Все элементы содержатся
    }

    // Метод для добавления всех элементов из коллекции в список
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.size() == 0) {
            return false; // Если коллекция пуста, ничего не добавляем
        }

        ensureCapacity(size + c.size()); // Обеспечиваем достаточную ёмкость
        for (E e : c) {
            elements[size++] = e; // Добавляем элементы в список
        }
        return true;
    }

    // Метод для добавления всех элементов из коллекции в список с определённого индекса
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) { // Проверяем валидность индекса
            throw new IndexOutOfBoundsException();
        }

        if (c.size() == 0) {
            return false; // Если коллекция пуста, ничего не добавляем
        }

        ensureCapacity(size + c.size()); // Обеспечиваем достаточную ёмкость

        // Сдвигаем все элементы после индекса вправо, чтобы освободить место для новых
        for (int i = size - 1; i >= index; i--) {
            elements[i + c.size()] = elements[i];
        }

        // Добавляем новые элементы
        int i = index;
        for (E e : c) {
            elements[i++] = e;
        }

        size += c.size(); // Увеличиваем размер
        return true;
    }

    // Метод для удаления всех элементов из списка, которые содержатся в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            while (remove(o)) { // Пока можем удалить элементы, продолжаем
                modified = true;
            }
        }
        return modified; // Возвращаем true, если хотя бы один элемент был удалён
    }

    // Метод для оставления в списке только тех элементов, которые содержатся в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) { // Если элемент не содержится в коллекции
                remove(i); // Удаляем его
                i--; // Корректируем индекс после удаления
                modified = true;
            }
        }
        return modified; // Возвращаем true, если были удалены элементы
    }

    // Метод для преобразования списка в массив
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size]; // Создаём новый массив
        for (int i = 0; i < size; i++) {
            array[i] = elements[i]; // Копируем элементы в массив
        }
        return array; // Возвращаем массив
    }

    // Метод для преобразования списка в массив указанного типа
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException(); // Не поддерживается в данной реализации
    }

    // Метод для обеспечения достаточной ёмкости массива
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) { // Если требуется больше места, увеличиваем ёмкость
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            Object[] newElements = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i]; // Копируем элементы в новый массив
            }
            elements = newElements; // Обновляем ссылку на массив
        }
    }

    // Метод для проверки валидности индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size) { // Если индекс не валиден, выбрасываем исключение
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Необязательные методы для итератора            ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return null; // Итератор не реализован в этой версии
    }
}
