package by.it.group551051.makhmudov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        if (n > 0) {
            quickSort3Way(segments, 0, n - 1);
        }
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;
            int firstIndex = binarySearchFirst(segments, point);
            if (firstIndex != -1) {
                for (int j = firstIndex; j >= 0; j--) {
                    if (segments[j].start <= point && point <= segments[j].stop) {
                        count++;
                    }
                }
                for (int j = firstIndex + 1; j < n; j++) {
                    if (segments[j].start > point) {
                        break;
                    }
                    if (segments[j].start <= point && point <= segments[j].stop) {
                        count++;
                    }
                }
            }
            result[i] = count;
        }
        return result;
    }

    private void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            if (high - low < 16) {
                insertionSort(arr, low, high);
                return;
            }
            int mid = low + (high - low) / 2;
            if (arr[mid].compareTo(arr[low]) < 0) {
                swap(arr, low, mid);
            }
            if (arr[high].compareTo(arr[low]) < 0) {
                swap(arr, low, high);
            }
            if (arr[high].compareTo(arr[mid]) < 0) {
                swap(arr, mid, high);
            }
            swap(arr, mid, high - 1);
            Segment pivot = arr[high - 1];

            int i = low;
            int j = high - 1;
            int p = low;
            int q = high - 1;

            while (true) {
                while (arr[++i].compareTo(pivot) < 0) {
                }
                while (arr[--j].compareTo(pivot) > 0 && j > low) {
                }
                if (i >= j) {
                    break;
                }
                swap(arr, i, j);
                if (arr[i].compareTo(pivot) == 0) {
                    swap(arr, i, p++);
                }
                if (arr[j].compareTo(pivot) == 0) {
                    swap(arr, j, q--);
                }
            }
            swap(arr, i, high - 1);

            if (i - low < high - i) {
                quickSort3Way(arr, low, i - 1);
                low = i + 1;
            } else {
                quickSort3Way(arr, i + 1, high);
                high = i - 1;
            }
        }
    }

    private void insertionSort(Segment[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            Segment key = arr[i];
            int j = i - 1;
            while (j >= low && arr[j].compareTo(key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private int binarySearchFirst(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start > stop) {
                this.start = stop;
                this.stop = start;
            } else {
                this.start = start;
                this.stop = stop;
            }
        }

        @Override
        public int compareTo(Segment o) {
            if (start != o.start) {
                return Integer.compare(start, o.start);
            }
            return Integer.compare(stop, o.stop);
        }
    }

}
