package by.it.group451051.naumchik.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: рюкзак с повторами

Первая строка входа содержит целые числа
    1<=W<=100000     вместимость рюкзака
    1<=n<=300        сколько есть вариантов золотых слитков
                     (каждый можно использовать множество раз).
Следующая строка содержит n целых чисел, задающих веса слитков:
  0<=w[1]<=100000 ,..., 0<=w[n]<=100000

Найдите методами динамического программирования
максимальный вес золота, который можно унести в рюкзаке.


Sample Input:
10 3
1 4 8
Sample Output:
10

Sample Input 2:

15 3
2 8 16
Sample Output 2:
14

*/

public class A_Knapsack {

    int getMaxWeight(InputStream stream ) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int w=scanner.nextInt();
        int n=scanner.nextInt();
        int gold[]=new int[n];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }
        scanner.close();

        // dp[i] == true => можно набрать вес i, используя любые слитки (каждый много раз)
        boolean[] dp = new boolean[w + 1];
        dp[0] = true;   // вес 0 всегда достижим (ничего не кладём)

        // Основной цикл: перебираем все достижимые веса и пытаемся добавить каждый слиток
        // Идём от меньшего веса к большему, потому что предметы можно использовать повторно
        for (int i = 0; i <= w; i++) {
            if (dp[i]) {                      // если текущий вес i достижим
                for (int g : gold) {          // пробуем добавить слиток веса g
                    if (i + g <= w) {         // не превышаем вместимость
                        dp[i + g] = true;    // помечаем новый вес как достижимый
                    }
                }
            }
        }

        // Ищем максимальный достижимый вес (идём от w вниз)
        int result = 0;
        for (int i = w; i >= 0; i--) {
            if (dp[i]) {
                result = i;
                break;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
