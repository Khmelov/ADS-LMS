package by.it.holupova.lesson04;

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

    long mergeSandC(int [] a, int l, int r) {
        long k = 0;
        if (l < r) {
            int s = (l + r) / 2;

            //инверсия в левой
            k += mergeSandC(a, l, s);
            //инверсия в правой
            k += mergeSandC(a, s + 1, r);
            //инверсии при слиянии
            k += mergeandC(a, l, s, r);
        }
        return k;
    }
    //метод слияния и подсчета инверсий
    long mergeandC(int [] a, int l, int s, int r) {
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
        long inver = 0; //счетчик инверсий
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                a[c] = L[i];
                i++;
            } else {
                a[c] = R[j];
                j++;
                inver += (n1 - 1);
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
        return inver;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        long result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    long calc(InputStream stream) throws FileNotFoundException {
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
        long result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!
        long inver = mergeSandC(a, 0, n-1);
        result = (int) inver;

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
