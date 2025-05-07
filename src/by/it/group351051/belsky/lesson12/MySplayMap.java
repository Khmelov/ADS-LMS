package by.it.group351051.belsky.lesson12;

import java.util.*;

/*
    Задание на уровень C

    Создайте class MySplayMap, который реализует интерфейс NavigableMap<Integer, String>
    и работает на основе splay-дерева
    БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    Метод toString() должен выводить элементы в порядке возрастания ключей
    Формат вывода: скобки (фигурные) и разделители
    (знак равенства и запятая с пробелом) должны
    быть такими же как в методе toString() обычной коллекции
 */

/**
 * Реализация NavigableMap с использованием Splay-дерева.
 * Обеспечивает быстрый доступ к часто используемым элементам.
 */
public class MySplayMap implements NavigableMap<Integer, String> {

    /**
     * Внутренний класс для представления узлов Splay-дерева.
     */
    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        /**
         * Конструктор узла.
         *
         * @param key    ключ узла
         * @param value  значение узла
         * @param parent родительский узел
         */
        Node(Integer key, String value, Node parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    /**
     * Возвращает строковое представление карты.
     *
     * @return строковое представление
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        toStringInOrder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    /**
     * Добавляет или обновляет значение по ключу.
     *
     * @param key   ключ
     * @param value значение
     * @return предыдущее значение или null, если ключ отсутствовал
     */
    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value, null);
            size++;
            return null;
        }

        Node curr = root;
        Node parent = null;
        while (curr != null) {
            parent = curr;
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else {
                String oldVal = curr.value;
                curr.value = value;
                splay(curr);
                return oldVal;
            }
        }

        Node newNode = new Node(key, value, parent);
        if (key < parent.key) parent.left = newNode;
        else parent.right = newNode;
        splay(newNode);
        size++;
        return null;
    }

    /**
     * Удаляет элемент по ключу.
     *
     * @param key ключ для удаления
     * @return удаленное значение или null, если ключ отсутствовал
     */
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        Node node = getNode(intKey);
        if (node == null) return null;

        splay(node);
        String oldValue = node.value;

        if (node.left == null) {
            replaceRoot(node.right);
        } else {
            Node maxLeft = node.left;
            while (maxLeft.right != null) maxLeft = maxLeft.right;
            splay(maxLeft);
            maxLeft.right = node.right;
            if (node.right != null) node.right.parent = maxLeft;
            replaceRoot(maxLeft);
        }

        size--;
        return oldValue;
    }

    /**
     * Возвращает значение по ключу.
     *
     * @param key ключ для поиска
     * @return значение или null, если ключ отсутствует
     */
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = getNode((Integer) key);
        if (node != null) splay(node);
        return node == null ? null : node.value;
    }

    /**
     * Проверяет наличие ключа в карте.
     *
     * @param key ключ для проверки
     * @return true, если ключ присутствует
     */
    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return getNode((Integer) key) != null;
    }

    /**
     * Проверяет наличие значения в карте.
     *
     * @param value значение для проверки
     * @return true, если значение присутствует
     */
    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return containsValue(root, (String) value);
    }

    /**
     * Возвращает количество элементов в карте.
     *
     * @return количество элементов
     */
    @Override
    public int size() {
        return size;
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
     * Проверяет, пуста ли карта.
     *
     * @return true, если карта не содержит элементов
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Возвращает часть карты для ключей меньше toKey.
     *
     * @param toKey верхняя граница (исключительно)
     * @return новая карта с подходящими элементами
     */
    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap result = new MySplayMap();
        collectRange(root, null, toKey, result);
        return result;
    }

    /**
     * Возвращает часть карты для ключей больше или равных fromKey.
     *
     * @param fromKey нижняя граница (включительно)
     * @return новая карта с подходящими элементами
     */
    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap result = new MySplayMap();
        collectRange(root, fromKey, null, result);
        return result;
    }

    /**
     * Возвращает первый (наименьший) ключ.
     *
     * @return первый ключ
     * @throws NoSuchElementException если карта пуста
     */
    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.left != null) node = node.left;
        splay(node);
        return node.key;
    }

    /**
     * Возвращает последний (наибольший) ключ.
     *
     * @return последний ключ
     * @throws NoSuchElementException если карта пуста
     */
    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node node = root;
        while (node.right != null) node = node.right;
        splay(node);
        return node.key;
    }

    /**
     * Возвращает наибольший ключ, меньший заданного.
     *
     * @param key ключ для сравнения
     * @return найденный ключ или null
     */
    @Override
    public Integer lowerKey(Integer key) {
        Node current = root;
        Integer result = null;

        while (current != null) {
            if (key > current.key) {
                result = current.key;
                current = current.right;
            } else {
                current = current.left;
            }
        }

        return result;
    }

    /**
     * Возвращает наибольший ключ, меньший или равный заданному.
     *
     * @param key ключ для сравнения
     * @return найденный ключ или null
     */
    @Override
    public Integer floorKey(Integer key) {
        Node current = root;
        Integer result = null;

        while (current != null) {
            if (key < current.key) {
                current = current.left;
            } else {
                result = current.key;
                current = current.right;
            }
        }

        return result;
    }

    /**
     * Возвращает наименьший ключ, больший или равный заданному.
     *
     * @param key ключ для сравнения
     * @return найденный ключ или null
     */
    @Override
    public Integer ceilingKey(Integer key) {
        Node current = root;
        Integer result = null;

        while (current != null) {
            if (key > current.key) {
                current = current.right;
            } else {
                result = current.key;
                current = current.left;
            }
        }

        return result;
    }

    /**
     * Возвращает наименьший ключ, больший заданного.
     *
     * @param key ключ для сравнения
     * @return найденный ключ или null
     */
    @Override
    public Integer higherKey(Integer key) {
        Node current = root;
        Integer result = null;

        while (current != null) {
            if (key < current.key) {
                result = current.key;
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return result;
    }

    /**
     * Добавляет все элементы из указанной карты.
     *
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
     * Выполняет левый поворот вокруг узла x.
     *
     * @param x узел, вокруг которого выполняется поворот
     */
    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y != null) {
            x.right = y.left;
            if (y.left != null) y.left.parent = x;
            y.parent = x.parent;
        }

        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;

        if (y != null) y.left = x;
        x.parent = y;
    }

    /**
     * Выполняет правый поворот вокруг узла x.
     *
     * @param x узел, вокруг которого выполняется поворот
     */
    private void rotateRight(Node x) {
        Node y = x.left;
        if (y != null) {
            x.left = y.right;
            if (y.right != null) y.right.parent = x;
            y.parent = x.parent;
        }

        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;

        if (y != null) y.right = x;
        x.parent = y;
    }

    /**
     * Выполняет операцию splay для узла x (перемещает его в корень).
     *
     * @param x узел для перемещения в корень
     */
    private void splay(Node x) {
        if (x == null) return;
        while (x.parent != null) {
            if (x.parent.parent == null) {
                // Zig-шаг
                if (x.parent.left == x) rotateRight(x.parent);
                else rotateLeft(x.parent);
            } else if (x.parent.left == x && x.parent.parent.left == x.parent) {
                // Zig-zig-шаг
                rotateRight(x.parent.parent);
                rotateRight(x.parent);
            } else if (x.parent.right == x && x.parent.parent.right == x.parent) {
                // Zig-zig-шаг
                rotateLeft(x.parent.parent);
                rotateLeft(x.parent);
            } else {
                // Zig-zag-шаг
                if (x.parent.left == x) {
                    rotateRight(x.parent);
                    rotateLeft(x.parent);
                } else {
                    rotateLeft(x.parent);
                    rotateRight(x.parent);
                }
            }
        }
        root = x;
    }

    /**
     * Находит узел по ключу.
     *
     * @param key ключ для поиска
     * @return найденный узел или null
     */
    private Node getNode(Integer key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x;
        }
        return null;
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) return false;
        return value.equals(node.value)
                || containsValue(node.left, value)
                || containsValue(node.right, value);
    }

    /**
     * Заменяет корень дерева.
     *
     * @param newRoot новый корень дерева
     */
    private void replaceRoot(Node newRoot) {
        root = newRoot;
        if (newRoot != null) newRoot.parent = null;
    }

    /**
     * Собирает элементы в заданном диапазоне.
     *
     * @param node текущий узел
     * @param from нижняя граница (null - нет ограничения)
     * @param to   верхняя граница (null - нет ограничения)
     * @param map  карта для сохранения результатов
     */
    private void collectRange(Node node, Integer from, Integer to, MySplayMap map) {
        if (node == null) return;
        if (from != null && node.key.compareTo(from) < 0) {
            collectRange(node.right, from, to, map);
        } else if (to != null && node.key.compareTo(to) >= 0) {
            collectRange(node.left, from, to, map);
        } else {
            collectRange(node.left, from, to, map);
            map.put(node.key, node.value);
            collectRange(node.right, from, to, map);
        }
    }

    private void toStringInOrder(Node node, StringBuilder sb) {
        if (node == null) return;
        toStringInOrder(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        toStringInOrder(node.right, sb);
    }

    // Остальные методы интерфейса NavigableMap<Integer, String> можно оставить нереализованными
    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("Метод не реализован");
    }

    @Override
    public Comparator<? super Integer> comparator() {
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
    public Set<Map.Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("Метод не реализован");
    }
}