package by.it.group451051.pekarskij.lesson12;

import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import java.util.Map;

public class MySplayMap implements NavigableMap<Integer, String> {

    // узел дерева
    private static class Node {
        Integer key; String value; Node left, right;
        Node(Integer k, String v) { key = k; value = v; }
    }

    private Node root;
    private int size;

    // правое вращение
    private Node rotateRight(Node x) { Node y = x.left; x.left = y.right; y.right = x; return y; }
    // левое вращение
    private Node rotateLeft(Node x) { Node y = x.right; x.right = y.left; y.left = x; return y; }

    // подъем узла к корню
    private Node splay(Node h, Integer key) {
        if (h == null) return null;
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            if (h.left == null) return h;
            if (key.compareTo(h.left.key) < 0) { h.left.left = splay(h.left.left, key); h = rotateRight(h); }
            else if (key.compareTo(h.left.key) > 0) { h.left.right = splay(h.left.right, key); if (h.left.right != null) h.left = rotateLeft(h.left); }
            return h.left != null ? rotateRight(h) : h;
        } else if (cmp > 0) {
            if (h.right == null) return h;
            if (key.compareTo(h.right.key) < 0) { h.right.left = splay(h.right.left, key); if (h.right.left != null) h.right = rotateRight(h.right); }
            else if (key.compareTo(h.right.key) > 0) { h.right.right = splay(h.right.right, key); h = rotateLeft(h); }
            return h.right != null ? rotateLeft(h) : h;
        }
        return h;
    }

    // поиск элемента
    private Node get(Node h, Integer key) {
        if (h == null) return null;
        root = splay(h, key);
        return key.equals(root.key) ? root : null;
    }

    // добавление элемента
    private Node put(Node h, Integer key, String value) {
        if (h == null) { 
            size++; // новый узел
            return new Node(key, value); 
        }
        root = splay(h, key);
        int cmp = key.compareTo(root.key);
        if (cmp == 0) { 
            root.value = value; // обновление значения
            return root; 
        }
        size++; // вставка нового ключа
        Node n = new Node(key, value);
        if (cmp < 0) { 
            n.left = root.left; 
            n.right = root; 
            root.left = null; 
        } else { 
            n.right = root.right; 
            n.left = root; 
            root.right = null; 
        }
        return n;
    }

    // удаление элемента
    private Node remove(Node h, Integer key) {
        if (h == null) return null;
        root = splay(h, key);
        if (!key.equals(root.key)) return root; // ключ не найден
        size--;
        if (root.left == null) return root.right;
        Node newRoot = splay(root.left, key);
        newRoot.right = root.right;
        return newRoot;
    }

    // поиск минимума
    private Node findMin(Node h) { while (h != null && h.left != null) h = h.left; return h; }
    // поиск максимума
    private Node findMax(Node h) { while (h != null && h.right != null) h = h.right; return h; }

    // поиск floor
    private Node findFloor(Node h, Integer key) {
        if (h == null) return null;
        int cmp = key.compareTo(h.key);
        if (cmp == 0) return h;
        if (cmp < 0) return findFloor(h.left, key);
        Node r = findFloor(h.right, key);
        return r != null ? r : h;
    }

    // поиск ceiling
    private Node findCeiling(Node h, Integer key) {
        if (h == null) return null;
        int cmp = key.compareTo(h.key);
        if (cmp == 0) return h;
        if (cmp > 0) return findCeiling(h.right, key);
        Node l = findCeiling(h.left, key);
        return l != null ? l : h;
    }

    // обход дерева
    private void inorder(Node h, StringBuilder sb) {
        if (h == null) return;
        inorder(h.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(h.key).append("=").append(h.value);
        inorder(h.right, sb);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        return sb.append("}").toString();
    }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public void clear() { root = null; size = 0; }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        Node old = get(root, key);
        String prev = old == null ? null : old.value;
        root = put(root, key, value);
        return prev;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());
    }

    @Override
    public String get(Object key) {
        if (key == null) return null;
        Node n = get(root, (Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public String remove(Object key) {
        if (key == null) return null;
        Node old = get(root, (Integer) key);
        if (old == null) return null;
        String prev = old.value;
        root = remove(root, (Integer) key);
        return prev;
    }

    @Override public boolean containsKey(Object key) { return get(root, (Integer) key) != null; }
    @Override public boolean containsValue(Object value) {
        if (value == null) return false;
        for (Node h = root; h != null; ) {
            if (value.equals(h.value)) return true;
            if (h.left != null) { Node p = h.left; while (p.right != null) p = p.right; h = p; }
            else { if (value.equals(h.value)) return true; h = h.right; }
        }
        return false;
    }

    @Override public Integer firstKey() { if (isEmpty()) throw new java.util.NoSuchElementException(); return findMin(root).key; }
    @Override public Integer lastKey() { if (isEmpty()) throw new java.util.NoSuchElementException(); return findMax(root).key; }

    @Override public Integer lowerKey(Integer key) { if (key == null) throw new NullPointerException(); Node n = findLower(root, key); return n == null ? null : n.key; }
    @Override public Integer floorKey(Integer key) { if (key == null) throw new NullPointerException(); Node n = findFloor(root, key); return n == null ? null : n.key; }
    @Override public Integer ceilingKey(Integer key) { if (key == null) throw new NullPointerException(); Node n = findCeiling(root, key); return n == null ? null : n.key; }
    @Override public Integer higherKey(Integer key) { if (key == null) throw new NullPointerException(); Node n = findHigher(root, key); return n == null ? null : n.key; }

    private Node findLower(Node h, Integer key) {
        if (h == null) return null;
        int cmp = key.compareTo(h.key);
        if (cmp <= 0) return findLower(h.left, key);
        Node r = findLower(h.right, key);
        return r != null ? r : h;
    }
    private Node findHigher(Node h, Integer key) {
        if (h == null) return null;
        int cmp = key.compareTo(h.key);
        if (cmp >= 0) return findHigher(h.right, key);
        Node l = findHigher(h.left, key);
        return l != null ? l : h;
    }

    @Override public NavigableMap<Integer, String> headMap(Integer toKey) { return subMap(null, toKey, false, false); }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey) { return subMap(fromKey, null, true, false); }
    
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return subMap(null, toKey, false, inclusive);
    }
    
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return subMap(fromKey, null, inclusive, false);
    }
    
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, toKey, true, false);
    }
    
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return subMap(fromKey, toKey, fromInclusive, toInclusive);
    }
    
    private NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey, boolean fromInc, boolean toInc) {
        MySplayMap sub = new MySplayMap();
        collectRange(root, sub, fromKey, toKey, fromInc, toInc);
        return sub;
    }
    
    private void collectRange(Node h, MySplayMap sub, Integer lo, Integer hi, boolean loInc, boolean hiInc) {
        if (h == null) return;
        if (lo == null || h.key.compareTo(lo) > 0 || (loInc && h.key.equals(lo))) 
            collectRange(h.left, sub, lo, hi, loInc, hiInc);
        boolean include = true;
        if (lo != null && h.key.compareTo(lo) < 0) include = false;
        if (lo != null && h.key.equals(lo) && !loInc) include = false;
        if (hi != null && h.key.compareTo(hi) > 0) include = false;
        if (hi != null && h.key.equals(hi) && !hiInc) include = false;
        if (include) sub.put(h.key, h.value);
        if (hi == null || h.key.compareTo(hi) < 0 || (hiInc && h.key.equals(hi))) 
            collectRange(h.right, sub, lo, hi, loInc, hiInc);
    }

    @Override public Comparator<? super Integer> comparator() { return null; }
    @Override public Set<Integer> keySet() { return null; }
    @Override public Collection<String> values() { return null; }
    @Override public Set<Map.Entry<Integer, String>> entrySet() { return null; }
    @Override public Map.Entry<Integer, String> firstEntry() { return null; }
    @Override public Map.Entry<Integer, String> lastEntry() { return null; }
    @Override public Map.Entry<Integer, String> lowerEntry(Integer key) { return null; }
    @Override public Map.Entry<Integer, String> floorEntry(Integer key) { return null; }
    @Override public Map.Entry<Integer, String> ceilingEntry(Integer key) { return null; }
    @Override public Map.Entry<Integer, String> higherEntry(Integer key) { return null; }
    @Override public Map.Entry<Integer, String> pollFirstEntry() { return null; }
    @Override public Map.Entry<Integer, String> pollLastEntry() { return null; }
    @Override public NavigableMap<Integer, String> descendingMap() { return null; }
    @Override public NavigableSet<Integer> navigableKeySet() { return null; }
    @Override public NavigableSet<Integer> descendingKeySet() { return null; }
}