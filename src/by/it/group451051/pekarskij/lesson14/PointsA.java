package by.it.group451051.pekarskij.lesson14;

import java.util.*;

public class PointsA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // читаем допустимое расстояние и количество точек
        double d = sc.nextDouble();
        int n = sc.nextInt();
        
        // храним координаты всех точек
        double[][] points = new double[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = sc.nextDouble();
            points[i][1] = sc.nextDouble();
            points[i][2] = sc.nextDouble();
        }

        // создаем структуру DSU для объединения точек
        DSU dsu = new DSU(n);
        double dSq = d * d;

        // проверяем все пары точек
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // вычисляем квадрат расстояния между точками
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];
                double distSq = dx * dx + dy * dy + dz * dz;
                // объединяем, если расстояние меньше допустимого
                if (distSq < dSq) {
                    dsu.union(i, j);
                }
            }
        }

        // считаем размер каждого кластера
        Map<Integer, Integer> counts = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            counts.put(root, counts.getOrDefault(root, 0) + 1);
        }

        // собираем размеры и сортируем по убыванию
        List<Integer> sizes = new ArrayList<>(counts.values());
        Collections.sort(sizes, Collections.reverseOrder());

        // выводим размеры через пробел
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            sb.append(sizes.get(i));
            if (i < sizes.size() - 1) sb.append(" ");
        }
        System.out.println(sb);
    }

    // структура данных DSU с объединением по размеру
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            // инициализируем: каждая точка - отдельное множество
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // поиск корня сжатием пути
        int find(int i) {
            if (parent[i] != i) parent[i] = find(parent[i]);
            return parent[i];
        }

        // объединение двух множеств
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