package by.it.group351051.belsky.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/*
    Задание на уровень C

    Создайте class MyTreeSet<E>, который реализует интерфейс Set<E>
    и работает на основе отсортированного массива (любым способом)
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    Метод toString() должен выводить элементы в порядке их возрастания
    Формат вывода: скобки (квадратные) и разделитель (запятая с пробелом) должны
    быть такими же как в методе toString() обычной коллекции
 */

/**
 * Реализация множества MyTreeSet на основе отсортированного массива.
 * Элементы хранятся в порядке возрастания.
 *
 * Особенности:
 * - Элементы добавляются в массив с сохранением порядка сортировки.
 * - Для поиска используется бинарный поиск, что ускоряет операции contains.
 * - При добавлении или удалении элементы сдвигаются, что может быть затратным.
 *
 * @param <E> тип элементов множества, должен быть Comparable
 */
public class MyTreeSet<E> implements Set<E> {
    private static final int DEFAULT_CAPACITY = 16; // Начальная емкость массива
    private E[] elements; // Массив для хранения элементов
    private int size; // Текущее количество элементов в множестве

    /**
     * Конструктор, инициализирующий множество с начальной емкостью.
     * Массив создается пустым, но с фиксированной начальной емкостью.
     */
    public MyTreeSet() {
        elements = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        // Формируем строку с элементами в порядке возрастания
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

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        // Очищает массив, создавая новый пустой массив с начальной емкостью
        elements = (E[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException("Элемент не может быть null");
        if (contains(e)) return false; // Дубликаты не добавляются

        // Увеличиваем массив, если он заполнен
        if (size == elements.length) {
            resize();
        }

        // Находим индекс для вставки нового элемента
        int index = findInsertIndex(e);

        // Сдвигаем элементы вправо, чтобы освободить место для нового элемента
        System.arraycopy(elements, index, elements, index + 1, size - index);

        // Вставляем элемент и увеличиваем размер
        elements[index] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || !contains(o)) return false;

        // Находим индекс элемента для удаления
        int index = findIndex((E) o);

        // Сдвигаем элементы влево, чтобы заполнить "пустое место"
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);

        // Уменьшаем размер и обнуляем последний элемент
        elements[--size] = null;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;

        // Используем бинарный поиск для проверки наличия элемента
        int left = 0, right = size - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = ((Comparable<E>) o).compareTo(elements[mid]);
            if (cmp == 0) return true; // Элемент найден
            if (cmp < 0) right = mid - 1; // Ищем в левой половине
            else left = mid + 1; // Ищем в правой половине
        }
        return false; // Элемент не найден
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // Проверяем, содержатся ли все элементы из коллекции
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        // Добавляем все элементы из коллекции
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        // Удаляем все элементы, которые содержатся в коллекции
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        // Удаляем элементы, которые не содержатся в коллекции
        for (int i = 0; i < size; i++) {
            if (!c.contains(elements[i])) {
                remove(elements[i]);
                i--; // Уменьшаем индекс, так как массив сдвинулся
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Увеличивает размер массива в два раза.
     * Это необходимо для обеспечения динамического роста множества.
     */
    private void resize() {
        E[] newElements = (E[]) new Comparable[elements.length * 2];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    /**
     * Находит индекс для вставки нового элемента.
     * Используется бинарный поиск для определения позиции.
     *
     * @param e элемент для вставки
     * @return индекс для вставки
     */
    private int findInsertIndex(E e) {
        int left = 0, right = size;
        while (left < right) {
            int mid = (left + right) / 2;
            if (((Comparable<E>) e).compareTo(elements[mid]) < 0) {
                right = mid; // Ищем в левой половине
            } else {
                left = mid + 1; // Ищем в правой половине
            }
        }
        return left; // Возвращаем индекс для вставки
    }

    /**
     * Находит индекс элемента в массиве.
     * Используется линейный поиск, так как массив отсортирован.
     *
     * @param e элемент для поиска
     * @return индекс элемента или -1, если элемент не найден
     */
    private int findIndex(E e) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(e)) {
                return i;
            }
        }
        return -1; // Элемент не найден
    }

    // Остальные методы интерфейса Set<E> можно оставить нереализованными
    @Override
    public Iterator<E> iterator() {
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
}