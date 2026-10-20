package by.it.group551051.shulvinski.lesson04;

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
            System.out.println(a[i]);
        }

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием

        a = mergeSort(a);


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


    int[] mergeSort(int[] arr){
        if (arr.length <= 1){
            return arr;
        }
        int mid = arr.length  / 2; // делится массив пополам

        int[] left = new int[mid];
        int[] right = new int[arr.length - mid];

        for (int i = 0; i < mid; i++){
            left[i] = arr[i]; // комирование левой половины
        }

        for (int i = mid; i < arr.length; i++){
            right[i - mid] = arr[i]; // копирование правой половины
        }

        return merge(mergeSort(left),
                mergeSort(right)); // рекурсивный вызов для сортировки левой и правой половины
    }

    int[] merge(int[] left, int[] right) {
        // результирующий отсортированный массив
        int[] result = new int[left.length + right.length];

        int leftIndex = 0;
        int rightIndex = 0;
        int resultIndex = 0;

        // пока в обеих половинах есть непросмотренные элементы
        while(leftIndex < left.length
        && rightIndex < right.length){
            if (left[leftIndex] < right[rightIndex]){
                result[resultIndex] = left[leftIndex]; // берем меньший элемент слева
                leftIndex++;
            }else{
                result[resultIndex] = right[rightIndex]; // меньший эоемент справа
                rightIndex++;
            }
            resultIndex++;
        }

        // дописывание в конец оставшихся элементов обеих половин
        while(leftIndex < left.length){
            result[resultIndex] = left[leftIndex];
            leftIndex++;
            resultIndex++;
        }
        while(rightIndex < right.length){
            result[resultIndex] = right[rightIndex];
            rightIndex++;
            resultIndex++;
        }

        return result;
    }



}
