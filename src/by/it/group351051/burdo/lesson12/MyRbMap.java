package by.it.group351051.burdo.lesson12;

import java.util.*;

/**
 * Реализация красно-черной карты (Map) с использованием красно-черного дерева.
 */
public class MyRbMap implements SortedMap<Integer, String> {
    private Node root;
    private int size;

    // Константы для определения цвета узлов
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    /**
     * Вложенный класс для представления узлов красно-черного дерева.
     */
    private static class Node {
        int key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.color = RED;  // Новый узел всегда красный
        }
    }

    /**
     * Конструктор по умолчанию, инициализирует пустое дерево.
     */
    public MyRbMap() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Строковое представление карты.
     * @return строка, представляющая все элементы карты.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (size > 0) {
            toStringHelper(root, sb);
            sb.setLength(sb.length() - 2);  // Удаление последней запятой
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Рекурсивно добавляет элементы в строку.
     * @param node текущий узел.
     * @param sb строка, в которую добавляются элементы.
     */
    private void toStringHelper(Node node, StringBuilder sb) {
        if (node != null) {
            toStringHelper(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            toStringHelper(node.right, sb);
        }
    }

    /**
     * Добавляет элемент в карту.
     * @param key ключ.
     * @param value значение.
     * @return старое значение, если оно было, иначе null.
     */
    public String put(Integer key, String value) {
        if (key == null || value == null) throw new NullPointerException();

        Node existingNode = getNode(key);
        String oldValue = null;

        if (existingNode != null) {
            oldValue = existingNode.value;
            existingNode.value = value;  // Обновление существующего значения
        } else {
            Node newNode = new Node(key, value);
            if (root == null) {
                root = newNode;  // Дерево пустое, новый узел становится корнем
                root.color = BLACK;  // Корень всегда черный
            } else {
                insertNode(root, newNode);  // Вставка нового узла
                fixAfterInsertion(newNode);  // Исправление нарушений красно-черного дерева
            }
            size++;
        }
        return oldValue;
    }

    /**
     * Удаляет элемент по ключу.
     * @param key ключ.
     * @return удаленное значение.
     */
    @Override
    public String remove(Object key) {
        if (key == null) throw new NullPointerException();
        Node node = getNode((Integer) key);
        if (node == null) return null;

        String oldValue = node.value;
        deleteNode(node);  // Удаление узла
        size--;
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    // Вставка нового узла в дерево
    private void insertNode(Node root, Node newNode) {
        if (newNode.key < root.key) {
            if (root.left == null) {
                root.left = newNode;
                newNode.parent = root;
            } else {
                insertNode(root.left, newNode);  // Рекурсивный вызов для левого поддерева
            }
        } else {
            if (root.right == null) {
                root.right = newNode;
                newNode.parent = root;
            } else {
                insertNode(root.right, newNode);  // Рекурсивный вызов для правого поддерева
            }
        }
    }

    // Исправление нарушений красно-черного дерева после вставки
    private void fixAfterInsertion(Node node) {
        while (node != null && node != root && node.parent.color == RED) {
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle != null && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;  // Переход к родителю
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);  // Вращение влево
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateRight(node.parent.parent);  // Вращение вправо
                }
            } else {
                Node uncle = node.parent.parent.left;
                if (uncle != null && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);  // Вращение вправо
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateLeft(node.parent.parent);  // Вращение влево
                }
            }
        }
        root.color = BLACK;  // Корень всегда черный
    }

    // Вращение влево
    private void rotateLeft(Node node) {
        Node right = node.right;
        node.right = right.left;
        if (right.left != null) right.left.parent = node;
        right.parent = node.parent;
        if (node.parent == null) {
            root = right;
        } else if (node == node.parent.left) {
            node.parent.left = right;
        } else {
            node.parent.right = right;
        }
        right.left = node;
        node.parent = right;
    }

    // Вращение вправо
    private void rotateRight(Node node) {
        Node left = node.left;
        node.left = left.right;
        if (left.right != null) left.right.parent = node;
        left.parent = node.parent;
        if (node.parent == null) {
            root = left;
        } else if (node == node.parent.right) {
            node.parent.right = left;
        } else {
            node.parent.left = left;
        }
        left.right = node;
        node.parent = left;
    }

    // Удаление узла
    private void deleteNode(Node node) {
        if (node.left != null && node.right != null) {
            Node successor = minimum(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node = successor;
        }

        Node replacement = (node.left != null) ? node.left : node.right;

        if (replacement != null) {
            replacement.parent = node.parent;
            if (node.parent == null) {
                root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }
            if (node.color == BLACK) {
                fixAfterDeletion(replacement);
            }
        } else if (node.parent == null) {
            root = null;
        } else {
            if (node.color == BLACK) {
                fixAfterDeletion(node);
            }
            if (node.parent != null) {
                if (node == node.parent.left) {
                    node.parent.left = null;
                } else {
                    node.parent.right = null;
                }
            }
        }
    }

    // Исправление нарушений красно-черного дерева после удаления
    private void fixAfterDeletion(Node node) {
        // Обработка различных случаев для исправления нарушений
        while (node != root && node.color == BLACK) {
            if (node == node.parent.left) {
                Node sibling = node.parent.right;
                if (sibling != null && sibling.color == RED) {
                    sibling.color = BLACK;
                    node.parent.color = RED;
                    rotateLeft(node.parent);
                    sibling = node.parent.right;
                }
                if (sibling != null && (sibling.left == null || sibling.left.color == BLACK) &&
                        (sibling.right == null || sibling.right.color == BLACK)) {
                    sibling.color = RED;
                    node = node.parent;
                } else {
                    if (sibling != null && (sibling.right == null || sibling.right.color == BLACK)) {
                        if (sibling.left != null) {
                            sibling.left.color = BLACK;
                        }
                        sibling.color = RED;
                        rotateRight(sibling);
                        sibling = node.parent.right;
                    }
                    if (sibling != null) {
                        sibling.color = node.parent.color;
                        node.parent.color = BLACK;
                        if (sibling.right != null) {
                            sibling.right.color = BLACK;
                        }
                        rotateLeft(node.parent);
                        node = root;
                    }
                }
            } else {
                Node sibling = node.parent.left;
                if (sibling != null && sibling.color == RED) {
                    sibling.color = BLACK;
                    node.parent.color = RED;
                    rotateRight(node.parent);
                    sibling = node.parent.left;
                }
                if (sibling != null && (sibling.right == null || sibling.right.color == BLACK) &&
                        (sibling.left == null || sibling.left.color == BLACK)) {
                    sibling.color = RED;
                    node = node.parent;
                } else {
                    if (sibling != null && (sibling.left == null || sibling.left.color == BLACK)) {
                        if (sibling.right != null) {
                            sibling.right.color = BLACK;
                        }
                        sibling.color = RED;
                        rotateLeft(sibling);
                        sibling = node.parent.left;
                    }
                    if (sibling != null) {
                        sibling.color = node.parent.color;
                        node.parent.color = BLACK;
                        if (sibling.left != null) {
                            sibling.left.color = BLACK;
                        }
                        rotateRight(node.parent);
                        node = root;
                    }
                }
            }
        }
        node.color = BLACK;
    }

    // Поиск минимального элемента в поддереве
    private Node minimum(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // Получение узла по ключу
    public String get(Integer key) {
        if (key == null) throw new NullPointerException();
        Node node = getNode(key);
        return (node != null) ? node.value : null;
    }

    private Node getNode(Integer key) {
        Node current = root;
        while (current != null) {
            if (key.equals(current.key)) {
                return current;
            } else if (key < current.key) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    /**
     * Проверка наличия ключа в карте.
     * @param key ключ.
     * @return true, если ключ существует.
     */
    public boolean containsKey(Integer key) {
        return getNode(key) != null;
    }

    /**
     * Проверка наличия значения в карте.
     * @param value значение.
     * @return true, если значение существует.
     */
    public boolean containsValue(String value) {
        return values().contains(value);
    }

    public int size() {
        return size;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer && getNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) return false;
        for (String val : values()) {
            if (val.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String get(Object key) {
        return key instanceof Integer ? get((Integer) key) : null;
    }

    public Integer firstKey() {
        if (isEmpty()) throw new NoSuchElementException();
        Node node = minimum(root);
        return node.key;
    }

    public Integer lastKey() {
        if (isEmpty()) throw new NoSuchElementException();
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException();
        }
        MyRbMap resultMap = new MyRbMap();
        fillHeadMap(root, toKey, resultMap);
        return resultMap;
    }

    private void fillHeadMap(Node node, Integer toKey, MyRbMap resultMap) {
        if (node == null) {
            return;
        }

        if (node.key < toKey) {
            resultMap.put(node.key, node.value);
            fillHeadMap(node.left, toKey, resultMap);
            fillHeadMap(node.right, toKey, resultMap);
        } else {
            fillHeadMap(node.left, toKey, resultMap);
        }
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException();
        }
        MyRbMap resultMap = new MyRbMap();
        fillTailMap(root, fromKey, resultMap);
        return resultMap;
    }

    private void fillTailMap(Node node, Integer fromKey, MyRbMap resultMap) {
        if (node == null) {
            return;
        }

        if (node.key >= fromKey) {
            resultMap.put(node.key, node.value);
            fillTailMap(node.left, fromKey, resultMap);
            fillTailMap(node.right, fromKey, resultMap);
        } else {
            fillTailMap(node.right, fromKey, resultMap);
        }
    }

    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;  // Не реализовано
    }

    public Comparator<? super Integer> comparator() {
        return null;  // Сортировка по ключу по умолчанию
    }

    public Set<Map.Entry<Integer, String>> entrySet() {
        return null;  // Не реализовано
    }

    public Collection<String> values() {
        List<String> valuesList = new ArrayList<>();
        valuesHelper(root, valuesList);
        return valuesList;
    }

    private void valuesHelper(Node node, List<String> valuesList) {
        if (node != null) {
            valuesHelper(node.left, valuesList);
            valuesList.add(node.value);
            valuesHelper(node.right, valuesList);
        }
    }

    public Set<Integer> keySet() {
        Set<Integer> keys = new HashSet<>();
        keySetHelper(root, keys);
        return keys;
    }

    private void keySetHelper(Node node, Set<Integer> keys) {
        if (node != null) {
            keySetHelper(node.left, keys);
            keys.add(node.key);
            keySetHelper(node.right, keys);
        }
    }

    public static void main(String[] args) {
        // Создание экземпляра MyRbMap
        MyRbMap map = new MyRbMap();

        // Проверка, пуста ли карта
        System.out.println("Карта пуста? " + map.isEmpty());

        // Добавление элементов
        map.put(1, "Monday");
        map.put(2, "Tuesday");
        map.put(3, "Wednesday");
        map.put(4, "Thursday");
        map.put(5, "Friday");

        // Вывод карты
        System.out.println("Карта после добавления элементов: " + map.toString());

        // Проверка размера карты
        System.out.println("Размер карты: " + map.size());

        // Проверка наличия ключа
        System.out.println("Содержит ключ 3? " + map.containsKey(3));
        System.out.println("Содержит ключ 10? " + map.containsKey(10));

        // Проверка наличия значения
        System.out.println("Содержит значение 'Tuesday'? " + map.containsValue("Tuesday"));
        System.out.println("Содержит значение 'Sunday'? " + map.containsValue("Sunday"));

        // Получение значения по ключу
        System.out.println("Значение для ключа 2: " + map.get(2));
        System.out.println("Значение для ключа 10: " + map.get(10));

        // Удаление элемента
        System.out.println("Удаление элемента с ключом 3: " + map.remove(3));
        System.out.println("Карта после удаления: " + map.toString());

        // Получение первого и последнего ключа
        System.out.println("Первый ключ: " + map.firstKey());
        System.out.println("Последний ключ: " + map.lastKey());

        // Создание подкарты headMap (ключи меньше toKey)
        SortedMap<Integer, String> headMap = map.headMap(4);
        System.out.println("HeadMap (ключи < 4): " + headMap.toString());

        // Создание подкарты tailMap (ключи больше или равны fromKey)
        SortedMap<Integer, String> tailMap = map.tailMap(3);
        System.out.println("TailMap (ключи >= 3): " + tailMap.toString());

        // Получение множества ключей
        System.out.println("Множество ключей: " + map.keySet());

        // Получение коллекции значений
        System.out.println("Коллекция значений: " + map.values());

        // Очистка карты
        map.clear();
        System.out.println("Карта после очистки: " + map.toString());
        System.out.println("Карта пуста? " + map.isEmpty());
    }
}
