package by.it.group451051.sinenko.lesson13;

import java.util.*;

public class GraphA {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Чтение входных данных
        String input = scanner.nextLine();        // Вершины и рёбра
        
        Map<String, Set<String>> adj = new HashMap<>();  // Граф в виде списка смежности
        Map<String, Integer> inDegree = new HashMap<>();   // Вершины графа
        Set<String> vertices = new HashSet<>();       
        
        String[] edges = input.split(",\\s*");      // Разделение входных данных на рёбра
        
        // Разделение входных данных на рёбра
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            if (parts.length == 2) {
                String from = parts[0];
                String to = parts[1];
                
                vertices.add(from);
                vertices.add(to);
                
                adj.computeIfAbsent(from, k -> new TreeSet<>()).add(to);
                inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
                inDegree.putIfAbsent(from, 0);
            }
        }
        
        // Добавляем все вершины, даже если у них нет рёбер
        for (String v : vertices) {
            inDegree.putIfAbsent(v, 0);
        }
        
        // Сортировка вершин по топологическому порядку
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        // Счётчик обработанных вершин
        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            result.add(vertex);
            
            // Обновляем входящую степень соседей
            Set<String> neighbors = adj.getOrDefault(vertex, new TreeSet<>());
            for (String neighbor : neighbors) {
                int newDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newDegree);
                if (newDegree == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        // Добавляем вершины что могли быть пропущены
        for (String v : vertices) {
            if (!result.contains(v)) {
                result.add(v);
            }
        }
        
        System.out.println(String.join(" ", result));
    }
}