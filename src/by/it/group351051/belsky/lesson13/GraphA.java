package by.it.group351051.belsky.lesson13;

import java.util.*;

/*
Создайте класс GraphA в методе main которого считывается строка структуры орграфа вида:
0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1

Затем в консоль выводится его топологическая сортировка вида:
0 1 2 3

P.S. При равнозначности вершин их порядок вывода - лексикографический (т.е. по алфавиту)

 */

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine(); // Чтение строки вида "A -> B, B -> C"
        scanner.close();

        // Граф в виде списка смежности, отсортированного по алфавиту
        Map<String, List<String>> graph = new TreeMap<>();

        // Таблица входящих степеней для каждой вершины
        Map<String, Integer> inDegree = new HashMap<>();

        // Разделение строки на рёбра и построение графа
        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim(); // Убираем лишние пробелы
            String[] parts = edge.split("->"); // Разделяем по стрелке
            String from = parts[0].trim(); // Вершина-источник
            String to = parts[1].trim();   // Вершина-назначение

            // Добавляем вершины и ребра в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.putIfAbsent(to, new ArrayList<>()); // Добавляем вершину "to", если её нет

            // Увеличиваем входящую степень для вершины "to"
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            // Обеспечиваем наличие вершины "from" в таблице степеней
            inDegree.putIfAbsent(from, inDegree.getOrDefault(from, 0));
        }

        // Очередь с приоритетом для выбора лексикографически минимальной вершины
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String node : graph.keySet()) {
            if (inDegree.getOrDefault(node, 0) == 0) {
                queue.add(node); // Добавляем вершины без входящих рёбер
            }
        }

        // Список для хранения топологически отсортированных вершин
        List<String> result = new ArrayList<>();

        // Алгоритм Кана для топологической сортировки
        while (!queue.isEmpty()) {
            String current = queue.poll(); // Берём вершину с минимальным именем
            result.add(current); // Добавляем в итоговый список
            for (String neighbor : graph.get(current)) {
                // Уменьшаем входящую степень у соседей
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                // Если входящая степень стала 0, добавляем вершину в очередь
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Если количество вершин в результатах не совпадает с числом вершин в графе — цикл
        if (result.size() != graph.size()) {
            System.out.println("Граф содержит цикл!");
        } else {
            // Выводим результат через пробел
            System.out.println(String.join(" ", result));
        }
    }
}
