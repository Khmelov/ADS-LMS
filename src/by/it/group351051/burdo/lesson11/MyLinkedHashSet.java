package by.it.group351051.burdo.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class MyLinkedHashSet<E> implements Set<E> {
    // Константы, определяющие начальный размер таблицы и коэффициент загрузки
    private static final int DEFAULT_CAPACITY = 16; // Начальный размер таблицы
    private static final float LOAD_FACTOR = 0.75f; // Коэффициент загрузки, определяющий момент, когда нужно расширить таблицу

    private Node<E>[] table; // Таблица хеширования
    private Node<E> head; // Ссылка на первый добавленный элемент (для поддержания порядка добавления)
    private Node<E> tail; // Ссылка на последний добавленный элемент (для поддержания порядка добавления)
    private int size; // Размер множества

    // Конструктор, инициализирующий таблицу с дефолтным размером
    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY]; // Инициализация таблицы с заданным размером
        size = 0; // Начальный размер множества равен 0
    }

    // Вспомогательный класс для хранения элементов множества и поддержания порядка добавления
    private static class Node<E> {
        final E element; // Хранимый элемент
        final int hash; // Хеш элемента
        Node<E> next; // Ссылка на следующий элемент в цепочке коллизий
        Node<E> before, after; // Ссылки для поддержания порядка добавления элементов

        // Конструктор узла
        Node(E element, int hash, Node<E> next) {
            this.element = element;
            this.hash = hash;
            this.next = next;
        }
    }

    // Метод для представления множества в виде строки
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head; // Начинаем с первого добавленного элемента
        boolean first = true;

        // Проходим по всем элементам множества в порядке их добавления
        while (current != null) {
            if (!first) {
                sb.append(", "); // Добавляем запятую между элементами
            }
            sb.append(current.element); // Добавляем элемент в строку
            first = false;
            current = current.after; // Переходим к следующему элементу
        }

        sb.append("]");
        return sb.toString();
    }

    // Метод для получения размера множества
    @Override
    public int size() {
        return size; // Возвращаем количество элементов
    }

    // Метод для проверки, пусто ли множество
    @Override
    public boolean isEmpty() {
        return size == 0; // Если размер 0, значит множество пусто
    }

    // Метод для очистки множества
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null; // Очищаем таблицу
        }
        head = tail = null; // Очищаем ссылки на первый и последний элементы
        size = 0; // Размер множества = 0
    }

    // Метод для проверки, содержится ли элемент в множестве
    @Override
    public boolean contains(Object o) {
        if (o == null) return false; // Если объект null, то его нет в множестве

        int hash = hash(o); // Вычисляем хеш объекта
        int index = (table.length - 1) & hash; // Находим индекс в таблице

        // Проходим по цепочке коллизий в таблице
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && o.equals(node.element)) { // Если нашли совпадение, возвращаем true
                return true;
            }
        }

        return false; // Если не нашли, возвращаем false
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    // Метод для добавления элемента в множество
    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException(); // Нельзя добавить null

        int hash = hash(e); // Вычисляем хеш элемента
        int index = (table.length - 1) & hash; // Находим индекс в таблице

        // Проверяем, есть ли уже такой элемент в цепочке коллизий
        for (Node<E> node = table[index]; node != null; node = node.next) {
            if (node.hash == hash && e.equals(node.element)) {
                return false; // Если такой элемент уже есть, не добавляем
            }
        }

        // Добавляем новый элемент в начало цепочки коллизий
        Node<E> newNode = new Node<>(e, hash, table[index]);
        table[index] = newNode;

        // Добавляем элемент в конец списка для поддержания порядка добавления
        linkNodeLast(newNode);

        size++; // Увеличиваем размер множества

        // Проверяем, нужно ли расширить таблицу
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }

        return true; // Элемент добавлен
    }

    // Метод для удаления элемента из множества
    @Override
    public boolean remove(Object o) {
        if (o == null) return false; // Если объект null, ничего не удаляем

        int hash = hash(o); // Вычисляем хеш объекта
        int index = (table.length - 1) & hash; // Находим индекс в таблице
        Node<E> prev = null;

        // Проходим по цепочке коллизий в таблице
        for (Node<E> node = table[index]; node != null; prev = node, node = node.next) {
            if (node.hash == hash && o.equals(node.element)) { // Если нашли элемент
                // Удаляем элемент из цепочки коллизий
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }

                // Удаляем элемент из списка для поддержания порядка добавления
                unlinkNode(node);

                size--; // Уменьшаем размер множества
                return true; // Элемент удален
            }
        }

        return false; // Если не нашли элемент, возвращаем false
    }

    // Метод для проверки, содержатся ли все элементы из коллекции в текущем множестве
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) { // Если хотя бы одного элемента нет, возвращаем false
                return false;
            }
        }
        return true; // Все элементы содержатся в множестве
    }

    // Метод для добавления всех элементов из коллекции в множество
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) { // Если элемент был добавлен, устанавливаем modified в true
                modified = true;
            }
        }
        return modified;
    }

    // Метод для удаления всех элементов из коллекции из множества
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            while (remove(o)) { // Пока элемент можно удалить, удаляем его
                modified = true;
            }
        }
        return modified;
    }

    // Метод для оставления только тех элементов, которые содержатся в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<E> current = head;

        // Проходим по всем элементам в порядке добавления
        while (current != null) {
            Node<E> next = current.after;
            if (!c.contains(current.element)) { // Если элемента нет в коллекции, удаляем его
                remove(current.element);
                modified = true;
            }
            current = next;
        }

        return modified;
    }

    // Метод для вычисления хеша объекта
    private int hash(Object o) {
        int h = o.hashCode();
        return h ^ (h >>> 16); // Улучшаем распределение хешей
    }

    // Метод для добавления узла в конец списка для поддержания порядка добавления
    private void linkNodeLast(Node<E> node) {
        if (tail == null) {
            head = tail = node; // Если это первый элемент, он и head, и tail
        } else {
            tail.after = node; // Связываем текущий tail с новым узлом
            node.before = tail; // Новый узел указывает на старый tail
            tail = node; // Новый узел становится tail
        }
    }

    // Метод для удаления узла из списка порядка добавления
    private void unlinkNode(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;

        if (before == null) {
            head = after; // Если это был первый элемент, head сдвигается на следующий
        } else {
            before.after = after; // Связываем предыдущий элемент с следующим
        }

        if (after == null) {
            tail = before; // Если это был последний элемент, tail сдвигается на предыдущий
        } else {
            after.before = before; // Связываем следующий элемент с предыдущим
        }

        node.before = node.after = null; // Разрываем связи узла
    }

    // Метод для увеличения размера таблицы и перехеширования элементов
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1; // Удваиваем размер

        Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
        table = newTable;

        // Перехешируем все элементы и добавляем их в новую таблицу
        for (int i = 0; i < oldCapacity; i++) {
            Node<E> node = oldTable[i];
            while (node != null) {
                Node<E> next = node.next;
                int index = (newCapacity - 1) & node.hash;
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
    }
}
