package by.it.holupova.lesson03;

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
        MaxHeap heap = new MaxHeap();
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(stream);
        if (!scanner.hasNextInt()) return 0L;
        Integer count = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < count; i++ ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null) {
                    System.out.println(res);
                    if (res > maxValue) {
                        maxValue = res;
                    }
                }
            }

            else if (s.startsWith("Insert")) {
                String[] parts = s.split(" ");
                if (parts.length >= 2) {
                    Long value = Long.parseLong(parts[1]);
                    heap.insert(value);
                }
            }
        }
        return maxValue;
    }

    private class MaxHeap {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение.
        //Будет мало? Ну тогда можете его собрать как Generic и/или использовать в варианте B
        private List<Long> heap = new ArrayList<>();

        //метод для обмена
        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }

        int siftUp(int i) { //просеивание вверх
            while (i > 0) {
                int pr = (i - 1) / 2; //индекс родителя
                //замена
                if (heap.get(i) > heap.get(pr)) {
                    swap(i, pr);
                    i = pr;
                } else {
                    break;
                }
            }
            return i;
        }

        int siftDown(int i) { //просеивание вниз
            int size = heap.size();
            while (true) {
                int l = 2 * i + 1;
                int r = 2 * i + 2;
                int larg = i;
                //проверка есть ли левый и больше ли
                if (l < size && heap.get(l) > heap.get(larg)) {
                    larg = l;
                }
                //проверка есть ли правый и больше ли
                if (r < size && heap.get(r) > heap.get(larg)) {
                    larg = r;
                }
                if (larg != i) {
                    swap(i, larg); //смена мест
                    i = larg;
                } else {
                    break;
                }
            }
                return i;
            }
            void insert ( long value){
                //вставка
                heap.add(value);
                siftUp(heap.size() - 1);
            }

            Long extractMax () { //извлечение и удаление максимума
                if (heap.isEmpty()) return null; //если куча пустая
                Long max = heap.get(0);
                Long psl = heap.remove(heap.size() - 1); //удаляем последний эл
                if (!heap.isEmpty()) {
                    heap.set(0, psl); //последний в корень
                    siftDown(0); //просеивание
                }

                return max;
            }
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        }
    }

    // РЕМАРКА. Это задание исключительно учебное.
    // Свои собственные кучи нужны довольно редко.
    // В реальном приложении все иначе. Изучите и используйте коллекции
    // TreeSet, TreeMap, PriorityQueue и т.д. с нужным CompareTo() для объекта внутри.

