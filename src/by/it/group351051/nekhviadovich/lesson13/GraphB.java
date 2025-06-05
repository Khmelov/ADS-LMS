package by.it.group351051.nekhviadovich.lesson13;

import java.util.*;
//lesson 13-2
class GraphB {
    private Map<Integer, List<Integer>> adjacencyList;

    public GraphB() {
        adjacencyList = new HashMap<>();
    }

    public void addEdge(int source, int destination) {
        adjacencyList.computeIfAbsent(source, k -> new ArrayList<>()).add(destination);
    }

    public boolean hasCycle() {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recursionStack = new HashSet<>();

        for (Integer node : adjacencyList.keySet()) {
            if (hasCycleUtil(node, visited, recursionStack)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasCycleUtil(int node, Set<Integer> visited, Set<Integer> recursionStack) {
        if (recursionStack.contains(node)) {
            return true;
        }
        if (visited.contains(node)) {
            return false;
        }

        visited.add(node);
        recursionStack.add(node);

        List<Integer> neighbors = adjacencyList.getOrDefault(node, new ArrayList<>());
        for (Integer neighbor : neighbors) {
            if (hasCycleUtil(neighbor, visited, recursionStack)) {
                return true;
            }
        }

        recursionStack.remove(node);
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите строку структуры орграфа:");
        String input = scanner.nextLine();

        GraphB graph = new GraphB();
        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] nodes = edge.split(" -> ");
            int source = Integer.parseInt(nodes[0]);
            int destination = Integer.parseInt(nodes[1]);
            graph.addEdge(source, destination);
        }

        if (graph.hasCycle()) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }
}