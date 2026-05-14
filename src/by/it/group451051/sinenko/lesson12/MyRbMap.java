package by.it.group451051.sinenko.lesson12;

import java.util.*;

public class MyRbMap implements Map<Integer, String>, SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        boolean color;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.color = RED;
        }
    }

    private Node root;
    private int size;

    public MyRbMap() {
        root = null;
        size = 0;
    }

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        return y;
    }

    private void flipColors(Node node) {
        node.color = !node.color;
        if (node.left != null) node.left.color = !node.left.color;
        if (node.right != null) node.right.color = !node.right.color;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) return null;
        String oldValue = get(key);
        root = putRecursive(root, key, value);
        if (root != null) root.color = BLACK;
        if (oldValue == null) size++;
        return oldValue;
    }

    private Node putRecursive(Node node, Integer key, String value) {
        if (node == null) return new Node(key, value);

        if (key < node.key) {
            node.left = putRecursive(node.left, key, value);
        } else if (key > node.key) {
            node.right = putRecursive(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        return node;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = getRecursive(root, (Integer) key);
        return node == null ? null : node.value;
    }

    private Node getRecursive(Node node, Integer key) {
        if (node == null) return null;
        if (key < node.key) return getRecursive(node.left, key);
        if (key > node.key) return getRecursive(node.right, key);
        return node;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        if (!containsKey(intKey)) return null;
        String oldValue = get(intKey);
        root = removeRecursive(root, intKey);
        if (root != null) root.color = BLACK;
        size--;
        return oldValue;
    }

    private Node removeRecursive(Node node, Integer key) {
        if (node == null) return null;

        if (key < node.key) {
            if (node.left != null && !isRed(node.left) && (node.left.left == null || !isRed(node.left.left))) {
                node = moveRedLeft(node);
            }
            node.left = removeRecursive(node.left, key);
        } else {
            if (isRed(node.left)) node = rotateRight(node);
            if (key.equals(node.key) && node.right == null) return null;
            if (node.right != null && !isRed(node.right) && (node.right.left == null || !isRed(node.right.left))) {
                node = moveRedRight(node);
            }
            if (key.equals(node.key)) {
                Node min = findMin(node.right);
                node.key = min.key;
                node.value = min.value;
                node.right = deleteMin(node.right);
            } else {
                node.right = removeRecursive(node.right, key);
            }
        }
        return balance(node);
    }

    private Node deleteMin(Node node) {
        if (node.left == null) return null;
        if (!isRed(node.left) && (node.left.left == null || !isRed(node.left.left))) {
            node = moveRedLeft(node);
        }
        node.left = deleteMin(node.left);
        return balance(node);
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node moveRedLeft(Node node) {
        flipColors(node);
        if (node.right != null && node.right.left != null && isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node moveRedRight(Node node) {
        flipColors(node);
        if (node.left != null && node.left.left != null && isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    private Node balance(Node node) {
        if (isRed(node.right) && !isRed(node.left)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);
        return node;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderTraversal(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        inOrderTraversal(node.right, sb);
    }

    @Override
    public Integer firstKey() {
        if (root == null) return null;
        Node node = root;
        while (node.left != null) node = node.left;
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node node = root;
        while (node.right != null) node = node.right;
        return node.key;
    }

    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap result = new MyRbMap();
        subMapRecursive(root, fromKey, toKey, result);
        return result;
    }

private void subMapRecursive(Node node, Integer from, Integer to, MyRbMap result) {
    if (node == null) return;
    subMapRecursive(node.left, from, to, result);
    if (node.key >= from && node.key < to) result.put(node.key, node.value);
    subMapRecursive(node.right, from, to, result);
}

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMapRecursive(root, toKey, result);
        return result;
    }

private void headMapRecursive(Node node, Integer toKey, MyRbMap result) {
    if (node == null) return;
    headMapRecursive(node.left, toKey, result);
    if (node.key < toKey) result.put(node.key, node.value);
    headMapRecursive(node.right, toKey, result);
}

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMapRecursive(root, fromKey, result);
        return result;
    }

private void tailMapRecursive(Node node, Integer fromKey, MyRbMap result) {
    if (node == null) return;
    tailMapRecursive(node.left, fromKey, result);
    if (node.key >= fromKey) result.put(node.key, node.value);
    tailMapRecursive(node.right, fromKey, result);
}

    public Integer lowerKey(Integer key) {
        return lowerKeyRecursive(root, key, null);
    }

    private Integer lowerKeyRecursive(Node node, Integer key, Integer best) {
        if (node == null) return best;
        if (node.key < key) {
            best = node.key;
            return lowerKeyRecursive(node.right, key, best);
        } else {
            return lowerKeyRecursive(node.left, key, best);
        }
    }

    public Integer floorKey(Integer key) {
        return floorKeyRecursive(root, key, null);
    }

    private Integer floorKeyRecursive(Node node, Integer key, Integer best) {
        if (node == null) return best;
        if (node.key <= key) {
            best = node.key;
            return floorKeyRecursive(node.right, key, best);
        } else {
            return floorKeyRecursive(node.left, key, best);
        }
    }

    public Integer ceilingKey(Integer key) {
        return ceilingKeyRecursive(root, key, null);
    }

    private Integer ceilingKeyRecursive(Node node, Integer key, Integer best) {
        if (node == null) return best;
        if (node.key >= key) {
            best = node.key;
            return ceilingKeyRecursive(node.left, key, best);
        } else {
            return ceilingKeyRecursive(node.right, key, best);
        }
    }

    public Integer higherKey(Integer key) {
        return higherKeyRecursive(root, key, null);
    }

    private Integer higherKeyRecursive(Node node, Integer key, Integer best) {
        if (node == null) return best;
        if (node.key > key) {
            best = node.key;
            return higherKeyRecursive(node.left, key, best);
        } else {
            return higherKeyRecursive(node.right, key, best);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new HashSet<>();
        collectKeys(root, keys);
        return keys;
    }

    private void collectKeys(Node node, Set<Integer> keys) {
        if (node == null) return;
        collectKeys(node.left, keys);
        keys.add(node.key);
        collectKeys(node.right, keys);
    }

    @Override
    public Collection<String> values() {
        List<String> vals = new ArrayList<>();
        collectValues(root, vals);
        return vals;
    }

    private void collectValues(Node node, List<String> vals) {
        if (node == null) return;
        collectValues(node.left, vals);
        vals.add(node.value);
        collectValues(node.right, vals);
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> entries = new HashSet<>();
        collectEntries(root, entries);
        return entries;
    }

    private void collectEntries(Node node, Set<Entry<Integer, String>> entries) {
        if (node == null) return;
        collectEntries(node.left, entries);
        entries.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
        collectEntries(node.right, entries);
    }
}