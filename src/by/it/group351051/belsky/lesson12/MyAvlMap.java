package by.it.group351051.belsky.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

/*
    Задание на уровень А

    Создайте class MyAvlMap, который реализует интерфейс Map<Integer, String>
    и работает на основе АВЛ-дерева
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    Метод toString() должен выводить элементы в порядке возрастания ключей
    Формат вывода: скобки (фигурные) и разделители
    (знак равенства и запятая с пробелом) должны
    быть такими же как в методе toString() обычной коллекции
 */

/**
 * Реализация интерфейса Map<Integer, String> на основе АВЛ-дерева.
 * Особенности:
 * - АВЛ-дерево обеспечивает балансировку и логарифмическую сложность операций.
 * - Элементы хранятся в порядке возрастания ключей.
 */
public class MyAvlMap implements Map<Integer, String> {

    /**
     * Узел АВЛ-дерева, содержащий ключ, значение, ссылки на потомков и высоту.
     */
    private static class Node {
        Integer key; // Ключ узла
        String value; // Значение узла
        Node left, right; // Левый и правый потомки
        int height; // Высота узла

        /**
         * Конструктор узла.
         *
         * @param key   ключ узла
         * @param value значение узла
         */
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1; // Новый узел всегда имеет высоту 1
        }
    }

    private Node root; // Корень дерева
    private int size; // Количество элементов в дереве

    /**
     * Конструктор по умолчанию. Создает пустое дерево.
     */
    public MyAvlMap() {
        root = null;
        size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление дерева в порядке возрастания ключей.
     * Формат: {key1=value1, key2=value2, ...}.
     *
     * @return строковое представление дерева
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Удаляем последнюю запятую и пробел
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Добавляет элемент в дерево или обновляет значение существующего ключа.
     *
     * @param key   ключ
     * @param value значение
     * @return предыдущее значение или null, если ключ отсутствовал
     */
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Ключ не может быть null");
        Node[] result = new Node[1]; // Массив для хранения найденного узла
        root = insert(root, key, value, result);
        if (result[0] == null) {
            size++; // Увеличиваем размер, если ключ новый
            return null;
        } else {
            String oldValue = result[0].value;
            result[0].value = value; // Обновляем значение
            return oldValue;
        }
    }

    /**
     * Удаляет элемент по ключу.
     *
     * @param key ключ
     * @return удалённое значение или null, если ключ отсутствовал
     */
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node[] result = new Node[1]; // Массив для хранения удалённого узла
        root = delete(root, (Integer) key, result);
        if (result[0] != null) {
            size--; // Уменьшаем размер, если элемент был удалён
            return result[0].value;
        }
        return null;
    }

    /**
     * Возвращает значение по ключу.
     *
     * @param key ключ
     * @return значение или null, если ключ отсутствует
     */
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = getNode(root, (Integer) key);
        return node == null ? null : node.value;
    }

    /**
     * Проверяет наличие ключа в дереве.
     *
     * @param key ключ
     * @return true, если ключ присутствует
     */
    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return getNode(root, (Integer) key) != null;
    }

    /**
     * Возвращает количество элементов в дереве.
     *
     * @return размер дерева
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Очищает дерево, удаляя все элементы.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Проверяет, пусто ли дерево.
     *
     * @return true, если дерево пусто
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Обход дерева в порядке возрастания ключей.
     *
     * @param node текущий узел
     * @param sb   строковый буфер для накопления результата
     */
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    /**
     * Вставляет новый узел или обновляет существующий.
     *
     * @param node   текущий узел
     * @param key    ключ
     * @param value  значение
     * @param result массив для хранения найденного узла
     * @return сбалансированный узел
     */
    private Node insert(Node node, Integer key, String value, Node[] result) {
        if (node == null) return new Node(key, value);
        if (key < node.key) {
            node.left = insert(node.left, key, value, result);
        } else if (key > node.key) {
            node.right = insert(node.right, key, value, result);
        } else {
            result[0] = node;
            return node;
        }
        return balance(node);
    }

    /**
     * Удаляет узел по ключу.
     *
     * @param node   текущий узел
     * @param key    ключ
     * @param result массив для хранения удалённого узла
     * @return сбалансированный узел
     */
    private Node delete(Node node, Integer key, Node[] result) {
        if (node == null) return null;
        if (key < node.key) {
            node.left = delete(node.left, key, result);
        } else if (key > node.key) {
            node.right = delete(node.right, key, result);
        } else {
            result[0] = node;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node min = getMin(node.right);
            node.key = min.key;
            node.value = min.value;
            node.right = delete(node.right, min.key, new Node[1]);
        }
        return balance(node);
    }

    /**
     * Возвращает узел по ключу.
     *
     * @param node текущий узел
     * @param key  ключ
     * @return найденный узел или null
     */
    private Node getNode(Node node, Integer key) {
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

    /**
     * Возвращает узел с минимальным ключом.
     *
     * @param node текущий узел
     * @return узел с минимальным ключом
     */
    private Node getMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    /**
     * Балансирует узел.
     *
     * @param node текущий узел
     * @return сбалансированный узел
     */
    private Node balance(Node node) {
        updateHeight(node);
        int balanceFactor = getBalanceFactor(node);
        if (balanceFactor > 1) {
            if (getBalanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (balanceFactor < -1) {
            if (getBalanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    /**
     * Обновляет высоту узла.
     *
     * @param node текущий узел
     */
    private void updateHeight(Node node) {
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    /**
     * Возвращает высоту узла.
     *
     * @param node текущий узел
     * @return высота узла
     */
    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    /**
     * Вычисляет фактор баланса узла.
     *
     * @param node текущий узел
     * @return фактор баланса
     */
    private int getBalanceFactor(Node node) {
        return getHeight(node.left) - getHeight(node.right);
    }

    /**
     * Выполняет левый поворот узла.
     *
     * @param node текущий узел
     * @return новый корень поддерева
     */
    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    /**
     * Выполняет правый поворот узла.
     *
     * @param node текущий узел
     * @return новый корень поддерева
     */
    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    // Остальные методы интерфейса Map<Integer, String> можно оставить нереализованными
    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public String getOrDefault(Object key, String defaultValue) {
        throw new UnsupportedOperationException("Метод не реализован");
    }
}