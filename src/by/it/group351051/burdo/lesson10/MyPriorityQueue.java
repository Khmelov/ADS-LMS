package by.it.group351051.burdo.lesson10;

import java.util.Iterator;
import java.util.Collection;
import java.util.Queue;
import java.util.NoSuchElementException;

public class MyPriorityQueue<E> implements Queue<E> {
    // Дефолтная емкость для очереди
    private static final int DEFAULT_CAPACITY = 10;
    // Массив для хранения элементов очереди
    private E[] heap;
    // Размер очереди
    private int size;

    // Конструктор с дефолтной емкостью
    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = (E[]) new Object[DEFAULT_CAPACITY];  // Инициализация массива для хранения элементов
        size = 0;  // Начальный размер очереди 0
    }

    // Переопределение метода toString для отображения содержимого очереди
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {  // Проходим по всем элементам очереди
            if (i > 0) sb.append(", ");  // Добавляем запятую между элементами
            sb.append(heap[i]);  // Добавляем текущий элемент
        }
        sb.append("]");  // Закрываем строку
        return sb.toString();  // Возвращаем строковое представление очереди
    }

    // Возвращает размер очереди
    @Override
    public int size() {
        return size;  // Размер очереди
    }

    // Очищает очередь
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;  // Обнуляем все элементы
        }
        size = 0;  // Устанавливаем размер в 0
    }

    // Добавляет элемент в очередь (реализует метод из интерфейса Queue)
    @Override
    public boolean add(E element) {
        return offer(element);  // Используем метод offer для добавления элемента
    }

    // Удаляет и возвращает элемент из очереди (выбрасывает исключение, если очередь пуста)
    @Override
    public E remove() {
        if (size == 0) throw new NoSuchElementException();  // Проверка на пустоту очереди
        return poll();  // Воспользуемся методом poll для удаления и получения элемента
    }

    // Проверка, содержится ли элемент в очереди
    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {  // Проходим по всем элементам
            if (element.equals(heap[i])) {  // Если элемент найден
                return true;  // Возвращаем true
            }
        }
        return false;  // Если элемент не найден
    }

    // Добавляет элемент в очередь с проверкой на размер и при необходимости увеличивает массив
    @Override
    public boolean offer(E element) {
        if (element == null) throw new NullPointerException();  // Проверка на null

        if (size >= heap.length) {  // Если размер массива больше или равен текущей емкости
            resize();  // Увеличиваем емкость массива
        }

        heap[size] = element;  // Добавляем элемент в конец массива
        siftUp(size);  // После добавления элемента восстанавливаем свойства кучи
        size++;  // Увеличиваем размер очереди
        return true;  // Возвращаем true, так как операция успешна
    }

    // Удаляет и возвращает наименьший элемент очереди
    @Override
    public E poll() {
        if (size == 0) return null;  // Если очередь пуста, возвращаем null

        E result = heap[0];  // Сохраняем наименьший элемент
        heap[0] = heap[size - 1];  // Перемещаем последний элемент в корень
        heap[size - 1] = null;  // Обнуляем последний элемент
        size--;  // Уменьшаем размер очереди
        siftDown(0);  // Восстанавливаем свойства кучи
        return result;  // Возвращаем удаленный элемент
    }

    // Возвращает наименьший элемент без его удаления (или null, если очередь пуста)
    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];  // Если очередь пуста, возвращаем null
    }

    // Возвращает наименьший элемент, выбрасывает исключение, если очередь пуста
    @Override
    public E element() {
        if (size == 0) throw new NoSuchElementException();  // Если очередь пуста, выбрасываем исключение
        return heap[0];  // Возвращаем наименьший элемент
    }

    // Проверка на пустоту очереди
    @Override
    public boolean isEmpty() {
        return size == 0;  // Если размер очереди равен 0, значит очередь пуста
    }

    // Проверка, содержатся ли все элементы коллекции в очереди
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {  // Проходим по всем элементам коллекции
            if (!contains(element)) {  // Если хотя бы одного элемента нет в очереди
                return false;  // Возвращаем false
            }
        }
        return true;  // Все элементы содержатся в очереди
    }

    // Добавляет все элементы из коллекции в очередь
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {  // Проходим по всем элементам коллекции
            if (add(element)) {  // Добавляем элементы в очередь
                modified = true;  // Если хотя бы один элемент добавлен, изменяем флаг
            }
        }
        return modified;  // Возвращаем true, если хотя бы один элемент был добавлен
    }

    // Удаляет все элементы коллекции из очереди
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

    // Сохраняет только те элементы, которые содержатся в коллекции
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

    // Удаляет элемент из очереди
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {  // Проходим по всем элементам очереди
            if (o.equals(heap[i])) {  // Если нашли элемент
                removeAt(i);  // Удаляем его
                return true;  // Возвращаем true, если элемент был удален
            }
        }
        return false;  // Если элемент не найден
    }

    // Метод, который не поддерживается
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();  // Не поддерживаем метод toArray()
    }

    // Метод, который не поддерживается
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();  // Не поддерживаем метод toArray()
    }

    // Метод, который не поддерживается
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();  // Не поддерживаем метод iterator()
    }

    // Увеличивает размер массива, если очередь переполнилась
    private void resize() {
        @SuppressWarnings("unchecked")
        E[] newHeap = (E[]) new Object[heap.length * 2];  // Удваиваем размер массива
        System.arraycopy(heap, 0, newHeap, 0, heap.length);  // Копируем старые элементы в новый массив
        heap = newHeap;  // Обновляем ссылку на массив
    }

    // Метод для восстановления свойств кучи при добавлении элемента
    private void siftUp(int index) {
        E element = heap[index];  // Запоминаем добавленный элемент
        while (index > 0) {  // Пока не достигнем корня
            int parentIndex = (index - 1) >>> 1;  // Индекс родителя
            E parent = heap[parentIndex];  // Родительский элемент
            if (compare(element, parent) >= 0) {  // Если элемент не меньше родителя
                break;  // Прерываем цикл
            }
            heap[index] = parent;  // Перемещаем родителя вниз
            index = parentIndex;  // Двигаемся к родителю
        }
        heap[index] = element;  // Размещаем элемент на правильную позицию
    }

    // Метод для восстановления свойств кучи при удалении элемента
    private void siftDown(int index) {
        E element = heap[index];  // Запоминаем элемент
        int half = size >>> 1;  // Только половина массива содержит дочерние элементы
        while (index < half) {  // Пока не дойдем до листа
            int childIndex = (index << 1) + 1;  // Индекс левого ребенка
            E child = heap[childIndex];  // Левый ребенок
            int rightIndex = childIndex + 1;  // Индекс правого ребенка
            if (rightIndex < size && compare(child, heap[rightIndex]) > 0) {  // Если правый ребенок меньше
                childIndex = rightIndex;  // Двигаем указатель на правого ребенка
                child = heap[rightIndex];
            }
            if (compare(element, child) <= 0) {  // Если элемент не меньше ребенка
                break;  // Прерываем цикл
            }
            heap[index] = child;  // Перемещаем ребенка наверх
            index = childIndex;  // Двигаемся к ребенку
        }
        heap[index] = element;  // Размещаем элемент на правильную позицию
    }

    // Сравнение элементов
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        return ((Comparable<? super E>) a).compareTo(b);  // Сравниваем элементы
    }

    // Удаляет элемент по индексу и восстанавливает кучу
    private void removeAt(int index) {
        size--;  // Уменьшаем размер очереди
        if (size == index) {  // Если удаляем последний элемент
            heap[index] = null;
        } else {
            E moved = heap[size];  // Запоминаем последний элемент
            heap[size] = null;  // Убираем последний элемент
            heap[index] = moved;  // Перемещаем последний элемент на удаленную позицию
            siftDown(index);  // Восстанавливаем свойства кучи
            if (heap[index] == moved) {  // Если элемент не переместился
                siftUp(index);  // Перемещаем его вверх
            }
        }
    }
}
