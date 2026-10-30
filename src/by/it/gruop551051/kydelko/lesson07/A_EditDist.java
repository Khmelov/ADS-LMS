package by.it.gruop551051.kydelko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class A_EditDist {

    int[][] memo;
    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // создаем таблицу и заполняем ее -1 (значит "еще не считали")
        memo = new int[one.length() + 1][two.length() + 1];
        for (int i = 0; i < memo.length; i++) {
            Arrays.fill(memo[i], -1);
        }

        int result = dist(one, two, one.length(), two.length());
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    int dist(String one, String two, int i, int j) {
        // если одна строка пустая, то надо столько операций, какая длина у другой
        if (i == 0) {
            return j;
        }
        if (j == 0) {
            return i;
        }

        // если уже считали, то просто берем из таблицы
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        int res;
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            // последние буквы одинаковые, ничего делать не надо
            res = dist(one, two, i - 1, j - 1);
        } else {
            int del = dist(one, two, i - 1, j) + 1;      // удаление
            int ins = dist(one, two, i, j - 1) + 1;      // вставка
            int rep = dist(one, two, i - 1, j - 1) + 1;  // замена
            res = Math.min(del, Math.min(ins, rep));
        }

        memo[i][j] = res;
        return res;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
