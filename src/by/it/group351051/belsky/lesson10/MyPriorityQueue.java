package by.it.group351051.belsky.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/*
    Задание на уровень C

    Создайте class MyPriorityQueue<E>, который реализует интерфейс Queue<E>
    и работает на основе кучи, построенной на приватном массиве типа E[]
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
 */

/**
 * Класс MyPriorityQueue - реализация очереди с приоритетом на основе кучи.
 *
 * @param <E> тип элементов очереди
 */
public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 10; // Начальная емкость массива
    private E[] heap; // Массив для хранения элементов
    private int size; // Текущий размер очереди

    /**
     * Конструктор, инициализирующий массив с начальной емкостью.
     */
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление очереди.
     * Формат: [элемент1, элемент2, ...].
     *
     * @return строка, представляющая элементы очереди
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Возвращает размер очереди.
     *
     * @return количество элементов в очереди
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Очищает очередь, удаляя все элементы.
     */
    @Override
    public void clear() {
        // Обнуляем все элементы массива
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    /**
     * Добавляет элемент в очередь.
     * Если массив заполнен, увеличивает его размер.
     *
     * @param element элемент для добавления
     * @return true, если элемент успешно добавлен
     */
    @Override
    public boolean add(E element) {
        if (size == heap.length) {
            resize(); // Увеличиваем массив при необходимости
        }
        heap[size] = element; // Добавляем элемент в конец массива
        siftUp(size++); // Восстанавливаем свойства кучи
        return true;
    }

    /**
     * Удаляет и возвращает элемент с наивысшим приоритетом.
     *
     * @return элемент с наивысшим приоритетом
     * @throws IllegalStateException если очередь пуста
     */
    @Override
    public E remove() {
        if (size == 0) {
            throw new IllegalStateException("Очередь пуста");
        }
        E result = heap[0]; // Сохраняем корень кучи
        heap[0] = heap[--size]; // Перемещаем последний элемент в корень
        heap[size] = null; // Удаляем последний элемент
        siftDown(0); // Восстанавливаем свойства кучи
        return result;
    }

    /**
     * Удаляет элемент из очереди.
     *
     * @param o элемент для удаления
     * @return true, если элемент был удален
     */
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(o)) {
                heap[i] = heap[--size]; // Перемещаем последний элемент на место удаленного
                heap[size] = null; // Удаляем последний элемент
                siftDown(i); // Восстанавливаем свойства кучи
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет, содержится ли элемент в очереди.
     *
     * @param o элемент для проверки
     * @return true, если элемент содержится в очереди
     */
    @Override
    public boolean contains(Object o) {
        // Проверяем наличие элемента в массиве
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Добавляет элемент в очередь (аналогично {@link #add(Object)}).
     *
     * @param element элемент для добавления
     * @return true, если элемент успешно добавлен
     */
    @Override
    public boolean offer(E element) {
        return add(element);
    }

    /**
     * Удаляет и возвращает элемент с наивысшим приоритетом.
     *
     * @return элемент с наивысшим приоритетом или null, если очередь пуста
     */
    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = heap[0]; // Сохраняем корень кучи
        heap[0] = heap[--size]; // Перемещаем последний элемент в корень
        heap[size] = null; // Удаляем последний элемент
        siftDown(0); // Восстанавливаем свойства кучи
        return result;
    }

    /**
     * Возвращает элемент с наивысшим приоритетом без удаления.
     *
     * @return элемент с наивысшим приоритетом или null, если очередь пуста
     */
    @Override
    public E peek() {
        return size == 0 ? null : heap[0];
    }

    /**
     * Возвращает элемент с наивысшим приоритетом без удаления.
     *
     * @return элемент с наивысшим приоритетом
     * @throws IllegalStateException если очередь пуста
     */
    @Override
    public E element() {
        if (size == 0) {
            throw new IllegalStateException("Очередь пуста");
        }
        return heap[0];
    }

    /**
     * Проверяет, пуста ли очередь.
     *
     * @return true, если очередь пуста
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Проверяет, содержатся ли все элементы коллекции в очереди.
     *
     * @param c коллекция для проверки
     * @return true, если все элементы содержатся в очереди
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Добавляет все элементы из коллекции в очередь.
     *
     * @param c коллекция для добавления
     * @return true, если элементы успешно добавлены
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E element : c) {
            add(element);
        }
        return true;
    }

    /**
     * Удаляет все элементы коллекции из очереди.
     *
     * @param c коллекция для удаления
     * @return true, если элементы были удалены
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Object[] newHeap = new Object[heap.length];
        int newSize = 0;

        for (int i = 0; i < size; i++) {  // Проходим по всем элементам очереди
            if (!c.contains(heap[i])) {  // Если элемент не содержится в коллекции
                newHeap[newSize++] = heap[i];  // Перемещаем его в новый массив
            } else {
                modified = true;  // Если элемент был удален, изменяем флаг
            }
        }

        if (modified) {  // Если были изменения
            System.arraycopy(newHeap, 0, heap, 0, newSize);  // Копируем элементы обратно в основной массив
            for (int i = newSize; i < size; i++) {
                heap[i] = null;  // Обнуляем удаленные элементы
            }
            size = newSize;  // Обновляем размер очереди
            for (int i = (size >>> 1) - 1; i >= 0; i--) {  // Восстанавливаем свойства кучи
                siftDown(i);
            }
        }

        return modified;  // Возвращаем true, если были изменения
    }

    /**
     * Удаляет все элементы, не содержащиеся в коллекции.
     *
     * @param c коллекция для проверки
     * @return true, если очередь была изменена
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        int newSize = 0;

        for (int i = 0; i < size; i++) {  // Проходим по всем элементам очереди
            if (c.contains(heap[i])) {  // Если элемент содержится в коллекции
                heap[newSize++] = heap[i];  // Перемещаем его в новый массив
            } else {
                modified = true;  // Если элемент был удален, изменяем флаг
            }
        }

        for (int i = newSize; i < size; i++) {
            heap[i] = null;  // Обнуляем удаленные элементы
        }

        size = newSize;  // Обновляем размер очереди

        if (modified) {  // Если были изменения
            for (int i = (size >>> 1) - 1; i >= 0; i--) {  // Восстанавливаем свойства кучи
                siftDown(i);
            }
        }

        return modified;  // Возвращаем true, если были изменения
    }

    /**
     * Увеличивает размер массива в два раза, копируя существующие элементы в новый массив.
     */
    private void resize() {
        E[] newHeap = (E[]) new Object[heap.length * 2];
        System.arraycopy(heap, 0, newHeap, 0, heap.length);
        heap = newHeap;
    }

    /**
     * Поднимает элемент вверх по куче, чтобы восстановить её свойства.
     *
     * @param index индекс элемента, который нужно поднять
     */
    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (((Comparable<E>) heap[index]).compareTo(heap[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    /**
     * Опускает элемент вниз по куче, чтобы восстановить её свойства.
     *
     * @param index индекс элемента, который нужно опустить
     */
    private void siftDown(int index) {
        while (index * 2 + 1 < size) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int smallest = left;
            if (right < size && ((Comparable<E>) heap[right]).compareTo(heap[left]) < 0) {
                smallest = right;
            }
            if (((Comparable<E>) heap[index]).compareTo(heap[smallest]) <= 0) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    /**
     * Меняет местами два элемента в массиве heap.
     *
     * @param i индекс первого элемента
     * @param j индекс второго элемента
     */
    private void swap(int i, int j) {
        E temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Остальные методы интерфейса Deque<E> можно оставить нереализованными
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