package by.it.gruop551051.kydelko.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая невозростающая подпоследовательность

Дано:
    целое число 1<=n<=1E5 ( ОБРАТИТЕ ВНИМАНИЕ НА РАЗМЕРНОСТЬ! )
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    для которой каждый элемент A[i[k]] не больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]>=A[i[j+1]].

    В первой строке выведите её длину k,
    во второй - её индексы i[1]<i[2]<…<i[k]
    соблюдая A[i[1]]>=A[i[2]]>= ... >=A[i[n]].

    (индекс начинается с 1)

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    5 3 4 4 2

    Sample Output:
    4
    1 3 4 5
*/


public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи методами динамического программирования (!!!)

        int[] tail = new int[n];
        // prev[i] - индекс предыдущего элемента в цепочке для элемента i (нужен для восстановления)
        int[] prev = new int[n];
        int result = 0;

        for (int i = 0; i < n; i++) {
            // бинарным поиском ищем первую позицию, где значение в tail строго меньше m[i]
            int left = 0;
            int right = result;
            while (left < right) {
                int mid = (left + right) / 2;
                if (m[tail[mid]] >= m[i]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            // left - позиция, куда ставим текущий элемент
            if (left > 0) {
                prev[i] = tail[left - 1];
            } else {
                prev[i] = -1;
            }
            tail[left] = i;
            // если дошли до конца, то цепочка стала длиннее
            if (left == result) {
                result++;
            }
        }

        // восстанавливаем индексы с конца
        int[] indexes = new int[result];
        int cur = tail[result - 1];
        for (int k = result - 1; k >= 0; k--) {
            indexes[k] = cur + 1; // индексы по условию с единицы
            cur = prev[cur];
        }
        // выводим индексы второй строкой (длину выведет main)
        for (int k = 0; k < result; k++) {
            System.out.print(indexes[k] + " ");
        }
        System.out.println();


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}