package by.it.group451051.shiman.lesson12;

import java.util.*;

/**
 * Реализация NavigableMap на основе Splay-дерева.
 * Все обязательные методы реализованы и проходят тесты.
 */
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
    private int size;

    // ------------------------------------------------------------
    // Вспомогательные методы (повороты, splay, поиск)
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

    // Поиск ключа с последующим splay последнего посещённого узла
    private Node find(int key) {
        Node cur = root;
        Node last = null;
        while (cur != null) {
            last = cur;
            if (key < cur.key) cur = cur.left;
            else if (key > cur.key) cur = cur.right;
            else {
                splay(cur);
                return cur;
            }
        }
        if (last != null) splay(last);
        return null;
    }

    // Рекурсивный обход для построения подотображений
    private void buildSubMap(Node node,
                             Integer from, boolean fromInc,
                             Integer to, boolean toInc,
                             MySplayMap result) {
        if (node == null) return;
        buildSubMap(node.left, from, fromInc, to, toInc, result);
        boolean include = true;
        if (from != null) {
            int cmp = node.key - from;
            if (cmp < 0) include = false;
            else if (cmp == 0 && !fromInc) include = false;
        }
        if (to != null && include) {
            int cmp = node.key - to;
            if (cmp > 0) include = false;
            else if (cmp == 0 && !toInc) include = false;
        }
        if (include) result.put(node.key, node.value);
        buildSubMap(node.right, from, fromInc, to, toInc, result);
    }

    // ------------------------------------------------------------
    // Основные методы Map
    // ------------------------------------------------------------
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        Node existing = find(key);
        if (existing != null) {
            String old = existing.value;
            existing.value = value;
            return old;
        }
        Node newNode = new Node(key, value);
        if (root == null) {
            root = newNode;
        } else {
            Node cur = root;
            while (true) {
                if (key < cur.key) {
                    if (cur.left == null) {
                        cur.left = newNode;
                        newNode.parent = cur;
                        break;
                    }
                    cur = cur.left;
                } else {
                    if (cur.right == null) {
                        cur.right = newNode;
                        newNode.parent = cur;
                        break;
                    }
                    cur = cur.right;
                }
            }
            splay(newNode);
        }
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = find((Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = find((Integer) key);
        if (node == null) return null;
        String oldValue = node.value;

        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        } else if (node.right == null) {
            root = node.left;
            if (root != null) root.parent = null;
        } else {
            Node leftTree = node.left;
            Node rightTree = node.right;
            leftTree.parent = null;
            rightTree.parent = null;
            root = leftTree;
            Node maxLeft = leftTree;
            while (maxLeft.right != null) maxLeft = maxLeft.right;
            splay(maxLeft);
            maxLeft.right = rightTree;
            rightTree.parent = maxLeft;
            root = maxLeft;
        }
        size--;
        return oldValue;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node node, Object value) {
        if (node == null) return false;
        if (node.value.equals(value)) return true;
        return containsValueRec(node.left, value) || containsValueRec(node.right, value);
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

    // ------------------------------------------------------------
    // Методы NavigableMap / SortedMap
    // ------------------------------------------------------------
    @Override
    public Integer firstKey() {
        if (root == null) return null;
        Node cur = root;
        while (cur.left != null) cur = cur.left;
        splay(cur);
        return cur.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node cur = root;
        while (cur.right != null) cur = cur.right;
        splay(cur);
        return cur.key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (key > cur.key) {
                result = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return result;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (key >= cur.key) {
                result = cur.key;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return result;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (key <= cur.key) {
                result = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return result;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node cur = root;
        Integer result = null;
        while (cur != null) {
            if (key < cur.key) {
                result = cur.key;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return result;
    }

    // ------------------------------------------------------------
    // Методы subMap, headMap, tailMap
    // ------------------------------------------------------------
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap sub = new MySplayMap();
        buildSubMap(root, null, false, toKey, inclusive, sub);
        return sub;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap sub = new MySplayMap();
        buildSubMap(root, fromKey, inclusive, null, false, sub);
        return sub;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive,
                                                Integer toKey, boolean toInclusive) {
        MySplayMap sub = new MySplayMap();
        buildSubMap(root, fromKey, fromInclusive, toKey, toInclusive, sub);
        return sub;
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
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    // ------------------------------------------------------------
    // Методы, возвращающие Entry (минимальная реализация)
    // ------------------------------------------------------------
    private static class SimpleEntry implements Map.Entry<Integer, String> {
        private final Integer key;
        private final String value;
        SimpleEntry(Integer key, String value) { this.key = key; this.value = value; }
        public Integer getKey() { return key; }
        public String getValue() { return value; }
        public String setValue(String value) { throw new UnsupportedOperationException(); }
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        Integer k = lowerKey(key);
        return k == null ? null : new SimpleEntry(k, get(k));
    }
    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        Integer k = floorKey(key);
        return k == null ? null : new SimpleEntry(k, get(k));
    }
    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        Integer k = ceilingKey(key);
        return k == null ? null : new SimpleEntry(k, get(k));
    }
    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        Integer k = higherKey(key);
        return k == null ? null : new SimpleEntry(k, get(k));
    }
    @Override
    public Map.Entry<Integer, String> firstEntry() {
        Integer k = firstKey();
        return k == null ? null : new SimpleEntry(k, get(k));
    }
    @Override
    public Map.Entry<Integer, String> lastEntry() {
        Integer k = lastKey();
        return k == null ? null : new SimpleEntry(k, get(k));
    }
    @Override
    public Map.Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override
    public Map.Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }

    // ------------------------------------------------------------
    // Остальные методы NavigableMap (заглушки)
    // ------------------------------------------------------------
    @Override
    public Comparator<? super Integer> comparator() { return null; }
    @Override
    public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }

    // ------------------------------------------------------------
    // toString (вывод в порядке возрастания ключей)
    // ------------------------------------------------------------
    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inorder(sb, root);
        sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inorder(StringBuilder sb, Node node) {
        if (node == null) return;
        inorder(sb, node.left);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inorder(sb, node.right);
    }
}