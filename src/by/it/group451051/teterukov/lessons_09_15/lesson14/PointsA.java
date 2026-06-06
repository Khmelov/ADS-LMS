package by.it.group451051.teterukov.lessons_09_15.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class PointsA {

    static int[] parent, rank;

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static void union(int a, int b) {
        a = find(a); b = find(b);
        if (a == b) return;
        if (rank[a] < rank[b]) { int t = a; a = b; b = t; }
        parent[b] = a;
        if (rank[a] == rank[b]) rank[a]++;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        double d = Double.parseDouble(st.nextToken());
        int    n = Integer.parseInt(st.nextToken());

        double[] x = new double[n];
        double[] y = new double[n];
        double[] z = new double[n];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            x[i] = Double.parseDouble(st.nextToken());
            y[i] = Double.parseDouble(st.nextToken());
            z[i] = Double.parseDouble(st.nextToken());
        }

        parent = new int[n];
        rank   = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        double d2 = d * d;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dx = x[i] - x[j];
                double dy = y[i] - y[j];
                double dz = z[i] - z[j];
                if (dx*dx + dy*dy + dz*dz < d2) union(i, j);
            }
        }

        Map<Integer, Integer> sizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = find(i);
            sizes.put(root, sizes.getOrDefault(root, 0) + 1);
        }

        List<Integer> result = new ArrayList<>(sizes.values());
        Collections.sort(result, Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (int s : result) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(s);
        }
        System.out.println(sb);
    }
}