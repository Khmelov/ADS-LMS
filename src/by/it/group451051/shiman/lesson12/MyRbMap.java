package by.it.group451051.shiman.lesson12;

import java.util.*;

/**
 * Реализация SortedMap на основе красно-черного дерева.
 * Не использует стандартные коллекции Java (кроме интерфейса и базовых типов).
 */
public class MyRbMap implements SortedMap<Integer, String> {
    private Node root;
    private int size;

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.color = RED;
        }
    }

    // ------------------------------------------------------------
    // Вспомогательные методы для работы с деревом
    // ------------------------------------------------------------

    private void rotateLeft(Node x) {
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
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private void fixAfterInsert(Node z) {
        while (z != root && colorOf(parentOf(z)) == RED) {
            Node grand = parentOf(parentOf(z));
            if (parentOf(z) == grand.left) {
                Node y = grand.right;
                if (colorOf(y) == RED) {
                    setColor(parentOf(z), BLACK);
                    setColor(y, BLACK);
                    setColor(grand, RED);
                    z = grand;
                } else {
                    if (z == parentOf(z).right) {
                        z = parentOf(z);
                        rotateLeft(z);
                    }
                    setColor(parentOf(z), BLACK);
                    setColor(grand, RED);
                    rotateRight(grand);
                }
            } else {
                Node y = grand.left;
                if (colorOf(y) == RED) {
                    setColor(parentOf(z), BLACK);
                    setColor(y, BLACK);
                    setColor(grand, RED);
                    z = grand;
                } else {
                    if (z == parentOf(z).left) {
                        z = parentOf(z);
                        rotateRight(z);
                    }
                    setColor(parentOf(z), BLACK);
                    setColor(grand, RED);
                    rotateLeft(grand);
                }
            }
        }
        setColor(root, BLACK);
    }

    private Node findNode(Integer key) {
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else return cur;
        }
        return null;
    }

    private Node minimum(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    private void fixAfterDelete(Node x, Node parentOfX) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == parentOfX.left) {
                Node w = parentOfX.right;
                if (colorOf(w) == RED) {
                    setColor(w, BLACK);
                    setColor(parentOfX, RED);
                    rotateLeft(parentOfX);
                    w = parentOfX.right;
                }
                if (colorOf(w.left) == BLACK && colorOf(w.right) == BLACK) {
                    setColor(w, RED);
                    x = parentOfX;
                    parentOfX = parentOf(x);
                } else {
                    if (colorOf(w.right) == BLACK) {
                        setColor(w.left, BLACK);
                        setColor(w, RED);
                        rotateRight(w);
                        w = parentOfX.right;
                    }
                    setColor(w, colorOf(parentOfX));
                    setColor(parentOfX, BLACK);
                    setColor(w.right, BLACK);
                    rotateLeft(parentOfX);
                    x = root;
                }
            } else {
                Node w = parentOfX.left;
                if (colorOf(w) == RED) {
                    setColor(w, BLACK);
                    setColor(parentOfX, RED);
                    rotateRight(parentOfX);
                    w = parentOfX.left;
                }
                if (colorOf(w.right) == BLACK && colorOf(w.left) == BLACK) {
                    setColor(w, RED);
                    x = parentOfX;
                    parentOfX = parentOf(x);
                } else {
                    if (colorOf(w.left) == BLACK) {
                        setColor(w.right, BLACK);
                        setColor(w, RED);
                        rotateLeft(w);
                        w = parentOfX.left;
                    }
                    setColor(w, colorOf(parentOfX));
                    setColor(parentOfX, BLACK);
                    setColor(w.left, BLACK);
                    rotateRight(parentOfX);
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    private void deleteNode(Node z) {
        Node y = z;
        Node x = null;
        Node parentOfX = null;
        boolean yOriginalColor = colorOf(y);

        if (z.left == null) {
            x = z.right;
            transplant(z, z.right);
            parentOfX = z.parent;
        } else if (z.right == null) {
            x = z.left;
            transplant(z, z.left);
            parentOfX = z.parent;
        } else {
            y = minimum(z.right);
            yOriginalColor = colorOf(y);
            x = y.right;
            if (y.parent == z) {
                if (x != null) parentOfX = x.parent = y;
                else parentOfX = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
                parentOfX = y.parent;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            setColor(y, colorOf(z));
        }
        if (yOriginalColor == BLACK) fixAfterDelete(x, parentOfX);
    }

    private Node parentOf(Node node) { return node == null ? null : node.parent; }
    private boolean colorOf(Node node) { return node == null ? BLACK : node.color; }
    private void setColor(Node node, boolean color) { if (node != null) node.color = color; }

    private void inorderToString(Node node, StringBuilder sb) {
        if (node == null) return;
        inorderToString(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        inorderToString(node.right, sb);
    }

    private void inorderCollect(Node node, MyRbMap target, Integer bound, boolean lessThan, boolean inclusive) {
        if (node == null) return;
        inorderCollect(node.left, target, bound, lessThan, inclusive);
        int cmp = node.key.compareTo(bound);
        boolean take = lessThan ? (inclusive ? cmp <= 0 : cmp < 0) : (inclusive ? cmp >= 0 : cmp > 0);
        if (take) target.put(node.key, node.value);
        inorderCollect(node.right, target, bound, lessThan, inclusive);
    }

    private Node firstNode() {
        Node cur = root;
        if (cur == null) return null;
        while (cur.left != null) cur = cur.left;
        return cur;
    }

    private Node lastNode() {
        Node cur = root;
        if (cur == null) return null;
        while (cur.right != null) cur = cur.right;
        return cur;
    }

    // ------------------------------------------------------------
    // Реализация методов SortedMap
    // ------------------------------------------------------------

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValueRec(node.left, value) || containsValueRec(node.right, value);
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null || value == null) throw new NullPointerException();
        Node cur = root, parent = null;
        int cmp = 0;
        while (cur != null) {
            parent = cur;
            cmp = key.compareTo(cur.key);
            if (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else {
                String old = cur.value;
                cur.value = value;
                return old;
            }
        }
        Node newNode = new Node(key, value, parent);
        if (parent == null) root = newNode;
        else if (cmp < 0) parent.left = newNode;
        else parent.right = newNode;
        fixAfterInsert(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findNode((Integer) key);
        if (node == null) return null;
        String old = node.value;
        deleteNode(node);
        size--;
        return old;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Entry<? extends Integer, ? extends String> e : map.entrySet())
            put(e.getKey(), e.getValue());
    }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        if (fromKey.compareTo(toKey) >= 0) throw new IllegalArgumentException();
        MyRbMap result = new MyRbMap();
        inorderCollect(root, result, fromKey, false, true);
        MyRbMap filtered = new MyRbMap();
        inorderCollect(result.root, filtered, toKey, true, false);
        return filtered;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        inorderCollect(root, result, toKey, true, false);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        inorderCollect(root, result, fromKey, false, true);
        return result;
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return firstNode().key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return lastNode().key;
    }

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inorderToString(root, sb);
        sb.append("}");
        return sb.toString();
    }
}