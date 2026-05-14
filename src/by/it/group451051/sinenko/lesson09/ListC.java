package by.it.group451051.sinenko.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class ListC<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    private Object[] elements;  // массив для хранения элементов
    private int size;           // количество элементов в списке
    private static final int DEFAULT_CAPACITY = 10;  // начальный размер массива

    public ListC() {
        elements = new Object[DEFAULT_CAPACITY]; // создаём массив для хранения элементов
        size = 0;                                // изначально элементов 0
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");   // объект для сборки строки, начинаем с символа [ 
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");       // после первого элемента всем следующим добавляем запятую
            sb.append(elements[i]);               // плюс сам элемент
        }
        sb.append("]");         //закрывает строку скобка
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        // если массив заполнен то расширяем его
        if (size == elements.length) { 
            Object[] newElements = new Object[elements.length * 3 / 2 + 1];       // создаём новый массив большего размера
            System.arraycopy(elements, 0, newElements, 0, size);  // копируем все элементы в новый массив
            elements = newElements;                                                // замена старого массива
        }

        elements[size] = e;
        size++;

        return true;
    }

    @Override
    public E remove(int index) {
        // проверка индекса
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        // сохраняем удаляемый элемент
        E removed = (E) elements[index];

        // сдвиг элементов влево
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        // освобождаем ссылку
        elements[size - 1] = null;  
        size--;
        
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, E element) { // добавление элемента по индексу
        if (index < 0 || index > size) {    // проверка индекса 
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size); // если индекс не в пределах от 0 до size, то выбрасываем исключение
        }
        if (size == elements.length) { // если массив заполнен, то расширяем его
            grow();
        }
        for (int i = size; i > index; i--) { // сдвигаем элементы вправо, начиная с последнего до индекса
            elements[i] = elements[i - 1];
        } 
        elements[index] = element; // вставляем новый элемент на нужную позицию
        size++;
    }

    @Override
    public boolean remove(Object o) { // удаление элемента по значению
        for (int i = 0; i < size; i++) {    
            if (Objects.equals(elements[i], o)) { // если элемент найден, то удаляем его и возвращаем true
                remove(i);
                return true;    
            }
        }
        return false;
    }

    @Override
    public E set(int index, E element) {     // замена элемента по индексу
        checkIndex(index);                   // проверка индекса
        @SuppressWarnings("unchecked")       // сохраняем старый элемент для возврата
        E old = (E) elements[index];         // заменяем элемент на новый
        elements[index] = element;           // возвращаем старый элемент
        return old;
    }
    


    @Override
    public boolean isEmpty() {
        return size == 0;        // список пуст, если size равен 0
    }


    @Override
    public void clear() {     // очистка списка, освобождение всех ссылок и установка размера в 0
        for (int i = 0; i < size; i++) {       
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public int indexOf(Object o) {         // поиск индекса первого вхождения элемента
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], o)) {    // если элемент найден, возвращаем его индекс
                return i;
            }
        }
        return -1;       // если элемент не найден, возвращаем -1
    }

    @Override
    public E get(int index) {              // получение элемента по индексу
        checkIndex(index);                 // проверка индекса
        @SuppressWarnings("unchecked")     // приведение к типу E
        E result = (E) elements[index];    // возвращаем элемент
        return result;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;              // элемент содержится в списке, если его индекс не равен -1
    }

    @Override
    public int lastIndexOf(Object o) {      // поиск индекса последнего вхождения элемента
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(elements[i], o)) {     // если элемент найден, возвращаем его индекс
                return i;
            }
        }
        return -1;        // если элемент не найден, возвращаем -1
    }

    // вспомогательные методы
    private void grow() {        // расширение массива при необходимости
        int newCapacity = elements.length * 3 / 2 + 1;                         // новый размер массива
        Object[] newElements = new Object[newCapacity];                        // создаём новый массив
        System.arraycopy(elements, 0, newElements, 0, size);   // копируем все элементы в новый массив
        elements = newElements;                                                // замена старого массива
    }

    private void checkIndex(int index) {  // проверка индекса на валидность
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);    // если индекс не в пределах от 0 до size-1, то выбрасываем исключение
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {     // проверка, что все элементы коллекции содержатся в списке
        for (Object item : c) {
            if (!contains(item)) return false;        // если хотя бы одного элемента нет, возвращаем false
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {     // добавление всех элементов коллекции в конец списка
        boolean changed = false;                           // флаг для отслеживания изменений
        for (E item : c) {
            if (add(item)) changed = true;                 // если элемент успешно добавлен, устанавливаем флаг в true
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {    // добавление всех элементов коллекции по индексу
        boolean changed = false;
        for (E item : c) {
            add(index++, item);          // добавляем элемент по индексу и увеличиваем индекс для следующего элемента
            changed = true;              // если хотя бы один элемент добавлен, устанавливаем флаг в true
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {      // удаление всех элементов коллекции из списка
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {       // проходим в обратном порядке, чтобы корректно удалять элементы
            if (c.contains(elements[i])) {          // если элемент содержится в коллекции, удаляем его
                remove(i);                          // удаление по индексу для корректного сдвига элементов
                changed = true;                     // если элемент успешно удалён, устанавливаем флаг в true
            }        
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {       // удаление всех элементов, которые не содержатся в коллекции
        boolean changed = false;
        for (int i = size - 1; i >= 0; i--) {         // проходим в обратном порядке, чтобы корректно удалять элементы
            if (!c.contains(get(i))) {                // если элемент не содержится в коллекции, удаляем его
                remove(i);                            // удаление по индексу для корректного сдвига элементов
                changed = true;                       // если хотя бы один элемент удалён, устанавливаем флаг в true
            }
        }
        return changed;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
