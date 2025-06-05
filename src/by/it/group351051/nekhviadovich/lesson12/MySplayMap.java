package by.it.group351051.nekhviadovich.lesson12;

import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.ArrayList;
//lesson 12-3
public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    @Override
    public Comparator<? super Integer> comparator() {
        return null; // Используем естественный порядок для Integer
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new TreeSet<>();
        inOrderTraversal(root, keys);
        return keys;
    }

    @Override
    public Collection<String> values() {
        Collection<String> values = new ArrayList<>();
        inOrderTraversalValues(root, values);
        return values;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Entry<? extends Integer, ? extends String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    private void inOrderTraversal(Node node, Set<Integer> keys) {
        if (node != null) {
            inOrderTraversal(node.left, keys);
            keys.add(node.key);
            inOrderTraversal(node.right, keys);
        }
    }

    private void inOrderTraversalValues(Node node, Collection<String> values) {
        if (node != null) {
            inOrderTraversalValues(node.left, values);
            values.add(node.value);
            inOrderTraversalValues(node.right, values);
        }
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value); // Убрано приведение к String
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) {
            return false;
        }
        if (value.equals(node.value)) { // Сравнение как Object
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private void splay(Node node) {
        while (node.parent != null) {
            Node parent = node.parent;
            Node grandparent = parent.parent;

            if (grandparent == null) {
                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else {
                if (node == parent.left && parent == grandparent.left) {
                    rotateRight(grandparent);
                    rotateRight(parent);
                } else if (node == parent.right && parent == grandparent.right) {
                    rotateLeft(grandparent);
                    rotateLeft(parent);
                } else if (node == parent.right && parent == grandparent.left) {
                    rotateLeft(parent);
                    rotateRight(grandparent);
                } else {
                    rotateRight(parent);
                    rotateLeft(grandparent);
                }
            }
        }
        root = node;
    }

    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.parent = node.parent;
        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }
        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.parent = node.parent;
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }
        leftChild.right = node;
        node.parent = leftChild;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }

        Node current = root;
        Node parent = null;
        while (current != null) {
            parent = current;
            if (key.compareTo(current.key) < 0) {
                current = current.left;
            } else if (key.compareTo(current.key) > 0) {
                current = current.right;
            } else {
                String oldValue = current.value;
                current.value = value;
                splay(current);
                return oldValue;
            }
        }

        Node newNode = new Node(key, value);
        newNode.parent = parent;
        if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        Node node = findNode((Integer) key);
        if (node == null) {
            return null;
        }

        splay(node);

        if (node.left == null) {
            root = node.right;
            if (root != null) {
                root.parent = null;
            }
        } else {
            Node rightSubtree = node.right;
            root = node.left;
            root.parent = null;
            Node maxLeft = findMax(root);
            splay(maxLeft);
            maxLeft.right = rightSubtree;
            if (rightSubtree != null) {
                rightSubtree.parent = maxLeft;
            }
        }

        size--;
        return node.value;
    }

    private Node findNode(Integer key) {
        Node current = root;
        while (current != null) {
            if (key.compareTo(current.key) < 0) {
                current = current.left;
            } else if (key.compareTo(current.key) > 0) {
                current = current.right;
            } else {
                return current;
            }
        }
        return null;
    }

    private Node findMax(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    @Override
    public String get(Object key) {
        Node node = findNode((Integer) key);
        if (node != null) {
            splay(node);
            return node.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return findNode((Integer) key) != null;
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
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, inclusive, result);
        return result;
    }

    private void headMap(Node node, Integer toKey, boolean inclusive, MySplayMap result) {
        if (node == null) {
            return;
        }
        if (node.key.compareTo(toKey) < 0 || (inclusive && node.key.equals(toKey))) {
            result.put(node.key, node.value);
            headMap(node.right, toKey, inclusive, result);
        }
        headMap(node.left, toKey, inclusive, result);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, inclusive, result);
        return result;
    }

    private void tailMap(Node node, Integer fromKey, boolean inclusive, MySplayMap result) {
        if (node == null) {
            return;
        }
        if (node.key.compareTo(fromKey) > 0 || (inclusive && node.key.equals(fromKey))) {
            result.put(node.key, node.value);
            tailMap(node.left, fromKey, inclusive, result);
        }
        tailMap(node.right, fromKey, inclusive, result);
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = findMin(root);
        splay(node);
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new NoSuchElementException();
        }
        Node node = findMax(root);
        splay(node);
        return node.key;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node node = lowerNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    private Node lowerNode(Node node, Integer key) {
        if (node == null) {
            return null;
        }
        if (node.key.compareTo(key) >= 0) {
            return lowerNode(node.left, key);
        } else {
            Node right = lowerNode(node.right, key);
            return right != null ? right : node;
        }
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = floorNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    private Node floorNode(Node node, Integer key) {
        if (node == null) {
            return null;
        }
        if (node.key.compareTo(key) == 0) {
            return node;
        }
        if (node.key.compareTo(key) > 0) {
            return floorNode(node.left, key);
        } else {
            Node right = floorNode(node.right, key);
            return right != null ? right : node;
        }
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = ceilingNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    private Node ceilingNode(Node node, Integer key) {
        if (node == null) {
            return null;
        }
        if (node.key.compareTo(key) == 0) {
            return node;
        }
        if (node.key.compareTo(key) < 0) {
            return ceilingNode(node.right, key);
        } else {
            Node left = ceilingNode(node.left, key);
            return left != null ? left : node;
        }
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = higherNode(root, key);
        if (node != null) {
            splay(node);
            return node.key;
        }
        return null;
    }

    private Node higherNode(Node node, Integer key) {
        if (node == null) {
            return null;
        }
        if (node.key.compareTo(key) <= 0) {
            return higherNode(node.right, key);
        } else {
            Node left = higherNode(node.left, key);
            return left != null ? left : node;
        }
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        MySplayMap result = new MySplayMap();
        subMap(root, fromKey, fromInclusive, toKey, toInclusive, result);
        return result;
    }

    private void subMap(Node node, Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive, MySplayMap result) {
        if (node == null) {
            return;
        }
        if (node.key.compareTo(fromKey) > 0 || (fromInclusive && node.key.equals(fromKey))) {
            subMap(node.left, fromKey, fromInclusive, toKey, toInclusive, result);
        }
        if ((node.key.compareTo(fromKey) > 0 || (fromInclusive && node.key.equals(fromKey))) &&
                (node.key.compareTo(toKey) < 0 || (toInclusive && node.key.equals(toKey)))) {
            result.put(node.key, node.value);
        }
        if (node.key.compareTo(toKey) < 0 || (toInclusive && node.key.equals(toKey))) {
            subMap(node.right, fromKey, fromInclusive, toKey, toInclusive, result);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    private void toString(Node node, StringBuilder sb) {
        if (node != null) {
            toString(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            toString(node.right, sb);
        }
    }

    // Остальные методы интерфейса NavigableMap не реализованы для краткости
    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}