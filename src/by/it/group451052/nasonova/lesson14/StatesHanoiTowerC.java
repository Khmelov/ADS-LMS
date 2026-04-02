package by.it.group451052.nasonova.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static int[] parent;
    static int[] sz;
    static int[] first;
    static int[] heights;
    static int step;

    static int find(int v) {
        if (parent[v] != v) {
            parent[v] = find(parent[v]);
        }
        return parent[v];
    }

    static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a != b) {
            if (sz[a] < sz[b]) {
                int t = a;
                a = b;
                b = t;
            }
            parent[b] = a;
            sz[a] += sz[b];
        }
    }

    static void saveState() {
        int max = heights[0];
        if (heights[1] > max) max = heights[1];
        if (heights[2] > max) max = heights[2];

        if (first[max] == -1) {
            first[max] = step;
        } else {
            union(step, first[max]);
        }
        step++;
    }

    static void move(int n, int from, int to, int aux) {
        if (n == 0) return;

        move(n - 1, from, aux, to);

        heights[from]--;
        heights[to]++;
        saveState();

        move(n - 1, aux, to, from);
    }

    static void sort(int[] a, int n) {
        for (int i = 1; i < n; i++) {
            int x = a[i];
            int j = i - 1;
            while (j >= 0 && a[j] > x) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = x;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        int total = (1 << n) - 1;

        parent = new int[total];
        sz = new int[total];
        first = new int[n + 1];
        heights = new int[3];

        for (int i = 0; i < total; i++) {
            parent[i] = i;
            sz[i] = 1;
        }

        for (int i = 0; i <= n; i++) {
            first[i] = -1;
        }

        heights[0] = n;
        heights[1] = 0;
        heights[2] = 0;
        step = 0;

        move(n, 0, 1, 2);

        int[] ans = new int[n];
        int count = 0;

        for (int h = 1; h <= n; h++) {
            if (first[h] != -1) {
                ans[count++] = sz[find(first[h])];
            }
        }

        sort(ans, count);

        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(ans[i]);
        }
    }
}