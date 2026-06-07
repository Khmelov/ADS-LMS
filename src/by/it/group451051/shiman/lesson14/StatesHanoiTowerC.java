package by.it.group451051.shiman.lesson14;

import java.util.Arrays;

public class StatesHanoiTowerC {
    public static void main(String[] args) {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        int N = sc.nextInt();
        sc.close();

        int totalMoves = (1 << N) - 1; // 2^N - 1
        int[] heights = new int[totalMoves];

        // Стеки для стержней A(0), B(1), C(2)
        int[][] disks = new int[3][N];
        int[] top = new int[3];
        // Инициализация стержня A: диски N, N-1, ..., 1 (дно -> вершина)
        for (int i = 0; i < N; i++) {
            disks[0][i] = N - i;
        }
        top[0] = N;
        top[1] = 0;
        top[2] = 0;

        int[] stepCounter = new int[1]; // для передачи по ссылке

        // Рекурсивное решение Ханойских башен
        solve(N, 0, 1, 2, disks, top, heights, stepCounter);

        // DSU для группировки шагов с одинаковой максимальной высотой
        DSU dsu = new DSU(totalMoves);
        int[] firstOccurrence = new int[N + 1]; // высоты от 1 до N
        Arrays.fill(firstOccurrence, -1);

        for (int i = 0; i < totalMoves; i++) {
            int h = heights[i];
            if (firstOccurrence[h] == -1) {
                firstOccurrence[h] = i;
            } else {
                dsu.union(firstOccurrence[h], i);
            }
        }

        // Подсчёт размеров кластеров
        int[] clusterSize = new int[totalMoves];
        for (int i = 0; i < totalMoves; i++) {
            int root = dsu.find(i);
            clusterSize[root]++;
        }

        // Собираем ненулевые размеры
        int[] result = new int[totalMoves];
        int cnt = 0;
        for (int i = 0; i < totalMoves; i++) {
            if (clusterSize[i] > 0) {
                result[cnt++] = clusterSize[i];
            }
        }
        // Обрезаем массив до реального количества
        result = Arrays.copyOf(result, cnt);
        Arrays.sort(result);

        // Вывод
        for (int i = 0; i < result.length; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result[i]);
        }
        System.out.println();
    }

    private static void solve(int n, int from, int to, int via,
                              int[][] disks, int[] top,
                              int[] heights, int[] stepCounter) {
        if (n == 0) return;
        solve(n - 1, from, via, to, disks, top, heights, stepCounter);
        moveDisk(from, to, disks, top, heights, stepCounter);
        solve(n - 1, via, to, from, disks, top, heights, stepCounter);
    }

    private static void moveDisk(int from, int to,
                                 int[][] disks, int[] top,
                                 int[] heights, int[] stepCounter) {
        // Перемещаем верхний диск с from на to
        int disk = disks[from][top[from] - 1];
        top[from]--;
        disks[to][top[to]] = disk;
        top[to]++;

        // Вычисляем максимальную высоту после хода
        int h = Math.max(top[0], Math.max(top[1], top[2]));
        heights[stepCounter[0]++] = h;
    }

    // DSU с эвристиками по размеру и сжатием пути
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            if (size[ra] < size[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
            }
        }
    }
}