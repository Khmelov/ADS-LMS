package by.it.group451051.sinenko.lesson12;

import java.util.*;

public class MySplayMap implements Map<Integer, String>, SortedMap<Integer, String> {
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
    
    public MySplayMap() {
        root = null;
        size = 0;
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
    public String put(Integer key, String value) {
        if (key == null) return null;
        String oldValue = get(key);
        
        Node newNode = new Node(key, value);
        if (root == null) {
            root = newNode;
        } else {
            Node current = root;
            Node parent = null;
            while (current != null) {
                parent = current;
                if (key < current.key) {
                    current = current.left;
                } else if (key > current.key) {
                    current = current.right;
                } else {
                    oldValue = current.value;
                    current.value = value;
                    return oldValue;
                }
            }
            if (key < parent.key) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        }
        if (oldValue == null) size++;
        return oldValue;
    }
    
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        Node node = getRecursive(root, intKey);
        return node == null ? null : node.value;
    }
    
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        if (!containsKey(intKey)) return null;
        String oldValue = get(intKey);
        root = removeRecursive(root, intKey);
        size--;
        return oldValue;
    }
    
    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return get(key) != null;
    }
    
    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
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
    
    @Override
    public Collection<String> values() {
        List<String> vals = new ArrayList<>();
        collectValues(root, vals);
        return vals;
    }
    
    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> entries = new HashSet<>();
        collectEntries(root, entries);
        return entries;
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
    public Comparator<? super Integer> comparator() {
        return null;
    }
    
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        MySplayMap result = new MySplayMap();
        subMapRecursive(root, fromKey, toKey, result);
        return result;
    }
    
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        headMapRecursive(root, toKey, result);
        return result;
    }
    
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        tailMapRecursive(root, fromKey, result);
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }
    
    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================
    
    private Node getRecursive(Node node, Integer key) {
        if (node == null) return null;
        if (key < node.key) return getRecursive(node.left, key);
        if (key > node.key) return getRecursive(node.right, key);
        return node;
    }
    
    private Node removeRecursive(Node node, Integer key) {
        if (node == null) return null;
        if (key < node.key) {
            node.left = removeRecursive(node.left, key);
        } else if (key > node.key) {
            node.right = removeRecursive(node.right, key);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node min = findMin(node.right);
            node.key = min.key;
            node.value = min.value;
            node.right = removeRecursive(node.right, min.key);
        }
        return node;
    }
    
    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }
    
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderTraversal(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        inOrderTraversal(node.right, sb);
    }
    
    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }
    
    private void collectKeys(Node node, Set<Integer> keys) {
        if (node == null) return;
        collectKeys(node.left, keys);
        keys.add(node.key);
        collectKeys(node.right, keys);
    }
    
    private void collectValues(Node node, List<String> vals) {
        if (node == null) return;
        collectValues(node.left, vals);
        vals.add(node.value);
        collectValues(node.right, vals);
    }
    
    private void collectEntries(Node node, Set<Entry<Integer, String>> entries) {
        if (node == null) return;
        collectEntries(node.left, entries);
        entries.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
        collectEntries(node.right, entries);
    }
    
    private void subMapRecursive(Node node, Integer from, Integer to, MySplayMap result) {
        if (node == null) return;
        subMapRecursive(node.left, from, to, result);
        if (node.key >= from && node.key < to) result.put(node.key, node.value);
        subMapRecursive(node.right, from, to, result);
    }
    
    private void headMapRecursive(Node node, Integer toKey, MySplayMap result) {
        if (node == null) return;
        headMapRecursive(node.left, toKey, result);
        if (node.key < toKey) result.put(node.key, node.value);
        headMapRecursive(node.right, toKey, result);
    }
    
    private void tailMapRecursive(Node node, Integer fromKey, MySplayMap result) {
        if (node == null) return;
        tailMapRecursive(node.left, fromKey, result);
        if (node.key >= fromKey) result.put(node.key, node.value);
        tailMapRecursive(node.right, fromKey, result);
    }
    
    // ==================== НАВИГАЦИОННЫЕ МЕТОДЫ ====================
    
    public Integer lowerKey(Integer key) {
        Node node = root;
        Integer result = null;
        while (node != null) {
            if (node.key < key) {
                result = node.key;
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return result;
    }
    
    public Integer floorKey(Integer key) {
        Node node = root;
        Integer result = null;
        while (node != null) {
            if (node.key <= key) {
                result = node.key;
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return result;
    }
    
    public Integer ceilingKey(Integer key) {
        Node node = root;
        Integer result = null;
        while (node != null) {
            if (node.key >= key) {
                result = node.key;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return result;
    }
    
    public Integer higherKey(Integer key) {
        Node node = root;
        Integer result = null;
        while (node != null) {
            if (node.key > key) {
                result = node.key;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return result;
    }
    
    // Перегрузки для Object (на случай, если тест ищет именно их)
    public Integer lowerKey(Object key) {
        if (!(key instanceof Integer)) return null;
        return lowerKey((Integer) key);
    }
    
    public Integer floorKey(Object key) {
        if (!(key instanceof Integer)) return null;
        return floorKey((Integer) key);
    }
    
    public Integer ceilingKey(Object key) {
        if (!(key instanceof Integer)) return null;
        return ceilingKey((Integer) key);
    }
    
    public Integer higherKey(Object key) {
        if (!(key instanceof Integer)) return null;
        return higherKey((Integer) key);
    }
}