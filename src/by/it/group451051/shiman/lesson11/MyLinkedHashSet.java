package by.it.group451051.shiman.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Реализация хеш-множества с сохранением порядка добавления элементов.
 * На основе массива с односвязными списками для коллизий и двусвязного списка для порядка.
 *
 * @param <E> тип элементов множества
 */
public class MyLinkedHashSet<E> implements Set<E> {

    // === Узел, участвующий и в хеш-таблице, и в двусвязном списке порядка ===
    private static class Node<E> {
        final E value;
        Node<E> nextInBucket;  // связь для разрешения коллизий
        Node<E> prevOrder;     // предыдущий в порядке добавления
        Node<E> nextOrder;     // следующий в порядке добавления

        Node(E value, Node<E> nextInBucket, Node<E> prevOrder, Node<E> nextOrder) {
            this.value = value;
            this.nextInBucket = nextInBucket;
            this.prevOrder = prevOrder;
            this.nextOrder = nextOrder;
        }
    }

    // === Поля ===
    private Node<E>[] buckets;          // массив корзин
    private int size;                   // количество элементов
    private int capacity;               // текущая ёмкость массива
    private final float loadFactor;     // коэффициент загрузки
    private int threshold;              // предельный размер для расширения
    private Node<E> head;               // первый элемент в порядке добавления
    private Node<E> tail;               // последний элемент в порядке добавления

    // === Конструкторы ===
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        this.capacity = 16;
        this.loadFactor = 0.75f;
        this.threshold = (int) (capacity * loadFactor);
        this.buckets = (Node<E>[]) new Node[capacity];
        this.size = 0;
        this.head = this.tail = null;
    }

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Неверная начальная ёмкость");
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) throw new IllegalArgumentException("Неверный loadFactor");
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.threshold = (int) (capacity * loadFactor);
        this.buckets = (Node<E>[]) new Node[capacity];
        this.size = 0;
        this.head = this.tail = null;
    }

    public MyLinkedHashSet(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    // === Вспомогательные методы хеширования ===
    private int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    private int indexFor(int hash) {
        return (hash & 0x7FFFFFFF) % capacity;
    }

    // Поиск узла по значению (без учёта порядка)
    private Node<E> findNode(Object o) {
        int index = indexFor(hash(o));
        Node<E> current = buckets[index];
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                return current;
            }
            current = current.nextInBucket;
        }
        return null;
    }

    // Удаление узла из структуры (из корзины и из списка порядка)
    private void removeNode(Node<E> node) {
        // 1. Удаление из односвязного списка корзины
        int index = indexFor(hash(node.value));
        Node<E> prev = null;
        Node<E> curr = buckets[index];
        while (curr != null) {
            if (curr == node) {
                if (prev == null) {
                    buckets[index] = curr.nextInBucket;
                } else {
                    prev.nextInBucket = curr.nextInBucket;
                }
                break;
            }
            prev = curr;
            curr = curr.nextInBucket;
        }

        // 2. Удаление из двусвязного списка порядка
        if (node.prevOrder != null) {
            node.prevOrder.nextOrder = node.nextOrder;
        } else {
            head = node.nextOrder; // удаляем голову
        }
        if (node.nextOrder != null) {
            node.nextOrder.prevOrder = node.prevOrder;
        } else {
            tail = node.prevOrder; // удаляем хвост
        }

        size--;
    }

    // Расширение и перехеширование
    @SuppressWarnings("unchecked")
    private void resize() {
        if (size < threshold) return;
        int oldCapacity = capacity;
        capacity *= 2;
        threshold = (int) (capacity * loadFactor);
        Node<E>[] oldBuckets = buckets;
        buckets = (Node<E>[]) new Node[capacity];

        // Проходим по порядку добавления и перехешируем каждый узел
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.nextOrder; // сохраняем следующий по порядку
            int newIndex = indexFor(hash(current.value));
            // вставка в начало списка корзины
            current.nextInBucket = buckets[newIndex];
            buckets[newIndex] = current;
            current = next;
        }
    }

    // === Методы интерфейса Set ===

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return findNode(o) != null;
    }

    @Override
    public boolean add(E e) {
        if (size + 1 > threshold) {
            resize();
        }
        if (findNode(e) != null) {
            return false; // элемент уже есть
        }
        int index = indexFor(hash(e));
        // Создаём новый узел. В корзину - в начало. В порядок - в конец.
        Node<E> newNode = new Node<>(e, buckets[index], tail, null);
        buckets[index] = newNode;
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.nextOrder = newNode;
            tail = newNode;
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Node<E> node = findNode(o);
        if (node == null) return false;
        removeNode(node);
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            buckets[i] = null;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (!c.contains(e)) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    // === Итератор, обходящий элементы в порядке добавления ===
    private class LinkedHashSetIterator implements Iterator<E> {
        private Node<E> current = head;
        private Node<E> lastReturned = null;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            lastReturned = current;
            E value = current.value;
            current = current.nextOrder;
            return value;
        }

        @Override
        public void remove() {
            if (lastReturned == null) throw new IllegalStateException();
            removeNode(lastReturned);
            lastReturned = null;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedHashSetIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (E e : this) {
            result[i++] = e;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int i = 0;
        for (E e : this) {
            a[i++] = (T) e;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    // === equals и hashCode, как требует интерфейс Set ===
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;
        Set<?> other = (Set<?>) o;
        if (other.size() != size()) return false;
        return containsAll(other);
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (E e : this) {
            if (e != null) h += e.hashCode();
        }
        return h;
    }

    // === toString в порядке добавления, в квадратных скобках через ", " ===
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> node = head;
        while (node != null) {
            sb.append(node.value == this ? "(this Collection)" : node.value);
            node = node.nextOrder;
            if (node != null) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}