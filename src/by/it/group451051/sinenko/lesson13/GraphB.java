package by.it.group451051.sinenko.lesson13;

import java.util.*;

public class GraphB {
    
    // Главный метод для чтения входных данных
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        //  граф и рёбра
        Map<String, Set<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> vertices = new HashSet<>();
        
        // Разделение входных данных на рёбра
        String[] edges = input.split(",\\s*");
        
        // рёбра в граф
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            if (parts.length == 2) {
                String from = parts[0];
                String to = parts[1];
                
                vertices.add(from);
                vertices.add(to);
                
                // Добавляем рёбра в граф и считаем входящую степень
                adj.computeIfAbsent(from, k -> new TreeSet<>()).add(to);
                inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
                inDegree.putIfAbsent(from, 0);
            }
        }
        
        // Добавляем все вершины в inDegree
        for (String v : vertices) {
            inDegree.putIfAbsent(v, 0);
        }
        
        // Сортировка вершин по топологическому порядку
        Queue<String> queue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        // Счётчик обработанных вершин
        int processedCount = 0;
        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            processedCount++;
            
            Set<String> neighbors = adj.getOrDefault(vertex, new TreeSet<>());
            for (String neighbor : neighbors) {
                int newDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newDegree);
                if (newDegree == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        // Вывод да или нет
        System.out.println(processedCount == vertices.size() ? "no" : "yes");
    }
}