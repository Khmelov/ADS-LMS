package by.it.group451051.teterukov.lessons_09_15.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static int[] parent;
    static int[] dsuSize;

    static void initDSU(int n) {
        parent = new int[n];
        dsuSize = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            dsuSize[i] = 1;
        }
    }

    static int find(int x) {
        if (parent[x] != x)
            parent[x] = find(parent[x]);
        return parent[x];
    }

    static void union(int x, int y) {
        int px = find(x);
        int py = find(y);
        if (px == py) return;
        if (dsuSize[px] < dsuSize[py]) {
            int tmp = px; px = py; py = tmp;
        }
        parent[py] = px;
        dsuSize[px] += dsuSize[py];
    }

    static int[] stackA, stackB, stackC;
    static int sA, sB, sC;

    static int[] stepMax;
    static int stepIdx = 0;

    static void moveDisc(int[] from, int fromId, int[] to, int toId) {

        int fSize = getSize(fromId);
        int tSize = getSize(toId);
        int auxSize = getAuxSize(fromId, toId);

        int disc = from[fSize - 1];
        setSize(fromId, fSize - 1);
        to[tSize] = disc;
        setSize(toId, tSize + 1);

        int newFSize = fSize - 1;
        int newTSize = tSize + 1;
        int maxH = Math.max(newFSize, Math.max(newTSize, auxSize));
        stepMax[stepIdx++] = maxH;
    }

    static int getSize(int id) {
        if (id == 0) return sA;
        if (id == 1) return sB;
        return sC;
    }

    static void setSize(int id, int val) {
        if (id == 0) sA = val;
        else if (id == 1) sB = val;
        else sC = val;
    }

    static int getAuxSize(int fromId, int toId) {
        for (int i = 0; i < 3; i++) {
            if (i != fromId && i != toId) return getSize(i);
        }
        return 0;
    }

    static int[] getStack(int id) {
        if (id == 0) return stackA;
        if (id == 1) return stackB;
        return stackC;
    }

    static void hanoi(int n, int fromId, int toId, int auxId) {
        if (n == 0) return;
        hanoi(n - 1, fromId, auxId, toId);
        moveDisc(getStack(fromId), fromId, getStack(toId), toId);
        hanoi(n - 1, auxId, toId, fromId);
    }

    static void sortArray(int[] arr, int len) {
        for (int i = 0; i < len - 1; i++)
            for (int j = 0; j < len - i - 1; j++)
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j]; arr[j] = arr[j+1]; arr[j+1] = tmp;
                }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        int totalSteps = (1 << N) - 1;

        stackA = new int[N];
        stackB = new int[N];
        stackC = new int[N];
        sA = N; sB = 0; sC = 0;
        for (int i = 0; i < N; i++) stackA[i] = N - i;

        stepMax = new int[totalSteps];
        stepIdx = 0;

        hanoi(N, 0, 1, 2);

        initDSU(totalSteps);

        int[] firstStep = new int[N + 1];
        boolean[] seen = new boolean[N + 1];
        for (int i = 0; i <= N; i++) firstStep[i] = -1;

        for (int i = 0; i < totalSteps; i++) {
            int h = stepMax[i];
            if (!seen[h]) {
                seen[h] = true;
                firstStep[h] = i;
            } else {
                union(firstStep[h], i);
            }
        }

        int rootCount = 0;
        for (int i = 0; i < totalSteps; i++)
            if (find(i) == i) rootCount++;

        int[] result = new int[rootCount];
        int idx = 0;
        for (int i = 0; i < totalSteps; i++)
            if (find(i) == i) result[idx++] = dsuSize[i];

        sortArray(result, rootCount);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rootCount; i++) {
            if (i > 0) sb.append(" ");
            sb.append(result[i]);
        }
        System.out.println(sb);
    }
}