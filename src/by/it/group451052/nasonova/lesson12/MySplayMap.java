package by.it.group451052.nasonova.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    private void setParent(Node child, Node parent) {
        if (child != null) {
            child.parent = parent;
        }
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y == null) {
            return;
        }

        x.right = y.left;
        setParent(y.left, x);

        y.parent = x.parent;

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        if (y == null) {
            return;
        }

        x.left = y.right;
        setParent(y.right, x);

        y.parent = x.parent;

        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.right = x;
        x.parent = y;
    }

    private void splay(Node x) {
        if (x == null) {
            return;
        }

        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;

            if (g == null) {
                if (x == p.left) {
                    rotateRight(p);
                } else {
                    rotateLeft(p);
                }
            } else if (x == p.left && p == g.left) {
                rotateRight(g);
                rotateRight(p);
            } else if (x == p.right && p == g.right) {
                rotateLeft(g);
                rotateLeft(p);
            } else if (x == p.right && p == g.left) {
                rotateLeft(p);
                rotateRight(g);
            } else {
                rotateRight(p);
                rotateLeft(g);
            }
        }
    }

    private Node findNode(Integer key) {
        Node current = root;
        Node last = null;

        while (current != null) {
            last = current;
            if (key < current.key) {
                current = current.left;
            } else if (key > current.key) {
                current = current.right;
            } else {
                splay(current);
                return current;
            }
        }

        if (last != null) {
            splay(last);
        }
        return null;
    }

    private Node subtreeMin(Node node) {
        if (node == null) {
            return null;
        }
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node subtreeMax(Node node) {
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return node;
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

    private void fillHeadMap(Node node, Integer toKey, MySplayMap result) {
        if (node == null) {
            return;
        }

        fillHeadMap(node.left, toKey, result);

        if (node.key < toKey) {
            result.put(node.key, node.value);
            fillHeadMap(node.right, toKey, result);
        }
    }

    private void fillTailMap(Node node, Integer fromKey, MySplayMap result) {
        if (node == null) {
            return;
        }

        if (node.key >= fromKey) {
            fillTailMap(node.left, fromKey, result);
            result.put(node.key, node.value);
        }
        fillTailMap(node.right, fromKey, result);
    }

    private Node lowerNode(Integer key) {
        Node current = root;
        Node result = null;

        while (current != null) {
            if (key <= current.key) {
                current = current.left;
            } else {
                result = current;
                current = current.right;
            }
        }

        if (result != null) {
            splay(result);
        }
        return result;
    }

    private Node floorNode(Integer key) {
        Node current = root;
        Node result = null;

        while (current != null) {
            if (key < current.key) {
                current = current.left;
            } else if (key > current.key) {
                result = current;
                current = current.right;
            } else {
                splay(current);
                return current;
            }
        }

        if (result != null) {
            splay(result);
        }
        return result;
    }

    private Node ceilingNode(Integer key) {
        Node current = root;
        Node result = null;

        while (current != null) {
            if (key < current.key) {
                result = current;
                current = current.left;
            } else if (key > current.key) {
                current = current.right;
            } else {
                splay(current);
                return current;
            }
        }

        if (result != null) {
            splay(result);
        }
        return result;
    }

    private Node higherNode(Integer key) {
        Node current = root;
        Node result = null;

        while (current != null) {
            if (key < current.key) {
                result = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }

        if (result != null) {
            splay(result);
        }
        return result;
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
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }

        Node current = root;
        Node parent = null;

        while (current != null) {
            parent = current;

            if (key < current.key) {
                current = current.left;
            } else if (key > current.key) {
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

        if (key < parent.key) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        splay(newNode);
        size++;
        return null;
    }

    public String remove(Integer key) {
        Node node = findNode(key);
        if (node == null || !node.key.equals(key)) {
            return null;
        }

        String oldValue = node.value;

        Node leftSub = node.left;
        Node rightSub = node.right;

        if (leftSub != null) {
            leftSub.parent = null;
        }
        if (rightSub != null) {
            rightSub.parent = null;
        }

        if (leftSub == null) {
            root = rightSub;
        } else {
            root = leftSub;
            Node maxLeft = subtreeMax(leftSub);
            splay(maxLeft);
            root.right = rightSub;
            setParent(rightSub, root);
        }

        size--;
        return oldValue;
    }

    public String get(Integer key) {
        Node node = findNode(key);
        return node != null && node.key.equals(key) ? node.value : null;
    }

    public boolean containsKey(Integer key) {
        Node node = findNode(key);
        return node != null && node.key.equals(key);
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
        MySplayMap result = new MySplayMap();
        fillHeadMap(root, toKey, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        fillTailMap(root, fromKey, result);
        return result;
    }

    @Override
    public Integer firstKey() {
        Node min = subtreeMin(root);
        if (min == null) {
            return null;
        }
        splay(min);
        return min.key;
    }

    @Override
    public Integer lastKey() {
        Node max = subtreeMax(root);
        if (max == null) {
            return null;
        }
        splay(max);
        return max.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node node = lowerNode(key);
        return node == null ? null : node.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = floorNode(key);
        return node == null ? null : node.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = ceilingNode(key);
        return node == null ? null : node.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = higherNode(key);
        return node == null ? null : node.key;
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
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
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