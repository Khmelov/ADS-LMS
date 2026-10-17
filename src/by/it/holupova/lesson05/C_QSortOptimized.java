package by.it.holupova.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        //сортировка отрезков по началу
        java.util.Arrays.sort(segments);

        //массив концов + его сортировка
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            ends[i] = segments[i].stop;
        }
        java.util.Arrays.sort(ends);

        //бинарный поиск для каждой точки
        for (int i = 0; i < m; i++) {
            int p = points[i];
            //бин. поиск в segments по start <= p
            int kS = up(segments, p);
            //бин. поиск в ends по stop < p
            int kE = down(ends, p);

            result[i] = kS - kE;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //метод для поиска кол-во элементов в массиве, где начало меньше искомого
    private int up(Segment[] arr, int c) {
        int l = 0;
        int r = arr.length;
        while (l < r) {
            int s = (l + r) / 2;
            if (arr[s].start <= c) {
                l = s + 1;
            } else {
                r = s;
            }
        }
        return l;
    }

    //метод для поиска кол-во элементов в массиве, где меньше искомого
    private int down(int[] arr, int c) {
        int l = 0;
        int r = arr.length;
        while (l < r) {
            int s = (l + r) / 2;
            if (arr[s] < c) {
                l = s + 1;
            } else {
                r = s;
            }
        }
        return l;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }

}

