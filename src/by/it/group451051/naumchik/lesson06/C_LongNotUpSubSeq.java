package by.it.group451051.naumchik.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

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
        scanner.close();

        //тут реализуйте логику задачи методами динамического программирования (!!!)
        //        int result = 0;

        // Алгоритм O(n log n)
        ArrayList<Integer> d = new ArrayList<>();
        int[] pos = new int[n];
        int[] prev = new int[n];



        for (int i = 0; i < n; i++) {
            int x = m[i];
            // Бинарный поиск первого индекса pos, где d.get(pos) < x
            // Используем инвариант: d[0..lo] >= x, d[hi..] < x (lo = -1, hi = d.size())
            int lo = -1, hi = d.size();
            while (hi - lo > 1) {
                int mid = (lo + hi) / 2;
                if (d.get(mid) >= x) {
                    lo = mid;   // текущий d[mid] >= x, сдвигаем левую границу
                } else {
                    hi = mid;   // d[mid] < x, сдвигаем правую границу
                }
            }
            int idx = hi;
            if (idx == d.size()) {
                d.add(x);
                pos[idx] = i;
            } else {
                d.set(idx, x);
                pos[idx] = i;
            }
            prev[i] = (idx > 0) ? pos[idx - 1] : -1;
        }

        // длина наибольшей невозрастающей подпоследовательности
        int len = d.size();

        // Восстановление индексов (1-индексация) из массива prev
        // Идём от последнего элемента наибольшей подпоследовательности (pos[len-1])
        // и двигаемся по ссылкам prev, пока не дойдём до -1.
        ArrayList<Integer> indices = new ArrayList<>();
        int cur = pos[len - 1];
        while (cur != -1) {
            indices.add(cur + 1);
            cur = prev[cur];
        }

        // обращаем порядок, чтобы индексы шли по возрастанию
        Collections.reverse(indices);

        // Вывод
        System.out.println(len);
        for (int idx : indices) {
            System.out.print(idx + " ");
        }
        System.out.println();

        return len;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }

}