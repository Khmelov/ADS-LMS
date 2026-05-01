package by.it.group451051.markhel.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        scanner.close();

        // Множество всех вершин
        Set<String> verticesSet = new HashSet<>();
        // Список смежности исходного графа
        Map<String, List<String>> graph = new HashMap<>();
        // Список смежности транспонированного графа
        Map<String, List<String>> reverseGraph = new HashMap<>();

        if (!input.isEmpty()) {
            String[] edges = input.split(",");
            for (String edge : edges) {
                edge = edge.trim();
                if (edge.isEmpty()) continue;
                String[] parts = edge.split("->");
                if (parts.length != 2) continue;
                String from = parts[0].trim();
                String to = parts[1].trim();

                verticesSet.add(from);
                verticesSet.add(to);

                graph.putIfAbsent(from, new ArrayList<>());
                graph.get(from).add(to);

                reverseGraph.putIfAbsent(to, new ArrayList<>());
                reverseGraph.get(to).add(from);
            }
        }

        // Для всех вершин инициализируем пустые списки
        for (String v : verticesSet) {
            graph.putIfAbsent(v, new ArrayList<>());
            reverseGraph.putIfAbsent(v, new ArrayList<>());
        }

        // Список всех вершин для детерминированного обхода (лексикографический порядок)
        List<String> vertices = new ArrayList<>(verticesSet);
        vertices.sort(Comparator.naturalOrder());

        // Шаг 1: DFS на исходном графе для получения порядка завершения
        Set<String> visited = new HashSet<>();
        Stack<String> finishOrder = new Stack<>();
        for (String v : vertices) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, finishOrder);
            }
        }

        // Шаг 2: DFS на транспонированном графе в порядке убывания finishOrder
        visited.clear();
        List<Set<String>> components = new ArrayList<>();
        // Используем список вершин в порядке finishOrder (стек даёт обратный порядок)
        // Достаём из стека в порядке LIFO
        while (!finishOrder.isEmpty()) {
            String v = finishOrder.pop();
            if (!visited.contains(v)) {
                Set<String> component = new HashSet<>();
                dfs2(v, reverseGraph, visited, component);
                components.add(component);
            }
        }

        // Построение графа конденсации
        int n = components.size();
        // Присваиваем каждой вершине индекс компоненты
        Map<String, Integer> vertexToComp = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (String v : components.get(i)) {
                vertexToComp.put(v, i);
            }
        }

        // Граф конденсации: список смежности (индексы компонент)
        List<Set<Integer>> condGraph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            condGraph.add(new HashSet<>());
        }
        int[] inDegree = new int[n];

        for (String u : verticesSet) {
            int compU = vertexToComp.get(u);
            for (String v : graph.get(u)) {
                int compV = vertexToComp.get(v);
                if (compU != compV && condGraph.get(compU).add(compV)) {
                    inDegree[compV]++;
                }
            }
        }

        // Топологическая сортировка конденсации с лексикографическим порядком компонент
        // Компоненты сравниваются по их строковому представлению (отсортированные вершины)
        PriorityQueue<Integer> queue = new PriorityQueue<>((a, b) -> {
            String sa = componentToString(components.get(a));
            String sb = componentToString(components.get(b));
            return sa.compareTo(sb);
        });

        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int comp = queue.poll();
            topoOrder.add(comp);
            for (int neighbor : condGraph.get(comp)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Вывод компонент в топологическом порядке
        for (int idx : topoOrder) {
            // Вершины компоненты уже отсортированы лексикографически при создании строки
            System.out.println(componentToString(components.get(idx)));
        }
    }

    private static void dfs1(String v, Map<String, List<String>> graph,
                             Set<String> visited, Stack<String> finishOrder) {
        visited.add(v);
        // Соседей обходим в лексикографическом порядке для детерминированности
        List<String> neighbors = graph.get(v);
        neighbors.sort(Comparator.naturalOrder());
        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor, graph, visited, finishOrder);
            }
        }
        finishOrder.push(v);
    }

    private static void dfs2(String v, Map<String, List<String>> reverseGraph,
                             Set<String> visited, Set<String> component) {
        visited.add(v);
        component.add(v);
        // Соседей обходим в лексикографическом порядке (хотя порядок внутри компоненты не важен, так как потом сортируем)
        List<String> neighbors = reverseGraph.get(v);
        neighbors.sort(Comparator.naturalOrder());
        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, reverseGraph, visited, component);
            }
        }
    }

    private static String componentToString(Set<String> component) {
        List<String> list = new ArrayList<>(component);
        list.sort(Comparator.naturalOrder());
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
        }
        return sb.toString();
    }
}