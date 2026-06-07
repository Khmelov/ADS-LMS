package by.it.group451051.shiman.lesson14;

import java.util.*;

public class SitesB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, Integer> map = new HashMap<>();
        List<int[]> pairs = new ArrayList<>();

        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            String[] parts = line.split("\\+");
            if (parts.length != 2) continue;
            String a = parts[0];
            String b = parts[1];
            if (!map.containsKey(a)) map.put(a, map.size());
            if (!map.containsKey(b)) map.put(b, map.size());
            pairs.add(new int[]{map.get(a), map.get(b)});
        }
        sc.close();

        int n = map.size();
        DSU dsu = new DSU(n);
        for (int[] p : pairs) {
            dsu.union(p[0], p[1]);
        }

        int[] size = new int[n];
        for (int i = 0; i < n; i++) {
            size[dsu.find(i)]++;
        }

        List<Integer> clusters = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (size[i] > 0) clusters.add(size[i]);
        }
        // сортировка по убыванию (как в примере и тестах)
        clusters.sort(Collections.reverseOrder());

        for (int i = 0; i < clusters.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusters.get(i));
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
                parent[x] = find(parent[x]); // path compression
            }
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            // union by size
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