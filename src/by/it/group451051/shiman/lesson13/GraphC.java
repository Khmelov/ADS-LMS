package by.it.group451051.shiman.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inputLine = scanner.nextLine().trim();
        scanner.close();

        Set<String> verticesSet = new HashSet<>();
        List<String[]> edges = new ArrayList<>();

        // Парсинг строки
        if (!inputLine.isEmpty()) {
            String[] parts = inputLine.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (trimmed.isEmpty()) continue;
                String[] arrowSplit = trimmed.split("->");
                if (arrowSplit.length != 2) continue;
                String from = arrowSplit[0].trim();
                String to = arrowSplit[1].trim();
                edges.add(new String[]{from, to});
                verticesSet.add(from);
                verticesSet.add(to);
            }
        }

        List<String> vertices = new ArrayList<>(verticesSet);
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, List<String>> radj = new HashMap<>();

        for (String v : vertices) {
            adj.put(v, new ArrayList<>());
            radj.put(v, new ArrayList<>());
        }

        for (String[] edge : edges) {
            String u = edge[0];
            String v = edge[1];
            adj.get(u).add(v);
            radj.get(v).add(u);
        }

        // Алгоритм Косарайю
        Deque<String> stack = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        // Первый обход (прямой граф) для заполнения стека
        for (String v : vertices) {
            if (!visited.contains(v)) {
                dfs1(v, adj, visited, stack);
            }
        }

        // Второй обход (обратный граф) для выделения компонент
        Map<String, Integer> compId = new HashMap<>();
        Map<Integer, List<String>> components = new HashMap<>();
        visited.clear();
        int compCounter = 0;

        while (!stack.isEmpty()) {
            String v = stack.pop();
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, radj, visited, comp);
                comp.sort(null); // лексикографический порядок внутри компоненты
                components.put(compCounter, comp);
                for (String vertex : comp) {
                    compId.put(vertex, compCounter);
                }
                compCounter++;
            }
        }

        // Построение графа конденсации
        int n = components.size();
        Set<Integer>[] condAdj = new Set[n];
        int[] inDegree = new int[n];
        for (int i = 0; i < n; i++) {
            condAdj[i] = new HashSet<>();
        }

        for (String[] edge : edges) {
            int uComp = compId.get(edge[0]);
            int vComp = compId.get(edge[1]);
            if (uComp != vComp && !condAdj[uComp].contains(vComp)) {
                condAdj[uComp].add(vComp);
                inDegree[vComp]++;
            }
        }

        // Топологическая сортировка конденсации (алгоритм Кана)
        // При равнозначности (несколько компонент с нулевой степенью) выбираем лексикографически меньшую строку
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> {
            String sa = String.join("", components.get(a));
            String sb = String.join("", components.get(b));
            return sa.compareTo(sb);
        });

        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                pq.offer(i);
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!pq.isEmpty()) {
            int u = pq.poll();
            topoOrder.add(u);
            for (int v : condAdj[u]) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    pq.offer(v);
                }
            }
        }

        // Вывод компонент в порядке топологической сортировки (от истоков к стокам)
        for (int idx : topoOrder) {
            List<String> comp = components.get(idx);
            StringBuilder sb = new StringBuilder();
            for (String s : comp) {
                sb.append(s);
            }
            System.out.println(sb.toString());
        }
    }

    private static void dfs1(String v, Map<String, List<String>> adj, Set<String> visited, Deque<String> stack) {
        visited.add(v);
        for (String to : adj.get(v)) {
            if (!visited.contains(to)) {
                dfs1(to, adj, visited, stack);
            }
        }
        stack.push(v);
    }

    private static void dfs2(String v, Map<String, List<String>> radj, Set<String> visited, List<String> comp) {
        visited.add(v);
        comp.add(v);
        for (String from : radj.get(v)) {
            if (!visited.contains(from)) {
                dfs2(from, radj, visited, comp);
            }
        }
    }
}