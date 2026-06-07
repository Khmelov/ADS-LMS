package by.it.group451051.naumchik.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) return;

        // Парсинг ребер графа
        String[] edges = line.split(", ");
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String u = parts[0];
            String v = parts[1];

            graph.putIfAbsent(u, new HashSet<>());
            graph.putIfAbsent(v, new HashSet<>());
            inDegree.putIfAbsent(u, 0);
            inDegree.putIfAbsent(v, 0);

            // Исключаем дубликаты ребер для корректного подсчета степеней
            if (graph.get(u).add(v)) {
                inDegree.put(v, inDegree.get(v) + 1);
            }
        }

        // Очередь для вершин с нулевой степенью захода
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        int visitedCount = 0;

        // Обход графа
        while (!queue.isEmpty()) {
            String current = queue.poll();
            visitedCount++;

            if (graph.containsKey(current)) {
                for (String neighbor : graph.get(current)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Если посетили меньше вершин, чем всего есть в графе — есть цикл
        if (visitedCount < inDegree.size()) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }
}