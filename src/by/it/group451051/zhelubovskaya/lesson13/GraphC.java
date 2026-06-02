package by.it.group451051.zhelubovskaya.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return;
        String input = sc.nextLine();
        sc.close();

        Map<String, List<String>> g = new HashMap<>();
        Map<String, List<String>> gr = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] edges = input.split(",");

        for (String e : edges) {
            if (!e.contains("->")) continue;
            String[] p = e.trim().split("->");

            String a = p[0].trim();
            String b = p[1].trim();

            vertices.add(a);
            vertices.add(b);

            g.computeIfAbsent(a, k -> new ArrayList<>()).add(b);
            g.computeIfAbsent(b, k -> new ArrayList<>());

            gr.computeIfAbsent(a, k -> new ArrayList<>());
            gr.computeIfAbsent(b, k -> new ArrayList<>()).add(a);
        }
        // Сортируем списки смежности строго по алфавиту (натуральный порядок)
        for (List<String> neighbors : g.values()) {
            neighbors.sort(Comparator.naturalOrder());
        }
        for (List<String> neighbors : gr.values()) {
            neighbors.sort(Comparator.naturalOrder());
        }

        Set<String> used = new HashSet<>();
        List<String> order = new ArrayList<>();

        // Первичный перебор вершин для DFS1 тоже строго по алфавиту
        List<String> sortedVertices = new ArrayList<>(vertices);
        sortedVertices.sort(Comparator.naturalOrder());

        for (String v : sortedVertices) {
            if (!used.contains(v)) {
                dfs1(v, g, used, order);
            }
        }

        used.clear();
        List<List<String>> components = new ArrayList<>();

        // Собираем компоненты сильной связности с конца списка order
        for (int i = order.size() - 1; i >= 0; i--) {
            String v = order.get(i);
            if (!used.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, gr, used, comp);
                Collections.sort(comp); // Сортируем только буквы внутри самой компоненты
                components.add(comp);
            }
        }

        for (List<String> comp : components) {
            StringBuilder sb = new StringBuilder();
            for (String s : comp) sb.append(s);
            System.out.println(sb);
        }
    }

    private static void dfs1(String v, Map<String, List<String>> g, Set<String> used, List<String> order) {
        used.add(v);
        for (String to : g.getOrDefault(v, new ArrayList<>())) {
            if (!used.contains(to)) {
                dfs1(to, g, used, order);
            }
        }
        order.add(v);
    }

    private static void dfs2(String v, Map<String, List<String>> gr, Set<String> used, List<String> comp) {
        used.add(v);
        comp.add(v);
        for (String to : gr.getOrDefault(v, new ArrayList<>())) {
            if (!used.contains(to)) {
                dfs2(to, gr, used, comp);
            }
        }
    }
}