package by.it.group451051.kozakov.lesson14;

import java.util.*;

public class PointsA {
    static class DSU {
        int[] parent;
        int[] rank;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int xRoot = find(x);
            int yRoot = find(y);
            if (xRoot == yRoot) {
                return;
            }
            if (rank[xRoot] < rank[yRoot]) {
                parent[xRoot] = yRoot;
                size[yRoot] += size[xRoot];
            } else {
                parent[yRoot] = xRoot;
                size[xRoot] += size[yRoot];
                if (rank[xRoot] == rank[yRoot]) {
                    rank[xRoot]++;
                }
            }
        }
    }

    static double distance(double[] p1, double[] p2) {
        return Math.sqrt(
                Math.pow(p1[0] - p2[0], 2) +
                        Math.pow(p1[1] - p2[1], 2) +
                        Math.pow(p1[2] - p2[2], 2)
        );
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double maxDist = scanner.nextDouble();
        int N = scanner.nextInt();
        double[][] points = new double[N][3];

        for (int i = 0; i < N; i++) {
            points[i][0] = scanner.nextDouble();
            points[i][1] = scanner.nextDouble();
            points[i][2] = scanner.nextDouble();
        }

        DSU dsu = new DSU(N);
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double dist = distance(points[i], points[j]);
                if (dist < maxDist) {
                    dsu.union(i, j);
                }
            }
        }

        Map<Integer, Integer> rootToSize = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            rootToSize.put(root, dsu.size[root]);
        }

        List<Integer> clusterSizes = new ArrayList<>(rootToSize.values());

        Collections.sort(clusterSizes, Collections.reverseOrder());

        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(clusterSizes.get(i));
        }
        System.out.println();
    }
}