package by.it.group451051.teterukov.lessons_09_15.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphC {

    public static void main(String[] args) throws Exception {
        String line;
        if (args.length > 0) {
            line = args[0].trim();
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            line = br.readLine();
            if (line == null) line = "";
            line = line.trim();
        }

        TreeMap<String, TreeSet<String>> graph     = new TreeMap<>();
        TreeMap<String, TreeSet<String>> transposed = new TreeMap<>();

        if (!line.isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                edge = edge.trim();
                String[] parts = edge.split("->");
                String from = parts[0].trim();
                String to   = parts[1].trim();

                graph.computeIfAbsent(from, k -> new TreeSet<>()).add(to);
                graph.computeIfAbsent(to,   k -> new TreeSet<>());
                transposed.computeIfAbsent(to,   k -> new TreeSet<>()).add(from);
                transposed.computeIfAbsent(from, k -> new TreeSet<>());
            }
        }

        Set<String>        visited = new HashSet<>();
        LinkedList<String> order   = new LinkedList<>();

        for (String v : graph.keySet()) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, order);
            }
        }

        visited.clear();
        List<List<String>> sccs = new ArrayList<>();

        while (!order.isEmpty()) {
            String v = order.pollLast();
            if (!visited.contains(v)) {
                List<String> scc = new ArrayList<>();
                dfs2(v, transposed, visited, scc);
                Collections.sort(scc);
                sccs.add(scc);
            }
        }

        for (List<String> scc : sccs) {
            StringBuilder sb = new StringBuilder();
            for (String v : scc) sb.append(v);
            System.out.println(sb);
        }
    }

    private static void dfs1(String v,
                             TreeMap<String, TreeSet<String>> graph,
                             Set<String> visited,
                             LinkedList<String> order) {
        visited.add(v);
        for (String neighbor : graph.getOrDefault(v, new TreeSet<>())) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor, graph, visited, order);
            }
        }
        order.addLast(v);
    }

    private static void dfs2(String v,
                             TreeMap<String, TreeSet<String>> transposed,
                             Set<String> visited,
                             List<String> scc) {
        visited.add(v);
        scc.add(v);
        for (String neighbor : transposed.getOrDefault(v, new TreeSet<>())) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, transposed, visited, scc);
            }
        }
    }
}