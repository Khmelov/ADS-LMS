package by.it.group451051.teterukov.lessons_09_15.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private static class Node {
        int     key;
        String  value;
        boolean color;
        Node    left, right, parent;

        Node(int key, String value, boolean color, Node nil) {
            this.key    = key;
            this.value  = value;
            this.color  = color;
            this.left   = nil;
            this.right  = nil;
            this.parent = nil;
        }
    }

    private final Node NIL;
    private Node  root;
    private int   size;

    public MyRbMap() {
        NIL        = new Node(0, null, BLACK, null);
        NIL.left   = NIL;
        NIL.right  = NIL;
        NIL.parent = NIL;
        root       = NIL;
    }

    private void rotateLeft(Node x) {
        Node y   = x.right;
        x.right  = y.left;
        if (y.left != NIL) y.left.parent = x;
        y.parent = x.parent;
        if      (x.parent == NIL)       root         = y;
        else if (x == x.parent.left)    x.parent.left  = y;
        else                            x.parent.right = y;
        y.left   = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y   = x.left;
        x.left   = y.right;
        if (y.right != NIL) y.right.parent = x;
        y.parent = x.parent;
        if      (x.parent == NIL)       root          = y;
        else if (x == x.parent.right)   x.parent.right = y;
        else                            x.parent.left  = y;
        y.right  = x;
        x.parent = y;
    }

    @Override
    public String put(Integer key, String value) {
        Node cur = root;
        Node par = NIL;
        while (cur != NIL) {
            par = cur;
            int cmp = key.compareTo(cur.key);
            if      (cmp < 0) cur = cur.left;
            else if (cmp > 0) cur = cur.right;
            else {
                String old = cur.value;
                cur.value  = value;
                return old;
            }
        }
        Node z   = new Node(key, value, RED, NIL);
        z.parent = par;
        if      (par == NIL)              root       = z;
        else if (key < par.key)           par.left   = z;
        else                              par.right  = z;
        size++;
        insertFix(z);
        return null;
    }

    private void insertFix(Node z) {
        while (z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (y.color == RED) {
                    z.parent.color         = BLACK;
                    y.color                = BLACK;
                    z.parent.parent.color  = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color        = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y.color == RED) {
                    z.parent.color        = BLACK;
                    y.color               = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color        = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    private void transplant(Node u, Node v) {
        if      (u.parent == NIL)         root          = v;
        else if (u == u.parent.left)      u.parent.left  = v;
        else                              u.parent.right = v;
        v.parent = u.parent;
    }

    private Node minimum(Node n) {
        while (n.left != NIL) n = n.left;
        return n;
    }

    private Node maximum(Node n) {
        while (n.right != NIL) n = n.right;
        return n;
    }

    @Override
    public String remove(Object key) {
        Node z = find(root, (Integer) key);
        if (z == NIL) return null;
        String old = z.value;
        delete(z);
        size--;
        return old;
    }

    private void delete(Node z) {
        Node y = z;
        Node x;
        boolean origColor = y.color;

        if (z.left == NIL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y         = minimum(z.right);
            origColor = y.color;
            x         = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right         = z.right;
                y.right.parent  = y;
            }
            transplant(z, y);
            y.left         = z.left;
            y.left.parent  = y;
            y.color        = z.color;
        }
        if (origColor == BLACK) deleteFix(x);
    }

    private void deleteFix(Node x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.color == RED) {
                    w.color         = BLACK;
                    x.parent.color  = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.right.color == BLACK) {
                        w.left.color = BLACK;
                        w.color      = RED;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.color         = x.parent.color;
                    x.parent.color  = BLACK;
                    w.right.color   = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.color == RED) {
                    w.color        = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color       = RED;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.color        = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color   = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    private Node find(Node n, int key) {
        while (n != NIL) {
            int cmp = Integer.compare(key, n.key);
            if      (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else              return n;
        }
        return NIL;
    }

    @Override
    public String get(Object key) {
        Node n = find(root, (Integer) key);
        return n == NIL ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(root, (Integer) key) != NIL;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node n, Object value) {
        if (n == NIL) return false;
        if (value == null ? n.value == null : value.equals(n.value)) return true;
        return containsValue(n.left, value) || containsValue(n.right, value);
    }

    @Override public int     size()    { return size; }
    @Override public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        root = NIL;
        size = 0;
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == NIL) return;
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
        if (root == NIL) throw new NoSuchElementException();
        return minimum(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == NIL) throw new NoSuchElementException();
        return maximum(root).key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap result = new MyRbMap();
        headMap(root, toKey, result);
        return result;
    }

    private void headMap(Node n, int toKey, MyRbMap result) {
        if (n == NIL) return;
        if (n.key < toKey) {
            result.put(n.key, n.value);
            headMap(n.left,  toKey, result);
            headMap(n.right, toKey, result);
        } else {
            headMap(n.left, toKey, result);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap result = new MyRbMap();
        tailMap(root, fromKey, result);
        return result;
    }

    private void tailMap(Node n, int fromKey, MyRbMap result) {
        if (n == NIL) return;
        if (n.key >= fromKey) {
            result.put(n.key, n.value);
            tailMap(n.left,  fromKey, result);
            tailMap(n.right, fromKey, result);
        } else {
            tailMap(n.right, fromKey, result);
        }
    }

    @Override public Comparator<? super Integer> comparator()         { return null; }
    @Override public SortedMap<Integer,String> subMap(Integer f,
                                                      Integer t)      { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer,
            ? extends String> m)                 { throw new UnsupportedOperationException(); }
    @Override public Set<Integer>               keySet()              { throw new UnsupportedOperationException(); }
    @Override public Collection<String>         values()              { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer,String>> entrySet()            { throw new UnsupportedOperationException(); }
}