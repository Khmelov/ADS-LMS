package by.it.group451051.kozakov.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Разбираем входную строку
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reversedGraph = new HashMap<>();
        Set<String> nodes = new HashSet<>();
        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            nodes.add(from);
            nodes.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reversedGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
        }

        // Находим компоненты сильной связности
        List<Set<String>> components = findStronglyConnectedComponents(graph, reversedGraph, nodes);

        // Выводим результат
        for (Set<String> component : components) {
            List<String> sorted = new ArrayList<>(component);
            Collections.sort(sorted);
            for (String node : sorted) {
                System.out.print(node);
            }
            System.out.println();
        }
    }

    public static List<Set<String>> findStronglyConnectedComponents(
            Map<String, List<String>> graph,
            Map<String, List<String>> reversedGraph,
            Set<String> nodes) {
        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();
        List<Set<String>> components = new ArrayList<>();

        // Первый проход: заполняем порядок
        for (String node : nodes) {
            if (!visited.contains(node)) {
                dfs(node, graph, visited, order);
            }
        }

        // Второй проход: обрабатываем транспонированный граф в обратном порядке
        visited.clear();
        Collections.reverse(order);
        for (String node : order) {
            if (!visited.contains(node)) {
                Set<String> component = new HashSet<>();
                dfsReversed(node, reversedGraph, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private static void dfs(String node, Map<String, List<String>> graph, Set<String> visited, List<String> order) {
        visited.add(node);
        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
                    dfs(neighbor, graph, visited, order);
                }
            }
        }
        order.add(node);
    }

    private static void dfsReversed(String node, Map<String, List<String>> reversedGraph, Set<String> visited, Set<String> component) {
        visited.add(node);
        component.add(node);
        if (reversedGraph.containsKey(node)) {
            for (String neighbor : reversedGraph.get(node)) {
                if (!visited.contains(neighbor)) {
                    dfsReversed(neighbor, reversedGraph, visited, component);
                }
            }
        }
    }
}