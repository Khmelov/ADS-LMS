package by.it.group451052.nasonova.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(",\\s*");

        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);

            graph.get(from).add(to);
            inDegree.put(to, inDegree.get(to) + 1);
        }

        PriorityQueue<String> queue = new PriorityQueue<>();

        for (String vertex : inDegree.keySet()) {
            if (inDegree.get(vertex) == 0) {
                queue.offer(vertex);
            }
        }

        StringBuilder result = new StringBuilder();

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(current);

            for (String neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        System.out.println(result);
    }
}