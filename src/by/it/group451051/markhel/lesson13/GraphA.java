package by.it.group451051.markhel.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Читаем всю строку ввода
        String input = scanner.nextLine().trim();
        scanner.close();

        // Множество всех вершин
        Set<String> verticesSet = new HashSet<>();
        // Список смежности: для каждой вершины множество соседей (уникальные рёбра)
        Map<String, Set<String>> adj = new HashMap<>();
        // Степень входа
        Map<String, Integer> inDegree = new HashMap<>();

        // Разбиваем строку на отдельные рёбра
        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;
            // Разделяем на from и to по "->"
            String[] parts = edge.split("->");
            if (parts.length != 2) continue;
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Добавляем вершины
            verticesSet.add(from);
            verticesSet.add(to);

            // Инициализируем списки смежности и степени входа, если ещё не были
            adj.putIfAbsent(from, new HashSet<>());
            adj.putIfAbsent(to, new HashSet<>());
            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);

            // Добавляем ребро, если его ещё нет
            if (adj.get(from).add(to)) {
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        // Очередь с приоритетом для лексикографического порядка
        PriorityQueue<String> queue = new PriorityQueue<>();
        // Добавляем все вершины с нулевой степенью входа
        for (String v : verticesSet) {
            if (inDegree.getOrDefault(v, 0) == 0) {
                queue.offer(v);
            }
        }

        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // Уменьшаем степени входа соседей
            for (String neighbor : adj.getOrDefault(current, Collections.emptySet())) {
                int newDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newDegree);
                if (newDegree == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Вывод результата
        System.out.println(String.join(" ", result));
    }
}