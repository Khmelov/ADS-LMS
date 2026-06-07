package by.it.group451051.shiman.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Реализация хеш-множества на основе массива с односвязными списками для разрешения коллизий.
 * Без использования каких-либо классов стандартной библиотеки, кроме интерфейсов и базовых.
 *
 * @param <E> тип элементов множества
 */
public class MyHashSet<E> implements Set<E> {

    // === Внутренний узел односвязного списка ===
    private static class Node<E> {
        final E value;
        Node<E> next;

        Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }
    }

    // === Поля ===
    private Node<E>[] buckets;      // массив корзин (голов списков)
    private int size;               // количество элементов
    private int capacity;           // текущая ёмкость массива
    private final float loadFactor; // коэффициент загрузки
    private int threshold;          // предельный размер, после которого расширяемся

    // === Конструкторы ===
    @SuppressWarnings("unchecked")
    public MyHashSet() {
        this.capacity = 16;
        this.loadFactor = 0.75f;
        this.threshold = (int) (capacity * loadFactor);
        this.buckets = (Node<E>[]) new Node[capacity];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyHashSet(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Неверная начальная ёмкость");
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) throw new IllegalArgumentException("Неверный loadFactor");
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.threshold = (int) (capacity * loadFactor);
        this.buckets = (Node<E>[]) new Node[capacity];
        this.size = 0;
    }

    public MyHashSet(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    // === Вспомогательные методы ===
    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int indexFor(int hash) {
        return (hash & 0x7FFFFFFF) % capacity;
    }

    private Node<E> findNode(int bucketIndex, Object value) {
        Node<E> current = buckets[bucketIndex];
        while (current != null) {
            if (value == null ? current.value == null : value.equals(current.value)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        if (size < threshold) return;
        int oldCapacity = capacity;
        capacity = oldCapacity * 2;
        threshold = (int) (capacity * loadFactor);
        Node<E>[] oldBuckets = buckets;
        buckets = (Node<E>[]) new Node[capacity];

        for (Node<E> head : oldBuckets) {
            Node<E> current = head;
            while (current != null) {
                Node<E> next = current.next;
                int newIndex = indexFor(hash(current.value));
                current.next = buckets[newIndex];
                buckets[newIndex] = current;
                current = next;
            }
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
        int index = indexFor(hash(o));
        return findNode(index, o) != null;
    }

    @Override
    public boolean add(E e) {
        if (size + 1 > threshold) {
            resize();
        }
        int index = indexFor(hash(e));
        Node<E> existing = findNode(index, e);
        if (existing != null) {
            return false;
        }
        buckets[index] = new Node<>(e, buckets[index]);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexFor(hash(o));
        Node<E> prev = null;
        Node<E> current = buckets[index];
        while (current != null) {
            if (o == null ? current.value == null : o.equals(current.value)) {
                if (prev == null) {
                    buckets[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            buckets[i] = null;
        }
        size = 0;
    }

    // === Итератор ===
    private class MyHashSetIterator implements Iterator<E> {
        private int currentBucket = 0;
        private Node<E> currentNode = null;
        private int remaining = size;

        MyHashSetIterator() {
            advanceToNextBucket();
        }

        private void advanceToNextBucket() {
            while (currentBucket < capacity && buckets[currentBucket] == null) {
                currentBucket++;
            }
            if (currentBucket < capacity) {
                currentNode = buckets[currentBucket];
            } else {
                currentNode = null;
            }
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public E next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            E value = currentNode.value;
            currentNode = currentNode.next;
            if (currentNode == null) {
                currentBucket++;
                advanceToNextBucket();
            }
            remaining--;
            return value;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new MyHashSetIterator();
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

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        MyHashSet<E> toRemove = new MyHashSet<>();
        for (E e : this) {
            if (!c.contains(e)) {
                toRemove.add(e);
            }
        }
        for (E e : toRemove) {
            if (remove(e)) modified = true;
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
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

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

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}