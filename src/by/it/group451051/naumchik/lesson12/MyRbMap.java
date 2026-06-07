package by.it.group451051.naumchik.lesson12;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

/**
 * Реализация SortedMap&lt;Integer, String&gt; на основе красно-чёрного дерева.
 * Ключи хранятся в отсортированном порядке.
 * Не использует классы стандартной библиотеки (кроме интерфейсов).
 */
public class MyRbMap implements SortedMap<Integer, String> {

    // Цвета узлов
    private static final boolean RED   = false;
    private static final boolean BLACK = true;

    /** Внутренний узел дерева */
    private static final class Node {
        int key;
        String value;
        Node left;
        Node right;
        Node parent;
        boolean color = BLACK;

        Node(int key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    // ------------------ вспомогательные методы для работы с цветом ------------------
    private static boolean colorOf(Node p) {
        return p == null ? BLACK : p.color;
    }

    private static void setColor(Node p, boolean c) {
        if (p != null) {
            p.color = c;
        }
    }

    private static Node parentOf(Node p) {
        return p == null ? null : p.parent;
    }

    private static Node leftOf(Node p) {
        return p == null ? null : p.left;
    }

    private static Node rightOf(Node p) {
        return p == null ? null : p.right;
    }

    // ------------------ повороты ------------------
    private void rotateLeft(Node p) {
        if (p == null) return;
        Node r = p.right;
        p.right = r.left;
        if (r.left != null) {
            r.left.parent = p;
        }
        r.parent = p.parent;
        if (p.parent == null) {
            root = r;
        } else if (p.parent.left == p) {
            p.parent.left = r;
        } else {
            p.parent.right = r;
        }
        r.left = p;
        p.parent = r;
    }

    private void rotateRight(Node p) {
        if (p == null) return;
        Node l = p.left;
        p.left = l.right;
        if (l.right != null) {
            l.right.parent = p;
        }
        l.parent = p.parent;
        if (p.parent == null) {
            root = l;
        } else if (p.parent.right == p) {
            p.parent.right = l;
        } else {
            p.parent.left = l;
        }
        l.right = p;
        p.parent = l;
    }

    // ------------------ основные методы Map ------------------

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
        if (key == null) {
            throw new NullPointerException("Ключ не может быть null");
        }
        int k = key;

        Node t = root;
        if (t == null) {
            root = new Node(k, value, null);
            size = 1;
            return null;
        }

        Node parent;
        int cmp;
        do {
            parent = t;
            cmp = compare(k, t.key);
            if (cmp < 0) {
                t = t.left;
            } else if (cmp > 0) {
                t = t.right;
            } else {
                // замена значения
                String oldValue = t.value;
                t.value = value;
                return oldValue;
            }
        } while (t != null);

        Node e = new Node(k, value, parent);
        if (cmp < 0) {
            parent.left = e;
        } else {
            parent.right = e;
        }
        e.color = RED;
        size++;
        fixAfterInsertion(e);
        return null;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        Node p = getNode(k);
        return (p == null) ? null : p.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return getNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        // обход всего дерева
        return containsValue(root, value);
    }

    private boolean containsValue(Node node, Object value) {
        if (node == null) return false;
        if (equals(value, node.value)) return true;
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private static boolean equals(Object a, Object b) {
        return (a == null) ? b == null : a.equals(b);
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = (Integer) key;
        Node p = getNode(k);
        if (p == null) return null;
        String oldValue = p.value;
        deleteNode(p);
        return oldValue;
    }

    // ------------------ методы SortedMap ------------------

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException("Карта пуста");
        return findMin(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException("Карта пуста");
        return findMax(root).key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException("toKey не может быть null");
        MyRbMap result = new MyRbMap();
        collectHead(root, toKey, result);
        return result;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException("fromKey не может быть null");
        MyRbMap result = new MyRbMap();
        collectTail(root, fromKey, result);
        return result;
    }

    // заглушки для остальных методов SortedMap/Map
    @Override
    public Comparator<? super Integer> comparator() {
        return null; // естественный порядок
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("subMap не реализован");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet не реализован");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values не реализован");
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet не реализован");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("putAll не реализован");
    }

    // ------------------ toString ------------------

    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        inorderTraversal(root, sb);
        sb.setLength(sb.length() - 2); // убираем последнюю запятую и пробел
        sb.append("}");
        return sb.toString();
    }

    private void inorderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        inorderTraversal(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inorderTraversal(node.right, sb);
    }

    // ------------------ приватные методы работы с деревом ------------------

    private int compare(int k1, int k2) {
        return Integer.compare(k1, k2);
    }

    private Node getNode(int key) {
        Node p = root;
        while (p != null) {
            int cmp = compare(key, p.key);
            if (cmp < 0) p = p.left;
            else if (cmp > 0) p = p.right;
            else return p;
        }
        return null;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node findMax(Node node) {
        while (node.right != null) node = node.right;
        return node;
    }

    /** Следующий по величине узел */
    private Node successor(Node t) {
        if (t == null) return null;
        if (t.right != null) {
            return findMin(t.right);
        }
        Node p = t.parent;
        while (p != null && t == p.right) {
            t = p;
            p = p.parent;
        }
        return p;
    }

    // ------------------ балансировка после вставки ------------------

    private void fixAfterInsertion(Node x) {
        x.color = RED;
        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
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
            } else { // симметричный случай
                Node y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
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

    // ------------------ удаление узла и балансировка ------------------

    private void deleteNode(Node p) {
        size--;
        // Если у узла двое детей, заменяем на следующего
        if (p.left != null && p.right != null) {
            Node s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        }

        Node replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            // Удаление узла с одним ребёнком
            replacement.parent = p.parent;
            if (p.parent == null) {
                root = replacement;
            } else if (p == p.parent.left) {
                p.parent.left = replacement;
            } else {
                p.parent.right = replacement;
            }

            p.left = p.right = p.parent = null;

            if (p.color == BLACK) {
                fixAfterDeletion(replacement);
            }
        } else if (p.parent == null) {
            // Удаление корня без детей
            root = null;
        } else {
            // Удаление листа
            if (p.color == BLACK) {
                fixAfterDeletion(p);
            }
            if (p.parent != null) {
                if (p == p.parent.left) {
                    p.parent.left = null;
                } else {
                    p.parent.right = null;
                }
                p.parent = null;
            }
        }
    }

    private void fixAfterDeletion(Node x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Node sib = rightOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }
                if (colorOf(leftOf(sib)) == BLACK &&
                        colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // симметричный случай
                Node sib = leftOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }
                if (colorOf(rightOf(sib)) == BLACK &&
                        colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    // ------------------ вспомогательные методы для headMap/tailMap ------------------

    private void collectHead(Node node, int toKey, MyRbMap result) {
        if (node == null) return;
        if (node.key < toKey) {
            result.put(node.key, node.value);
            collectHead(node.left, toKey, result);
            collectHead(node.right, toKey, result);
        } else {
            collectHead(node.left, toKey, result);
        }
    }

    private void collectTail(Node node, int fromKey, MyRbMap result) {
        if (node == null) return;
        if (node.key >= fromKey) {
            result.put(node.key, node.value);
            collectTail(node.left, fromKey, result);
            collectTail(node.right, fromKey, result);
        } else {
            collectTail(node.right, fromKey, result);
        }
    }
}
