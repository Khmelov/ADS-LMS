package by.it.group451051.kozakov.lesson12;

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

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode((Integer) key) != null;
    }

    @Override
    public String get(Object key) {
        Node node = getNode((Integer) key);
        return node == null ? null : node.value;
    }

    private Node getNode(Integer key) {
        Node current = root;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        NodeResult result = put(root, key, value);
        root = result.node;
        return result.oldValue;
    }

    private NodeResult put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new NodeResult(new Node(key, value), null);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            NodeResult leftResult = put(node.left, key, value);
            node.left = leftResult.node;
            return new NodeResult(balance(node), leftResult.oldValue);
        } else if (cmp > 0) {
            NodeResult rightResult = put(node.right, key, value);
            node.right = rightResult.node;
            return new NodeResult(balance(node), rightResult.oldValue);
        } else {
            String oldValue = node.value;
            node.value = value;
            return new NodeResult(node, oldValue);
        }
    }

    private static class NodeResult {
        Node node;
        String oldValue;

        NodeResult(Node node, String oldValue) {
            this.node = node;
            this.oldValue = oldValue;
        }
    }

    private Node balance(Node node) {
        int balanceFactor = getBalance(node);

        if (balanceFactor > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (balanceFactor < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        return node;
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    @Override
    public String remove(Object key) {
        Integer intKey = (Integer) key;
        NodeResult result = remove(root, intKey);
        root = result.node;
        return result.oldValue;
    }

    private NodeResult remove(Node node, Integer key) {
        if (node == null) {
            return new NodeResult(null, null);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            NodeResult leftResult = remove(node.left, key);
            node.left = leftResult.node;
            return new NodeResult(balance(node), leftResult.oldValue);
        } else if (cmp > 0) {
            NodeResult rightResult = remove(node.right, key);
            node.right = rightResult.node;
            return new NodeResult(balance(node), rightResult.oldValue);
        } else {
            String oldValue = node.value;
            if (node.left == null || node.right == null) {
                Node temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    node = temp;
                }
                size--;
            } else {
                Node temp = minValueNode(node.right);
                node.key = temp.key;
                node.value = temp.value;
                NodeResult rightResult = remove(node.right, temp.key);
                node.right = rightResult.node;
            }
            if (node != null) {
                node.height = 1 + Math.max(height(node.left), height(node.right));
            }
            return new NodeResult(node, oldValue);
        }
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
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

    // Необязательные методы интерфейса Map
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (Objects.equals(value, node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> keySet = new HashSet<>();
        inOrderKeyTraversal(root, keySet);
        return keySet;
    }

    private void inOrderKeyTraversal(Node node, Set<Integer> keySet) {
        if (node != null) {
            inOrderKeyTraversal(node.left, keySet);
            keySet.add(node.key);
            inOrderKeyTraversal(node.right, keySet);
        }
    }

    @Override
    public Collection<String> values() {
        List<String> values = new ArrayList<>();
        inOrderValueTraversal(root, values);
        return values;
    }

    private void inOrderValueTraversal(Node node, List<String> values) {
        if (node != null) {
            inOrderValueTraversal(node.left, values);
            values.add(node.value);
            inOrderValueTraversal(node.right, values);
        }
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> entrySet = new HashSet<>();
        inOrderEntryTraversal(root, entrySet);
        return entrySet;
    }

    private void inOrderEntryTraversal(Node node, Set<Entry<Integer, String>> entrySet) {
        if (node != null) {
            inOrderEntryTraversal(node.left, entrySet);
            entrySet.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
            inOrderEntryTraversal(node.right, entrySet);
        }
    }
}