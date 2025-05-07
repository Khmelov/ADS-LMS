package by.it.group351051.belsky.lesson14;

import java.util.*;

public class StatesHanoiTowerC {
    static class DSU {
        int[] parent;
        int[] size;

        // Инициализация структуры DSU
        DSU(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                parent[i] = i;  // Изначально каждый элемент является своим родителем
                size[i] = 1;  // Размер множества — 1 для каждой вершины
            }
        }

        // Поиск корня множества с применением сжатия пути
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        // Объединение двух множеств
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
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

    // Рекурсивная функция для решения задачи Ханойской башни
    static void hanoi(int n, char from, char to, char aux, DSU dsu, int[] heights, int step, List<Integer> maxHeights) {
        if (n == 1) {
            // Перемещаем один диск
            heights[from - 'A']--;
            heights[to - 'A']++;
            int maxHeight = Math.max(Math.max(heights[0], heights[1]), heights[2]);
            maxHeights.add(maxHeight);
            return;
        }
        // Рекурсивно решаем для подзадач
        hanoi(n - 1, from, aux, to, dsu, heights, step, maxHeights);
        heights[from - 'A']--;
        heights[to - 'A']++;
        int maxHeight = Math.max(Math.max(heights[0], heights[1]), heights[2]);
        maxHeights.add(maxHeight);
        hanoi(n - 1, aux, to, from, dsu, heights, step + 1, maxHeights);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();  // Количество дисков
        int totalSteps = (int) Math.pow(2, N) - 1;  // Количество шагов для N дисков
        DSU dsu = new DSU(totalSteps + 1);
        int[] heights = new int[3];  // Массив для высот башен
        heights[0] = N;  // Все диски на первой башне
        List<Integer> maxHeights = new ArrayList<>();
        hanoi(N, 'A', 'B', 'C', dsu, heights, 1, maxHeights);  // Запуск решения задачи

        // Подсчёт количества шагов для каждой высоты
        Map<Integer, Integer> groupSizes = new HashMap<>();
        for (int i = 0; i < maxHeights.size(); i++) {
            int maxHeight = maxHeights.get(i);
            groupSizes.put(maxHeight, groupSizes.getOrDefault(maxHeight, 0) + 1);
        }

        // Сортировка по величине шагов
        List<Integer> result = new ArrayList<>(groupSizes.values());
        Collections.sort(result);

        // Вывод результата
        for (int size : result) {
            System.out.print(size + " ");
        }
    }
}
