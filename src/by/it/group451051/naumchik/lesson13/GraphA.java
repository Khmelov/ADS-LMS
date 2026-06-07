package by.it.group451051.naumchik.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) return;

        // Разделяем строку на отдельные ребра
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

            // Если такое направленное ребро еще не добавляли
            if (graph.get(u).add(v)) {
                inDegree.put(v, inDegree.get(v) + 1);
            }
        }

        // PriorityQueue автоматически сортирует строки лексикографически
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();

        // Алгоритм Кана
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            if (graph.containsKey(current)) {
                for (String neighbor : graph.get(current)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Выводим результат, разделяя пробелами
        System.out.println(String.join(" ", result));
    }
}