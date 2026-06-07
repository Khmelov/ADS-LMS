package by.it.group451051.naumchik.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Реализация Map<Integer, String> на основе АВЛ-дерева.
 * Ключи хранятся в отсортированном порядке, балансировка поддерживается автоматически.
 * Не использует готовые коллекции из стандартной библиотеки.
 * Обязательные методы реализованы полностью, остальные – заглушки.
 */
public class MyAvlMap implements Map<Integer, String> {

    /** Узел АВЛ-дерева */
    private static class Node {
        int key;
        String value;
        Node left;
        Node right;
        int height;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;

    public MyAvlMap() {
    }

    // ================== ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ==================

    /**
     * Возвращает строковое представление Map в виде {key1=value1, key2=value2, ...},
     * элементы перечислены в порядке возрастания ключей.
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        buildString(root, sb);
        // Удаляем последнюю запятую и пробел, добавляем закрывающую скобку
        sb.setLength(sb.length() - 2); // убираем ", "
        sb.append("}");
        return sb.toString();
    }

    private void buildString(Node node, StringBuilder sb) {
        if (node != null) {
            buildString(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            buildString(node.right, sb);
        }
    }

    /**
     * Добавляет или заменяет значение по ключу.
     * @param key   ключ (не null, но Integer, так что null запрещён)
     * @param value значение (может быть null)
     * @return предыдущее значение, либо null если ключ отсутствовал
     * @throws NullPointerException если key == null
     */
    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException("Ключ не может быть null");
        }
        String[] oldValue = new String[1]; // контейнер для возврата старого значения
        root = put(root, key, value, oldValue);
        return oldValue[0];
    }

    private Node put(Node node, int key, String value, String[] oldValue) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (key < node.key) {
            node.left = put(node.left, key, value, oldValue);
        } else if (key > node.key) {
            node.right = put(node.right, key, value, oldValue);
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node; // высота не меняется
        }
        return balance(node);
    }

    /**
     * Удаляет пару ключ-значение по ключу.
     * @param key ключ (Integer)
     * @return предыдущее значение, либо null если ключ не найден
     */
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        int k = (Integer) key;
        String[] removedValue = new String[1];
        root = remove(root, k, removedValue);
        return removedValue[0];
    }

    private Node remove(Node node, int key, String[] removedValue) {
        if (node == null) {
            return null;
        }
        if (key < node.key) {
            node.left = remove(node.left, key, removedValue);
        } else if (key > node.key) {
            node.right = remove(node.right, key, removedValue);
        } else {
            removedValue[0] = node.value;
            size--;
            // Узел найден
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            // Два ребёнка: заменяем минимальным узлом правого поддерева
            Node min = findMin(node.right);
            node.key = min.key;
            node.value = min.value;
            // Удаляем минимальный узел из правого поддерева (его ключ дублируется)
            node.right = removeMin(node.right);
        }
        return balance(node);
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /** Удаляет минимальный узел в поддереве и возвращает новый корень. */
    private Node removeMin(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMin(node.left);
        return balance(node);
    }

    /**
     * Возвращает значение по ключу или null, если ключ отсутствует.
     * @param key ключ
     * @return значение или null
     */
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) {
            return null;
        }
        int k = (Integer) key;
        Node node = find(root, k);
        return node == null ? null : node.value;
    }

    /**
     * Проверяет, содержится ли ключ в Map.
     * @param key ключ
     * @return true, если ключ существует
     */
    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) {
            return false;
        }
        int k = (Integer) key;
        return find(root, k) != null;
    }

    /** Возвращает количество пар. */
    @Override
    public int size() {
        return size;
    }

    /** Удаляет все пары. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Проверяет, пуст ли Map. */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // ================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ AVL ==================

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int balanceFactor(Node node) {
        return height(node.right) - height(node.left);
    }

    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    /** Восстанавливает баланс узла после операций вставки/удаления. */
    private Node balance(Node node) {
        updateHeight(node);
        int bf = balanceFactor(node);
        if (bf == 2) {
            if (balanceFactor(node.right) < 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        if (bf == -2) {
            if (balanceFactor(node.left) > 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        return node;
    }

    /** Поиск узла по ключу. */
    private Node find(Node node, int key) {
        while (node != null) {
            if (key < node.key) {
                node = node.left;
            } else if (key > node.key) {
                node = node.right;
            } else {
                return node;
            }
        }
        return null;
    }

    // ================== ЗАГЛУШКИ ДЛЯ ОСТАЛЬНЫХ МЕТОДОВ MAP ==================

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
