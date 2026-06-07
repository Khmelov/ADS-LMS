package by.it.group451051.naumchik.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) return;

        // Парсинг ребер графа
        String[] edges = line.split(", ");
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Set<String>> reverseGraph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        for (String edge : edges) {
            String[] parts = edge.split("->");
            String u = parts[0];
            String v = parts[1];

            graph.putIfAbsent(u, new HashSet<>());
            graph.putIfAbsent(v, new HashSet<>());
            reverseGraph.putIfAbsent(u, new HashSet<>());
            reverseGraph.putIfAbsent(v, new HashSet<>());

            graph.get(u).add(v);
            reverseGraph.get(v).add(u); // Инвертированный граф

            allVertices.add(u);
            allVertices.add(v);
        }

        // Шаг 1 Алгоритма Косарайю: DFS для определения порядка выхода (таймингов)
        List<String> sortedVertices = new ArrayList<>(allVertices);
        Collections.sort(sortedVertices); // Сортируем для детерминированного обхода

        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        for (String vertex : sortedVertices) {
            if (!visited.contains(vertex)) {
                dfs1(vertex, graph, visited, stack);
            }
        }

        // Шаг 2 Алгоритма Косарайю: DFS по инвертированному графу в порядке из стека
        visited.clear();
        List<String> outputComponents = new ArrayList<>();

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                // Использование TreeSet автоматически сортирует вершины компонента лексикографически
                Set<String> component = new TreeSet<>();
                dfs2(vertex, reverseGraph, visited, component);

                // Объединяем вершины компонента в одну строку без пробелов
                StringBuilder sb = new StringBuilder();
                for (String v : component) {
                    sb.append(v);
                }
                outputComponents.add(sb.toString());
            }
        }

        // Шаг 3: Вывод компонент. В алгоритме Косарайю при извлечении из стека
        // компоненты естественным образом обрабатываются от истока к стоку.
        for (String compStr : outputComponents) {
            System.out.println(compStr);
        }
    }

    private static void dfs1(String u, Map<String, Set<String>> graph, Set<String> visited, Stack<String> stack) {
        visited.add(u);
        // Сортируем соседей для детерминированности, если это необходимо
        List<String> neighbors = new ArrayList<>(graph.get(u));
        Collections.sort(neighbors);
        for (String v : neighbors) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, stack);
            }
        }
        stack.push(u);
    }

    private static void dfs2(String u, Map<String, Set<String>> reverseGraph, Set<String> visited, Set<String> component) {
        visited.add(u);
        component.add(u);
        for (String v : reverseGraph.get(u)) {
            if (!visited.contains(v)) {
                dfs2(v, reverseGraph, visited, component);
            }
        }
    }
}
