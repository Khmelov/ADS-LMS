package by.it.group351051.burdo.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyTreeSet<E> implements Set<E> {
    // Константы для начальной емкости массива и для работы с коллекцией
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements; // Массив для хранения элементов множества
    private int size; // Количество элементов в множестве

    // Конструктор, инициализирует массив с начальной емкостью
    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        elements = new Object[DEFAULT_CAPACITY]; // Создание массива с заданной емкостью
        size = 0; // Начальный размер множества равен нулю
    }

    // Переопределение метода toString для вывода множества в виде строки
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) { // Проход по всем элементам
            if (i > 0) sb.append(", "); // Добавляем разделитель, если не первый элемент
            sb.append(elements[i]); // Добавляем элемент в строку
        }
        sb.append("]");
        return sb.toString(); // Возвращаем строковое представление множества
    }

    // Метод возвращает количество элементов в множестве
    @Override
    public int size() {
        return size;
    }

    // Метод проверяет, пусто ли множество
    @Override
    public boolean isEmpty() {
        return size == 0; // Если размер 0, то множество пустое
    }

    // Очищает множество, удаляя все элементы
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) { // Проходим по всем элементам
            elements[i] = null; // Очищаем каждый элемент
        }
        size = 0; // Обнуляем размер множества
    }

    // Метод проверяет, содержится ли элемент в множестве
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        if (o == null) return false; // Если объект null, возвращаем false
        Comparable<? super E> key = (Comparable<? super E>) o; // Приводим объект к Comparable

        int low = 0; // Начало диапазона
        int high = size - 1; // Конец диапазона

        // Бинарный поиск по отсортированному массиву
        while (low <= high) {
            int mid = (low + high) >>> 1; // Находим середину диапазона
            E midVal = (E) elements[mid]; // Серединный элемент
            int cmp = key.compareTo(midVal); // Сравниваем элементы

            if (cmp < 0) {
                high = mid - 1; // Ищем в левой части
            } else if (cmp > 0) {
                low = mid + 1; // Ищем в правой части
            } else {
                return true; // Если элемент найден, возвращаем true
            }
        }
        return false; // Элемент не найден
    }

    // Метод добавляет элемент в множество
    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E e) {
        if (e == null) throw new NullPointerException(); // Проверка на null

        Comparable<? super E> key = (Comparable<? super E>) e; // Приведение к Comparable
        int low = 0; // Начало диапазона
        int high = size - 1; // Конец диапазона
        int insertionPoint = size; // Точка вставки

        // Бинарный поиск для поиска места вставки
        while (low <= high) {
            int mid = (low + high) >>> 1; // Находим середину
            E midVal = (E) elements[mid]; // Серединный элемент
            int cmp = key.compareTo(midVal); // Сравниваем

            if (cmp < 0) {
                high = mid - 1; // Ищем в левой части
                insertionPoint = mid;
            } else if (cmp > 0) {
                low = mid + 1; // Ищем в правой части
                insertionPoint = mid + 1;
            } else {
                return false; // Если элемент уже существует, ничего не добавляем
            }
        }

        // Увеличиваем массив при необходимости
        if (size == elements.length) {
            resize(); // Увеличиваем размер массива
        }

        // Сдвигаем элементы массива, чтобы освободить место для нового
        System.arraycopy(elements, insertionPoint, elements, insertionPoint + 1, size - insertionPoint);
        elements[insertionPoint] = e; // Вставляем элемент
        size++; // Увеличиваем размер множества

        return true; // Успешно добавили элемент
    }

    // Метод удаляет элемент из множества
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        if (o == null) return false; // Если объект null, возвращаем false
        Comparable<? super E> key = (Comparable<? super E>) o; // Приведение к Comparable

        int low = 0; // Начало диапазона
        int high = size - 1; // Конец диапазона

        // Бинарный поиск для нахождения элемента
        while (low <= high) {
            int mid = (low + high) >>> 1; // Находим середину
            E midVal = (E) elements[mid]; // Серединный элемент
            int cmp = key.compareTo(midVal); // Сравниваем

            if (cmp < 0) {
                high = mid - 1; // Ищем в левой части
            } else if (cmp > 0) {
                low = mid + 1; // Ищем в правой части
            } else {
                // Нашли элемент, удаляем его
                int numMoved = size - mid - 1; // Количество элементов, которые нужно сдвинуть
                if (numMoved > 0) {
                    System.arraycopy(elements, mid + 1, elements, mid, numMoved); // Сдвигаем элементы
                }
                elements[--size] = null; // Очищаем последний элемент
                return true; // Элемент удален
            }
        }
        return false; // Элемент не найден
    }

    // Метод проверяет, содержат ли все элементы коллекции текущий набор
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) { // Если хотя бы одного элемента нет
                return false;
            }
        }
        return true; // Все элементы содержатся
    }

    // Метод добавляет все элементы из коллекции в множество
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) { // Если элемент добавлен, изменяем флаг
                modified = true;
            }
        }
        return modified; // Возвращаем результат
    }

    // Метод удаляет все элементы из коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            while (remove(o)) { // Удаляем элементы, пока они есть в множестве
                modified = true;
            }
        }
        return modified; // Возвращаем результат
    }

    // Метод сохраняет только те элементы, которые присутствуют в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) { // Проходим с конца, чтобы безопасно изменять список
            if (!c.contains(elements[i])) { // Если элемент не содержится в коллекции
                remove(elements[i]); // Удаляем его
                modified = true;
            }
        }
        return modified; // Возвращаем результат
    }

    // Метод для получения итератора (не поддерживается)
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    // Метод для преобразования в массив (не поддерживается)
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    // Метод для преобразования в массив с типом (не поддерживается)
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    // Метод для изменения размера массива
    private void resize() {
        int newCapacity = elements.length * 2; // Увеличиваем емкость массива в два раза
        Object[] newElements = new Object[newCapacity]; // Создаем новый массив
        System.arraycopy(elements, 0, newElements, 0, size); // Копируем элементы в новый массив
        elements = newElements; // Переназначаем ссылку на массив
    }
}
