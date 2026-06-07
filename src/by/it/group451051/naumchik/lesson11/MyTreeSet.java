package by.it.group451051.naumchik.lesson11;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.lang.reflect.Array;

/**
 * Реализация Set<E> на основе отсортированного массива.
 * Элементы хранятся в порядке возрастания, дубликаты запрещены.
 * Не использует стандартные классы коллекций (кроме интерфейсов).
 *
 * @param <E> тип элементов, должен быть сравнимым (Comparable) или через Comparator
 */
public class MyTreeSet<E> implements Set<E> {

    // Начальная ёмкость массива
    private static final int DEFAULT_CAPACITY = 10;

    // Массив для хранения элементов в отсортированном порядке
    private E[] data;

    // Текущее количество элементов
    private int size;

    // Компаратор (null означает использование естественного порядка Comparable)
    private final Comparator<? super E> comparator;

    // --------------------- Конструкторы ---------------------

    /** Создаёт пустое множество с естественным порядком элементов. */
    public MyTreeSet() {
        this(null);
    }

    /**
     * Создаёт пустое множество с заданным компаратором.
     * @param comparator компаратор для упорядочивания (null – Comparable)
     */
    @SuppressWarnings("unchecked")
    public MyTreeSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.data = (E[]) new Object[DEFAULT_CAPACITY];
    }

    // ================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ==================

    /**
     * Возвращает строковое представление множества в порядке возрастания.
     * Формат: [элемент1, элемент2, ...]
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
            // Безопасная обработка случая, когда элемент — сама коллекция
            sb.append(data[i] == this ? "(this Collection)" : data[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    /** Количество элементов в множестве. */
    @Override
    public int size() {
        return size;
    }

    /** Удаляет все элементы. */
    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /** Проверяет, пусто ли множество. */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Добавляет элемент в множество, если он отсутствовал.
     * @param element добавляемый элемент (не null)
     * @return true, если элемент был добавлен
     * @throws NullPointerException если element == null (недопустим)
     */
    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException("Множество не поддерживает null");
        }
        int pos = findInsertionPoint(element);
        // Если нашли точно такой же элемент (pos >= 0), не добавляем
        if (pos >= 0) {
            return false;
        }
        int insertIndex = -pos - 1;
        ensureCapacity();
        // Сдвигаем элементы вправо, освобождая место
        if (insertIndex < size) {
            System.arraycopy(data, insertIndex, data, insertIndex + 1, size - insertIndex);
        }
        data[insertIndex] = element;
        size++;
        return true;
    }

    /**
     * Удаляет один экземпляр элемента из множества, если он присутствует.
     * @param o удаляемый элемент (может быть null, но в множестве null нет)
     * @return true, если элемент был найден и удалён
     */
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false; // null не храним
        }
        int pos = findInsertionPoint((E) o); // Поиск точного совпадения
        if (pos < 0) {
            return false;
        }
        // Сдвигаем элементы влево
        if (pos < size - 1) {
            System.arraycopy(data, pos + 1, data, pos, size - pos - 1);
        }
        data[--size] = null; // зачищаем ссылку
        return true;
    }

    /**
     * Проверяет наличие элемента в множестве.
     * @param o искомый элемент (может быть null)
     * @return true, если элемент присутствует
     */
    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        return findInsertionPoint((E) o) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
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
        // Проходим по текущему массиву с конца, чтобы индексы не сбивались при удалении
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(data[i])) {
                remove(data[i]);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(data[i])) {
                remove(data[i]);
                modified = true;
            }
        }
        return modified;
    }

    // ========== ОСТАЛЬНЫЕ МЕТОДЫ ИНТЕРФЕЙСА SET / COLLECTION ==========

    /**
     * Итератор в порядке возрастания.
     * Метод remove() не поддерживается.
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
                return data[cursor++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove не поддерживается");
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(data, 0, result, 0, size);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            T[] newArray = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
            System.arraycopy(data, 0, newArray, 0, size);
            return newArray;
        }
        System.arraycopy(data, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    // ================== ПРИВАТНЫЕ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==================

    /** Расширяет внутренний массив, если он полностью заполнен. */
    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size == data.length) {
            int newCapacity = data.length + (data.length >> 1) + 1; // рост ≈ 1.5x
            E[] newData = (E[]) new Object[newCapacity];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
    }

    /**
     * Ищет индекс элемента (или точку вставки) с использованием бинарного поиска.
     * @param element элемент для поиска
     * @return если найден – индекс >= 0;
     *         если не найден – отрицательное число = -(insertionPoint) - 1,
     *         где insertionPoint – индекс, куда нужно вставить элемент для сохранения порядка.
     */
    private int findInsertionPoint(E element) {
        int low = 0;
        int high = size - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compare(element, data[mid]);
            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid; // точное совпадение
            }
        }
        // Не найдено: low – это место вставки
        return -(low + 1);
    }

    /**
     * Сравнивает два элемента с использованием компаратора или естественного порядка.
     * @throws ClassCastException если типы несопоставимы
     */
    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        }
        return ((Comparable<? super E>) a).compareTo(b);
    }
}
