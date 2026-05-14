package by.it.group451051.sinenko.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String>{

    // Внутренний класс для узла
    private static class Node {
        Integer key;      // ключ
        String value;     // значение
        Node left;        // левый потомок
        Node right;       // правый потомок
        int height;       // высота узла
        
        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.height = 1;  
        }
    }

    // Поля Класса
    private Node root;   // корень дерева
    private int size;    // количество элементов

    // Конструктор
    public MyAvlMap() {
        root = null;
        size = 0;
    }
    
    // Возвращаение высоты узла 
    private int height(Node node) {
        return node == null ? 0 : node.height;
    }
    
    // Обновление высоты узла
    private void updateHeight(Node node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }
    
    // Вычисление баланс-фактора или разница высот левого и правого поддеревьев
    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    // Методы баалансировки 4 типа поворотов

    // Правый поворот
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        
        x.right = y;
        y.left = T2;
        
        updateHeight(y);
        updateHeight(x);
        
        return x;  
    }
    
    // Левый поворот 
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        
        y.left = x;
        x.right = T2;
        
        updateHeight(x);
        updateHeight(y);
        
        return y; 
    }
    
    // Балансировка узла
    private Node balance(Node node) {
        if (node == null) return null;
        
        updateHeight(node);
        int balance = balanceFactor(node);
        
        if (balance > 1) {
            if (balanceFactor(node.left) >= 0) {
                return rotateRight(node);
            } else {
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }
        }
        
        if (balance < -1) {
            if (balanceFactor(node.right) <= 0) {
                return rotateLeft(node);
            } else {
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }
        }
        
        return node; 
    }

    // Вставка элемента в дерево
    @Override
    public String put(Integer key, String value) {
        if (key == null) return null;
        
        String oldValue = get(key);
        
        root = putRecursive(root, key, value);
        
        if (oldValue == null) {
            size++;
        }
        
        return oldValue;
    }
    
    // Рекурсивная вставка
    private Node putRecursive(Node node, Integer key, String value) {
        if (node == null) {
            return new Node(key, value);
        }
    
        if (key < node.key) {
            node.left = putRecursive(node.left, key, value);
        } else if (key > node.key) {
            node.right = putRecursive(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }
    
        return balance(node);
    }

    // Получение значения по ключу
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        Node node = getRecursive(root, intKey);
        return node == null ? null : node.value;
    }
    
    // Рекурсивный поиск
    private Node getRecursive(Node node, Integer key) {
        if (node == null) return null;
        
        if (key < node.key) {
            return getRecursive(node.left, key);
        } else if (key > node.key) {
            return getRecursive(node.right, key);
        } else {
            return node;
        }
    }

    // Удаление элемента по ключу
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Integer intKey = (Integer) key;
        if (!containsKey(intKey)) return null;
    
        String oldValue = get(intKey);
        root = removeRecursive(root, intKey);
        size--;
        return oldValue;
    }
    
    // Рекурсивное удаление
    private Node removeRecursive(Node node, Integer key) {
        if (node == null) return null;
        
        if (key < node.key) {
            node.left = removeRecursive(node.left, key);
        } else if (key > node.key) {
            node.right = removeRecursive(node.right, key);
        } else {
            if (node.left == null) {
                return node.right;  
            } else if (node.right == null) {
                return node.left; 
            }
            
            Node minNode = findMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = removeRecursive(node.right, minNode.key);
        }
    
        return balance(node);
    }
    
    // Нахождение узла с минимальным ключом 
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return get(key) != null;
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
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        sb.append("}");
        return sb.toString();
    }
    
    // Рекурсивный обход для формирования строки
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        
        inOrderTraversal(node.left, sb);
        if (sb.length() > 1) {
            sb.append(", ");
        }
        sb.append(node.key).append("=").append(node.value);
        inOrderTraversal(node.right, sb);
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public String getOrDefault(Object key, String defaultValue) {
        String value = get((Integer) key);
        return value == null ? defaultValue : value;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<String> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}
