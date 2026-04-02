package by.it.group451052.nasonova.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size;

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
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
        if (h.left != null) {
            h.left.color = BLACK;
        }
        if (h.right != null) {
            h.right.color = BLACK;
        }
    }

    private Node balance(Node h) {
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }
        return h;
    }

    private Node put(Node h, Integer key, String value) {
        if (h == null) {
            size++;
            return new Node(key, value, RED);
        }

        if (key < h.key) {
            h.left = put(h.left, key, value);
        } else if (key > h.key) {
            h.right = put(h.right, key, value);
        } else {
            h.value = value;
        }

        return balance(h);
    }

    private Node getNode(Node node, Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }

        Integer k = (Integer) key;
        while (node != null) {
            if (k < node.key) {
                node = node.left;
            } else if (k > node.key) {
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

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }

        if (containsValue(node.left, value)) {
            return true;
        }

        if (value == null ? node.value == null : value.equals(node.value)) {
            return true;
        }

        return containsValue(node.right, value);
    }

    private void rebuildWithout(Node node, Integer keyToRemove) {
        if (node == null) {
            return;
        }

        rebuildWithout(node.left, keyToRemove);

        if (!node.key.equals(keyToRemove)) {
            put(node.key, node.value);
        }

        rebuildWithout(node.right, keyToRemove);
    }

    private void fillHeadMap(Node node, Integer toKey, MyRbMap result) {
        if (node == null) {
            return;
        }

        fillHeadMap(node.left, toKey, result);

        if (node.key < toKey) {
            result.put(node.key, node.value);
            fillHeadMap(node.right, toKey, result);
        }
    }

    private void fillTailMap(Node node, Integer fromKey, MyRbMap result) {
        if (node == null) {
            return;
        }

        if (node.key >= fromKey) {
            fillTailMap(node.left, fromKey, result);
            result.put(node.key, node.value);
        }
        fillTailMap(node.right, fromKey, result);
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
        root.color = BLACK;
        return null;
    }

    public String remove(Integer key) {
        Node existing = getNode(root, key);
        if (existing == null) {
            return null;
        }

        String oldValue = existing.value;
        Node oldRoot = root;

        root = null;
        size = 0;
        rebuildWithout(oldRoot, key);

        if (root != null) {
            root.color = BLACK;
        }

        return oldValue;
    }

    public String get(Integer key) {
        Node node = getNode(root, key);
        return node == null ? null : node.value;
    }

    public boolean containsKey(Integer key) {
        return getNode(root, key) != null;
    }

    public boolean containsValue(String value) {
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
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        fillHeadMap(root, toKey, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        fillTailMap(root, fromKey, result);
        return result;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            return null;
        }

        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            return null;
        }

        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        return remove((Integer) key);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        return get((Integer) key);
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        return containsKey((Integer) key);
    }

    @Override
    public boolean containsValue(Object value) {
        if (value != null && !(value instanceof String)) {
            return false;
        }
        return containsValue((String) value);
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