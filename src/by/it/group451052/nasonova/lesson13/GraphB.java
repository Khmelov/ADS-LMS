package by.it.group451052.nasonova.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> visited = new HashMap<>();

        String[] edges = input.split(",\\s*");

        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);

            visited.putIfAbsent(from, 0);
            visited.putIfAbsent(to, 0);
        }

        boolean hasCycle = false;

        for (String vertex : graph.keySet()) {
            if (visited.get(vertex) == 0) {
                if (dfs(vertex, graph, visited)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    static boolean dfs(String v, Map<String, List<String>> graph, Map<String, Integer> visited) {
        visited.put(v, 1);

        for (String neighbor : graph.get(v)) {
            if (visited.get(neighbor) == 0) {
                if (dfs(neighbor, graph, visited)) {
                    return true;
                }
            } else if (visited.get(neighbor) == 1) {
                return true;
            }
        }

        visited.put(v, 2);
        return false;
    }
}