package by.it.group451051.markhel.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    // ========== Вспомогательные методы для AVL ==========

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
        if (bf > 1) { // левое поддерево выше
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        } else if (bf < -1) { // правое поддерево выше
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    // ========== Основные методы Map ==========

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        Node existing = getNode(root, key);
        String oldValue = existing == null ? null : existing.value;
        root = putRec(root, key, value);
        if (existing == null) size++;
        return oldValue;
    }

    private Node putRec(Node node, Integer key, String value) {
        if (node == null) return new Node(key, value);
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = putRec(node.left, key, value);
        } else if (cmp > 0) {
            node.right = putRec(node.right, key, value);
        } else {
            node.value = value; // обновление значения
        }
        return balance(node);
    }

    @Override
    public String get(Object key) {
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
        if (!(key instanceof Integer)) return false;
        return getNode(root, (Integer) key) != null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node node = getNode(root, k);
        if (node == null) return null;
        String oldValue = node.value;
        root = removeRec(root, k);
        size--;
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
            // узел найден
            if (node.left == null || node.right == null) {
                // 0 или 1 ребёнок
                Node temp = (node.left != null) ? node.left : node.right;
                return temp;
            } else {
                // два ребёнка: ищем преемника (минимальный в правом поддереве)
                Node successor = minNode(node.right);
                node.key = successor.key;
                node.value = successor.value;
                node.right = removeRec(node.right, successor.key);
            }
        }
        return balance(node);
    }

    private Node minNode(Node node) {
        Node current = node;
        while (current.left != null) current = current.left;
        return current;
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

    // ========== toString ==========

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inorderToString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inorderToString(Node node, StringBuilder sb) {
        if (node == null) return;
        inorderToString(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        inorderToString(node.right, sb);
    }

    // ========== Реализация остальных методов интерфейса Map (необязательные для задания) ==========

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValueRec(node.left, value) || containsValueRec(node.right, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
