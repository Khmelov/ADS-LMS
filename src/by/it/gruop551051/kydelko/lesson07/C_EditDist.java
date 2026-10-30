package by.it.gruop551051.kydelko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить алгоритм преобразования двух данных непустых строк
    Вывести через запятую редакционное предписание в формате:
     операция("+" вставка, "-" удаление, "~" замена, "#" копирование)
     символ замены или вставки

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    #,#,

    Sample Input 2:
    short
    ports
    Sample Output 2:
    -s,~p,#,#,#,+s,

    Sample Input 3:
    distance
    editing
    Sample Output 2:
    +e,#,#,-s,#,~i,#,-c,~g,


    P.S. В литературе обычно действия редакционных предписаний обозначаются так:
    - D (англ. delete) — удалить,
    + I (англ. insert) — вставить,
    ~ R (replace) — заменить,
    # M (match) — совпадение.
*/


public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length();
        int m = two.length();
        int[][] d = new int[n + 1][m + 1];

        // базовые случаи
        for (int i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        // заполняем таблицу расстояний
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = 1;
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    cost = 0;
                }
                int del = d[i - 1][j] + 1;
                int ins = d[i][j - 1] + 1;
                int rep = d[i - 1][j - 1] + cost;
                d[i][j] = Math.min(del, Math.min(ins, rep));
            }
        }

        // идем из правого нижнего угла в левый верхний и собираем предписание
        String result = "";
        int i = n;
        int j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1) && d[i][j] == d[i - 1][j - 1]) {
                // буквы одинаковые - копирование
                result = "#," + result;
                i--;
                j--;
            } else if (i > 0 && j > 0 && d[i][j] == d[i - 1][j - 1] + 1) {
                // замена
                result = "~" + two.charAt(j - 1) + "," + result;
                i--;
                j--;
            } else if (i > 0 && d[i][j] == d[i - 1][j] + 1) {
                // удаление буквы из первой строки
                result = "-" + one.charAt(i - 1) + "," + result;
                i--;
            } else {
                // вставка буквы из второй строки
                result = "+" + two.charAt(j - 1) + "," + result;
                j--;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}