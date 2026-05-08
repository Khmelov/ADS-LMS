package by.it.group451051.kozakov.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Разбираем входную строку
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> nodes = new HashSet<>();
        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            nodes.add(from);
            nodes.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        // Выполняем топологическую сортировку
        List<String> sortedNodes = topologicalSort(graph, nodes);

        // Выводим результат
        for (int i = 0; i < sortedNodes.size(); i++) {
            System.out.print(sortedNodes.get(i));
            if (i < sortedNodes.size() - 1) {
                System.out.print(" ");
            }
        }
    }

    public static List<String> topologicalSort(Map<String, List<String>> graph, Set<String> nodes) {
        // Вычисляем входящие степени для каждой вершины
        Map<String, Integer> inDegree = new HashMap<>();
        for (String node : nodes) {
            inDegree.put(node, 0);
        }

        for (List<String> neighbors : graph.values()) {
            for (String neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        // Очередь для вершин с нулевой входящей степенью
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        // Результат топологической сортировки
        List<String> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            String node = queue.poll();
            result.add(node);

            if (graph.containsKey(node)) {
                for (String neighbor : graph.get(node)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        return result;
    }
}