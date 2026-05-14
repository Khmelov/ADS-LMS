package by.it.group451051.sinenko.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    
    private Object[] elements;  // массив для хранения элементов
    private int size;           // количество элементов в списке
    private static final int DEFAULT_CAPACITY = 10;  // начальный размер массива

    public ListA() {
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

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element) {

    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }


    @Override
    public void clear() {

    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }


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
