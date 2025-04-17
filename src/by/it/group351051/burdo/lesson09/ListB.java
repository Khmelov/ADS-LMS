package by.it.group351051.burdo.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {

    // Создаём аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private static final int DEFAULT_CAPACITY = 10; // Константа для начальной ёмкости списка
    private Object[] elements; // Массив для хранения элементов списка
    private int size; // Текущий размер списка (количество элементов)

    public ListB() {
        this.elements = new Object[DEFAULT_CAPACITY]; // Инициализация массива с начальной ёмкостью
        this.size = 0; // Изначально список пустой
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]"; // Если список пустой, возвращаем пустой список
        }

        // Создаём строковое представление списка
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size - 1; i++) {
            sb.append(elements[i]).append(", "); // Добавляем элементы в строку через запятую
        }
        sb.append(elements[size - 1]).append("]"); // Добавляем последний элемент
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1); // Обеспечиваем достаточную ёмкость для добавления нового элемента
        elements[size++] = e; // Добавляем элемент в список
        return true; // Возвращаем true после успешного добавления
    }

    @Override
    public E remove(int index) {
        checkIndex(index); // Проверка корректности индекса

        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index]; // Сохраняем удаляемое значение

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved); // Сдвигаем элементы влево
        }

        elements[--size] = null; // Очищаем ссылку на элемент для GC
        return oldValue; // Возвращаем удалённый элемент
    }

    @Override
    public int size() {
        return size; // Возвращаем текущее количество элементов в списке
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // Проверка допустимости индекса
        }

        ensureCapacity(size + 1); // Обеспечиваем достаточную ёмкость
        System.arraycopy(elements, index, elements, index + 1, size - index); // Сдвигаем элементы вправо
        elements[index] = element; // Вставляем новый элемент
        size++; // Увеличиваем размер списка
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    remove(i); // Удаляем первый найденный элемент, равный null
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    remove(i); // Удаляем первый найденный элемент, равный o
                    return true;
                }
            }
        }
        return false; // Если элемент не найден, возвращаем false
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index); // Проверка корректности индекса

        @SuppressWarnings("unchecked")
        E oldValue = (E) elements[index]; // Сохраняем старое значение
        elements[index] = element; // Заменяем старый элемент новым
        return oldValue; // Возвращаем старое значение
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // Проверка на пустоту списка
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null; // Очищаем все элементы списка
        }
        size = 0; // Сбрасываем размер списка
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i; // Возвращаем индекс первого найденного null
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elements[i])) {
                    return i; // Возвращаем индекс первого найденного элемента, равного o
                }
            }
        }
        return -1; // Если элемент не найден, возвращаем -1
    }

    @Override
    public E get(int index) {
        checkIndex(index); // Проверка корректности индекса
        @SuppressWarnings("unchecked")
        E element = (E) elements[index]; // Получаем элемент по индексу
        return element; // Возвращаем элемент
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0; // Проверка на присутствие элемента в списке
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i] == null) {
                    return i; // Возвращаем индекс последнего найденного null
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elements[i])) {
                    return i; // Возвращаем индекс последнего найденного элемента, равного o
                }
            }
        }
        return -1; // Если элемент не найден, возвращаем -1
    }

    // Вспомогательный метод для увеличения ёмкости массива, если требуется
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity; // Увеличиваем ёмкость до необходимой
            }
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size); // Копируем старые элементы в новый массив
            elements = newElements; // Присваиваем новый массив
        }
    }

    // Вспомогательный метод для проверки корректности индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false; // Проверка на наличие всех элементов коллекции
            }
        }
        return true; // Если все элементы содержатся в списке, возвращаем true
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false; // Если коллекция пуста, ничего не добавляем
        }
        for (E e : c) {
            add(e); // Добавляем все элементы из коллекции
        }
        return true; // Возвращаем true после добавления всех элементов
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(); // Проверка корректности индекса
        }
        if (c.isEmpty()) {
            return false; // Если коллекция пуста, ничего не добавляем
        }

        int i = index;
        for (E e : c) {
            add(i++, e); // Добавляем элементы коллекции по порядку
        }
        return true; // Возвращаем true после добавления всех элементов
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            while (remove(o)) {
                modified = true; // Удаляем все элементы, содержащиеся в коллекции
            }
        }
        return modified; // Возвращаем true, если были изменения
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove(); // Удаляем элементы, не содержащиеся в коллекции
                modified = true;
            }
        }
        return modified; // Возвращаем true, если были изменения
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException(); // Не реализовано
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException(); // Не реализовано
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException(); // Не реализовано
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException(); // Не реализовано
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size]; // Создаём новый массив для результата
        for (int i = 0; i < size; i++) {
            result[i] = elements[i]; // Копируем элементы в новый массив
        }
        return result; // Возвращаем массив
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0; // Индекс текущего элемента
            private int lastReturned = -1; // Индекс последнего возвращённого элемента

            @Override
            public boolean hasNext() {
                return currentIndex < size; // Проверка на наличие следующего элемента
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException(); // Если нет следующего элемента, выбрасываем исключение
                }
                lastReturned = currentIndex;
                @SuppressWarnings("unchecked")
                E element = (E) elements[currentIndex++]; // Возвращаем следующий элемент
                return element;
            }

            @Override
            public void remove() {
                if (lastReturned < 0) {
                    throw new IllegalStateException(); // Если метод next() не был вызван, выбрасываем исключение
                }
                ListB.this.remove(lastReturned); // Удаляем элемент
                currentIndex = lastReturned; // Корректируем индекс
                lastReturned = -1; // Сбрасываем индекс последнего возвращённого элемента
            }
        };
    }
}
