package by.it.group351051.burdo.lesson12;

import java.util.*;

/**
 * Реализация интерфейса Map с использованием AVL-дерева.
 * Гарантирует логарифмическое время выполнения основных операций.
 */
public class MyAvlMap implements Map<Integer, String> {

    /**
     * Внутренний класс для представления узлов AVL-дерева.
     */
    private static class Node {
        Integer key;
        String value;
        int height;
        Node left, right;

        /**
         * Конструктор узла.
         * @param key ключ узла
         * @param value значение узла
         */
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            height = 1;
        }
    }

    private Node root;
    private int size = 0;

    /**
     * Возвращает количество элементов в карте.
     * @return количество элементов
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуста ли карта.
     * @return true, если карта не содержит элементов
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Проверяет наличие ключа в карте.
     * @param key ключ для проверки
     * @return true, если ключ присутствует
     */
    @Override
    public boolean containsKey(Object key) {
        return getNode(root, (Integer) key) != null;
    }

    /**
     * Проверяет наличие значения в карте.
     * @param value значение для проверки
     * @return true, если значение присутствует
     */
    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    /**
     * Возвращает значение по ключу.
     * @param key ключ для поиска
     * @return значение или null, если ключ отсутствует
     */
    @Override
    public String get(Object key) {
        Node node = getNode(root, (Integer) key);
        return node != null ? node.value : null;
    }

    /**
     * Добавляет или обновляет значение по ключу.
     * @param key ключ
     * @param value значение
     * @return предыдущее значение или null, если ключ отсутствовал
     */
    @Override
    public String put(Integer key, String value) {
        String[] oldValue = new String[1];
        root = insert(root, key, value, oldValue);
        return oldValue[0];
    }

    /**
     * Удаляет элемент по ключу.
     * @param key ключ для удаления
     * @return удаленное значение или null, если ключ отсутствовал
     */
    @Override
    public String remove(Object key) {
        String[] removed = new String[1];
        root = remove(root, (Integer) key, removed);
        return removed[0];
    }

    /**
     * Добавляет все элементы из указанной карты.
     * @param m карта для добавления
     */
    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        if (m != null) {
            for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Очищает карту.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Возвращает множество ключей.
     * @return множество ключей
     */
    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new HashSet<>();
        inOrderKeys(root, keys);
        return keys;
    }

    /**
     * Возвращает коллекцию значений.
     * @return коллекция значений
     */
    @Override
    public Collection<String> values() {
        List<String> values = new ArrayList<>();
        inOrderValues(root, values);
        return values;
    }

    /**
     * Возвращает множество пар ключ-значение.
     * @return множество записей
     */
    @Override
    public Set<Entry<Integer, String>> entrySet() {
        Set<Entry<Integer, String>> entries = new HashSet<>();
        inOrderEntries(root, entries);
        return entries;
    }

    /**
     * Возвращает строковое представление карты.
     * @return строковое представление
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Находит узел по ключу.
     * @param node корень поддерева
     * @param key искомый ключ
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
     * Вставляет новый узел или обновляет существующий.
     * @param node корень поддерева
     * @param key ключ
     * @param value значение
     * @param oldValue массив для возврата старого значения
     * @return новый корень поддерева
     */
    private Node insert(Node node, Integer key, String value, String[] oldValue) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (key < node.key) {
            node.left = insert(node.left, key, value, oldValue);
        } else if (key > node.key) {
            node.right = insert(node.right, key, value, oldValue);
        } else {
            oldValue[0] = node.value;
            node.value = value;
            return node;
        }
        updateHeight(node);
        return balance(node);
    }

    /**
     * Удаляет узел по ключу.
     * @param node корень поддерева
     * @param key ключ для удаления
     * @param removed массив для возврата удаленного значения
     * @return новый корень поддерева
     */
    private Node remove(Node node, Integer key, String[] removed) {
        if (node == null) {
            return null;
        }
        if (key < node.key) {
            node.left = remove(node.left, key, removed);
        } else if (key > node.key) {
            node.right = remove(node.right, key, removed);
        } else {
            removed[0] = node.value;
            size--;
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node min = getMin(node.right);
                node.key = min.key;
                node.value = min.value;
                node.right = remove(node.right, min.key, new String[1]);
            }
        }
        if (node != null) {
            updateHeight(node);
            node = balance(node);
        }
        return node;
    }

    /**
     * Находит узел с минимальным ключом в поддереве.
     * @param node корень поддерева
     * @return узел с минимальным ключом
     */
    private Node getMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Обновляет высоту узла.
     * @param node узел для обновления
     */
    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Возвращает высоту узла.
     * @param node узел
     * @return высота узла (0 для null)
     */
    private int height(Node node) {
        return node != null ? node.height : 0;
    }

    /**
     * Вычисляет баланс-фактор узла.
     * @param node узел
     * @return разница высот правого и левого поддеревьев
     */
    private int balanceFactor(Node node) {
        return height(node.right) - height(node.left);
    }

    /**
     * Выполняет правый поворот.
     * @param y узел для поворота
     * @return новый корень поддерева
     */
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T = x.right;
        x.right = y;
        y.left = T;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    /**
     * Выполняет левый поворот.
     * @param x узел для поворота
     * @return новый корень поддерева
     */
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T = y.left;
        y.left = x;
        x.right = T;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    /**
     * Балансирует поддерево.
     * @param node корень поддерева
     * @return новый корень поддерева
     */
    private Node balance(Node node) {
        int balance = balanceFactor(node);
        if (balance < -1) {
            if (balanceFactor(node.left) > 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (balance > 1) {
            if (balanceFactor(node.right) < 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    /**
     * Обходит дерево в порядке возрастания ключей.
     * @param node текущий узел
     * @param sb StringBuilder для построения строки
     */
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    /**
     * Собирает ключи в порядке возрастания.
     * @param node текущий узел
     * @param keys множество для сбора ключей
     */
    private void inOrderKeys(Node node, Set<Integer> keys) {
        if (node != null) {
            inOrderKeys(node.left, keys);
            keys.add(node.key);
            inOrderKeys(node.right, keys);
        }
    }

    /**
     * Собирает значения в порядке возрастания ключей.
     * @param node текущий узел
     * @param values список для сбора значений
     */
    private void inOrderValues(Node node, List<String> values) {
        if (node != null) {
            inOrderValues(node.left, values);
            values.add(node.value);
            inOrderValues(node.right, values);
        }
    }

    /**
     * Собирает записи в порядке возрастания ключей.
     * @param node текущий узел
     * @param entries множество для сбора записей
     */
    private void inOrderEntries(Node node, Set<Entry<Integer, String>> entries) {
        if (node != null) {
            inOrderEntries(node.left, entries);
            entries.add(new AbstractMap.SimpleEntry<>(node.key, node.value));
            inOrderEntries(node.right, entries);
        }
    }

    public static void main(String[] args) {
        MyAvlMap map = new MyAvlMap();

        // Проверка пустой карты
        System.out.println("Карта пуста? " + map.isEmpty());
        System.out.println("Размер карты: " + map.size());

        // Добавление элементов
        System.out.println("\nДобавляем элементы:");
        System.out.println("put(1, \"One\"): " + map.put(1, "One"));
        System.out.println("put(2, \"Two\"): " + map.put(2, "Two"));
        System.out.println("put(3, \"Three\"): " + map.put(3, "Three"));
        System.out.println("put(2, \"NewTwo\"): " + map.put(2, "NewTwo"));

        // Проверка содержимого
        System.out.println("\nТекущее состояние карты: " + map);
        System.out.println("Размер карты: " + map.size());
        System.out.println("Карта пуста? " + map.isEmpty());

        // Проверка наличия
        System.out.println("\nПроверка наличия:");
        System.out.println("containsKey(1): " + map.containsKey(1));
        System.out.println("containsKey(5): " + map.containsKey(5));
        System.out.println("containsValue(\"Three\"): " + map.containsValue("Three"));
        System.out.println("containsValue(\"Five\"): " + map.containsValue("Five"));

        // Получение значений
        System.out.println("\nПолучение значений:");
        System.out.println("get(1): " + map.get(1));
        System.out.println("get(2): " + map.get(2));
        System.out.println("get(5): " + map.get(5));

        // Удаление элементов
        System.out.println("\nУдаление элементов:");
        System.out.println("remove(2): " + map.remove(2));
        System.out.println("remove(5): " + map.remove(5));
        System.out.println("Текущее состояние карты: " + map);
        System.out.println("Размер карты: " + map.size());

        // Работа с множествами
        System.out.println("\nМножества:");
        System.out.println("keySet(): " + map.keySet());
        System.out.println("values(): " + map.values());
        System.out.println("entrySet(): " + map.entrySet());

        // Метод putAll
        System.out.println("\nИспользование putAll:");
        Map<Integer, String> anotherMap = new HashMap<>();
        anotherMap.put(4, "Four");
        anotherMap.put(5, "Five");
        map.putAll(anotherMap);
        System.out.println("Текущее состояние карты: " + map);

        // Очистка карты
        System.out.println("\nОчистка карты:");
        map.clear();
        System.out.println("Текущее состояние карты: " + map);
        System.out.println("Размер карты: " + map.size());
        System.out.println("Карта пуста? " + map.isEmpty());
    }
}