package by.it.group451051.shiman.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inputLine = scanner.nextLine().trim();
        scanner.close();

        Set<String> verticesSet = new HashSet<>();
        List<String[]> edges = new ArrayList<>();

        // Парсинг строки вида "A -> B, C -> D, ..."
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

        // Лексикографический порядок (естественный для строк)
        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : vertices) {
            if (inDegree.get(v) == 0) {
                pq.offer(v);
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String u = pq.poll();
            result.add(u);
            for (String v : adj.get(u)) {
                int newDegree = inDegree.get(v) - 1;
                inDegree.put(v, newDegree);
                if (newDegree == 0) {
                    pq.offer(v);
                }
            }
        }

        if (result.size() != vertices.size()) {
            System.out.println("Ошибка: граф содержит цикл");
        } else {
            for (int i = 0; i < result.size(); i++) {
                if (i > 0) System.out.print(" ");
                System.out.print(result.get(i));
            }
            System.out.println();
        }
    }
}