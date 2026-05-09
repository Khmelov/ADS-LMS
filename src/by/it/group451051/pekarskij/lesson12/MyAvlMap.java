package by.it.group451051.pekarskij.lesson12;

import java.util.Map;
import java.util.Collection;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key; String value;
        Node left, right; int height;
        Node(Integer k, String v) { key = k; value = v; height = 1; }
    }

    private Node root;
    private int size;

    private int height(Node n) { return n == null ? 0 : n.height; }

    private int getBalance(Node n) { return n == null ? 0 : height(n.left) - height(n.right); }

    private void updateHeight(Node n) { n.height = 1 + Math.max(height(n.left), height(n.right)); }

    private Node rotateRight(Node y) {
        Node x = y.left; Node t2 = x.right;
        x.right = y; y.left = t2;
        updateHeight(y); updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right; Node t2 = y.left;
        y.left = x; x.right = t2;
        updateHeight(x); updateHeight(y);
        return y;
    }

    private Node rebalance(Node n) {
        updateHeight(n);
        int bal = getBalance(n);
        if (bal > 1) {
            if (getBalance(n.left) < 0) n.left = rotateLeft(n.left);
            return rotateRight(n);
        }
        if (bal < -1) {
            if (getBalance(n.right) > 0) n.right = rotateRight(n.right);
            return rotateLeft(n);
        }
        return n;
    }

    private Node put(Node n, Integer key, String value) {
        if (n == null) { size++; return new Node(key, value); }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) n.left = put(n.left, key, value);
        else if (cmp > 0) n.right = put(n.right, key, value);
        else { n.value = value; return n; }
        return rebalance(n);
    }

    private Node get(Node n, Integer key) {
        if (n == null) return null;
        int cmp = key.compareTo(n.key);
        if (cmp < 0) return get(n.left, key);
        if (cmp > 0) return get(n.right, key);
        return n;
    }

    private Node findMin(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node remove(Node n, Integer key, boolean[] found) {
        if (n == null) return null;
        int cmp = key.compareTo(n.key);
        if (cmp < 0) n.left = remove(n.left, key, found);
        else if (cmp > 0) n.right = remove(n.right, key, found);
        else {
            found[0] = true;
            if (n.left == null) return n.right;
            if (n.right == null) return n.left;
            Node min = findMin(n.right);
            n.key = min.key; n.value = min.value;
            n.right = remove(n.right, min.key, found);
        }
        return rebalance(n);
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;
        inorder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append("=").append(n.value);
        inorder(n.right, sb);
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
        return prev;
    }

    @Override
    public String get(Object key) {
        if (key == null) return null;
        Node n = get(root, (Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public String remove(Object key) {
        if (key == null) return null;
        Node old = get(root, (Integer) key);
        if (old == null) return null;
        String prev = old.value;
        boolean[] found = {false};
        root = remove(root, (Integer) key, found);
        if (found[0]) size--;
        return prev;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(root, (Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) { return false; }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {}
    @Override
    public Set<Integer> keySet() { return null; }
    @Override
    public Collection<String> values() { return null; }
    @Override
    public Set<Entry<Integer, String>> entrySet() { return null; }
}