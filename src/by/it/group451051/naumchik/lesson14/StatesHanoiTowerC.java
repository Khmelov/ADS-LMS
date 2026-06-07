package by.it.group451051.naumchik.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    private static int N;
    private static int M;
    private static int[][] stacks;
    private static int[] tops;
    private static int[] maxHeights;
    private static int step;

    // Перемещение диска с одного стержня на другой
    private static void move(int from, int to) {
        int disk = stacks[from][tops[from]--];
        stacks[to][++tops[to]] = disk;
        int max = Math.max(tops[0] + 1, Math.max(tops[1] + 1, tops[2] + 1));
        maxHeights[step++] = max;
    }

    // Рекурсивный алгоритм Ханойских башен
    private static void hanoi(int n, int from, int to, int aux) {
        if (n == 1) {
            move(from, to);
        } else {
            hanoi(n - 1, from, aux, to);
            move(from, to);
            hanoi(n - 1, aux, to, from);
        }
    }

    // DSU: поиск корня с сжатием пути
    private static int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }

    // DSU: объединение по размеру
    private static void union(int[] parent, int[] size, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);
        if (rootX == rootY) return;
        if (size[rootX] < size[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        M = (1 << N) - 1;                     // количество шагов

        // Инициализация стеков для стержней A(0), B(1), C(2)
        stacks = new int[3][N];
        tops = new int[3];
        tops[0] = -1;
        tops[1] = -1;
        tops[2] = -1;
        for (int i = N; i >= 1; i--) {
            stacks[0][++tops[0]] = i;
        }

        maxHeights = new int[M];
        step = 0;
        hanoi(N, 0, 1, 2);                    // генерируем все шаги

        // DSU инициализация
        int[] parent = new int[M];
        int[] size = new int[M];
        for (int i = 0; i < M; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // Объединяем шаги с одинаковой максимальной высотой
        int[] firstIdx = new int[N + 2];       // максимальная высота от 1 до N
        for (int i = 0; i <= N + 1; i++) firstIdx[i] = -1;

        for (int i = 0; i < M; i++) {
            int h = maxHeights[i];
            if (firstIdx[h] == -1) {
                firstIdx[h] = i;
            } else {
                union(parent, size, i, firstIdx[h]);
            }
        }

        // Подсчёт размеров каждого множества
        int[] rootCount = new int[M];
        for (int i = 0; i < M; i++) {
            int root = find(parent, i);
            rootCount[root]++;
        }

        // Сбор ненулевых размеров
        int[] result = new int[M];
        int resCount = 0;
        for (int i = 0; i < M; i++) {
            if (rootCount[i] > 0) {
                result[resCount++] = rootCount[i];
            }
        }

        // Сортировка вставками (по возрастанию)
        for (int i = 1; i < resCount; i++) {
            int key = result[i];
            int j = i - 1;
            while (j >= 0 && result[j] > key) {
                result[j + 1] = result[j];
                j--;
            }
            result[j + 1] = key;
        }

        // Вывод
        for (int i = 0; i < resCount; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result[i]);
        }
        System.out.println();
        scanner.close();
    }
}
