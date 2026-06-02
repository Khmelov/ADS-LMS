package by.it.group451051.zhelubovskaya.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        int key;
        String value;
        Node left, right;
        boolean color;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.color = RED;
        }
    }

    private Node root;
    private int size = 0;

    private boolean isRed(Node x) {
        return x != null && x.color == RED;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = RED;
        if (h.left != null) h.left.color = BLACK;
        if (h.right != null) h.right.color = BLACK;
    }

    private Node balance(Node h) {
        if (h == null) return null;
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    private Node put(Node h, int key, String value) {
        if (h == null) {
            size++;
            return new Node(key, value);
        }
        if (key < h.key) h.left = put(h.left, key, value);
        else if (key > h.key) h.right = put(h.right, key, value);
        else h.value = value;
        return balance(h);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) return null;
        String old = get(key);
        root = put(root, key, value);
        if (root != null) root.color = BLACK;
        return old;
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (h.right != null && isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (h.left != null && isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private Node min(Node x) {
        while (x.left != null) x = x.left;
        return x;
    }

    private Node removeMin(Node h) {
        if (h.left == null) {
            size--;
            return null;
        }
        if (!isRed(h.left) && h.left != null && !isRed(h.left.left)) {
            h = moveRedLeft(h);
        }
        h.left = removeMin(h.left);
        return balance(h);
    }

    private Node remove(Node h, int key) {
        if (h == null) return null;
        if (key < h.key) {
            if (h.left != null && !isRed(h.left) && !isRed(h.left.left)) {
                h = moveRedLeft(h);
            }
            h.left = remove(h.left, key);
        } else {
            if (isRed(h.left)) h = rotateRight(h);
            if (key == h.key && h.right == null) {
                size--;
                return null;
            }
            if (h.right != null && !isRed(h.right) && !isRed(h.right.left)) {
                h = moveRedRight(h);
            }
            if (key == h.key) {
                Node x = min(h.right);
                h.key = x.key;
                h.value = x.value;
                h.right = removeMin(h.right);
            } else {
                h.right = remove(h.right, key);
            }
        }
        return balance(h);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        String old = get(k);
        if (old == null) return null;
        if (root != null && !isRed(root.left) && !isRed(root.right)) {
            root.color = RED;
        }
        root = remove(root, k);
        if (root != null) root.color = BLACK;
        return old;
    }

    private String get(Node x, int key) {
        while (x != null) {
            if (key < x.key) x = x.left;
            else if (key > x.key) x = x.right;
            else return x.value;
        }
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        return get(root, (Integer) key);
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    private boolean containsValue(Node x, Object value) {
        if (x == null) return false;
        if (Objects.equals(x.value, value)) return true;
        return containsValue(x.left, value) || containsValue(x.right, value);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
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

    @Override
    public Integer firstKey() {
        if (root == null) return null;
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node x = root;
        while (x.right != null) x = x.right;
        return x.key;
    }

    private void headMap(Node x, int toKey, MyRbMap result) {
        if (x == null) return;
        headMap(x.left, toKey, result);
        if (x.key < toKey) {
            result.put(x.key, x.value);
        }
        headMap(x.right, toKey, result);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMap(root, toKey, result);
        return result;
    }

    private void tailMap(Node x, int fromKey, MyRbMap result) {
        if (x == null) return;
        tailMap(x.left, fromKey, result);
        if (x.key >= fromKey) {
            result.put(x.key, x.value);
        }
        tailMap(x.right, fromKey, result);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void inOrder(Node x, StringBuilder sb) {
        if (x == null) return;
        inOrder(x.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(x.key).append("=").append(x.value);
        inOrder(x.right, sb);
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public Comparator<? super Integer> comparator() { return null; }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override
    public Set<Integer> keySet() { return null; }
    @Override
    public Collection<String> values() { return null; }
    @Override
    public Set<Entry<Integer, String>> entrySet() { return null; }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {}
}