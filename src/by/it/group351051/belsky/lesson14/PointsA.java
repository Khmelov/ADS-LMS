package by.it.group351051.belsky.lesson14;

import java.util.*;

public class PointsA {
    static class DSU {
        int[] parent;
        int[] size;

        // Конструктор для инициализации структуры данных DSU
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // Поиск корня множества для элемента x с применением пути сжатия
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        // Объединение двух множеств, объединяя меньший в больший
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                // Объединяем меньший по размеру корень с большим
                if (size[rootX] < size[rootY]) {
                    int temp = rootX;
                    rootX = rootY;
                    rootY = temp;
                }
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int distance = scanner.nextInt();  // Максимальное расстояние для объединения
        int n = scanner.nextInt();  // Количество точек
        int[][] points = new int[n][3];  // Массив для хранения координат и радиуса каждой точки

        // Считывание координат и радиуса для каждой точки
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        // Создаём структуру данных DSU для объединения точек
        DSU dsu = new DSU(n);

        // Сравниваем расстояние между каждой парой точек
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Вычисление евклидова расстояния между точками
                double dist = Math.hypot(Math.hypot(points[i][0] - points[j][0], points[i][1] - points[j][1]), points[i][2] - points[j][2]);
                // Если расстояние меньше заданного, объединяем точки в одно множество
                if (dist < distance) {
                    dsu.union(i, j);
                }
            }
        }

        // Создание мапы для подсчета размера каждого множества
        Map<Integer, Integer> groups = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);  // Находим корень множества для точки
            groups.put(root, groups.getOrDefault(root, 0) + 1);  // Увеличиваем размер множества
        }

        // Собираем размеры множеств в список и сортируем по убыванию
        List<Integer> result = new ArrayList<>(groups.values());
        result.sort(Collections.reverseOrder());

        // Выводим размеры компонент на экране
        for (int size : result) {
            System.out.print(size + " ");
        }
    }
}
