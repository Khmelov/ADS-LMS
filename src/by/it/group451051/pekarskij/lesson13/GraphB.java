package by.it.group451051.pekarskij.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        // храним список смежности
        Map<String, List<String>> adj = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        // разбираем входную строку на отдельные ребра
        for (String edge : line.split(",")) {
            String[] parts = edge.trim().split("->");
            String u = parts[0].trim();
            String v = parts[1].trim();

            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            allVertices.add(u);
            allVertices.add(v);
        }

        // отслеживаем посещенные вершины и текущий путь поиска
        Set<String> visited = new HashSet<>();
        Set<String> path = new HashSet<>();
        boolean hasCycle = false;

        // запускаем обход от каждой вершины
        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                if (dfs(vertex, adj, visited, path)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        // выводим результат
        System.out.println(hasCycle ? "yes" : "no");
    }

    // рекурсивный поиск цикла
    private static boolean dfs(String v, Map<String, List<String>> adj, Set<String> visited, Set<String> path) {
        visited.add(v);
        path.add(v);

        for (String neighbor : adj.getOrDefault(v, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                if (dfs(neighbor, adj, visited, path)) return true;
            } else if (path.contains(neighbor)) {
                return true; // вершина уже в текущем пути, значит есть цикл
            }
        }

        path.remove(v); // выходим из вершины, убираем из пути
        return false;
    }
}