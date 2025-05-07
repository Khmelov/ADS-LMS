package by.it.group351051.belsky.lesson12;

import java.util.*;

/*
    Задание на уровень B

    Создайте class MyRbMap, который реализует интерфейс SortedMap<Integer, String>
    и работает на основе красно-черного дерева
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    Метод toString() должен выводить элементы в порядке возрастания ключей
    Формат вывода: скобки (фигурные) и разделители
    (знак равенства и запятая с пробелом) должны
    быть такими же как в методе toString() обычной коллекции
 */

/**
 * Реализация интерфейса SortedMap на основе структуры данных красно-черное дерево.
 * Данный класс предоставляет функциональность для хранения и манипуляции элементами в порядке возрастания ключей.
 * Все операции выполняются с учетом сортировки по ключам, в порядке возрастания.
 */
public class MyRbMap implements SortedMap<Integer, String> {

    private Node root;  // Корень дерева
    private int size;   // Количество элементов в дереве

    private static final boolean RED = false;   // Цвет узла - красный
    private static final boolean BLACK = true;  // Цвет узла - черный

    /**
     * Вспомогательный класс для представления узлов красно-черного дерева.
     * Каждый узел хранит ключ, значение и информацию о своих детях и родителе.
     */
    private static class Node {
        int key;         // Ключ узла
        String value;    // Значение узла
        Node left, right, parent;  // Левый, правый потомок и родитель
        boolean color;   // Цвет узла (красный или черный)

        /**
         * Конструктор для создания нового узла.
         * @param key ключ узла
         * @param value значение узла
         */
        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.color = RED;  // Новый узел по умолчанию красный
        }
    }

    /**
     * Конструктор для создания пустого дерева.
     * Изначально дерево пусто, корень равен null, а размер равен 0.
     */
    public MyRbMap() {
        this.root = null;
        this.size = 0;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление карты в порядке возрастания ключей.
     * Рекурсивно обходит дерево и формирует строку с парами ключ-значение.
     * @return строковое представление карты
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        sb.append("}");
        return sb.toString();
    }

    /**
     * Вставляет новый элемент с заданным ключом и значением в дерево.
     * Если ключ уже существует, заменяет старое значение на новое.
     * Осуществляет восстановление баланса дерева после вставки.
     * @param key ключ элемента
     * @param value значение элемента
     * @return старое значение, если ключ уже был в дереве, иначе null
     */
    @Override
    public String put(Integer key, String value) {
        Node newNode = new Node(key, value);
        if (root == null) {
            root = newNode;  // Если дерево пустое, новый узел становится корнем
            root.color = BLACK;  // Корень всегда черный
            size++;
            return null;
        }

        Node parent = null;
        Node current = root;
        // Поиск подходящего места для вставки нового узла
        while (current != null) {
            parent = current;
            if (key < current.key) {
                current = current.left;  // Переходим в левое поддерево
            } else if (key > current.key) {
                current = current.right;  // Переходим в правое поддерево
            } else {
                String oldValue = current.value;
                current.value = value;  // Если ключ уже существует, заменяем его значение
                return oldValue;
            }
        }

        // Добавление нового узла в дерево
        if (key < parent.key) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        newNode.parent = parent;
        fixAfterInsertion(newNode);  // Восстанавливаем баланс дерева после вставки
        size++;
        return null;
    }

    /**
     * Удаляет элемент по ключу из дерева.
     * Если ключ найден, узел удаляется и баланс дерева восстанавливается.
     * @param key ключ элемента, который нужно удалить
     * @return старое значение элемента, если ключ был найден, иначе null
     */
    @Override
    public String remove(Object key) {
        return remove((Integer) key);
    }

    /**
     * Возвращает значение по ключу, или null, если ключ не найден.
     * @param key ключ, по которому нужно найти значение
     * @return значение, соответствующее ключу, или null
     */
    @Override
    public String get(Object key) {
        Node node = getNode((Integer) key);
        return (node == null) ? null : node.value;
    }

    /**
     * Проверяет, содержится ли ключ в дереве.
     * @param key ключ, который нужно проверить
     * @return true, если ключ присутствует, иначе false
     */
    @Override
    public boolean containsKey(Object key) {
        return getNode((Integer) key) != null;
    }

    /**
     * Проверяет, содержится ли значение в дереве.
     * @param value значение, которое нужно проверить
     * @return true, если значение присутствует, иначе false
     */
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

    /**
     * Возвращает количество элементов в дереве.
     * @return количество элементов
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
     * @return true, если дерево пусто, иначе false
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Возвращает подмножество карты с элементами, чьи ключи меньше заданного ключа.
     * @param toKey ключ, до которого включительно нужно выбрать элементы
     * @return подмножество карты с элементами, чьи ключи меньше toKey
     */
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException();
        }
        MyRbMap resultMap = new MyRbMap();
        fillHeadMap(root, toKey, resultMap);
        return resultMap;
    }

    /**
     * Возвращает подмножество карты с элементами, чьи ключи больше или равны заданному ключу.
     * @param fromKey ключ, с которого включительно нужно выбрать элементы
     * @return подмножество карты с элементами, чьи ключи больше или равны fromKey
     */
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap resultMap = new MyRbMap();
        fillTailMap(root, fromKey, resultMap);
        return resultMap;
    }

    /**
     * Возвращает минимальный ключ в дереве.
     * @return минимальный ключ
     */
    @Override
    public Integer firstKey() {
        Node node = minimum(root);
        return node == null ? null : node.key;
    }

    /**
     * Возвращает максимальный ключ в дереве.
     * @return максимальный ключ
     */
    @Override
    public Integer lastKey() {
        Node node = maximum(root);
        return node == null ? null : node.key;
    }

    /**
     * Рекурсивно строит строку с элементами карты в порядке возрастания ключей.
     * Использует средне-обход (in-order traversal).
     * @param node текущий узел
     * @param sb строковый строитель для накопления результата
     */
    private void toString(Node node, StringBuilder sb) {
        if (node == null) return;  // Базовый случай: если узел пустой, ничего не делаем
        toString(node.left, sb);   // Рекурсивно обходим левое поддерево
        if (sb.length() > 1) sb.append(", "); // Добавляем разделитель между парами ключ-значение
        sb.append(node.key).append("=").append(node.value); // Добавляем пару ключ-значение
        toString(node.right, sb);  // Рекурсивно обходим правое поддерево
    }

    /**
     * Исправляет структуру дерева после вставки, чтобы соблюсти правила красно-черного дерева.
     * Использует вращения и изменение цветов узлов для восстановления баланса.
     * @param node узел, с которого начинается восстановление
     */
    private void fixAfterInsertion(Node node) {
        while (node != root && node.parent.color == RED) {  // Пока родитель узла красный, продолжаем исправление
            if (node.parent == node.parent.parent.left) {  // Если родитель — левый потомок
                Node uncle = node.parent.parent.right;  // Дядя — правый потомок дедушки
                if (uncle != null && uncle.color == RED) {  // Если дядя красный, делаем перекраску
                    uncle.color = BLACK;
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;  // Переходим к дедушке
                } else {  // Если дядя черный, делаем повороты
                    if (node == node.parent.right) {  // Если узел правый потомок, делаем левый поворот
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateRight(node.parent.parent);  // Поворачиваем вправо
                }
            } else {  // Если родитель — правый потомок, зеркально выполняем те же действия
                Node uncle = node.parent.parent.left;
                if (uncle != null && uncle.color == RED) {
                    uncle.color = BLACK;
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.color = BLACK;  // Корень всегда черный
    }

    /**
     * Выполняет левый поворот относительно заданного узла.
     * Поворот изменяет местами позиции узла и его правого потомка.
     * @param node узел, относительно которого выполняется поворот
     */
    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.parent = node.parent;
        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }
        rightChild.left = node;
        node.parent = rightChild;
    }

    /**
     * Выполняет правый поворот относительно заданного узла.
     * Поворот изменяет местами позиции узла и его левого потомка.
     * @param node узел, относительно которого выполняется поворот
     */
    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.parent = node.parent;
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }
        leftChild.right = node;
        node.parent = leftChild;
    }

    /**
     * Удаляет элемент по ключу из дерева.
     * Если ключ найден, узел удаляется и баланс дерева восстанавливается.
     * @param key ключ элемента, который нужно удалить
     * @return старое значение элемента, если ключ был найден, иначе null
     */
    public String remove(Integer key) {
        Node node = getNode(key);
        if (node == null) return null;

        String oldValue = node.value;
        deleteNode(node);
        size--;
        return oldValue;
    }

    /**
     * Находит узел по ключу.
     * Рекурсивно ищет узел, сравнивая ключ с текущим узлом.
     * @param key ключ, по которому нужно найти узел
     * @return узел с данным ключом, если он существует, иначе null
     */
    private Node getNode(Integer key) {
        Node current = root;
        while (current != null) {
            if (key < current.key) {
                current = current.left;
            } else if (key > current.key) {
                current = current.right;
            } else {
                return current;  // Нашли нужный узел
            }
        }
        return null;
    }

    /**
     * Удаляет узел из дерева.
     * Если узел имеет двух потомков, на его место ставится его преемник.
     * После удаления узла восстанавливается баланс дерева.
     * @param node узел, который нужно удалить
     */
    private void deleteNode(Node node) {
        if (node.left != null && node.right != null) {
            Node successor = minimum(node.right);  // Находим преемника
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
            node.left = node.right = node.parent = null;
            if (node.color == BLACK) {
                fixAfterDeletion(replacement);  // Восстанавливаем баланс после удаления
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
                node.parent = null;
            }
        }
    }

    /**
     * Исправляет дерево после удаления узла, чтобы соблюсти правила красно-черного дерева.
     * Включает корректировку цветов узлов и выполнение поворотов для восстановления баланса.
     * @param node узел, с которого начинается восстановление после удаления
     */
    private void fixAfterDeletion(Node node) {
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
        node.color = BLACK;  // Восстанавливаем цвет узла
    }

    /**
     * Заполняет подмножество картой с элементами, чьи ключи меньше заданного.
     * Рекурсивно обходит дерево и добавляет элементы, чьи ключи меньше toKey.
     * @param node текущий узел
     * @param toKey ключ, до которого нужно выбрать элементы
     * @param resultMap результат, в который добавляются элементы
     */
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

    /**
     * Заполняет подмножество картой с элементами, чьи ключи больше или равны заданному.
     * Рекурсивно обходит дерево и добавляет элементы, чьи ключи больше или равны fromKey.
     * @param node текущий узел
     * @param fromKey ключ, с которого нужно выбрать элементы
     * @param resultMap результат, в который добавляются элементы
     */
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

    /**
     * Находит узел с минимальным ключом в поддереве.
     * @param node узел, с которого начинается поиск
     * @return узел с минимальным ключом
     */
    private Node minimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Находит узел с максимальным ключом в поддереве.
     * @param node узел, с которого начинается поиск
     * @return узел с максимальным ключом
     */
    private Node maximum(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    // Остальные методы интерфейса SortedMap<Integer, String> можно оставить нереализованными
    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Comparator<? super Integer> comparator() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("Метод не реализован");
    }
}
