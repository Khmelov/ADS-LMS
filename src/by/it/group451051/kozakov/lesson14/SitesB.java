package by.it.group451051.kozakov.lesson14;

import java.util.*;

public class SitesB {
    static class DSU {
        private Map<String, String> parent;
        private Map<String, Integer> rank;
        private Map<String, Integer> size;

        DSU() {
            parent = new HashMap<>();
            rank = new HashMap<>();
            size = new HashMap<>();
        }

        String find(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
                size.put(x, 1);
                return x;
            }
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        void union(String x, String y) {
            String xRoot = find(x);
            String yRoot = find(y);
            if (xRoot.equals(yRoot)) {
                return;
            }
            if (rank.get(xRoot) < rank.get(yRoot)) {
                parent.put(xRoot, yRoot);
                size.put(yRoot, size.get(yRoot) + size.get(xRoot));
            } else {
                parent.put(yRoot, xRoot);
                size.put(xRoot, size.get(xRoot) + size.get(yRoot));
                if (rank.get(xRoot).equals(rank.get(yRoot))) {
                    rank.put(xRoot, rank.get(xRoot) + 1);
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();

        while (true) {
            String line = scanner.nextLine();
            if (line.equals("end")) {
                break;
            }
            String[] sites = line.split("\\+");
            String site1 = sites[0];
            String site2 = sites[1];
            dsu.union(site1, site2);
        }

        // Собираем корни и их размеры
        Map<String, Integer> rootToSize = new HashMap<>();
        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);
            rootToSize.put(root, dsu.size.get(root));
        }

        // Собираем размеры кластеров
        List<Integer> clusterSizes = new ArrayList<>(rootToSize.values());
        Collections.sort(clusterSizes, Collections.reverseOrder());

        // Выводим размеры кластеров в порядке возрастания
        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(clusterSizes.get(i));
        }
        System.out.println();
    }
}