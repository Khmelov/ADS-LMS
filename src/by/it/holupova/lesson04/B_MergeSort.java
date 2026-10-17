package by.it.holupova.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Реализуйте сортировку слиянием для одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо отсортировать полученный массив.

Sample Input:
5
2 3 9 2 9
Sample Output:
2 2 3 9 9
*/
public class B_MergeSort {

    //рекурсивный метод деления массива
    void mergeSort(int [] a, int l, int r) {
        if (l < r) {
            int s = (l + r) / 2;
            ;

            //рекурсивная сортировка слева
            mergeSort(a, l, s);
            //рекурсивная сортировка справа
            mergeSort(a, s + 1, r);
            //слияние двух частей
            merge(a, l, s, r);
        }
    }

    //Метод слияния двух частей
    void merge(int [] a, int l, int s, int r) {
        //размеры временных половин
        int n1 = s - l + 1;
        int n2 = r - s;
        //временные массивы
        int[] L = new int[n1];
        int[] R = new int[n2];
        ;

        //копия данных
        for (int i = 0; i < n1; i++) {
            L[i] = a[l + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = a[s + 1 + j];
        }
        //индексы для слияния и слияние
        int i = 0;
        int j = 0;
        int c = l; //i и j - для L и R; c для основного
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                a[c] = L[i];
                i++;
            } else {
                a[c] = R[j];
                j++;
            }
            c++;
        }
            //копии из L
            while (i < n1) {
                a[c] = L[i];
                i++;
                c++;
            }
            //копии из R
            while(j < n2) {
                a[c] = R[j];
                j++;
                c++;
            }

     }
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием
        mergeSort(a, 0, n-1);


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
