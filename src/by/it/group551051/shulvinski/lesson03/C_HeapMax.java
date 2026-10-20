package by.it.group551051.shulvinski.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Lesson 3. C_Heap.
// Задача: построить max-кучу = пирамиду = бинарное сбалансированное дерево на массиве.
// ВАЖНО! НЕЛЬЗЯ ИСПОЛЬЗОВАТЬ НИКАКИЕ КОЛЛЕКЦИИ, КРОМЕ ARRAYLIST (его можно, но только для массива)

//      Проверка проводится по данным файла
//      Первая строка входа содержит число операций 1 ≤ n ≤ 100000.
//      Каждая из последующих nn строк задают операцию одного из следующих двух типов:

//      Insert x, где 0 ≤ x ≤ 1000000000 — целое число;
//      ExtractMax.

//      Первая операция добавляет число x в очередь с приоритетами,
//      вторая — извлекает максимальное число и выводит его.

//      Sample Input:
//      6
//      Insert 200
//      Insert 10
//      ExtractMax
//      Insert 5
//      Insert 500
//      ExtractMax
//
//      Sample Output:
//      200
//      500


public class C_HeapMax {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_HeapMax.class.getResourceAsStream("dataC.txt");
        C_HeapMax instance = new C_HeapMax();
        System.out.println("MAX=" + instance.findMaxValue(stream));
    }

    //эта процедура читает данные из файла, ее можно не менять.
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap<Long> heap = new MaxHeap<>();
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt();
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) maxValue = res;
                System.out.println();
                i++;
            }
            if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert"))
                    heap.insert(Long.parseLong(p[1]));
                i++;
                //System.out.println(heap); //debug
            }
        }
        return maxValue;
    }

    private class MaxHeap<T extends Comparable<T>> {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение.
        //Будет мало? Ну тогда можете его собрать как Generic и/или использовать в варианте B
        private List<T> heap = new ArrayList<>();

        int siftDown(int i) { //просеивание вниз
            while(true){
                int l = i * 2 + 1; // вычисление индекса левого потомка
                int r = i * 2 + 2; // вычисление индекса правого потомка
                int m = i; //


                if(l < heap.size() && heap.get(l).compareTo(heap.get(m)) > 0){
                    m = l; // обновление кандидата на максимум
                }

                if(r < heap.size() && heap.get(r).compareTo(heap.get(m))> 0){
                    m = r; // обновление кандидата на максимум
                }

                if(m == i){
                    break;
                }

                // изменение узлов местами
                T temp = heap.get(i);
                heap.set(i,heap.get(m));
                heap.set(m,temp);

                i = m; // просеивание вниз с новой позиции
            }


            return i;
        }

        int siftUp(int i) { //просеивание вверх
            while(i > 0){
                int p = (i -1) / 2;
                if (heap.get(i).compareTo(heap.get(p)) > 0){

                    T temp = heap.get(i);
                    heap.set(i,heap.get(p)); // поднятие элемента выше
                    heap.set(p,temp);
                    i = p;
                }else{
                    break;
                }
            }

            return i;
        }

        void insert(T value) { //вставка

            heap.add(value);
            siftUp(heap.size() -1);


        }

        T extractMax() { //извлечение и удаление максимума
            if (heap.isEmpty()){
                return null;
            }
            T result = heap.get(0); // получение максимума

            T last = heap.remove(heap.size() - 1); // получение и удаление последнего элемента
            if (!heap.isEmpty()){
                heap.set(0,last); // постановка бывшего последнего элемента на место корня
                siftDown(0); // просеивание его вниз до восстановления порядка кучи
            }


            return result;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.
}
