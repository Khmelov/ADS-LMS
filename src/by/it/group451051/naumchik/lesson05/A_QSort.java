package by.it.group451051.naumchik.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Stack;

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
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(Math.min(a, b), Math.max(a, b));
        }

        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // создаём массивы начал и концов
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        // сортируем массивы быстрой сортировкой
        quickSortIterative(starts);
        quickSortIterative(ends);



        // для каждой точки ищем количество отрезков, её покрывающих
        for (int i = 0; i < m; i++) {
            int point = points[i];
            // сколько отрезков начинаются не позже точки (start <= point)
            int leftCount = upperBound(starts, point);
            // сколько отрезков заканчиваются раньше точки (end < point)
            int rightCount = lowerBound(ends, point);
            result[i] = leftCount - rightCount;
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    // быстрая сортировка
    // ---------- Итеративная быстрая сортировка ----------
    private void quickSortIterative(int[] arr) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{0, arr.length - 1});
        while (!stack.isEmpty()) {
            int[] bounds = stack.pop();
            int left = bounds[0];
            int right = bounds[1];
            if (left < right) {
                int pivotIndex = partition(arr, left, right);
                // сначала кладём правую часть, потом левую (порядок не важен)
                stack.push(new int[]{left, pivotIndex - 1});
                stack.push(new int[]{pivotIndex + 1, right});
            }
        }
    }


    private int partition(int[] arr, int left, int right) {
        // выбираем случайный опорный элемент (избегаем худшего случая)
        int randomIndex = left + (int) (Math.random() * (right - left + 1));
        swap(arr, randomIndex, right);
        int pivot = arr[right];
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, right);
        return i + 1;
    }

    private void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    // возвращает количество элементов <= x (upper_bound)
    private int upperBound(int[] arr, int x) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (arr[mid] <= x) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo; // количество элементов <= x
    }


    // возвращает количество элементов < x (lower_bound)
    private int lowerBound(int[] arr, int x) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (arr[mid] < x) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo; // количество элементов < x
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
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков

            return 0;
        }
    }

}
