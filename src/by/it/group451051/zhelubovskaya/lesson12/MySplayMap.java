package by.it.group451051.zhelubovskaya.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left, right, parent;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    private void rotateLeft(Node x) {
        if (x == null || x.right == null) return;
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        if (x == null || x.left == null) return;
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.right = x;
        x.parent = y;
    }

    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            } else {
                if (x == p.left && p == g.left) {
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
    }

    private Node find(int key) {
        Node x = root;
        Node last = null;
        while (x != null) {
            last = x;
            if (key < x.key) x = x.left;
            else if (key > x.key) x = x.right;
            else {
                splay(x);
                return x;
            }
        }
        if (last != null) splay(last);
        return null;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) return null;
        String old = get(key);
        if (root == null) {
            root = new Node(key, value);
            size++;
            return old;
        }
        Node x = root;
        Node parent = null;
        while (x != null) {
            parent = x;
            if (key < x.key) x = x.left;
            else if (key > x.key) x = x.right;
            else {
                old = x.value;
                x.value = value;
                splay(x);
                return old;
            }
        }
        Node newNode = new Node(key, value);
        newNode.parent = parent;
        if (key < parent.key) parent.left = newNode;
        else parent.right = newNode;
        size++;
        splay(newNode);
        return old;
    }

    private Node merge(Node left, Node right) {
        if (left == null) return right;
        if (right == null) return left;
        Node max = left;
        while (max.right != null) max = max.right;
        splay(max);
        max.right = right;
        if (right != null) right.parent = max;
        return max;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        String old = get(k);
        if (old == null) return null;
        Node x = find(k);
        if (x == null) return old;
        splay(x);
        Node newRoot = merge(x.left, x.right);
        if (newRoot != null) newRoot.parent = null;
        root = newRoot;
        size--;
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node x = find((Integer) key);
        return x == null ? null : x.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    private boolean containsValue(Node x, Object value) {
        if (x == null) return false;
        if (Objects.equals(x.value, value)) return true;
        return containsValue(x.left, value) || containsValue(x.right, value);
    }

    @Override
    public boolean containsValue(Object value) {
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
    public Integer firstKey() {
        if (root == null) return null;
        Node x = root;
        while (x.left != null) x = x.left;
        splay(x);
        return x.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node x = root;
        while (x.right != null) x = x.right;
        splay(x);
        return x.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) return null;
        Node x = root;
        Integer result = null;
        while (x != null) {
            if (key > x.key) {
                result = x.key;
                x = x.right;
            } else {
                x = x.left;
            }
        }
        if (result != null) find(result);
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) return null;
        Node x = root;
        Integer result = null;
        while (x != null) {
            if (key >= x.key) {
                result = x.key;
                x = x.right;
            } else {
                x = x.left;
            }
        }
        if (result != null) find(result);
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) return null;
        Node x = root;
        Integer result = null;
        while (x != null) {
            if (key <= x.key) {
                result = x.key;
                x = x.left;
            } else {
                x = x.right;
            }
        }
        if (result != null) find(result);
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) return null;
        Node x = root;
        Integer result = null;
        while (x != null) {
            if (key < x.key) {
                result = x.key;
                x = x.left;
            } else {
                x = x.right;
            }
        }
        if (result != null) find(result);
        return result;
    }

    private void headMap(Node x, int toKey, MySplayMap result) {
        if (x == null) return;
        headMap(x.left, toKey, result);
        if (x.key < toKey) {
            result.put(x.key, x.value);
        }
        headMap(x.right, toKey, result);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        headMap(root, toKey, result);
        return result;
    }

    private void tailMap(Node x, int fromKey, MySplayMap result) {
        if (x == null) return;
        tailMap(x.left, fromKey, result);
        if (x.key >= fromKey) {
            result.put(x.key, x.value);
        }
        tailMap(x.right, fromKey, result);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void inOrder(Node x, StringBuilder sb) {
        if (x == null) return;
        inOrder(x.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(x.key).append("=").append(x.value);
        inOrder(x.right, sb);
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        sb.append("}");
        return sb.toString();
    }

    // Заглушки
    @Override
    public Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override
    public Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override
    public Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override
    public Entry<Integer, String> firstEntry() { return null; }
    @Override
    public Entry<Integer, String> lastEntry() { return null; }
    @Override
    public Entry<Integer, String> pollFirstEntry() { return null; }
    @Override
    public Entry<Integer, String> pollLastEntry() { return null; }
    @Override
    public NavigableMap<Integer, String> descendingMap() { return null; }
    @Override
    public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override
    public NavigableSet<Integer> descendingKeySet() { return null; }
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { return null; }
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { return null; }
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { return null; }
    @Override
    public Comparator<? super Integer> comparator() { return null; }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { return null; }
    @Override
    public Set<Integer> keySet() { return null; }
    @Override
    public Collection<String> values() { return null; }
    @Override
    public Set<Entry<Integer, String>> entrySet() { return null; }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {}
}