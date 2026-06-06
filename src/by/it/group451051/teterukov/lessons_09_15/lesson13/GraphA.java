package by.it.group451051.teterukov.lessons_09_15.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphA {

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

        TreeMap<String, List<String>> graph    = new TreeMap<>();
        TreeMap<String, Integer>      inDegree = new TreeMap<>();

        if (!line.isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                edge = edge.trim();
                String[] parts = edge.split("->");
                String from = parts[0].trim();
                String to   = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                inDegree.putIfAbsent(from, 0);
                inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            }
        }

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) queue.add(entry.getKey());
        }

        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            String node = queue.poll();
            if (sb.length() > 0) sb.append(" ");
            sb.append(node);

            for (String neighbor : graph.getOrDefault(node, Collections.emptyList())) {
                int deg = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, deg);
                if (deg == 0) queue.add(neighbor);
            }
        }

        System.out.println(sb);
    }
}