package by.it.group451051.kozakov.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {
    private static class Node {
        Integer key;
        String value;
        Node left, right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
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
        if (!(key instanceof Integer)) {
            return false;
        }
        root = splay(root, (Integer) key);
        return root != null && root.key.equals(key);
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
        if (!(key instanceof Integer)) {
            return null;
        }
        root = splay(root, (Integer) key);
        if (root == null || !root.key.equals(key)) {
            return null;
        }
        return root.value;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        root = splay(root, key);
        int cmp = key.compareTo(root.key);

        if (cmp < 0) {
            Node newNode = new Node(key, value);
            newNode.left = root.left;
            newNode.right = root;
            root.left = null;
            root = newNode;
            size++;
            return null;
        } else if (cmp > 0) {
            Node newNode = new Node(key, value);
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
            root = newNode;
            size++;
            return null;
        } else {
            String oldValue = root.value;
            root.value = value;
            return oldValue;
        }
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        Integer intKey = (Integer) key;
        if (root == null) {
            return null;
        }

        root = splay(root, intKey);
        int cmp = intKey.compareTo(root.key);

        if (cmp != 0) {
            return null;
        }

        String oldValue = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node rightSubtree = root.right;
            root = splay(root.left, intKey);
            root.right = rightSubtree;
        }
        size--;
        return oldValue;
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

    private Node splay(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp1 = key.compareTo(node.key);
        if (cmp1 < 0) {
            if (node.left == null) {
                return node;
            }
            int cmp2 = key.compareTo(node.left.key);
            if (cmp2 < 0) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (cmp2 > 0) {
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }
            return node.left == null ? node : rotateRight(node);
        } else if (cmp1 > 0) {
            if (node.right == null) {
                return node;
            }
            int cmp2 = key.compareTo(node.right.key);
            if (cmp2 < 0) {
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            } else if (cmp2 > 0) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            }
            return node.right == null ? node : rotateLeft(node);
        } else {
            return node;
        }
    }

    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        return newRoot;
    }

    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        return newRoot;
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
        root = splay(root, current.key);
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
        root = splay(root, current.key);
        return current.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node node = lowerNode(root, key);
        if (node == null) {
            return null;
        }
        root = splay(root, node.key);
        return node.key;
    }

    private Node lowerNode(Node node, Integer key) {
        Node result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp > 0) {
                result = node;
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = floorNode(root, key);
        if (node == null) {
            return null;
        }
        root = splay(root, node.key);
        return node.key;
    }

    private Node floorNode(Node node, Integer key) {
        Node result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp == 0) {
                return node;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                result = node;
                node = node.right;
            }
        }
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = ceilingNode(root, key);
        if (node == null) {
            return null;
        }
        root = splay(root, node.key);
        return node.key;
    }

    private Node ceilingNode(Node node, Integer key) {
        Node result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp == 0) {
                return node;
            } else if (cmp < 0) {
                result = node;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = higherNode(root, key);
        if (node == null) {
            return null;
        }
        root = splay(root, node.key);
        return node.key;
    }

    private Node higherNode(Node node, Integer key) {
        Node result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                result = node;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return result;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap headMap = new MySplayMap();
        headMapTraversal(root, toKey, inclusive, headMap);
        return headMap;
    }

    private void headMapTraversal(Node node, Integer toKey, boolean inclusive, MySplayMap headMap) {
        if (node == null) {
            return;
        }
        int cmp = toKey.compareTo(node.key);
        if (cmp > 0 || (inclusive && cmp == 0)) {
            headMap.put(node.key, node.value);
            headMapTraversal(node.left, toKey, inclusive, headMap);
            headMapTraversal(node.right, toKey, inclusive, headMap);
        } else {
            headMapTraversal(node.left, toKey, inclusive, headMap);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap tailMap = new MySplayMap();
        tailMapTraversal(root, fromKey, inclusive, tailMap);
        return tailMap;
    }

    private void tailMapTraversal(Node node, Integer fromKey, boolean inclusive, MySplayMap tailMap) {
        if (node == null) {
            return;
        }
        int cmp = fromKey.compareTo(node.key);
        if (cmp < 0 || (inclusive && cmp == 0)) {
            tailMap.put(node.key, node.value);
            tailMapTraversal(node.left, fromKey, inclusive, tailMap);
            tailMapTraversal(node.right, fromKey, inclusive, tailMap);
        } else {
            tailMapTraversal(node.right, fromKey, inclusive, tailMap);
        }
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        MySplayMap subMap = new MySplayMap();
        subMapTraversal(root, fromKey, fromInclusive, toKey, toInclusive, subMap);
        return subMap;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    private void subMapTraversal(Node node, Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive, MySplayMap subMap) {
        if (node == null) {
            return;
        }
        int cmpFrom = fromKey.compareTo(node.key);
        int cmpTo = toKey.compareTo(node.key);
        if ((cmpFrom < 0 || (fromInclusive && cmpFrom == 0)) &&
                (cmpTo > 0 || (toInclusive && cmpTo == 0))) {
            subMap.put(node.key, node.value);
            subMapTraversal(node.left, fromKey, fromInclusive, toKey, toInclusive, subMap);
            subMapTraversal(node.right, fromKey, fromInclusive, toKey, toInclusive, subMap);
        } else if (cmpFrom > 0) {
            subMapTraversal(node.right, fromKey, fromInclusive, toKey, toInclusive, subMap);
        } else {
            subMapTraversal(node.left, fromKey, fromInclusive, toKey, toInclusive, subMap);
        }
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        if (root == null) {
            return null;
        }
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        root = splay(root, current.key);
        return new AbstractMap.SimpleEntry<>(current.key, current.value);
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        if (root == null) {
            return null;
        }
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        root = splay(root, current.key);
        return new AbstractMap.SimpleEntry<>(current.key, current.value);
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        if (root == null) {
            return null;
        }
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        root = splay(root, current.key);
        Entry<Integer, String> entry = new AbstractMap.SimpleEntry<>(current.key, current.value);
        remove(current.key);
        return entry;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        if (root == null) {
            return null;
        }
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        root = splay(root, current.key);
        Entry<Integer, String> entry = new AbstractMap.SimpleEntry<>(current.key, current.value);
        remove(current.key);
        return entry;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        Node node = lowerNode(root, key);
        if (node == null) {
            return null;
        }
        root = splay(root, node.key);
        return new AbstractMap.SimpleEntry<>(node.key, node.value);
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        Node node = floorNode(root, key);
        if (node == null) {
            return null;
        }
        root = splay(root, node.key);
        return new AbstractMap.SimpleEntry<>(node.key, node.value);
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        Node node = ceilingNode(root, key);
        if (node == null) {
            return null;
        }
        root = splay(root, node.key);
        return new AbstractMap.SimpleEntry<>(node.key, node.value);
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        Node node = higherNode(root, key);
        if (node == null) {
            return null;
        }
        root = splay(root, node.key);
        return new AbstractMap.SimpleEntry<>(node.key, node.value);
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

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}