package by.it.group451051.teterukov.lessons_09_15.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphB {

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

        TreeMap<String, List<String>> graph = new TreeMap<>();

        if (!line.isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                edge = edge.trim();
                String[] parts = edge.split("->");
                String from = parts[0].trim();
                String to   = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                graph.computeIfAbsent(to,   k -> new ArrayList<>());
            }
        }

        Map<String, Integer> state = new HashMap<>();
        for (String v : graph.keySet()) state.put(v, 0);

        boolean hasCycle = false;
        for (String v : graph.keySet()) {
            if (state.get(v) == 0) {
                if (dfs(v, graph, state)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(String v,
                               TreeMap<String, List<String>> graph,
                               Map<String, Integer> state) {
        state.put(v, 1);
        for (String neighbor : graph.getOrDefault(v, Collections.emptyList())) {
            int s = state.getOrDefault(neighbor, 0);
            if (s == 1) return true;
            if (s == 0 && dfs(neighbor, graph, state)) return true;
        }
        state.put(v, 2);
        return false;
    }
}