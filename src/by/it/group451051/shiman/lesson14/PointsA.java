package by.it.group451051.shiman.lesson14;

import java.util.*;

public class PointsA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double D = sc.nextDouble();
        int N = sc.nextInt();
        int[][] points = new int[N][3];
        for (int i = 0; i < N; i++) {
            points[i][0] = sc.nextInt();
            points[i][1] = sc.nextInt();
            points[i][2] = sc.nextInt();
        }
        sc.close();

        long D2 = (long) (D * D);
        DSU dsu = new DSU(N);
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                long dx = points[i][0] - points[j][0];
                long dy = points[i][1] - points[j][1];
                long dz = points[i][2] - points[j][2];
                long dist2 = dx * dx + dy * dy + dz * dz;
                // строго меньше D (не включая D)
                if (dist2 < D2) {
                    dsu.union(i, j);
                }
            }
        }

        int[] clusterSizes = new int[N];
        for (int i = 0; i < N; i++) {
            clusterSizes[dsu.find(i)]++;
        }

        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (clusterSizes[i] > 0) {
                sizes.add(clusterSizes[i]);
            }
        }
        // сортировка по убыванию
        sizes.sort(Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }

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