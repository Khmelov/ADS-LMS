package by.it.group351051.burdo.lesson13;

import java.util.*;

/**
 * Класс GraphB проверяет наличие циклов в ориентированном графе.
 */
public class GraphB {

    /**
     * Точка входа в программу.
     * Считывает описание графа из строки и проверяет, содержит ли он цикл.
     *
     * Пример входа: 1 -> 2, 1 -> 3, 2 -> 3
     * Пример выхода: no
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine(); // Пример: 1 -> 2, 1 -> 3, 2 -> 3
        scanner.close();

        // Построение графа
        Map<String, List<String>> graph = new HashMap<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.putIfAbsent(to, new ArrayList<>());
        }

        // Множества для отслеживания состояний узлов
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        // Проверка на наличие цикла в графе
        for (String node : graph.keySet()) {
            if (hasCycle(node, graph, visited, recStack)) {
                System.out.println("yes");
                return;
            }
        }

        System.out.println("no");
    }

    /**
     * Рекурсивная проверка наличия цикла с помощью DFS.
     *
     * @param node текущая вершина
     * @param graph граф в виде списка смежности
     * @param visited множество посещённых вершин
     * @param recStack множество вершин в текущем рекурсивном стеке
     * @return true если найден цикл, иначе false
     */
    private static boolean hasCycle(String node, Map<String, List<String>> graph,
                                    Set<String> visited, Set<String> recStack) {
        if (recStack.contains(node)) {
            return true; // Цикл найден
        }
        if (visited.contains(node)) {
            return false; // Уже обработан
        }

        visited.add(node);
        recStack.add(node);

        for (String neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (hasCycle(neighbor, graph, visited, recStack)) {
                return true;
            }
        }

        recStack.remove(node); // Убираем из стека
        return false;
    }
}
