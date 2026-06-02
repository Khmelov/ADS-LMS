package by.it.group451051.zhelubovskaya.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Парсим граф
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            inDegree.putIfAbsent(from, 0);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }

        // Добавляем все вершины из графа в inDegree
        for (String node : graph.keySet()) {
            inDegree.putIfAbsent(node, 0);
        }

        // Топологическая сортировка (Kahn's algorithm) с лексикографическим порядком
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String node = queue.poll();
            result.add(node);

            if (graph.containsKey(node)) {
                for (String neighbor : graph.get(node)) {
                    int deg = inDegree.get(neighbor) - 1;
                    inDegree.put(neighbor, deg);
                    if (deg == 0) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Вывод результата
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}