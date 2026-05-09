package by.it.group451051.pekarskij.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        // храним список смежности: вершина -> список соседей
        Map<String, List<String>> adj = new HashMap<>();
        // храним входящие степени вершин
        Map<String, Integer> inDegree = new HashMap<>();

        // разбираем ребра из входной строки
        for (String edge : line.split(",")) {
            String[] parts = edge.trim().split("->");
            String u = parts[0].trim();
            String v = parts[1].trim();

            // добавляем ребро u -> v
            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            // увеличиваем входящую степень v
            inDegree.put(v, inDegree.getOrDefault(v, 0) + 1);
            // убеждаемся, что u тоже есть в карте
            inDegree.putIfAbsent(u, 0);
        }

        // приоритетная очередь для лексикографического порядка
        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String vertex : inDegree.keySet()) {
            if (inDegree.get(vertex) == 0) {
                pq.add(vertex);
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String u = pq.poll();
            result.add(u);

            // уменьшаем степени соседей
            if (adj.containsKey(u)) {
                for (String v : adj.get(u)) {
                    inDegree.put(v, inDegree.get(v) - 1);
                    if (inDegree.get(v) == 0) {
                        pq.add(v);
                    }
                }
            }
        }

        // выводим вершины через пробел
        System.out.println(String.join(" ", result));
    }
}