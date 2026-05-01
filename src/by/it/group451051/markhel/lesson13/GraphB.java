package by.it.group451051.markhel.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        scanner.close();

        // Множество всех вершин
        Set<String> vertices = new HashSet<>();
        // Список смежности
        Map<String, Set<String>> adj = new HashMap<>();
        // Степень входа
        Map<String, Integer> inDegree = new HashMap<>();

        // Парсинг рёбер
        if (!input.isEmpty()) {
            String[] edges = input.split(",");
            for (String edge : edges) {
                edge = edge.trim();
                if (edge.isEmpty()) continue;
                String[] parts = edge.split("->");
                if (parts.length != 2) continue;
                String from = parts[0].trim();
                String to = parts[1].trim();

                vertices.add(from);
                vertices.add(to);

                adj.putIfAbsent(from, new HashSet<>());
                adj.putIfAbsent(to, new HashSet<>());
                inDegree.putIfAbsent(from, 0);
                inDegree.putIfAbsent(to, 0);

                // Добавляем ребро, если его ещё нет
                if (adj.get(from).add(to)) {
                    inDegree.put(to, inDegree.get(to) + 1);
                }
            }
        }

        // Очередь для вершин с нулевой степенью входа
        Queue<String> queue = new LinkedList<>();
        for (String v : vertices) {
            if (inDegree.getOrDefault(v, 0) == 0) {
                queue.offer(v);
            }
        }

        int processed = 0;
        while (!queue.isEmpty()) {
            String current = queue.poll();
            processed++;

            for (String neighbor : adj.getOrDefault(current, Collections.emptySet())) {
                int newDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newDegree);
                if (newDegree == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Если обработаны все вершины -> нет цикла, иначе есть
        if (processed == vertices.size()) {
            System.out.println("no");
        } else {
            System.out.println("yes");
        }
    }
}
