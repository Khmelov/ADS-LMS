package by.it.group451051.pekarskij.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        // списки смежности для прямого и транспонированного графа
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, List<String>> rev = new HashMap<>();
        // все вершины в отсортированном виде для детерминизма
        Set<String> vertices = new TreeSet<>();

        // разбираем ребра из строки
        for (String edge : line.split(",")) {
            String[] parts = edge.trim().split("->");
            String u = parts[0].trim();
            String v = parts[1].trim();

            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            rev.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
            vertices.add(u);
            vertices.add(v);
        }

        // первый обход: запоминаем порядок завершения вершин
        Deque<String> stack = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        for (String v : vertices) {
            if (!visited.contains(v)) dfsFinish(v, adj, visited, stack);
        }

        // второй обход на транспонированном графе: выделяем компоненты связности
        visited.clear();
        while (!stack.isEmpty()) {
            String v = stack.pop();
            if (!visited.contains(v)) {
                List<String> component = new ArrayList<>();
                dfsCollect(v, rev, visited, component);
                Collections.sort(component); // лексикографический порядок внутри
                System.out.println(String.join("", component));
            }
        }
    }

    // рекурсивный обход для заполнения стека по времени выхода
    private static void dfsFinish(String v, Map<String, List<String>> adj, Set<String> visited, Deque<String> stack) {
        visited.add(v);
        for (String neighbor : adj.getOrDefault(v, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsFinish(neighbor, adj, visited, stack);
            }
        }
        stack.push(v);
    }

    // рекурсивный обход для сбора вершин одной компоненты
    private static void dfsCollect(String v, Map<String, List<String>> rev, Set<String> visited, List<String> component) {
        visited.add(v);
        component.add(v);
        for (String neighbor : rev.getOrDefault(v, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsCollect(neighbor, rev, visited, component);
            }
        }
    }
}