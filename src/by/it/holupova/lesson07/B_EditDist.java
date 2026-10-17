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
    Итерационно вычислить расстояние редактирования двух данных непустых строк

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

public class B_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int a = one.length();
        int b = two.length();

        //создаю таблицу, для пустой строки - размер +1
        int [][] tabl= new int [a + 1][b + 1];
        //заполнение первой строки и столбца
        for(int i = 0; i <= a; i++) {
                tabl[i][0] = i; //удаление всех символов
            }
        for(int j = 0; j <= b; j++) {
                tabl[0][j] = j; //вставка всех символов
        }
        //заполнение таблицы построчно
        for(int i = 1; i <= a; i++) {
            for (int j = 1; j <= b; j++) {
                //если символы совпадают, то без изменений
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    tabl[i][j] = tabl[i - 1][j - 1];
                } else { //иначе выбираем минимум из трех операций
                    int change = tabl[i - 1][j - 1];  //замена
                    int delete = tabl[i - 1][j]; //удаление
                    int insert = tabl[i][j - 1];  //вставка
                    tabl[i][j] = Math.min(Math.min(change, delete), insert) + 1;
                }
            }

        }
            return tabl[a][b];

            //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        }



    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}