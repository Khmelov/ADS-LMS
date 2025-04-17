package by.it.group351051.stepanov.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public class MyAvlMap implements Map<Integer, String> {
    private Node root;
    private int size;

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

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        Node node = getNode(root, key);
        if (node != null) {
            String oldValue = node.value;
            node.value = value;
            return oldValue;
        }
        root = insert(root, key, value);
        size++;
        return null;
    }

    private Node insert(Node node, int key, String value) {
        if (node == null) {
            return new Node(key, value);
        }

        if (key < node.key) {
            node.left = insert(node.left, key, value);
        } else if (key > node.key) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    private Node balance(Node node) {
        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1) {
            if (getBalanceFactor(node.left) >= 0) {
                return rotateRight(node);
            } else {
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }
        }

        if (balanceFactor < -1) {
            if (getBalanceFactor(node.right) <= 0) {
                return rotateLeft(node);
            } else {
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }
        }

        return node;
    }

    private Node rotateLeft(Node y) {
        Node x = y.right;
        Node T2 = x.left;

        x.left = y;
        y.right = T2;

        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));

        return x;
    }

    private Node rotateRight(Node x) {
        Node y = x.left;
        Node T2 = y.right;

        y.right = x;
        x.left = T2;

        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));

        return y;
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return null; // Ключ должен быть Integer
        }
        Node node = getNode(root, (Integer) key);
        if (node == null) {
            return null;
        }
        root = delete(root, (Integer) key);
        size--;
        return node.value;
    }

    private Node delete(Node node, int key) {
        if (node == null) {
            return null;
        }

        if (key < node.key) {
            node.left = delete(node.left, key);
        } else if (key > node.key) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node temp = findMin(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = delete(node.right, temp.key);
            }
        }

        if (node == null) {
            return null;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return null; // Ключ должен быть Integer
        }
        Node node = getNode(root, (Integer) key);
        return (node != null) ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (!(key instanceof Integer)) {
            return false; // Ключ должен быть Integer
        }
        return getNode(root, (Integer) key) != null;
    }

    private Node getNode(Node node, int key) {
        if (node == null) {
            return null;
        }

        if (key < node.key) {
            return getNode(node.left, key);
        } else if (key > node.key) {
            return getNode(node.right, key);
        } else {
            return node;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Удаляем последнюю запятую и пробел
        }
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
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

    // Остальные методы интерфейса Map (заглушки)
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

    private int height(Node node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalanceFactor(Node node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }
}
