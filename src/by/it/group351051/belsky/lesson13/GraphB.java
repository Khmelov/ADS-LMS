package by.it.group351051.belsky.lesson13;

import java.util.*;

/*
Создайте класс GraphB в методе main которого считывается строка структуры орграфа вида:
1 -> 2, 1 -> 3, 2 -> 3

Затем в консоль выводится фраза о наличии циклов.
Возможные варианты yes и no.
Для указанного примера будет такой вывод:
no


 */

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine(); // Вводим строку с описанием графа, например: 1 -> 2, 1 -> 3, 2 -> 3
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

        // Множества для отслеживания посещённых вершин и текущего стека рекурсии
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        // Проверяем наличие цикла в графе
        for (String node : graph.keySet()) {
            if (hasCycle(node, graph, visited, recStack)) {
                System.out.println("yes"); // Если цикл найден
                return;
            }
        }

        System.out.println("no"); // Если цикла нет
    }

    // Рекурсивная проверка наличия цикла в графе с помощью DFS
    private static boolean hasCycle(String node, Map<String, List<String>> graph,
                                    Set<String> visited, Set<String> recStack) {
        if (recStack.contains(node)) {
            return true; // Если вершина уже в стеке рекурсии, значит, есть цикл
        }
        if (visited.contains(node)) {
            return false; // Вершина уже обработана, цикла нет
        }

        visited.add(node);
        recStack.add(node);

        for (String neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (hasCycle(neighbor, graph, visited, recStack)) {
                return true; // Если цикл найден в одном из соседей
            }
        }

        recStack.remove(node); // Убираем вершину из стека
        return false; // Цикла не найдено
    }
}
