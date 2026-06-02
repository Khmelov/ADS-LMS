package by.it.group451051.zhelubovskaya.lesson14;

import java.util.*;

public class SitesB {

    private static class DSU {
        private Map<String, String> parent = new HashMap<>();
        private Map<String, Integer> size = new HashMap<>();

        void makeSet(String site) {
            if (!parent.containsKey(site)) {
                parent.put(site, site);
                size.put(site, 1);
            }
        }

        String find(String site) {
            if (!parent.containsKey(site)) return null;
            if (!parent.get(site).equals(site)) {
                parent.put(site, find(parent.get(site))); // сжатие пути
            }
            return parent.get(site);
        }

        void union(String a, String b) {
            String rootA = find(a);
            String rootB = find(b);
            if (rootA == null || rootB == null) return;
            if (rootA.equals(rootB)) return;

            // эвристика по размеру
            if (size.get(rootA) < size.get(rootB)) {
                String temp = rootA;
                rootA = rootB;
                rootB = temp;
            }
            parent.put(rootB, rootA);
            size.put(rootA, size.get(rootA) + size.get(rootB));
        }

        int getSize(String site) {
            String root = find(site);
            return root == null ? 0 : size.get(root);
        }

        Collection<Integer> getClusterSizes() {
            Map<String, Integer> clusterSizes = new HashMap<>();
            for (String site : parent.keySet()) {
                String root = find(site);
                clusterSizes.put(root, size.get(root));
            }
            return clusterSizes.values();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();
        List<String[]> pairs = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("end")) break;
            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                String site1 = sites[0].trim();
                String site2 = sites[1].trim();
                pairs.add(new String[]{site1, site2});
                dsu.makeSet(site1);
                dsu.makeSet(site2);
            }
        }
        scanner.close();

        // Объединяем пары
        for (String[] pair : pairs) {
            dsu.union(pair[0], pair[1]);
        }

        // Собираем размеры кластеров
        List<Integer> sizes = new ArrayList<>(dsu.getClusterSizes());
        sizes.sort(Collections.reverseOrder()); // УБЫВАНИЕ

        // Вывод
        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i < sizes.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}