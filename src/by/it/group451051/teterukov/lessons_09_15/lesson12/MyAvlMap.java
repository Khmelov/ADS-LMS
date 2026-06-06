package by.it.group451051.teterukov.lessons_09_15.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int    key;
        String value;
        Node   left, right;
        int    height;

        Node(int key, String value) {
            this.key   = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int  size;

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int balance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private Node rotateRight(Node y) {
        Node x  = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left  = T2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y  = x.right;
        Node T2 = y.left;
        y.left  = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node rebalance(Node n) {
        updateHeight(n);
        int b = balance(n);

        if (b > 1) {
            if (balance(n.left) < 0)
                n.left = rotateLeft(n.left);
            return rotateRight(n);
        }
        if (b < -1) {
            if (balance(n.right) > 0)
                n.right = rotateRight(n.right);
            return rotateLeft(n);
        }
        return n;
    }

    private Node insert(Node n, int key, String value, String[] old) {
        if (n == null) {
            size++;
            return new Node(key, value);
        }
        if      (key < n.key) n.left  = insert(n.left,  key, value, old);
        else if (key > n.key) n.right = insert(n.right, key, value, old);
        else {
            old[0]  = n.value;
            n.value = value;
            return n;
        }
        return rebalance(n);
    }

    private Node minNode(Node n) {
        return n.left == null ? n : minNode(n.left);
    }

    private Node removeMin(Node n) {
        if (n.left == null) return n.right;
        n.left = removeMin(n.left);
        return rebalance(n);
    }

    private Node delete(Node n, int key, String[] removed) {
        if (n == null) return null;
        if      (key < n.key) n.left  = delete(n.left,  key, removed);
        else if (key > n.key) n.right = delete(n.right, key, removed);
        else {
            removed[0] = n.value;
            size--;
            if (n.right == null) return n.left;
            if (n.left  == null) return n.right;
            Node successor  = minNode(n.right);
            successor.right = removeMin(n.right);
            successor.left  = n.left;
            return rebalance(successor);
        }
        return rebalance(n);
    }

    private Node find(Node n, int key) {
        if (n == null)      return null;
        if (key < n.key)    return find(n.left,  key);
        if (key > n.key)    return find(n.right, key);
        return n;
    }

    @Override public int     size()    { return size; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String put(Integer key, String value) {
        String[] old = { null };
        root = insert(root, key, value, old);
        return old[0];
    }

    @Override
    public String remove(Object key) {
        String[] removed = { null };
        root = delete(root, (Integer) key, removed);
        return removed[0];
    }

    @Override
    public String get(Object key) {
        Node n = find(root, (Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(root, (Integer) key) != null;
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append("=").append(n.value);
        inOrder(n.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        return sb.append("}").toString();
    }

    @Override public boolean containsValue(Object value)              { throw new UnsupportedOperationException(); }
    @Override public void    putAll(Map<? extends Integer,
            ? extends String> m)              { throw new UnsupportedOperationException(); }
    @Override public Set<Integer>          keySet()                   { throw new UnsupportedOperationException(); }
    @Override public Collection<String>    values()                   { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet()           { throw new UnsupportedOperationException(); }
}