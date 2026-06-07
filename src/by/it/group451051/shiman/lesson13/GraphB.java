package by.it.group451051.shiman.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inputLine = scanner.nextLine().trim();
        scanner.close();

        Set<String> verticesSet = new HashSet<>();
        List<String[]> edges = new ArrayList<>();

        // Парсинг строки вида "1 -> 2, 1 -> 3, 2 -> 3"
        if (!inputLine.isEmpty()) {
            String[] parts = inputLine.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (trimmed.isEmpty()) continue;
                String[] arrowSplit = trimmed.split("->");
                if (arrowSplit.length != 2) continue;
                String from = arrowSplit[0].trim();
                String to = arrowSplit[1].trim();
                edges.add(new String[]{from, to});
                verticesSet.add(from);
                verticesSet.add(to);
            }
        }

        List<String> vertices = new ArrayList<>(verticesSet);
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        for (String v : vertices) {
            adj.put(v, new ArrayList<>());
            inDegree.put(v, 0);
        }

        for (String[] edge : edges) {
            String u = edge[0];
            String v = edge[1];
            adj.get(u).add(v);
            inDegree.put(v, inDegree.get(v) + 1);
        }

        // Очередь для вершин с нулевой входящей степенью (лексикографический порядок не обязателен, но для единообразия используем PriorityQueue)
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String v : vertices) {
            if (inDegree.get(v) == 0) {
                queue.offer(v);
            }
        }

        int processedCount = 0;
        while (!queue.isEmpty()) {
            String u = queue.poll();
            processedCount++;
            for (String v : adj.get(u)) {
                int newDegree = inDegree.get(v) - 1;
                inDegree.put(v, newDegree);
                if (newDegree == 0) {
                    queue.offer(v);
                }
            }
        }

        // Если обработаны не все вершины, значит есть цикл
        if (processedCount != vertices.size()) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }
}