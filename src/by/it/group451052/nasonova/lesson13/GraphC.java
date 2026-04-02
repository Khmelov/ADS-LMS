package by.it.group451052.nasonova.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] edges = input.split(",\\s*");

        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];

            vertices.add(from);
            vertices.add(to);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            reverseGraph.putIfAbsent(from, new ArrayList<>());
            reverseGraph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);
            reverseGraph.get(to).add(from);
        }

        for (String v : vertices) {
            Collections.sort(graph.get(v));
            Collections.sort(reverseGraph.get(v));
        }

        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();

        List<String> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);

        for (String v : sortedVertices) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, order);
            }
        }

        visited.clear();
        Collections.reverse(order);

        for (String v : order) {
            if (!visited.contains(v)) {
                List<String> component = new ArrayList<>();
                dfs2(v, reverseGraph, visited, component);
                Collections.sort(component);

                StringBuilder sb = new StringBuilder();
                for (String s : component) {
                    sb.append(s);
                }
                System.out.println(sb);
            }
        }
    }

    private static void dfs1(String v, Map<String, List<String>> graph, Set<String> visited, List<String> order) {
        visited.add(v);

        for (String to : graph.get(v)) {
            if (!visited.contains(to)) {
                dfs1(to, graph, visited, order);
            }
        }

        order.add(v);
    }

    private static void dfs2(String v, Map<String, List<String>> reverseGraph, Set<String> visited, List<String> component) {
        visited.add(v);
        component.add(v);

        for (String to : reverseGraph.get(v)) {
            if (!visited.contains(to)) {
                dfs2(to, reverseGraph, visited, component);
            }
        }
    }
}