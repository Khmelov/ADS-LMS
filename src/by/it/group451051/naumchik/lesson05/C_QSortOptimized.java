package by.it.group451051.naumchik.lesson05;

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
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(Math.min(a, b), Math.max(a, b));
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        scanner.close();
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        // Создаём массивы начал и концов
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        quickSort3Way(starts, 0, n - 1);
        quickSort3Way(ends, 0, n - 1);


        // Для каждой точки вычисляем количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int leftCount = upperBound(starts, point);   // количество start <= point
            int rightCount = lowerBound(ends, point);    // количество end < point
            result[i] = leftCount - rightCount;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }



    // Быстрая сортировка с трёхчастным разбиением
    // Сортировка массива arr на подмассиве [lo, hi]
    //  рекурсивно обрабатываем меньший подмассив,
    // а больший – итеративно (через цикл)
    private void quickSort3Way(int[] arr, int lo, int hi) {
        while (lo < hi) {
            // Выполняем трёхчастное разбиение относительно pivot = arr[lo]
            // (можно выбрать медиану или случайный, но для простоты – левый элемент)
            int[] pivotRange = partition3Way(arr, lo, hi);
            int lt = pivotRange[0];   // индекс первого элемента = pivot
            int gt = pivotRange[1];   // индекс последнего элемента = pivot
            // Рекурсивно сортируем левую часть (меньше pivot)
            quickSort3Way(arr, lo, lt - 1);
            // Теперь правая часть (больше pivot) – её мы будем обрабатывать в следующей итерации цикла
            lo = gt + 1;              // элиминация хвостовой рекурсии для правой части
        }
    }



    // Трёхчастное разбиение
    // arr[lo..hi] разбивается на три части: < pivot, = pivot, > pivot
    // возвращает массив из двух индексов: [первый индекс equal, последний индекс equal]
    private int[] partition3Way(int[] arr, int lo, int hi) {
        int pivot = arr[lo];
        int lt = lo;     // arr[lo..lt-1] < pivot
        int i = lo + 1;  // текущий элемент
        int gt = hi;     // arr[gt+1..hi] > pivot
        while (i <= gt) {
            if (arr[i] < pivot) {
                swap(arr, i, lt);
                lt++;
                i++;
            } else if (arr[i] > pivot) {
                swap(arr, i, gt);
                gt--;
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }


    private void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }



    // Бинарные поиски
    private int upperBound(int[] arr, int x) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (arr[mid] <= x) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }


    private int lowerBound(int[] arr, int x) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (arr[mid] < x) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }




    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            //подумайте, что должен возвращать компаратор отрезков
            Segment other = (Segment) o;
            return Integer.compare(this.start, other.start);
        }
    }

}
