package by.it.group451051.kozakov.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    private static int[] parentArray;
    private static int[] sizeArray;
    private static int[] firstOccurrence;
    private static int[] towerHeights;
    private static int currentStep;

    private static int findRoot(int vertex) {
        while (parentArray[vertex] != vertex) {
            parentArray[vertex] = parentArray[parentArray[vertex]];
            vertex = parentArray[vertex];
        }
        return vertex;
    }

    private static void uniteSets(int a, int b) {
        int rootA = findRoot(a);
        int rootB = findRoot(b);
        if (rootA != rootB) {
            if (sizeArray[rootA] < sizeArray[rootB]) {
                int temp = rootA;
                rootA = rootB;
                rootB = temp;
            }
            parentArray[rootB] = rootA;
            sizeArray[rootA] += sizeArray[rootB];
        }
    }

    private static void recordState() {
        int maxHeight = towerHeights[0];
        for (int i = 1; i < 3; i++) {
            if (towerHeights[i] > maxHeight) {
                maxHeight = towerHeights[i];
            }
        }

        if (firstOccurrence[maxHeight] == -1) {
            firstOccurrence[maxHeight] = currentStep;
        } else {
            uniteSets(currentStep, firstOccurrence[maxHeight]);
        }
        currentStep++;
    }

    private static void transferDisks(int diskCount, int source, int target, int auxiliary) {
        if (diskCount == 0) {
            return;
        }

        transferDisks(diskCount - 1, source, auxiliary, target);

        towerHeights[source]--;
        towerHeights[target]++;
        recordState();

        transferDisks(diskCount - 1, auxiliary, target, source);
    }

    private static void insertionSort(int[] array, int length) {
        for (int i = 1; i < length; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int diskNumber = input.nextInt();

        int totalSteps = (1 << diskNumber) - 1;

        parentArray = new int[totalSteps];
        sizeArray = new int[totalSteps];
        firstOccurrence = new int[diskNumber + 1];
        towerHeights = new int[3];

        for (int i = 0; i < totalSteps; i++) {
            parentArray[i] = i;
            sizeArray[i] = 1;
        }

        for (int i = 0; i <= diskNumber; i++) {
            firstOccurrence[i] = -1;
        }

        towerHeights[0] = diskNumber;
        towerHeights[1] = 0;
        towerHeights[2] = 0;
        currentStep = 0;

        transferDisks(diskNumber, 0, 1, 2);

        int[] result = new int[diskNumber];
        int resultIndex = 0;

        for (int height = 1; height <= diskNumber; height++) {
            if (firstOccurrence[height] != -1) {
                result[resultIndex++] = sizeArray[findRoot(firstOccurrence[height])];
            }
        }

        insertionSort(result, resultIndex);

        for (int i = 0; i < resultIndex; i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(result[i]);
        }
    }
}