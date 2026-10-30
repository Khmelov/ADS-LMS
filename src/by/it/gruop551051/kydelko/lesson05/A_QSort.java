package by.it.gruop551051.kydelko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь.
На площади установлена одна или несколько камер.
Известны данные о том, когда каждая из них включалась и выключалась (отрезки работы)
Известен список событий на площади (время начала каждого события).
Вам необходимо определить для каждого события сколько камер его записали.

В первой строке задано два целых числа:
    число включений камер (отрезки) 1<=n<=50000
    число событий (точки) 1<=m<=50000.

Следующие n строк содержат по два целых числа ai и bi (ai<=bi) -
координаты концов отрезков (время работы одной какой-то камеры).
Последняя строка содержит m целых чисел - координаты точек.
Все координаты не превышают 10E8 по модулю (!).

Точка считается принадлежащей отрезку, если она находится внутри него или на границе.

Для каждой точки в порядке их появления во вводе выведите,
скольким отрезкам она принадлежит.
    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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

        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        //сортируем оба массива
        quickSort(starts, 0, n - 1);
        quickSort(stops, 0, n - 1);

        //для каждой точки: (сколько отрезков уже началось) - (сколько уже закончилось)
        for (int i = 0; i < m; i++) {
            int p = points[i];
            int started = countLessOrEqual(starts, p);
            int finished = countLess(stops, p);
            result[i] = started - finished;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }



    //обычная быстрая сортировка
    private void quickSort(int[] a, int left, int right) {
        if (left >= right) {
            return;
        }
        int pivot = a[(left + right) / 2];
        int i = left;
        int j = right;
        while (i <= j) {
            while (a[i] < pivot) {
                i++;
            }
            while (a[j] > pivot) {
                j--;
            }
            if (i <= j) {
                int tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
                i++;
                j--;
            }
        }
        quickSort(a, left, j);
        quickSort(a, i, right);
    }

    //сколько элементов отсортированного массива <= p (бинарный поиск)
    private int countLessOrEqual(int[] a, int p) {
        int low = 0;
        int high = a.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (a[mid] <= p) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    //сколько элементов отсортированного массива < p (бинарный поиск)
    private int countLess(int[] a, int p) {
        int low = 0;
        int high = a.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (a[mid] < p) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
            if (start > stop) {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            if (this.start < o.start) {
                return -1;
            }
            if (this.start > o.start) {
                return 1;
            }
            return 0;
        }
    }

}
