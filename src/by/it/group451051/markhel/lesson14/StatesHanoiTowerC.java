package by.it.group451051.markhel.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();

        int totalSteps = (1 << N) - 1;               // 2^N - 1
        int[] heights = new int[totalSteps];        // максимальная высота после каждого хода
        int[] counts = {N, 0, 0};                   // количество колец на A, B, C
        int[] step = {0};                           // текущий индекс в heights

        move(N, 0, 1, 2, counts, heights, step);    // генерируем все ходы

        // DSU
        int[] parent = new int[totalSteps];
        int[] size = new int[totalSteps];
        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        int[] firstIdx = new int[N + 1];            // для каждой высоты запоминаем первый индекс
        for (int i = 0; i <= N; i++) firstIdx[i] = -1;

        for (int i = 0; i < totalSteps; i++) {
            int h = heights[i];
            if (firstIdx[h] == -1) {
                firstIdx[h] = i;
            } else {
                union(parent, size, i, firstIdx[h]);
            }
        }

        // собираем размеры корневых множеств
        int[] rootSizes = new int[N + 1];
        int rootCount = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (parent[i] == i) {
                rootSizes[rootCount++] = size[i];
            }
        }

        // сортируем по возрастанию
        for (int i = 0; i < rootCount - 1; i++) {
            for (int j = 0; j < rootCount - 1 - i; j++) {
                if (rootSizes[j] > rootSizes[j + 1]) {
                    int tmp = rootSizes[j];
                    rootSizes[j] = rootSizes[j + 1];
                    rootSizes[j + 1] = tmp;
                }
            }
        }

        // вывод
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rootCount; i++) {
            if (i > 0) sb.append(' ');
            sb.append(rootSizes[i]);
        }
        System.out.println(sb);
    }

    // рекурсивное решение Ханойских башен
    private static void move(int n, int from, int to, int aux,
                             int[] counts, int[] heights, int[] step) {
        if (n == 1) {
            counts[from]--;
            counts[to]++;
            heights[step[0]++] = max(counts[0], counts[1], counts[2]);
        } else {
            move(n - 1, from, aux, to, counts, heights, step);
            move(1, from, to, aux, counts, heights, step);
            move(n - 1, aux, to, from, counts, heights, step);
        }
    }

    private static int max(int a, int b, int c) {
        return Math.max(a, Math.max(b, c));
    }

    private static int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }

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
}