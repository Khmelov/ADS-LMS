package by.it.holupova.lesson07;

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
        int a = one.length();
        int b = two.length();

        //создание и заполнение таблицы
        int [][] tabl = new int [a + 1][b + 1];
        for( int i = 0; i <= a; i++) tabl[i][0] = i;
        for( int j = 0; j <= b; j++) tabl[0][j] = j;

        for (int i = 1; i <= a; i++) {
            for (int j = 1; j <= b; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    tabl[i][j] = tabl[i - 1][j - 1];
                } else {
                    tabl[i][j] = Math.min(Math.min(tabl[i - 1][j - 1], tabl[i - 1][j]), tabl[i][j - 1]) + 1;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        int i = a;
        int j = b;

        while (i > 0 || j > 0) {
            //если одна закончилась, доделываем оставшиеся операции
            if (i == 0) {
                //вставка оставшихся символов из второй
                sb.append("+").append(two.charAt(j - 1)).append(",");
                j--;
            } else if (j == 0) {
                //удаление оставшихся символов из первой
                sb.append("-").append(one.charAt(i - 1)).append(",");
                i--;
            }
            //если символы совпадают, то делаю копию
            else if (one.charAt(i - 1) == two.charAt(j - 1)) {
                sb.append(",#");
                i--;
                j--;
            }
            //иначе возвращаемся
            else {
                //проверка замены
                if (tabl[i][j] == tabl[i - 1][j - 1] + 1) {
                    sb.append("~").append(two.charAt(j - 1)).append(",");
                    i--;
                    j--;
                }
                //проверка вставки
                else if (tabl[i][j] == tabl[i][j - 1] + 1) {
                    sb.append("+").append(two.charAt(j - 1)).append(",");
                    j--;
                }
                //проверка удаления
                else {
                    sb.append("-").append(one.charAt(i - 1)).append(",");
                    i--;
                }
            }

        }

        //разворот строки (т.к. собирала с конца)
        String result = sb.reverse().toString();

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