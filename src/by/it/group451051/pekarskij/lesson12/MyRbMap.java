package by.it.group451051.pekarskij.lesson12;

import java.util.SortedMap;
import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import java.util.Map;

public class MyRbMap implements SortedMap<Integer, String> {

    private static class Node {
        Integer key; String value; boolean red;
        Node left, right;
        Node(Integer k, String v, boolean r) { key = k; value = v; red = r; }
    }

    private Node root;
    private int size;

    private boolean isRed(Node n) { return n != null && n.red; }

    private Node rotateLeft(Node h) {
        Node x = h.right; h.right = x.left; x.left = h;
        x.red = h.red; h.red = true; return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left; h.left = x.right; x.right = h;
        x.red = h.red; h.red = true; return x;
    }

    private void flipColors(Node h) {
        h.red = !h.red; h.left.red = !h.left.red; h.right.red = !h.right.red;
    }

    private Node fixUp(Node h) {
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    private Node put(Node h, Integer key, String value) {
        if (h == null) { size++; return new Node(key, value, true); }
        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = put(h.left, key, value);
        else if (cmp > 0) h.right = put(h.right, key, value);
        else { h.value = value; return h; }
        return fixUp(h);
    }

    private Node get(Node h, Integer key) {
        if (h == null) return null;
        int cmp = key.compareTo(h.key);
        if (cmp < 0) return get(h.left, key);
        if (cmp > 0) return get(h.right, key);
        return h;
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) { h.right = rotateRight(h.right); h = rotateLeft(h); flipColors(h); }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) { h = rotateRight(h); flipColors(h); }
        return h;
    }

    private Node deleteMin(Node h) {
        if (h.left == null) return null;
        if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
        h.left = deleteMin(h.left);
        return fixUp(h);
    }

    private Node remove(Node h, Integer key) {
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
            h.left = remove(h.left, key);
        } else {
            if (isRed(h.left)) h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && h.right == null) { size--; return null; }
            if (!isRed(h.right) && !isRed(h.right.left)) h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                Node min = findMin(h.right);
                h.key = min.key; h.value = min.value;
                h.right = deleteMin(h.right); size--;
            } else h.right = remove(h.right, key);
        }
        return fixUp(h);
    }

    private Node findMin(Node h) {
        while (h.left != null) h = h.left; return h;
    }

    private Node findMax(Node h) {
        while (h.right != null) h = h.right; return h;
    }

    private void inorder(Node h, StringBuilder sb) {
        if (h == null) return;
        inorder(h.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(h.key).append("=").append(h.value);
        inorder(h.right, sb);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        return sb.append("}").toString();
    }

    @Override
    public int size() { return size; }
    @Override
    public boolean isEmpty() { return size == 0; }
    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        Node old = get(root, key);
        String prev = old == null ? null : old.value;
        root = put(root, key, value);
        root.red = false;
        return prev;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public String get(Object key) {
        if (key == null) return null;
        Node n = get(root, (Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public String remove(Object key) {
        if (key == null || root == null) return null;
        Node old = get(root, (Integer) key);
        if (old == null) return null;
        String prev = old.value;
        if (!isRed(root.left) && !isRed(root.right)) root.red = true;
        root = remove(root, (Integer) key);
        if (!isEmpty()) root.red = false;
        return prev;
    }

    @Override
    public boolean containsKey(Object key) { return get(root, (Integer) key) != null; }
    @Override
    public boolean containsValue(Object value) {
        if (value == null) return false;
        for (Node h = root; h != null; ) {
            if (value.equals(h.value)) return true;
            if (h.left != null) { Node p = h.left; while (p.right != null) p = p.right; h = p; }
            else { if (value.equals(h.value)) return true; h = h.right; }
        }
        return false;
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        return findMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        return findMax(root).key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MyRbMap sub = new MyRbMap();
        collectRange(root, sub, null, toKey);
        return sub;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MyRbMap sub = new MyRbMap();
        collectRange(root, sub, fromKey, null);
        return sub;
    }

    private void collectRange(Node h, MyRbMap sub, Integer lo, Integer hi) {
        if (h == null) return;
        if (lo == null || h.key.compareTo(lo) > 0) collectRange(h.left, sub, lo, hi);
        if ((lo == null || h.key.compareTo(lo) >= 0) && (hi == null || h.key.compareTo(hi) < 0)) {
            sub.put(h.key, h.value);
        }
        if (hi == null || h.key.compareTo(hi) < 0) collectRange(h.right, sub, lo, hi);
    }

    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public Set<Integer> keySet() { return null; }
    @Override
    public Collection<String> values() { return null; }
    @Override
    public Set<Map.Entry<Integer, String>> entrySet() { return null; }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
}