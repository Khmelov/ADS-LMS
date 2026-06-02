package by.it.group451051.zhelubovskaya.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left;
        Node right;
        int height;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size = 0;

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private void updateHeight(Node n) {
        if (n != null) {
            n.height = 1 + Math.max(height(n.left), height(n.right));
        }
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

    private Node balance(Node n) {
        if (n == null) return null;
        updateHeight(n);
        int bf = balanceFactor(n);
        if (bf > 1) {
            if (balanceFactor(n.left) < 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        if (bf < -1) {
            if (balanceFactor(n.right) > 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        }
        return n;
    }

    private Node put(Node n, int key, String value) {
        if (n == null) {
            size++;
            return new Node(key, value);
        }
        if (key < n.key) {
            n.left = put(n.left, key, value);
        } else if (key > n.key) {
            n.right = put(n.right, key, value);
        } else {
            n.value = value;
            return n;
        }
        return balance(n);
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) return null;
        String old = get(key);
        root = put(root, key, value);
        return old;
    }

    private Node minNode(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node removeNode(Node n, int key) {
        if (n == null) return null;
        if (key < n.key) {
            n.left = removeNode(n.left, key);
        } else if (key > n.key) {
            n.right = removeNode(n.right, key);
        } else {
            if (n.left == null || n.right == null) {
                Node child = (n.left != null) ? n.left : n.right;
                if (child == null) {
                    size--;
                    return null;
                } else {
                    size--;
                    return child;
                }
            } else {
                Node successor = minNode(n.right);
                n.key = successor.key;
                n.value = successor.value;
                n.right = removeNode(n.right, successor.key);
                return balance(n);
            }
        }
        return balance(n);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        String old = get(k);
        if (old == null) return null;
        root = removeNode(root, k);
        return old;
    }
    
    private String get(Node n, int key) {
        if (n == null) return null;
        if (key < n.key) return get(n.left, key);
        if (key > n.key) return get(n.right, key);
        return n.value;
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

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append("=").append(n.value);
        inOrder(n.right, sb);
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    // Заглушки
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