package by.it.group451051.markhel.lesson12;

import java.util.*;

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
    private int size;

    // ======================== Splay операции ========================

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
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.right = x;
        x.parent = y;
    }

    private void splay(Node x) {
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

    private Node findSplay(Integer key) {
        Node node = root;
        Node last = null;
        while (node != null) {
            last = node;
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else {
                splay(node);
                return node;
            }
        }
        if (last != null) splay(last);
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

    private void inorderCollect(Node node, List<Entry<Integer, String>> list) {
        if (node == null) return;
        inorderCollect(node.left, list);
        list.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
        inorderCollect(node.right, list);
    }

    private boolean containsValueRec(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValueRec(node.left, value) || containsValueRec(node.right, value);
    }

    // ======================== Основные методы Map ========================

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        Node existing = findSplay(key);
        if (existing != null && existing.key.equals(key)) {
            String old = existing.value;
            existing.value = value;
            return old;
        }
        Node newNode = new Node(key, value);
        Node parent = root;
        int cmp = key.compareTo(parent.key);
        if (cmp < 0) {
            newNode.left = parent.left;
            if (parent.left != null) parent.left.parent = newNode;
            newNode.right = parent;
            parent.left = null;
        } else {
            newNode.right = parent.right;
            if (parent.right != null) parent.right.parent = newNode;
            newNode.left = parent;
            parent.right = null;
        }
        newNode.parent = null;
        parent.parent = newNode;
        root = newNode;
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findSplay((Integer) key);
        return (node != null && node.key.equals(key)) ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        Node node = findSplay((Integer) key);
        return node != null && node.key.equals(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = findSplay((Integer) key);
        if (node == null || !node.key.equals(key)) return null;
        String oldValue = node.value;
        Node leftTree = node.left;
        Node rightTree = node.right;
        if (leftTree == null) {
            root = rightTree;
            if (rightTree != null) rightTree.parent = null;
        } else {
            Node maxLeft = maxNode(leftTree);
            splay(maxLeft);
            maxLeft.right = rightTree;
            if (rightTree != null) rightTree.parent = maxLeft;
            root = maxLeft;
            root.parent = null;
        }
        size--;
        return oldValue;
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

    // ======================== SortedMap методы ========================

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = minNode(root);
        splay(node);
        return node.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = maxNode(root);
        splay(node);
        return node.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node node = root;
        Integer result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp > 0) {
                result = node.key;
                node = node.right;
            } else {
                node = node.left;
            }
        }
        if (result != null) findSplay(result);
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node node = root;
        Integer result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp >= 0) {
                result = node.key;
                node = node.right;
            } else {
                node = node.left;
            }
        }
        if (result != null) findSplay(result);
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node node = root;
        Integer result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp <= 0) {
                result = node.key;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        if (result != null) findSplay(result);
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node node = root;
        Integer result = null;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                result = node.key;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        if (result != null) findSplay(result);
        return result;
    }

    // ======================== NavigableMap методы ========================

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        Integer k = lowerKey(key);
        return k == null ? null : entryForKey(k);
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        Integer k = floorKey(key);
        return k == null ? null : entryForKey(k);
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        Integer k = ceilingKey(key);
        return k == null ? null : entryForKey(k);
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        Integer k = higherKey(key);
        return k == null ? null : entryForKey(k);
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        if (root == null) return null;
        Node node = minNode(root);
        splay(node);
        return new AbstractMap.SimpleEntry<>(node.key, node.value);
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        if (root == null) return null;
        Node node = maxNode(root);
        splay(node);
        return new AbstractMap.SimpleEntry<>(node.key, node.value);
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        if (root == null) return null;
        Entry<Integer, String> first = firstEntry();
        if (first != null) remove(first.getKey());
        return first;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        if (root == null) return null;
        Entry<Integer, String> last = lastEntry();
        if (last != null) remove(last.getKey());
        return last;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        // не требуется по заданию, но для компиляции – заглушка
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        // можно реализовать через TreeSet, но проще заглушка
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException();
    }

    // headMap / tailMap с булевыми флагами
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        for (Entry<Integer, String> entry : entrySet()) {
            int cmp = entry.getKey().compareTo(toKey);
            if (cmp < 0 || (inclusive && cmp == 0)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        for (Entry<Integer, String> entry : entrySet()) {
            int cmp = entry.getKey().compareTo(fromKey);
            if (cmp > 0 || (inclusive && cmp == 0)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    // ======================== Вспомогательные методы ========================

    private Entry<Integer, String> entryForKey(Integer key) {
        Node node = findSplay(key);
        if (node != null && node.key.equals(key)) {
            return new AbstractMap.SimpleEntry<>(node.key, node.value);
        }
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
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
        List<Entry<Integer, String>> list = new ArrayList<>();
        inorderCollect(root, list);
        return new AbstractSet<Entry<Integer, String>>() {
            @Override
            public Iterator<Entry<Integer, String>> iterator() {
                return list.iterator();
            }
            @Override
            public int size() {
                return size;
            }
        };
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
    }

    // ======================== toString ========================

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