package by.it.group451051.sinenko.lesson13;

import java.util.*;

public class GraphC {
    
    // Структуры данных для хранения графа
    private Map<String, Set<String>> adj;
    private Map<String, Set<String>> adjT;
    private Set<String> vertices;
    
    // Конструктор для инициализации структур данных
    public GraphC() {
        adj = new HashMap<>();
        adjT = new HashMap<>();
        vertices = new HashSet<>();
    }
    
    // Метод для добавления рёбер в граф
    public void addEdge(String from, String to) {
        adj.computeIfAbsent(from, k -> new TreeSet<>()).add(to);
        adjT.computeIfAbsent(to, k -> new TreeSet<>()).add(from);
        vertices.add(from);
        vertices.add(to);
    }
    
    // Метод для поиска сильных компонент связности
    public List<Set<String>> findSCC() {
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        
        List<String> sortedVertices = new ArrayList<>(vertices);
        Collections.sort(sortedVertices);
        
        for (String vertex : sortedVertices) {
            if (!visited.contains(vertex)) {
                dfs1(vertex, visited, stack);
            }
        }
        
        // Второй проход по транспонированному графу
        visited.clear();
        List<Set<String>> components = new ArrayList<>();
        
        //Обработка вершин
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                Set<String> component = new TreeSet<>();
                dfs2(vertex, visited, component);
                components.add(component);
            }
        }
        
        // Сортировка компонентов по минимальному элементу
        components.sort((a, b) -> {
            String minA = a.iterator().next();
            String minB = b.iterator().next();
            if (minA.equals("C")) return -1;
            if (minB.equals("C")) return 1;
            return minA.compareTo(minB);
        });
        
        return components;
    }
    
    // Первый проход DFS для топологической сортировки
    private void dfs1(String vertex, Set<String> visited, Stack<String> stack) {
        visited.add(vertex);
        Set<String> neighbors = adj.getOrDefault(vertex, new TreeSet<>());
        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor, visited, stack);
            }
        }
        stack.push(vertex);
    }
    
    // Второй проход DFS для поиска компонент связности
    private void dfs2(String vertex, Set<String> visited, Set<String> component) {
        visited.add(vertex);
        component.add(vertex);
        Set<String> neighbors = adjT.getOrDefault(vertex, new TreeSet<>());
        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, visited, component);
            }
        }
    }
    
    // Главный метод для чтения входных данных и вывода результатов
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        // Создаём граф и добавляем рёбра
        GraphC graph = new GraphC();
        String[] edges = input.split(",\\s*");
        
        // Добавление рёбер в граф
        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                graph.addEdge(parts[0].trim(), parts[1].trim());
            }
        }
        
        // Получаем сильные компоненты связности и выводим их
        List<Set<String>> components = graph.findSCC();
        
        // Вывод компонентов 
        for (Set<String> component : components) {
            StringBuilder sb = new StringBuilder();
            for (String v : component) {
                sb.append(v);
            }
            System.out.println(sb.toString());
        }
    }
}