package by.it.group451051.zhelubovskaya.lesson14;

import java.util.*;

public class StatesHanoiTowerC {

    private static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) return;
            if (size[rootA] < size[rootB]) {
                int temp = rootA;
                rootA = rootB;
                rootB = temp;
            }
            parent[rootB] = rootA;
            size[rootA] += size[rootB];
        }

        int getSize(int x) {
            return size[find(x)];
        }
    }

    // Класс для хранения состояния башен
    private static class State {
        int a, b, c; // количество колец на стержнях A, B, C
        State(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        int getMaxHeight() {
            return Math.max(a, Math.max(b, c));
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            State state = (State) obj;
            return a == state.a && b == state.b && c == state.c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c);
        }
    }

    // Генерация всех шагов решения Ханойских башен
    private static void generateHanoiSteps(int n, char from, char to, char helper, 
                                            List<State> steps, 
                                            Map<Character, Integer> ringCounts) {
        if (n == 0) return;
        
        generateHanoiSteps(n - 1, from, helper, to, steps, ringCounts);
        
        // Перемещаем диск n с from на to
        ringCounts.put(from, ringCounts.get(from) - 1);
        ringCounts.put(to, ringCounts.get(to) + 1);
        
        steps.add(new State(ringCounts.get('A'), ringCounts.get('B'), ringCounts.get('C')));
        
        generateHanoiSteps(n - 1, helper, to, from, steps, ringCounts);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();

        // Генерируем все шаги решения Ханойских башен
        List<State> steps = new ArrayList<>();
        Map<Character, Integer> ringCounts = new HashMap<>();
        ringCounts.put('A', N);
        ringCounts.put('B', 0);
        ringCounts.put('C', 0);
        
        generateHanoiSteps(N, 'A', 'B', 'C', steps, ringCounts);
        
        // Группируем шаги по наибольшей высоте
        Map<Integer, List<Integer>> heightMap = new HashMap<>();
        for (int i = 0; i < steps.size(); i++) {
            int maxHeight = steps.get(i).getMaxHeight();
            heightMap.computeIfAbsent(maxHeight, k -> new ArrayList<>()).add(i);
        }
        
        // Для каждой группы строим DSU
        List<Integer> result = new ArrayList<>();
        
        for (Map.Entry<Integer, List<Integer>> entry : heightMap.entrySet()) {
            List<Integer> indices = entry.getValue();
            if (indices.size() <= 1) {
                result.add(1);
                continue;
            }
            
            DSU dsu = new DSU(indices.size());
            
            // Объединяем последовательные шаги в группе
            for (int i = 0; i < indices.size() - 1; i++) {
                dsu.union(i, i + 1);
            }
            
            // Собираем размеры компонент
            Set<Integer> roots = new HashSet<>();
            for (int i = 0; i < indices.size(); i++) {
                roots.add(dsu.find(i));
            }
            
            for (int root : roots) {
                result.add(dsu.getSize(root));
            }
        }
        
        result.sort(Comparator.naturalOrder());
        
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}