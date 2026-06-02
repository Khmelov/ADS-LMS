package by.it.group451051.zhelubovskaya.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Парсим граф
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            allNodes.add(from);
            allNodes.add(to);
        }

        // Состояния: 0 = не посещён, 1 = в стеке (в процессе обхода), 2 = обработан
        Map<String, Integer> state = new HashMap<>();
        for (String node : allNodes) {
            state.put(node, 0);
        }

        boolean hasCycle = false;

        // Поиск цикла (DFS)
        for (String node : allNodes) {
            if (state.get(node) == 0) {
                if (dfs(node, graph, state)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(String node, Map<String, List<String>> graph, Map<String, Integer> state) {
        state.put(node, 1); // в стеке

        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (state.get(neighbor) == 1) {
                    return true; // найден цикл
                }
                if (state.get(neighbor) == 0) {
                    if (dfs(neighbor, graph, state)) {
                        return true;
                    }
                }
            }
        }

        state.put(node, 2); // обработан
        return false;
    }
}