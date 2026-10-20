package by.it.group551051.shulvinski.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор


        quicksort(segments,0,n-1);

        int[] stopCameras = new int[n];

        for (int i = 0; i < n; i++){
            stopCameras[i] = segments[i].stop;
        }

        quickSortCamera(stopCameras,0,n-1);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            int startCount = upperBoundStart(segments, p);
            int stopCount = upperBoundEnd(stopCameras, p);
            result[i] = startCount - stopCount;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    void quicksort(Segment[] a, int low, int high) {
        while (low < high) {
            int lt = low;
            int gt = high;

            Segment pivot = a[low + (high - low) / 2];

            int i = low;

            while (i <= gt) {
                int compare = a[i].compareTo(pivot);

                if (compare < 0) {
                    swapSegments(a,lt++,i++);
                } else if(compare > 0){
                    swapSegments(a,i,gt--);
                }else{
                    i++;
                }

            }
            if(lt - low < high - gt){
                quicksort(a,low,lt-1);
                low = gt + 1;
            }else{
                quicksort(a,gt+1,high);
                high = lt - 1;
            }

        }
    }

    int upperBoundEnd(int[] stopCamera, int point) {
        int left = 0;
        int right = stopCamera.length;

        while (left < right){
            int mid = (left + right) / 2;

            if(stopCamera[mid] < point){
                left = mid + 1;
            }else{
                right = mid;
            }
        }
        return left;
    }

    int upperBoundStart(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length;

        while (left < right){
            int mid = (left + right) / 2;

            if(segments[mid].start <= point){
                left = mid + 1;
            }else{
                right = mid;
            }
        }
        return left;
    }

    void swapCameras(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    void quickSortCamera(int[] cameras, int low, int high){
        while (low < high) {
            int lt = low; // граница меньше опорного
            int gt = high; // граница больше опорного

            int pivot = cameras[low + (high - low) / 2]; // опорный элемень

            int i = low;
            // проход по диапазону с разбиением на 3 части
            while (i <= gt) {

                if (cameras[i] < pivot) {
                    swapCameras(cameras,lt++,i++); // элемент меньший опорного переносим в левую часть
                } else if(cameras[i] > pivot){
                    swapCameras(cameras,i,gt--); // элемент больший опорного переносим в правую часть
                }else{
                    i++; // элемент равный опорному оставляем на месте
                }

            }
            // хвостовая оптимищзация (обработка меньшей из двух частей, а по большей цикл)
            if(lt - low < high - gt){
                quickSortCamera(cameras,low,lt-1);
                low = gt + 1;
            }else{
                quickSortCamera(cameras,gt+1,high);
                high = lt - 1;
            }

        }
    }
    void swapSegments(Segment[] arr, int i, int j) {
        Segment tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;

            if (stop < start){
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            return Integer.compare(this.start,o.start);
        }
    }

}
