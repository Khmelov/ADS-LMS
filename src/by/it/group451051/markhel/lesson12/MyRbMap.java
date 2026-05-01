package by.it.group451051.markhel.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.color = RED;
        }
    }

    private Node root;
    private int size;

    // ========== Вспомогательные методы для Red-Black Tree ==========

    private Node parentOf(Node node) { return node == null ? null : node.parent; }
    private boolean isRed(Node node) { return node != null && node.color == RED; }
    private boolean isBlack(Node node) { return node == null || node.color == BLACK; }
    private void setColor(Node node, boolean color) { if (node != null) node.color = color; }
    private Node leftOf(Node node) { return node == null ? null : node.left; }
    private Node rightOf(Node node) { return node == null ? null : node.right; }

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

    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) root = x;
        else if (y == y.parent.left) y.parent.left = x;
        else y.parent.right = x;
        x.right = y;
        y.parent = x;
    }

    private void fixAfterInsertion(Node x) {
        x.color = RED;
        while (x != null && x != root && isRed(parentOf(x))) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node y = rightOf(parentOf(parentOf(x)));
                if (isRed(y)) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Node y = leftOf(parentOf(parentOf(x)));
                if (isRed(y)) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }

    private Node putRec(Node node, Integer key, String value, Node parent) {
        if (node == null) {
            Node newNode = new Node(key, value);
            newNode.parent = parent;
            size++;
            return newNode;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = putRec(node.left, key, value, node);
        } else if (cmp > 0) {
            node.right = putRec(node.right, key, value, node);
        } else {
            node.value = value;
        }
        return node;
    }

    private Node getNode(Node node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node;
        }
        return null;
    }

    private Node minNode(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node maxNode(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    private void deleteNode(Node node) {
        if (node == null) return;
        Node replacement;
        Node child;
        boolean originalColor = node.color;

        if (node.left != null && node.right != null) {
            Node successor = minNode(node.right);
            node.key = successor.key;
            node.value = successor.value;
            deleteNode(successor);
            return;
        }
        child = (node.left != null) ? node.left : node.right;
        if (child != null) {
            replacement = child;
            if (node.parent == null) root = child;
            else if (node == node.parent.left) node.parent.left = child;
            else node.parent.right = child;
            child.parent = node.parent;
            originalColor = node.color;
            if (originalColor == BLACK) {
                fixAfterDeletion(child);
            }
        } else {
            replacement = null;
            if (node.parent == null) {
                root = null;
            } else {
                if (originalColor == BLACK) {
                    fixAfterDeletion(node);
                }
                if (node.parent != null) {
                    if (node == node.parent.left) node.parent.left = null;
                    else node.parent.right = null;
                }
            }
        }
    }

    private void fixAfterDeletion(Node x) {
        while (x != root && isBlack(x)) {
            if (x == leftOf(parentOf(x))) {
                Node sibling = rightOf(parentOf(x));
                if (isRed(sibling)) {
                    setColor(sibling, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sibling = rightOf(parentOf(x));
                }
                if (isBlack(leftOf(sibling)) && isBlack(rightOf(sibling))) {
                    setColor(sibling, RED);
                    x = parentOf(x);
                } else {
                    if (isBlack(rightOf(sibling))) {
                        setColor(leftOf(sibling), BLACK);
                        setColor(sibling, RED);
                        rotateRight(sibling);
                        sibling = rightOf(parentOf(x));
                    }
                    setColor(sibling, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sibling), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else {
                Node sibling = leftOf(parentOf(x));
                if (isRed(sibling)) {
                    setColor(sibling, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sibling = leftOf(parentOf(x));
                }
                if (isBlack(rightOf(sibling)) && isBlack(leftOf(sibling))) {
                    setColor(sibling, RED);
                    x = parentOf(x);
                } else {
                    if (isBlack(leftOf(sibling))) {
                        setColor(rightOf(sibling), BLACK);
                        setColor(sibling, RED);
                        rotateLeft(sibling);
                        sibling = leftOf(parentOf(x));
                    }
                    setColor(sibling, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sibling), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    private boolean colorOf(Node node) { return node == null ? BLACK : node.color; }

    // ========== Реализация SortedMap ==========

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        Node existing = getNode(root, key);
        String oldValue = existing == null ? null : existing.value;
        root = putRec(root, key, value, null);
        if (existing == null) {
            fixAfterInsertion(getNode(root, key));
        }
        return oldValue;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = getNode(root, (Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return getNode(root, (Integer) key) != null;
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
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer k = (Integer) key;
        Node node = getNode(root, k);
        if (node == null) return null;
        String oldValue = node.value;
        deleteNode(node);
        size--;
        return oldValue;
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMapRec(root, toKey, result);
        return result;
    }

    private void headMapRec(Node node, Integer toKey, MyRbMap map) {
        if (node == null) return;
        headMapRec(node.left, toKey, map);
        if (node.key.compareTo(toKey) < 0) {
            map.put(node.key, node.value);
        }
        headMapRec(node.right, toKey, map);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMapRec(root, fromKey, result);
        return result;
    }

    private void tailMapRec(Node node, Integer fromKey, MyRbMap map) {
        if (node == null) return;
        tailMapRec(node.left, fromKey, map);
        if (node.key.compareTo(fromKey) >= 0) {
            map.put(node.key, node.value);
        }
        tailMapRec(node.right, fromKey, map);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return minNode(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return maxNode(root).key;
    }

    @Override
    public Comparator<? super Integer> comparator() { return null; }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }

    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }

    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }

    // ========== toString ==========
    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inorderToString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    private void inorderToString(Node node, StringBuilder sb) {
        if (node == null) return;
        inorderToString(node.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        inorderToString(node.right, sb);
    }
}