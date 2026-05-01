package by.it.group451052.nasonova.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        int height;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;
    private String removedValue;

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void fixHeight(Node node) {
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);
        node.height = (leftHeight > rightHeight ? leftHeight : rightHeight) + 1;
    }

    private int balanceFactor(Node node) {
        return height(node.right) - height(node.left);
    }

    private Node rotateRight(Node p) {
        Node q = p.left;
        p.left = q.right;
        q.right = p;
        fixHeight(p);
        fixHeight(q);
        return q;
    }

    private Node rotateLeft(Node q) {
        Node p = q.right;
        q.right = p.left;
        p.left = q;
        fixHeight(q);
        fixHeight(p);
        return p;
    }

    private Node balance(Node node) {
        fixHeight(node);

        if (balanceFactor(node) == 2) {
            if (balanceFactor(node.right) < 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        if (balanceFactor(node) == -2) {
            if (balanceFactor(node.left) > 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        return node;
    }

    private Node findMin(Node node) {
        return node.left != null ? findMin(node.left) : node;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMin(node.left);
        return balance(node);
    }

    private Node put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        if (key < node.key) {
            node.left = put(node.left, key, value);
        } else if (key > node.key) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        return balance(node);
    }

    private Node remove(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        if (key < node.key) {
            node.left = remove(node.left, key);
        } else if (key > node.key) {
            node.right = remove(node.right, key);
        } else {
            removedValue = node.value;
            size--;

            Node left = node.left;
            Node right = node.right;

            if (right == null) {
                return left;
            }

            Node min = findMin(right);
            min.right = removeMin(right);
            min.left = left;
            return balance(min);
        }

        return balance(node);
    }

    private Node getNode(Node node, Integer key) {
        while (node != null) {
            if (key < node.key) {
                node = node.left;
            } else if (key > node.key) {
                node = node.right;
            } else {
                return node;
            }
        }
        return null;
    }

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }

        inOrderToString(node.left, sb);

        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);

        inOrderToString(node.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrderToString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        Node existing = getNode(root, key);
        if (existing != null) {
            String oldValue = existing.value;
            existing.value = value;
            return oldValue;
        }

        root = put(root, key, value);
        return null;
    }

    public String remove(Integer key) {
        Node existing = getNode(root, key);
        if (existing == null) {
            return null;
        }

        removedValue = null;
        root = remove(root, key);
        return removedValue;
    }

    public String get(Integer key) {
        Node node = getNode(root, key);
        return node == null ? null : node.value;
    }

    public boolean containsKey(Integer key) {
        return getNode(root, key) != null;
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
    public String remove(Object key) {
        return remove((Integer) key);
    }

    @Override
    public String get(Object key) {
        return get((Integer) key);
    }

    @Override
    public boolean containsKey(Object key) {
        return containsKey((Integer) key);
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
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