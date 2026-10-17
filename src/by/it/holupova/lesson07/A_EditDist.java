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


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
       int a = one.length();
       int b = two.length();

       //таблица для мемоизации
        int [][] tabl= new int [a + 1][b + 1];
        //заполняю значением -1 для непрочитанных ячеек
        for(int i = 0; i <= a; i++) {
            for(int j = 0; j <= b; j++) {
                tabl[i][j] = -1;
            }
        }
        //вызов рекурсивной функции
        return countDist(one, two, a, b, tabl);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

    }
    //рекурсивная функция
     private int countDist(String s1, String s2, int i, int j, int [][] tabl) {
        //если результат вычислен
        if (tabl[i][j] != -1) {
            return tabl[i][j];
        }

        //если первая строка пустая, то вставка символов из второй
         if (i == 0) {
             tabl[i][j] = j;
             return j;
         }
         //если вторая строка пустая, то удалить все из первой
         if (j == 0) {
             tabl[i][j] = i;
             return i;
         }
         //если последние совпадают
         if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
             tabl[i][j] = countDist(s1, s2, i - 1, j - 1, tabl);
             return tabl[i][j];
         }
         //если не совпадают, то:
         int change = countDist(s1, s2, i - 1, j - 1, tabl); //замена
         int delete = countDist(s1, s2, i -1 , j, tabl); //удаление
         int insert = countDist(s1, s2, i, j - 1, tabl); //вставка

         tabl[i][j] = Math.min(Math.min(change, delete), insert) + 1;
         return tabl[i][j];
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
