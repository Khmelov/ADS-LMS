package by.it.group451051.teterukov.lessons_09_15.lesson12;

import java.util.*;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        int    key;
        String value;
        Node   left, right, parent;

        Node(int key, String value) {
            this.key   = key;
            this.value = value;
        }
    }

    private Node root;
    private int  size;

    private void rotateRight(Node x) {
        Node y    = x.left;
        x.left    = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent  = x.parent;
        if      (x.parent == null)           root            = y;
        else if (x == x.parent.left)         x.parent.left   = y;
        else                                 x.parent.right  = y;
        y.right   = x;
        x.parent  = y;
    }

    private void rotateLeft(Node x) {
        Node y    = x.right;
        x.right   = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent  = x.parent;
        if      (x.parent == null)           root            = y;
        else if (x == x.parent.left)         x.parent.left   = y;
        else                                 x.parent.right  = y;
        y.left    = x;
        x.parent  = y;
    }

    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                if (x == p.left) rotateRight(p);
                else             rotateLeft(p);
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

    private Node findNode(int key) {
        Node cur = root;
        while (cur != null) {
            int cmp = Integer.compare(key, cur.key);
            if      (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else              return cur;
        }
        return null;
    }

    private Node minimum(Node n) {
        if (n == null) return null;
        while (n.left != null) n = n.left;
        return n;
    }

    private Node maximum(Node n) {
        if (n == null) return null;
        while (n.right != null) n = n.right;
        return n;
    }

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return null;
        }
        Node cur = root, par = null;
        while (cur != null) {
            par = cur;
            int cmp = Integer.compare(key, cur.key);
            if      (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else {
                String old = cur.value;
                cur.value  = value;
                splay(cur);
                return old;
            }
        }
        Node z   = new Node(key, value);
        z.parent = par;
        if (key < par.key) par.left  = z;
        else               par.right = z;
        splay(z);
        size++;
        return null;
    }

    @Override
    public String get(Object key) {
        Node n = findNode((Integer) key);
        if (n == null) return null;
        splay(n);
        return n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        Node n = findNode((Integer) key);
        if (n != null) splay(n);
        return n != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return containsValue(n.left, value) || containsValue(n.right, value);
    }

    @Override
    public String remove(Object key) {
        Node n = findNode((Integer) key);
        if (n == null) return null;
        String old = n.value;
        splay(n);
        Node left  = n.left;
        Node right = n.right;
        if (left  != null) left.parent  = null;
        if (right != null) right.parent = null;
        if (left == null) {
            root = right;
        } else if (right == null) {
            root = left;
        } else {
            Node maxLeft = maximum(left);
            root = left;
            splay(maxLeft);
            root.right    = right;
            right.parent  = root;
        }
        size--;
        return old;
    }

    @Override public int     size()    { return size; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append("=").append(n.value);
        inOrder(n.right, sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrder(root, sb);
        return sb.append("}").toString();
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node m = minimum(root);
        splay(m);
        return m.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node m = maximum(root);
        splay(m);
        return m.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        collectLess(root, toKey, result, false);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        collectLess(root, toKey, result, inclusive);
        return result;
    }

    private void collectLess(Node n, int bound, MySplayMap result, boolean inclusive) {
        if (n == null) return;
        int cmp = Integer.compare(n.key, bound);
        if (cmp < 0 || (cmp == 0 && inclusive)) {
            result.put(n.key, n.value);
            collectLess(n.left,  bound, result, inclusive);
            collectLess(n.right, bound, result, inclusive);
        } else {
            collectLess(n.left, bound, result, inclusive);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        collectGreater(root, fromKey, result, true);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        MySplayMap result = new MySplayMap();
        collectGreater(root, fromKey, result, inclusive);
        return result;
    }

    private void collectGreater(Node n, int bound, MySplayMap result, boolean inclusive) {
        if (n == null) return;
        int cmp = Integer.compare(n.key, bound);
        if (cmp > 0 || (cmp == 0 && inclusive)) {
            result.put(n.key, n.value);
            collectGreater(n.left,  bound, result, inclusive);
            collectGreater(n.right, bound, result, inclusive);
        } else {
            collectGreater(n.right, bound, result, inclusive);
        }
    }

    @Override
    public Integer lowerKey(Integer key) {
        Node result = null, cur = root;
        while (cur != null) {
            if (cur.key < key) { result = cur; cur = cur.right; }
            else                cur = cur.left;
        }
        return result == null ? null : result.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        Node result = null, cur = root;
        while (cur != null) {
            int cmp = Integer.compare(key, cur.key);
            if      (cmp > 0) { result = cur; cur = cur.right; }
            else if (cmp < 0)   cur = cur.left;
            else                return cur.key;
        }
        return result == null ? null : result.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        Node result = null, cur = root;
        while (cur != null) {
            int cmp = Integer.compare(key, cur.key);
            if      (cmp < 0) { result = cur; cur = cur.left; }
            else if (cmp > 0)   cur = cur.right;
            else                return cur.key;
        }
        return result == null ? null : result.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        Node result = null, cur = root;
        while (cur != null) {
            if (cur.key > key) { result = cur; cur = cur.left; }
            else                cur = cur.right;
        }
        return result == null ? null : result.key;
    }


    @Override public Comparator<? super Integer> comparator()               { return null; }
    @Override public SortedMap<Integer,String> subMap(Integer f, Integer t) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m){ throw new UnsupportedOperationException(); }
    @Override public Set<Integer>               keySet()                    { throw new UnsupportedOperationException(); }
    @Override public Collection<String>         values()                    { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer,String>> entrySet()                  { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer,String> lowerEntry(Integer key)          { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer,String> floorEntry(Integer key)          { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer,String> ceilingEntry(Integer key)        { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer,String> higherEntry(Integer key)         { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer,String> firstEntry()                     { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer,String> lastEntry()                      { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer,String> pollFirstEntry()                 { throw new UnsupportedOperationException(); }
    @Override public Entry<Integer,String> pollLastEntry()                  { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer,String> descendingMap()           { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer>        navigableKeySet()         { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer>        descendingKeySet()        { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer,String> subMap(Integer f,
                                                         boolean fi, Integer t, boolean ti)                 { throw new UnsupportedOperationException(); }
}