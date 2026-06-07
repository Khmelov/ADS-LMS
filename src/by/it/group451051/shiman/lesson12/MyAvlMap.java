package by.it.group451051.shiman.lesson12;

import java.util.*;

/**
 * Реализация Map на основе АВЛ-дерева.
 * Ключи сортируются в естественном порядке (Integer).
 * Не поддерживает null-ключи.
 * Реализованы все обязательные методы интерфейса Map.
 */
public class MyAvlMap implements Map<Integer, String> {

    // === Внутренний узел дерева ===
    private static class Node {
        Integer key;
        String value;
        Node left, right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    public MyAvlMap() {
        root = null;
        size = 0;
    }

    // === Вспомогательные методы для АВЛ ===
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node balance(Node node) {
        if (node == null) return null;
        updateHeight(node);
        int bf = balanceFactor(node);
        if (bf > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (bf < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    // === Основные методы (обязательные) ===

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Null keys not allowed");
        String oldValue = get(key);
        root = putRec(root, key, value);
        return oldValue;
    }

    private Node putRec(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = putRec(node.left, key, value);
        } else if (cmp > 0) {
            node.right = putRec(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }
        return balance(node);
    }

    @Override
    public String get(Object key) {
        if (key == null) throw new NullPointerException("Null keys not allowed");
        if (!(key instanceof Integer)) return null;
        Node node = getNode(root, (Integer) key);
        return node == null ? null : node.value;
    }

    private Node getNode(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) return getNode(node.left, key);
        else if (cmp > 0) return getNode(node.right, key);
        else return node;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException("Null keys not allowed");
        if (!(key instanceof Integer)) return false;
        return getNode(root, (Integer) key) != null;
    }

    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException("Null keys not allowed");
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        String oldValue = get(k);
        if (oldValue != null) {
            root = removeRec(root, k);
            size--;
        }
        return oldValue;
    }

    private Node removeRec(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = removeRec(node.left, key);
        } else if (cmp > 0) {
            node.right = removeRec(node.right, key);
        } else {
            if (node.left == null || node.right == null) {
                Node temp = (node.left != null) ? node.left : node.right;
                return temp;
            } else {
                Node successor = findMin(node.right);
                node.key = successor.key;
                node.value = successor.value;
                node.right = removeRec(node.right, successor.key);
            }
        }
        return balance(node);
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // === toString в порядке возрастания ключей ===
    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inorderToString(root, sb);
        sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inorderToString(Node node, StringBuilder sb) {
        if (node == null) return;
        inorderToString(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inorderToString(node.right, sb);
    }

    // === Реализация остальных методов интерфейса Map (минимально необходимые) ===

    // Простой стек на массиве для итераторов
    private static class SimpleStack<T> {
        private Object[] arr;
        private int top;
        SimpleStack(int capacity) {
            arr = new Object[capacity];
            top = -1;
        }
        void push(T item) {
            if (top + 1 == arr.length) {
                Object[] newArr = new Object[arr.length * 2];
                System.arraycopy(arr, 0, newArr, 0, arr.length);
                arr = newArr;
            }
            arr[++top] = item;
        }
        @SuppressWarnings("unchecked")
        T pop() {
            return (T) arr[top--];
        }
        boolean isEmpty() {
            return top == -1;
        }
        @SuppressWarnings("unchecked")
        T peek() {
            return (T) arr[top];
        }
    }

    // Итератор по ключам в порядке возрастания
    private class KeyIterator implements Iterator<Integer> {
        private final SimpleStack<Node> stack = new SimpleStack<>(height(root) + 1);
        KeyIterator() {
            pushLeft(root);
        }
        private void pushLeft(Node node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        @Override
        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node node = stack.pop();
            pushLeft(node.right);
            return node.key;
        }
    }

    // Итератор по значениям
    private class ValueIterator implements Iterator<String> {
        private final KeyIterator keyIt = new KeyIterator();
        @Override
        public boolean hasNext() {
            return keyIt.hasNext();
        }
        @Override
        public String next() {
            return get(keyIt.next());
        }
    }

    // Итератор по записям
    private class EntryIterator implements Iterator<Map.Entry<Integer, String>> {
        private final KeyIterator keyIt = new KeyIterator();
        @Override
        public boolean hasNext() {
            return keyIt.hasNext();
        }
        @Override
        public Map.Entry<Integer, String> next() {
            Integer key = keyIt.next();
            String val = get(key);
            return new SimpleEntry(key, val);
        }
    }

    // Простая реализация Entry
    private static class SimpleEntry implements Map.Entry<Integer, String> {
        private final Integer key;
        private String value;
        SimpleEntry(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
        @Override
        public Integer getKey() { return key; }
        @Override
        public String getValue() { return value; }
        @Override
        public String setValue(String value) {
            String old = this.value;
            this.value = value;
            return old;
        }
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>) o;
            return eq(key, e.getKey()) && eq(value, e.getValue());
        }
        @Override
        public int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }
    }

    // Сравнение на равенство без использования java.util.Objects
    private static boolean eq(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    // Реализация keySet
    @Override
    public Set<Integer> keySet() {
        return new KeySet();
    }

    private class KeySet extends AbstractSet<Integer> {
        @Override
        public Iterator<Integer> iterator() { return new KeyIterator(); }
        @Override
        public int size() { return size; }
        @Override
        public boolean contains(Object o) { return containsKey(o); }
        @Override
        public void clear() { MyAvlMap.this.clear(); }
        @Override
        public boolean remove(Object o) {
            if (containsKey(o)) {
                MyAvlMap.this.remove(o);
                return true;
            }
            return false;
        }
    }

    // Реализация values
    @Override
    public Collection<String> values() {
        return new ValueCollection();
    }

    private class ValueCollection extends AbstractCollection<String> {
        @Override
        public Iterator<String> iterator() { return new ValueIterator(); }
        @Override
        public int size() { return size; }
        @Override
        public void clear() { MyAvlMap.this.clear(); }
        @Override
        public boolean contains(Object o) {
            return containsValue(o);
        }
        @Override
        public boolean remove(Object o) {
            Iterator<Map.Entry<Integer, String>> it = entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, String> e = it.next();
                if (eq(e.getValue(), o)) {
                    MyAvlMap.this.remove(e.getKey());
                    return true;
                }
            }
            return false;
        }
    }

    // Реализация entrySet
    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        return new EntrySet();
    }

    private class EntrySet extends AbstractSet<Map.Entry<Integer, String>> {
        @Override
        public Iterator<Map.Entry<Integer, String>> iterator() { return new EntryIterator(); }
        @Override
        public int size() { return size; }
        @Override
        public void clear() { MyAvlMap.this.clear(); }
        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>) o;
            if (!(e.getKey() instanceof Integer)) return false;
            Integer key = (Integer) e.getKey();
            String val = get(key);
            return val != null && eq(val, e.getValue());
        }
        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>) o;
            if (!(e.getKey() instanceof Integer)) return false;
            Integer key = (Integer) e.getKey();
            if (containsKey(key) && eq(get(key), e.getValue())) {
                MyAvlMap.this.remove(key);
                return true;
            }
            return false;
        }
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public boolean containsValue(Object value) {
        Iterator<String> it = values().iterator();
        while (it.hasNext()) {
            if (eq(it.next(), value)) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Map)) return false;
        Map<?,?> other = (Map<?,?>) o;
        if (other.size() != size()) return false;
        try {
            for (Map.Entry<Integer, String> e : entrySet()) {
                String otherVal = (String) other.get(e.getKey());
                if (!eq(e.getValue(), otherVal)) return false;
            }
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (Map.Entry<Integer, String> e : entrySet()) {
            h += e.hashCode();
        }
        return h;
    }
}

// Вспомогательные абстрактные классы, реализующие базовое поведение коллекций
abstract class AbstractCollection<E> implements Collection<E> {
    public abstract Iterator<E> iterator();
    public abstract int size();
    public boolean isEmpty() { return size() == 0; }
    public boolean contains(Object o) {
        Iterator<E> it = iterator();
        while (it.hasNext()) if (eq(it.next(), o)) return true;
        return false;
    }
    public Object[] toArray() {
        Object[] arr = new Object[size()];
        int i = 0;
        for (E e : this) arr[i++] = e;
        return arr;
    }
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size()) a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size());
        int i = 0;
        for (E e : this) a[i++] = (T) e;
        if (a.length > size()) a[size()] = null;
        return a;
    }
    public boolean add(E e) { throw new UnsupportedOperationException(); }
    public boolean remove(Object o) { throw new UnsupportedOperationException(); }
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) if (!contains(o)) return false;
        return true;
    }
    public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
    public void clear() { throw new UnsupportedOperationException(); }

    private static boolean eq(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }
}

abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;
        Set<?> s = (Set<?>) o;
        if (s.size() != size()) return false;
        return containsAll(s);
    }
    public int hashCode() {
        int h = 0;
        for (E e : this) h += (e == null ? 0 : e.hashCode());
        return h;
    }
}