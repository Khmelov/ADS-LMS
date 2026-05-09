package by.it.group451051.pekarskij.lesson14;

import java.util.*;

public class StatesHanoiTowerC {
    // текущие высоты пирамид
    static int hA, hB, hC;
    // запоминаем наибольшую высоту после каждого шага
    static int[] maxHeights;
    static int stepIdx;
    static DSU dsu;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        
        int totalSteps = (1 << n) - 1;
        maxHeights = new int[totalSteps];
        stepIdx = 0;
        dsu = new DSU(totalSteps);

        // начальное состояние: все диски на A
        hA = n; hB = 0; hC = 0;
        
        // запускаем решение ханойских башен
        hanoi(n, 'A', 'B', 'C');

        // группируем шаги с одинаковой максимальной высотой через dsu
        // repForHeight[h] хранит индекс последнего шага, где была высота h
        int[] repForHeight = new int[n + 1];
        for (int i = 0; i <= n; i++) repForHeight[i] = -1;

        for (int i = 0; i < totalSteps; i++) {
            int h = maxHeights[i];
            if (repForHeight[h] != -1) {
                dsu.union(i, repForHeight[h]);
            }
            repForHeight[h] = i;
        }

        // считаем размеры получившихся групп
        int[] groupSizes = new int[totalSteps];
        for (int i = 0; i < totalSteps; i++) {
            groupSizes[dsu.find(i)]++;
        }

        // собираем ненулевые размеры
        int[] sizes = new int[n + 1];
        int count = 0;
        for (int i = 0; i < totalSteps; i++) {
            if (groupSizes[i] > 0) {
                sizes[count++] = groupSizes[i];
            }
        }

        // сортируем размеры по возрастанию (пузырьком, без коллекций)
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (sizes[i] > sizes[j]) {
                    int tmp = sizes[i];
                    sizes[i] = sizes[j];
                    sizes[j] = tmp;
                }
            }
        }

        // выводим результат через пробел
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(sizes[i]);
            if (i < count - 1) sb.append(" ");
        }
        System.out.println(sb);
    }

    // рекурсивное перемещение n дисков с from на to, используя aux
    static void hanoi(int n, char from, char to, char aux) {
        if (n == 0) return;
        hanoi(n - 1, from, aux, to);
        move(from, to);
        hanoi(n - 1, aux, to, from);
    }

    // физическое перемещение одного диска и фиксация состояния
    static void move(char from, char to) {
        if (from == 'A') hA--; else if (from == 'B') hB--; else hC--;
        if (to == 'A') hA++; else if (to == 'B') hB++; else hC++;
        
        maxHeights[stepIdx++] = Math.max(hA, Math.max(hB, hC));
    }

    // dsu с двумя эвристиками: сжатие пути и объединение по размеру
    static class DSU {
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

        int find(int i) {
            if (parent[i] != i) parent[i] = find(parent[i]);
            return parent[i];
        }

        void union(int i, int j) {
            int ri = find(i);
            int rj = find(j);
            if (ri != rj) {
                // прикрепляем меньшее дерево к большему
                if (size[ri] < size[rj]) {
                    int tmp = ri; ri = rj; rj = tmp;
                }
                parent[rj] = ri;
                size[ri] += size[rj];
            }
        }
    }
}