package by.it.group351051.burdo.lesson13;

import java.util.*;

/**
 * Класс GraphA выполняет построение ориентированного графа
 * и выводит его топологическую сортировку в лексикографическом порядке.
 */
public class GraphA {

    /**
     * Точка входа в программу.
     * Читает описание графа из строки, строит ориентированный граф
     * и выполняет топологическую сортировку.
     *
     * Пример входа: A -> B, B -> C, A -> C
     * Пример выхода: A B C
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine(); // Считываем строку вида "A -> B, B -> C"
        scanner.close();

        // Граф, представленный в виде списка смежности (сортировка по алфавиту)
        Map<String, List<String>> graph = new TreeMap<>();

        // Таблица входящих степеней для каждой вершины
        Map<String, Integer> inDegree = new HashMap<>();

        // Разделение строки по ребрам и построение графа
        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim(); // Удаляем пробелы по краям
            String[] parts = edge.split("->"); // Разделяем по стрелке
            String from = parts[0].trim(); // Вершина-источник
            String to = parts[1].trim();   // Вершина-назначение

            // Добавляем вершины и ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.putIfAbsent(to, new ArrayList<>()); // Убедимся, что "to" тоже в графе

            // Увеличиваем входящую степень для вершины "to"
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            // Гарантируем наличие вершины "from" в таблице входящих степеней
            inDegree.putIfAbsent(from, inDegree.getOrDefault(from, 0));
        }

        // Очередь с приоритетом для выбора лексикографически минимальной вершины
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String node : graph.keySet()) {
            if (inDegree.getOrDefault(node, 0) == 0) {
                queue.add(node); // Вершины без входящих рёбер
            }
        }

        // Список для хранения результата топологической сортировки
        List<String> result = new ArrayList<>();

        // Алгоритм Кана (топологическая сортировка)
        while (!queue.isEmpty()) {
            String current = queue.poll(); // Берём вершину с минимальным именем
            result.add(current); // Добавляем в результат
            for (String neighbor : graph.get(current)) {
                // Уменьшаем входящую степень у соседей
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                // Если входящая степень стала 0 — добавляем в очередь
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Если размер результата не совпадает с количеством вершин — есть цикл
        if (result.size() != graph.size()) {
            System.out.println("Graph has a cycle!");
        } else {
            // Выводим результат через пробел
            System.out.println(String.join(" ", result));
        }
    }
}
