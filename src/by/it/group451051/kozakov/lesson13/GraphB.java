package by.it.group451051.kozakov.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Разбираем входную строку
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> nodes = new HashSet<>();
        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            nodes.add(from);
            nodes.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        // Проверяем наличие циклов
        boolean hasCycle = isCyclic(graph, nodes);

        // Выводим результат
        System.out.println(hasCycle ? "yes" : "no");
    }

    public static boolean isCyclic(Map<String, List<String>> graph, Set<String> nodes) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String node : nodes) {
            if (isCyclicUtil(node, graph, visited, recursionStack)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isCyclicUtil(String node, Map<String, List<String>> graph, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(node)) {
            return true;
        }
        if (visited.contains(node)) {
            return false;
        }

        visited.add(node);
        recursionStack.add(node);

        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (isCyclicUtil(neighbor, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }

        recursionStack.remove(node);
        return false;
    }
}