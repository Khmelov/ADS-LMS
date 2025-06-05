package by.it.group351051.nekhviadovich.lesson13;

import java.util.*;
//lesson 13-1
public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] nodes = edge.split(" -> ");
            String u = nodes[0];
            String v = nodes[1];

            graph.putIfAbsent(u, new ArrayList<>());
            graph.get(u).add(v);

            inDegree.putIfAbsent(u, 0);
            inDegree.put(v, inDegree.getOrDefault(v, 0) + 1);
        }

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String u = queue.poll();
            result.add(u);

            if (graph.containsKey(u)) {
                for (String v : graph.get(u)) {
                    inDegree.put(v, inDegree.get(v) - 1);
                    if (inDegree.get(v) == 0) {
                        queue.add(v);
                    }
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}