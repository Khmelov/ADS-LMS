package by.it.holupova.lesson06;

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

        //массив для хранения длин и индексов
        int[] leng = new int[n];
        int[] ind = new int[n];

        //цепочка длины 1
        for (int i = 0; i < n; i++) {
            leng[i] = 1;
            ind[i] = -1; // -1, элемент первый
        }

        int result = 0; //длина
        int indx = 0; //индекс
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) { //перебор всех предыдущих элементов
                if (m[j] >= m[i]) {
                    if (leng[j] + 1 > leng[i]) {
                        leng[i] = leng[j] + 1;
                        ind[i] = j; //запоминание пути
                    }
                }
            }
            //обновление результата
            if (leng[i] > result) {
                result = leng[i];
                indx = i;
            }
        }
          //восстановление пути
        //сбор индексов
        int[] p = new int[result];
        int c = indx;
        for (int i = result - 1; i >= 0; i--) {
            p[i] = c + 1;
            c = ind[c];
        }
        //вывод ответов
        System.out.print(result);
        for (int i = 0; i < result; i++) {
            System.out.print(p[i] + " ");
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}