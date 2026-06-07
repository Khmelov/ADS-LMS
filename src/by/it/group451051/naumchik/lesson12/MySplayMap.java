package by.it.group451051.naumchik.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

/**
 * Реализация NavigableMap&lt;Integer, String&gt; на основе splay-дерева.
 * Ключи хранятся в отсортированном порядке, при доступе элементы перемещаются в корень.
 * Не использует стандартные коллекции (кроме интерфейсов).
 */
public class MySplayMap implements NavigableMap<Integer, String> {

    private static final class Node {
        int key;
        String value;
        Node left;
        Node right;
        Node parent;

        Node(int key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    public MySplayMap() {}

    // ================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ==================

    /**
     * Возвращает строку в формате {key1=value1, key2=value2, ...}
     * в порядке возрастания ключей.
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        inorder(root, sb);
        sb.setLength(sb.length() - 2); // удаляем последнюю ", "
        sb.append("}");
        return sb.toString();
    }

    private void inorder(Node node, StringBuilder sb) {
        if (node == null) return;
        inorder(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inorder(node.right, sb);
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
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("key == null");
        int k = key;
        if (root == null) {
            root = new Node(k, value, null);
            size = 1;
            return null;
        }
        Node node = root;
        while (true) {
            int cmp = Integer.compare(k, node.key);
            if (cmp < 0) {
                if (node.left == null) {
                    node.left = new Node(k, value, node);
                    size++;
                    splay(node.left);
                    return null;
                }
                node = node.left;
            } else if (cmp > 0) {
                if (node.right == null) {
                    node.right = new Node(k, value, node);
                    size++;
                    splay(node.right);
                    return null;
                }
                node = node.right;
            } else {
                String oldValue = node.value;
                node.value = value;
                splay(node);
                return oldValue;
            }
        }
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        Node node = find(k);
        return (node != null && node.key == k) ? node.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        int k = (Integer) key;
        Node node = find(k);
        return node != null && node.key == k;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (equals(node.value, value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        Node node = find(k);
        if (node == null || node.key != k) return null;
        String oldValue = node.value;
        delete(node);
        return oldValue;
    }

    // ---------- Методы NavigableMap (обязательные) ----------

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        return max(root).key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) throw new NullPointerException();
        int k = key;
        Node node = find(k);
        // После splay корень = ближайший узел (или точный)
        if (node == null) {
            // дерево пустое
            return null;
        }
        if (root.key < k) {
            return root.key;
        }
        // Ищем предшественника в левом поддереве
        if (root.left != null) {
            return max(root.left).key;
        }
        // Иначе ищем родителя, который меньше
        Node p = root.parent;
        Node ch = root;
        while (p != null && ch == p.left) {
            ch = p;
            p = p.parent;
        }
        return p == null ? null : p.key;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) throw new NullPointerException();
        int k = key;
        Node node = find(k);
        if (node == null) return null;
        if (root.key <= k) return root.key;
        // Корень > key, ищем предшественника
        if (root.left != null) {
            return max(root.left).key;
        }
        Node p = root.parent;
        Node ch = root;
        while (p != null && ch == p.left) {
            ch = p;
            p = p.parent;
        }
        return p == null ? null : p.key;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) throw new NullPointerException();
        int k = key;
        Node node = find(k);
        if (node == null) return null;
        if (root.key >= k) return root.key;
        // Ищем в правом поддереве
        if (root.right != null) {
            return min(root.right).key;
        }
        Node p = root.parent;
        Node ch = root;
        while (p != null && ch == p.right) {
            ch = p;
            p = p.parent;
        }
        return p == null ? null : p.key;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) throw new NullPointerException();
        int k = key;
        Node node = find(k);
        if (node == null) return null;
        if (root.key > k) return root.key;
        if (root.right != null) {
            return min(root.right).key;
        }
        Node p = root.parent;
        Node ch = root;
        while (p != null && ch == p.right) {
            ch = p;
            p = p.parent;
        }
        return p == null ? null : p.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MySplayMap sub = new MySplayMap();
        addSubTree(root, sub, toKey, true);
        return sub;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MySplayMap sub = new MySplayMap();
        addSubTree(root, sub, fromKey, false);
        return sub;
    }

    // ------------------ Вспомогательные методы splay ------------------

    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                // zig
                if (p.left == x) rotateRight(p);
                else rotateLeft(p);
            } else if (p.left == x && g.left == p) {
                // zig-zig (left-left)
                rotateRight(g);
                rotateRight(p);
            } else if (p.right == x && g.right == p) {
                // zig-zig (right-right)
                rotateLeft(g);
                rotateLeft(p);
            } else if (p.right == x && g.left == p) {
                // zig-zag (left-right)
                rotateLeft(p);
                rotateRight(g);
            } else {
                // zig-zag (right-left)
                rotateRight(p);
                rotateLeft(g);
            }
        }
        root = x;
    }

    private void rotateLeft(Node p) {
        Node r = p.right;
        p.right = r.left;
        if (r.left != null) r.left.parent = p;
        r.parent = p.parent;
        if (p.parent == null) root = r;
        else if (p.parent.left == p) p.parent.left = r;
        else p.parent.right = r;
        r.left = p;
        p.parent = r;
    }

    private void rotateRight(Node p) {
        Node l = p.left;
        p.left = l.right;
        if (l.right != null) l.right.parent = p;
        l.parent = p.parent;
        if (p.parent == null) root = l;
        else if (p.parent.right == p) p.parent.right = l;
        else p.parent.left = l;
        l.right = p;
        p.parent = l;
    }

    /** Поиск узла с заданным ключом (или ближайшего) и splay его. */
    private Node find(int key) {
        Node node = root;
        if (node == null) return null;
        while (true) {
            int cmp = Integer.compare(key, node.key);
            if (cmp < 0) {
                if (node.left == null) break;
                node = node.left;
            } else if (cmp > 0) {
                if (node.right == null) break;
                node = node.right;
            } else {
                break; // точное совпадение
            }
        }
        splay(node);
        return node;
    }

    /** Удаление узла (предполагается, что он уже в корне после splay). */
    private void delete(Node node) {
        // node должен быть в корне
        splay(node); // на всякий случай
        if (node.left == null) {
            root = node.right;
            if (root != null) root.parent = null;
        } else {
            Node maxLeft = max(node.left);
            splay(maxLeft); // теперь maxLeft в корне и его правое поддерево пусто
            root.right = node.right;
            if (node.right != null) node.right.parent = root;
        }
        size--;
    }

    private Node min(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node max(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    private static boolean equals(Object a, Object b) {
        return (a == null) ? b == null : a.equals(b);
    }

    // Для headMap/tailMap рекурсивно копируем подходящие узлы (без splay)
    private void addSubTree(Node node, MySplayMap target, int bound, boolean isHead) {
        if (node == null) return;
        if (isHead) {
            if (node.key < bound) {
                target.put(node.key, node.value);
                addSubTree(node.left, target, bound, true);
                addSubTree(node.right, target, bound, true);
            } else {
                addSubTree(node.left, target, bound, true);
            }
        } else { // tailMap
            if (node.key >= bound) {
                target.put(node.key, node.value);
                addSubTree(node.left, target, bound, false);
                addSubTree(node.right, target, bound, false);
            } else {
                addSubTree(node.right, target, bound, false);
            }
        }
    }

    // ================== ЗАГЛУШКИ ДЛЯ ОСТАЛЬНЫХ МЕТОДОВ ==================

    @Override
    public java.util.Map.Entry<Integer, String> lowerEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public java.util.Map.Entry<Integer, String> floorEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public java.util.Map.Entry<Integer, String> ceilingEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public java.util.Map.Entry<Integer, String> higherEntry(Integer key) { throw new UnsupportedOperationException(); }
    @Override
    public java.util.Map.Entry<Integer, String> firstEntry() { throw new UnsupportedOperationException(); }
    @Override
    public java.util.Map.Entry<Integer, String> lastEntry() { throw new UnsupportedOperationException(); }
    @Override
    public java.util.Map.Entry<Integer, String> pollFirstEntry() { throw new UnsupportedOperationException(); }
    @Override
    public java.util.Map.Entry<Integer, String> pollLastEntry() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { throw new UnsupportedOperationException(); }
    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override
    public Comparator<? super Integer> comparator() { return null; }
    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override
    public Set<Map.Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
}