package by.it.group451051.naumchik.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].

    (Такая пара элементов называется инверсией массива.
    Количество инверсий в массиве является в некотором смысле
    его мерой неупорядоченности: например, в упорядоченном по неубыванию
    массиве инверсий нет вообще, а в массиве, упорядоченном по убыванию,
    инверсию образуют каждые (т.е. любые) два элемента.
    )

Sample Input:
5
2 3 9 2 9
Sample Output:
2

Головоломка (т.е. не обязательно).
Попробуйте обеспечить скорость лучше, чем O(n log n) за счет многопоточности.
Докажите рост производительности замерами времени.
Большой тестовый массив можно прочитать свой или сгенерировать его программно.
*/


public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!
        // Вспомогательный массив для слияния (нужен, чтобы не создавать его на каждом шаге рекурсии)
        int[] temp = new int[n];
        // Запускаем рекурсивную сортировку слиянием, которая попутно считает инверсии
        result = mergeSortAndCount(a, temp, 0, n - 1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Рекурсивно сортирует отрезок массива arr[left..right] и возвращает количество инверсий внутри этого отрезка.
    private int mergeSortAndCount(int[] arr, int[] temp, int left, int right) {
        int invCount = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;   // находим середину, чтобы избежать переполнения
            // Считаем инверсии в левой половине
            invCount += mergeSortAndCount(arr, temp, left, mid);
            // Считаем инверсии в правой половине
            invCount += mergeSortAndCount(arr, temp, mid + 1, right);
            // Считаем инверсии, где один элемент из левой половины, другой из правой
            invCount += mergeAndCount(arr, temp, left, mid, right);
        }
        return invCount;
    }

    // * Сливает две упорядоченные части arr[left..mid] и arr[mid+1..right] в один упорядоченный отрезок.
    private int mergeAndCount(int[] arr, int[] temp, int left, int mid, int right) {
        int i = left;
        int j = mid + 1;
        int k = left;
        int invCount = 0;

        // Сливаем две половины, одновременно подсчитывая инверсии
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                // Элемент левой части не больше правого — инверсии нет
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
                invCount += (mid - i + 1);
            }
        }

        // Копируем остатки левой части (если остались)
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        // Копируем остатки правой части (если остались)
        while (j <= right) {
            temp[k++] = arr[j++];
        }

        // Копируем отсортированный отрезок обратно в исходный массив
        for (i = left; i <= right; i++) {
            arr[i] = temp[i];
        }
        return invCount;
    }

}
