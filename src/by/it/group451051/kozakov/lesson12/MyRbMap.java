package by.it.group451051.kozakov.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        boolean color;

        Node(Integer key, String value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
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
        if (root != null) {
            root.color = BLACK;
        }
        return result.oldValue;
    }

    private NodeResult put(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new NodeResult(new Node(key, value, RED), null);
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
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        return node;
    }

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        newRoot.color = node.color;
        node.color = RED;
        return newRoot;
    }

    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        newRoot.color = node.color;
        node.color = RED;
        return newRoot;
    }

    private void flipColors(Node node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    @Override
    public String remove(Object key) {
        Integer intKey = (Integer) key;
        NodeResult result = remove(root, intKey);
        root = result.node;
        if (root != null) {
            root.color = BLACK;
        }
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
            if (node.left == null) {
                size--;
                return new NodeResult(node.right, oldValue);
            }
            if (node.right == null) {
                size--;
                return new NodeResult(node.left, oldValue);
            }
            Node temp = min(node.right);
            node.key = temp.key;
            node.value = temp.value;
            NodeResult rightResult = remove(node.right, temp.key);
            node.right = rightResult.node;
            return new NodeResult(balance(node), oldValue);
        }
    }

    private Node min(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
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
            sb.setLength(sb.length() - 2);
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
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
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
            throw new NoSuchElementException();
        }
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap headMap = new MyRbMap();
        headMapTraversal(root, toKey, headMap);
        return headMap;
    }

    private void headMapTraversal(Node node, Integer toKey, MyRbMap headMap) {
        if (node == null) {
            return;
        }
        if (node.key < toKey) {
            headMap.put(node.key, node.value);
            headMapTraversal(node.left, toKey, headMap);
            headMapTraversal(node.right, toKey, headMap);
        } else {
            headMapTraversal(node.left, toKey, headMap);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap tailMap = new MyRbMap();
        tailMapTraversal(root, fromKey, tailMap);
        return tailMap;
    }

    private void tailMapTraversal(Node node, Integer fromKey, MyRbMap tailMap) {
        if (node == null) {
            return;
        }
        if (node.key >= fromKey) {
            tailMap.put(node.key, node.value);
            tailMapTraversal(node.left, fromKey, tailMap);
            tailMapTraversal(node.right, fromKey, tailMap);
        } else {
            tailMapTraversal(node.right, fromKey, tailMap);
        }
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MyRbMap subMap = new MyRbMap();
        subMapTraversal(root, fromKey, toKey, subMap);
        return subMap;
    }

    private void subMapTraversal(Node node, Integer fromKey, Integer toKey, MyRbMap subMap) {
        if (node == null) {
            return;
        }
        if (node.key >= fromKey && node.key < toKey) {
            subMap.put(node.key, node.value);
            subMapTraversal(node.left, fromKey, toKey, subMap);
            subMapTraversal(node.right, fromKey, toKey, subMap);
        } else if (node.key < fromKey) {
            subMapTraversal(node.right, fromKey, toKey, subMap);
        } else {
            subMapTraversal(node.left, fromKey, toKey, subMap);
        }
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
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