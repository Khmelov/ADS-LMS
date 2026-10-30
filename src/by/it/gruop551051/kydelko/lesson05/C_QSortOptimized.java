package by.it.gruop551051.kydelko.lesson05;

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
        //отрезки сортируем на месте по началу
        quickSort(segments, 0, n - 1);

        //для концов делаем такие же "отрезки" (stop, stop), чтобы
        //отсортировать их той же самой сортировкой
        Segment[] stops = new Segment[n];
        for (int i = 0; i < n; i++) {
            stops[i] = new Segment(segments[i].stop, segments[i].stop);
        }
        quickSort(stops, 0, n - 1);

        //для каждой точки бинарным поиском находим сколько отрезков началось
        //и сколько закончилось до неё, разница - это ответ
        for (int i = 0; i < m; i++) {
            int p = points[i];
            int started = countLessOrEqual(segments, p);
            int finished = countLess(stops, p);
            result[i] = started - finished;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //быстрая сортировка с 3-разбиением и без хвостовой рекурсии
    private void quickSort(Segment[] a, int left, int right) {
        while (left < right) {
            Segment pivot = a[(left + right) / 2];
            int lt = left;   //граница меньших
            int gt = right;  //граница больших
            int i = left;
            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(a, lt, i);
                    lt++;
                    i++;
                } else if (cmp > 0) {
                    swap(a, i, gt);
                    gt--;
                } else {
                    i++;
                }
            }
            //теперь [left..lt-1] меньше опорного, [lt..gt] равны ему,
            //[gt+1..right] больше. Равные трогать уже не надо.
            //в рекурсию идём в меньшую часть, а большую делаем циклом
            if (lt - left < right - gt) {
                quickSort(a, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort(a, gt + 1, right);
                right = lt - 1;}
        }
    }

    private void swap(Segment[] a, int i, int j) {
        Segment tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    //сколько отрезков имеют start <= p (массив отсортирован по start)
    private int countLessOrEqual(Segment[] a, int p) {
        int low = 0;
        int high = a.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (a[mid].start <= p) {
                low = mid + 1;
            } else {
                high = mid;}
        }
        return low;
    }

    //сколько отрезков имеют start < p (массив отсортирован по start)
    private int countLess(Segment[] a, int p) {
        int low = 0;
        int high = a.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (a[mid].start < p) {
                low = mid + 1;
            } else {
                high = mid;}
        }
        return low;
    }


    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
            //на случай если концы пришли в обратном порядке
            if (start > stop) {
                this.start = stop;
                this.stop = start;}
        }

        @Override
        public int compareTo(Object o) {
            //подумайте, что должен возвращать компаратор отрезков
            Segment other = (Segment) o;
            if (this.start < other.start) {
                return -1;
            }
            if (this.start > other.start) {
                return 1;
            }
            return 0;
        }
    }

}
