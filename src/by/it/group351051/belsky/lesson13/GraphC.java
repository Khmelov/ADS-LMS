package by.it.group351051.belsky.lesson13;

import java.util.*;

/*
Создайте класс GraphC в методе main которого считывается строка структуры орграфа вида:
C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G

Затем в консоль выводятся вершины компонент сильной связности
каждый компонент с новой строки, первый - исток, последний - сток,
пробелов и табуляции в выводе нигде нет, пример для введенного выше графа:
C
ABDHI
E
FGK

P.S. При равнозначности вершин в компоненте порядок их вывода - лексикографический (т.е. по алфавиту)

 */

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine(); // Вводим строку с описанием графа, например: C->B, C->I, I->A
        scanner.close();

        // Построение графа и его транспонированной версии
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reversedGraph = new HashMap<>();

        // Множество для всех вершин графа
        Set<String> allNodes = new HashSet<>();

        // Разбираем рёбра и строим графы
        for (String edge : input.split(",")) {
            String[] parts = edge.trim().split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reversedGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

            // Добавляем вершины в множество
            allNodes.add(from);
            allNodes.add(to);
            graph.putIfAbsent(to, new ArrayList<>());
            reversedGraph.putIfAbsent(from, new ArrayList<>());
        }

        // Сортируем списки смежности для лексикографического порядка
        for (List<String> neighbors : graph.values()) {
            Collections.sort(neighbors);
        }

        // Первая стадия: обычный DFS для получения порядка завершения
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

        // Выводим компоненты сильной связности
        for (List<String> component : components) {
            System.out.println(String.join("", component));
        }
    }

    // Обычный DFS для построения порядка завершения
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

    // DFS по транспонированному графу для поиска компоненты сильной связности
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
