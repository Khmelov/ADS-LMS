package by.it.group451051.sinenko.lesson11;

import java.util.*;

public class MyTreeSet<E> implements Set<E>  {
    // узел бинарного дерева
    private static class Node<E> {
        E item;
        Node<E> left;
        Node<E> right;
        
        Node(E item) {
            this.item = item;
            left = null;
            right = null;
        }
    }

    private Node<E> root; // корень дерева
    private int size;     // количество элементов

    public MyTreeSet() {
        root = null;
        size = 0;
    }

     // Возвращает количество элементов
    @Override
    public int size() {
        return size;
    }

    // Проверка пусто ли дерево
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Очистка дерева
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // Добавляет элемент
    @Override
    public boolean add(E e) {
        if (contains(e)) return false;
        
        root = addRecursive(root, e);
        size++;
        return true;
    }

    // Рекурсивное добавление 
    @SuppressWarnings("unchecked")
    private Node<E> addRecursive(Node<E> node, E e) {
        if (node == null) {
            return new Node<>(e);
        }
        
        Comparable<? super E> comp = (Comparable<? super E>) e;
        if (comp.compareTo(node.item) < 0) {
            node.left = addRecursive(node.left, e);
        } else if (comp.compareTo(node.item) > 0) {
            node.right = addRecursive(node.right, e);
        }
        
        return node;
    }

    // Удаляет элемент
    @Override
    public boolean remove(Object o) {
        if (!contains(o)) return false;
        
        root = removeRecursive(root, o);
        size--;
        return true;
    }

    // Рекурсивное удаление
    @SuppressWarnings("unchecked")
    private Node<E> removeRecursive(Node<E> node, Object o) {
        if (node == null) return null;
        
        Comparable<? super E> comp = (Comparable<? super E>) o;
        if (comp.compareTo(node.item) < 0) {
            node.left = removeRecursive(node.left, o);
        } else if (comp.compareTo(node.item) > 0) {
            node.right = removeRecursive(node.right, o);
        } else {
            // Нашли узел для удаления
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            // Узел с двумя детьми: находим минимальный в правом поддереве
            Node<E> minNode = findMin(node.right);
            node.item = minNode.item;
            node.right = removeRecursive(node.right, minNode.item);
        }
        
        return node;
    }

    // Находит минимальный элемент в дереве
    private Node<E> findMin(Node<E> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Проверяет наличие элемента рекурсивно
    @Override
    public boolean contains(Object o) {
        return containsRecursive(root, o);
    }

    // Рекурсивный поиск
    @SuppressWarnings("unchecked")
    private boolean containsRecursive(Node<E> node, Object o) {
        if (node == null) return false;
        
        Comparable<? super E> comp = (Comparable<? super E>) o;
        if (comp.compareTo(node.item) < 0) {
            return containsRecursive(node.left, o);
        } else if (comp.compareTo(node.item) > 0) {
            return containsRecursive(node.right, o);
        } else {
            return true;
        }
    }

    // Вывод элементов в отсортированном порядке
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        inOrderTraversal(root, sb);
        sb.append("]");
        return sb.toString();
    }

    private void inOrderTraversal(Node<E> node, StringBuilder sb) {
        if (node == null) return;
        
        inOrderTraversal(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.item);
        inOrderTraversal(node.right, sb);
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        fillArray(root, result, 0);
        return result;
    }

    private int fillArray(Node<E> node, Object[] arr, int index) {
        if (node == null) return index;
        index = fillArray(node.left, arr, index);
        arr[index++] = node.item;
        index = fillArray(node.right, arr, index);
        return index;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(toArray(), size, a.getClass());
        }
        System.arraycopy(toArray(), 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E item : c) {
            if (add(item)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object item : c) {
            if (remove(item)) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        List<E> toRemove = new ArrayList<>();
        for (E item : this) {
            if (!c.contains(item)) {
                toRemove.add(item);
            }
        }
        for (E item : toRemove) {
            remove(item);
            changed = true;
        }
        return changed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object item : c) {
            if (!contains(item)) return false;
        }
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
        private Object[] elements = toArray();
        private int current = 0;
        
        @Override
        public boolean hasNext() {
            return current < elements.length;
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (E) elements[current++];
        }
      };
    }
}

