package by.it.group451051.pekarskij.lesson14;

import java.util.*;

public class SitesB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // храним все пары сайтов для обработки после чтения
        List<String[]> pairs = new ArrayList<>();
        Set<String> siteSet = new HashSet<>();
        
        // читаем пары сайтов до строки "end"
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            
            String[] parts = line.split("\\+");
            String a = parts[0].trim();
            String b = parts[1].trim();
            
            pairs.add(new String[]{a, b});
            siteSet.add(a);
            siteSet.add(b);
        }
        
        // маппим имена сайтов на числовые id
        Map<String, Integer> idMap = new HashMap<>();
        int idx = 0;
        for (String site : siteSet) {
            idMap.put(site, idx++);
        }
        
        // создаем DSU нужного размера
        DSU dsu = new DSU(siteSet.size());
        
        // объединяем связанные сайты
        for (String[] pair : pairs) {
            int idA = idMap.get(pair[0]);
            int idB = idMap.get(pair[1]);
            dsu.union(idA, idB);
        }
        
        // считаем размер каждого кластера
        Map<Integer, Integer> counts = new HashMap<>();
        for (int i = 0; i < siteSet.size(); i++) {
            int root = dsu.find(i);
            counts.put(root, counts.getOrDefault(root, 0) + 1);
        }
        
        // собираем размеры и сортируем по убыванию
        List<Integer> sizes = new ArrayList<>(counts.values());
        Collections.sort(sizes, Collections.reverseOrder());
        
        // выводим через пробел
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            sb.append(sizes.get(i));
            if (i < sizes.size() - 1) sb.append(" ");
        }
        System.out.println(sb);
    }

    // DSU с двумя эвристиками: объединение по размеру + сжатие пути
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            // инициализируем: каждый сайт - отдельное множество
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // поиск корня со сжатием пути
        int find(int i) {
            if (parent[i] != i) parent[i] = find(parent[i]);
            return parent[i];
        }

        // объединение по размеру
        void union(int i, int j) {
            int ri = find(i);
            int rj = find(j);
            if (ri != rj) {
                // прикрепляем меньшее дерево к большему
                if (size[ri] < size[rj]) {
                    int tmp = ri; ri = rj; rj = tmp;
                }
                parent[rj] = ri;
                size[ri] += size[rj];
            }
        }
    }
}