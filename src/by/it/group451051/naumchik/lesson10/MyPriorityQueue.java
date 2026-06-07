package by.it.group451051.naumchik.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.lang.reflect.Array;

/**
 * Реализация очереди с приоритетом на основе двоичной кучи (min-heap).
 * В качестве внутреннего хранилища используется массив Object[].
 * Порядок элементов определяется компаратором, либо естественным порядком (Comparable).
 *
 * @param <E> тип элементов
 */
public class MyPriorityQueue<E> implements Queue<E> {

    // Начальная ёмкость по умолчанию
    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    // Массив для хранения элементов кучи
    private E[] heap;

    // Текущее количество элементов
    private int size;

    // Компаратор для определения порядка; null означает естественный порядок
    private final Comparator<? super E> comparator;

    // ------------------ Конструкторы ------------------

    /** Создаёт пустую очередь с приоритетом (естественный порядок). */
    public MyPriorityQueue() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    /**
     * Создаёт пустую очередь с заданным компаратором.
     * @param comparator компаратор для сравнения элементов (null – естественный порядок)
     */
    public MyPriorityQueue(Comparator<? super E> comparator) {
        this(DEFAULT_INITIAL_CAPACITY, comparator);
    }

    /**
     * Приватный конструктор для инициализации массива и компаратора.
     * @param initialCapacity начальная ёмкость
     * @param comparator компаратор
     */
    @SuppressWarnings("unchecked")
    private MyPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        this.heap = (E[]) new Object[initialCapacity];
        this.comparator = comparator;
    }

    // ================== Обязательные методы ==================

    /**
     * Возвращает строковое представление очереди в формате [e1, e2, ...].
     * Элементы перечисляются в порядке хранения в массиве.
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(heap[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    /** Возвращает количество элементов в очереди. */
    @Override
    public int size() {
        return size;
    }

    /** Удаляет все элементы из очереди. */
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null; // помогаем сборщику мусора
        }
        size = 0;
    }

    /**
     * Вставляет элемент в очередь.
     * @param element добавляемый элемент
     * @return true (всегда)
     * @throws NullPointerException если element == null
     */
    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Null элементы не допускаются");
        }
        return offer(element);
    }

    /**
     * Удаляет и возвращает элемент из головы очереди (минимальный).
     * @return удалённый минимальный элемент
     * @throws NoSuchElementException если очередь пуста
     */
    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        return removeAt(0); // удаляем корень кучи
    }

    /**
     * Проверяет, содержится ли заданный элемент в очереди.
     * @param o элемент для поиска (может быть null)
     * @return true, если элемент найден
     */
    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Вставляет элемент в очередь (без исключения при нарушении ёмкости).
     * @param element добавляемый элемент
     * @return true, если элемент был добавлен
     * @throws NullPointerException если element == null
     */
    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException("Null элементы не допускаются");
        }
        ensureCapacity(); // при необходимости увеличиваем массив
        heap[size] = element;
        siftUp(size);    // восстанавливаем свойство кучи
        size++;
        return true;
    }

    /**
     * Удаляет и возвращает головной элемент (минимальный) или null, если очередь пуста.
     * @return минимальный элемент или null
     */
    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        return removeAt(0);
    }

    /**
     * Возвращает головной элемент без удаления или null, если очередь пуста.
     * @return минимальный элемент или null
     */
    @Override
    public E peek() {
        return (size == 0) ? null : heap[0];
    }

    /**
     * Возвращает головной элемент без удаления.
     * @return минимальный элемент
     * @throws NoSuchElementException если очередь пуста
     */
    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        return heap[0];
    }

    /** Проверяет, пуста ли очередь. */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Проверяет, содержатся ли все элементы коллекции в очереди.
     * @param c коллекция для проверки
     * @return true, если все элементы коллекции присутствуют
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Добавляет все элементы из коллекции в очередь.
     * @param c коллекция добавляемых элементов
     * @return true, если очередь изменилась
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            if (add(e)) {
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Удаляет все элементы очереди, которые присутствуют в заданной коллекции.
     * @param c коллекция элементов, подлежащих удалению
     * @return true, если очередь изменилась
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        int newSize = 0;
        // Фильтруем массив, оставляя только те элементы, которых нет в c
        for (int i = 0; i < size; i++) {
            if (!c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            }
        }
        boolean changed = (newSize != size);
        // Очищаем ссылки в хвосте, чтобы не мешать сборщику мусора
        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }
        size = newSize;
        if (changed) {
            heapify(); // перестраиваем кучу
        }
        return changed;
    }

    /**
     * Оставляет в очереди только элементы, присутствующие в заданной коллекции.
     * @param c коллекция с элементами, которые нужно сохранить
     * @return true, если очередь изменилась
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i])) {
                heap[newSize++] = heap[i];
            }
        }
        boolean changed = (newSize != size);
        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }
        size = newSize;
        if (changed) {
            heapify();
        }
        return changed;
    }

    // ========== Остальные методы интерфейсов Queue/Collection ==========

    /**
     * Удаляет один экземпляр указанного элемента из очереди, если он присутствует.
     * @param o элемент для удаления
     * @return true, если элемент был найден и удалён
     */
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null ? heap[i] == null : o.equals(heap[i])) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Возвращает итератор по элементам очереди в порядке их хранения в массиве
     * (не в порядке приоритетов). Итератор не поддерживает удаление.
     * @return итератор
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                return heap[cursor++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove() в итераторе не поддерживается");
            }
        };
    }

    /** Возвращает массив, содержащий все элементы очереди. */
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(heap, 0, result, 0, size);
        return result;
    }

    /**
     * Возвращает массив заданного типа, содержащий все элементы очереди.
     * Если переданный массив достаточно велик, элементы помещаются в него,
     * иначе создаётся новый массив того же типа.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // Создаём новый массив того же типа, что и a
            T[] newArray = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
            System.arraycopy(heap, 0, newArray, 0, size);
            return newArray;
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null; // стандартное поведение: следующий за последним элемент null
        }
        return a;
    }

    // ================== Приватные методы для работы с кучей ==================

    /** Проверяет, достаточно ли места в массиве, и при необходимости увеличивает его. */
    private void ensureCapacity() {
        if (size >= heap.length) {
            grow();
        }
    }

    /** Увеличивает размер массива. */
    @SuppressWarnings("unchecked")
    private void grow() {
        int oldCapacity = heap.length;
        // Стратегия роста: для маленьких массивов +2, для больших +50%
        int newCapacity = oldCapacity < 64 ? oldCapacity + 2 : oldCapacity + (oldCapacity >> 1);
        E[] newHeap = (E[]) new Object[newCapacity];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }

    /**
     * Перестраивает весь массив в кучу.
     * Используется после фильтрации в removeAll/retainAll.
     */
    private void heapify() {
        // Начинаем с последнего родителя (size/2 - 1) и просеиваем вниз
        for (int i = (size >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /**
     * Просеивание вверх: элемент на позиции k поднимается, пока не выполнится свойство кучи.
     */
    private void siftUp(int k) {
        E item = heap[k];
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            E parentVal = heap[parent];
            if (compare(item, parentVal) >= 0) {
                break; // свойство кучи восстановлено
            }
            heap[k] = parentVal;
            k = parent;
        }
        heap[k] = item;
    }

    /**
     * Просеивание вниз: элемент на позиции k опускается, пока не выполнится свойство кучи.
     */
    private void siftDown(int k) {
        E item = heap[k];
        int half = size >>> 1; // половина размера – первый индекс без потомков
        while (k < half) {
            int child = (k << 1) + 1; // левый потомок
            E childVal = heap[child];
            int right = child + 1;    // правый потомок
            // Если правый потомок существует и меньше левого, выбираем его
            if (right < size && compare(childVal, heap[right]) > 0) {
                child = right;
                childVal = heap[child];
            }
            // Если item не больше меньшего потомка, свойство кучи выполнено
            if (compare(item, childVal) <= 0) {
                break;
            }
            // Меняем местами с потомком
            heap[k] = childVal;
            k = child;
        }
        heap[k] = item;
    }

    /**
     * Удаляет элемент по заданному индексу и восстанавливает кучу.
     * @param i индекс удаляемого элемента
     * @return удалённый элемент
     */
    private E removeAt(int i) {
        E removed = heap[i];
        int s = --size;
        if (s == i) { // удалялся последний элемент
            heap[i] = null;
        } else {
            E moved = heap[s]; // берём последний элемент
            heap[s] = null;    // очищаем ссылку
            heap[i] = moved;   // ставим на место удалённого
            siftDown(i);       // пробуем просеять вниз
            // Если элемент не ушёл вниз, возможно, его нужно поднять вверх
            if (heap[i] == moved) {
                siftUp(i);
            }
        }
        return removed;
    }

    /**
     * Сравнивает два элемента с учётом компаратора или естественного порядка.
     * @param a первый элемент
     * @param b второй элемент
     * @return отрицательное число, 0 или положительное число (a < b, a == b, a > b)
     */
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        // Если компаратора нет, элементы должны реализовывать Comparable
        return ((Comparable<? super E>) a).compareTo(b);
    }
}