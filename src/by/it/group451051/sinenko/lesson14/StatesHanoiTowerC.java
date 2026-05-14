package by.it.group451051.sinenko.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static int[] count;  // количество состояний с высотой i
    static int aHeight, bHeight, cHeight; // текущая высота башен A, B, C

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // читаем количество дисков
        int n = scanner.nextInt(); // количество дисков

        count = new int[n + 1]; // массив для подсчёта состояний по высоте

        // начальные условия
        aHeight = n;
        bHeight = 0;
        cHeight = 0;

        solve(n, 'A', 'B', 'C');

        // собираем результат 
        int[] result = new int[n + 1];
        int size = 0;
        
        // собираем только те высоты, которые были достигнуты
        for (int i = 1; i <= n; i++) {
            if (count[i] > 0) {
                result[size++] = count[i];
            }
        }

        // сортировка
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (result[i] > result[j]) {
                    int tmp = result[i];
                    result[i] = result[j];
                    result[j] = tmp;
                }
            }
        }

        // вывод
        for (int i = 0; i < size; i++) {
            System.out.print(result[i]);
            if (i < size - 1) System.out.print(" ");
        }
    }

    // рекурсивная функция для решения задачи и подсчёта состояний
    static void solve(int n, char from, char to, char aux) {
        if (n == 0) return;

        solve(n - 1, from, aux, to);

        move(from, to);

        int h = Math.max(aHeight, Math.max(bHeight, cHeight));
        count[h]++;

        solve(n - 1, aux, to, from);
    }

    // функция для перемещения диска и обновления высот башен
    static void move(char from, char to) {
        if (from == 'A') aHeight--;
        else if (from == 'B') bHeight--;
        else cHeight--;

        if (to == 'A') aHeight++;
        else if (to == 'B') bHeight++;
        else cHeight++;
    }
}