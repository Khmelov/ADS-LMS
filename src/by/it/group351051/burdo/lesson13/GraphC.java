package by.it.group351051.burdo.lesson13;

import java.util.*;

/**
 * Класс GraphC находит и выводит компоненты сильной связности
 * в ориентированном графе с использованием алгоритма Косарайю.
 */
public class GraphC {

    /**
     * Главный метод, считывающий граф и выводящий компоненты сильной связности.
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine(); // Пример: C->B, C->I, I->A, ...
        scanner.close();

        // Построение графа и его транспонированной версии
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reversedGraph = new HashMap<>();

        // Сбор всех вершин
        Set<String> allNodes = new HashSet<>();

        // Разбор ребер и построение графов
        for (String edge : input.split(",")) {
            String[] parts = edge.trim().split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reversedGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

            // Убедимся, что все вершины присутствуют
            allNodes.add(from);
            allNodes.add(to);
            graph.putIfAbsent(to, new ArrayList<>());
            reversedGraph.putIfAbsent(from, new ArrayList<>());
        }

        // Сортировка списков смежности для лексикографического порядка
        for (List<String> neighbors : graph.values()) {
            Collections.sort(neighbors);
        }

        // Первая стадия: обычный DFS для порядка завершения
        Set<String> visited = new HashSet<>();
        Deque<String> finishStack = new ArrayDeque<>();

        List<String> sortedNodes = new ArrayList<>(allNodes);
        Collections.sort(sortedNodes);

        for (String node : sortedNodes) {
            if (!visited.contains(node)) {
                dfs(node, graph, visited, finishStack);
            }
        }

        // Вторая стадия: DFS по транспонированному графу в порядке finishStack
        visited.clear();

        List<List<String>> components = new ArrayList<>();

        while (!finishStack.isEmpty()) {
            String node = finishStack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfsReverse(node, reversedGraph, visited, component);
                Collections.sort(component);
                components.add(component);
            }
        }

        // Вывод каждой компоненты на новой строке
        for (List<String> component : components) {
            System.out.println(String.join("", component));
        }
    }

    /**
     * Обычный DFS для построения порядка обхода.
     */
    private static void dfs(String node, Map<String, List<String>> graph,
                            Set<String> visited, Deque<String> finishStack) {
        visited.add(node);
        for (String neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, graph, visited, finishStack);
            }
        }
        finishStack.push(node);
    }

    /**
     * DFS по транспонированному графу для нахождения компоненты.
     */
    private static void dfsReverse(String node, Map<String, List<String>> reversedGraph,
                                   Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);
        for (String neighbor : reversedGraph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfsReverse(neighbor, reversedGraph, visited, component);
            }
        }
    }
}
