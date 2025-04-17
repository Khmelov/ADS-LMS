package by.it.group351051.burdo.lesson14;

import java.util.*;

/**
 * Класс для решения задачи объединения сайтов, которые связаны между собой.
 * Сайты могут быть объединены в группы, если между ними существует связь.
 */
public class SitesB {

    /**
     * Главный метод программы для решения задачи.
     * Он читает входные данные, находит связи между сайтами и объединяет сайты в группы.
     * Затем выводит количество сайтов в каждой группе.
     * @param args аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Хранение уникальных идентификаторов для каждого сайта
        Map<String, Integer> id = new HashMap<>();

        // Список пар связанных сайтов
        List<String[]> pairs = new ArrayList<>();

        // Индекс для уникальных идентификаторов
        int index = 0;

        // Чтение пар сайтов, пока не встретится строка "end"
        while (true) {
            String line = sc.nextLine();
            if (line.equals("end")) break;

            // Разделение строки на два сайта
            String[] split = line.split("\\+");
            String a = split[0];
            String b = split[1];

            // Присваиваем уникальные идентификаторы каждому сайту, если это еще не сделано
            if (!id.containsKey(a)) id.put(a, index++);
            if (!id.containsKey(b)) id.put(b, index++);

            // Добавляем пару сайтов в список
            pairs.add(new String[]{a, b});
        }

        // Инициализация структуры DSU для объединения сайтов
        DSU dsu = new DSU(index);

        // Объединение сайтов по найденным связям
        for (String[] pair : pairs) {
            dsu.union(id.get(pair[0]), id.get(pair[1]));
        }

        // Группировка сайтов по их родителям и подсчет размеров групп
        Map<Integer, Integer> groupSizes = new HashMap<>();
        for (int i = 0; i < index; i++) {
            int root = dsu.find(i);  // Находим родительский элемент для сайта
            groupSizes.put(root, groupSizes.getOrDefault(root, 0) + 1);  // Увеличиваем размер группы
        }

        // Получаем список размеров групп и сортируем их по убыванию
        List<Integer> sizes = new ArrayList<>(groupSizes.values());
        sizes.sort(Collections.reverseOrder());

        // Выводим количество сайтов в каждой группе
        for (int size : sizes) {
            System.out.print(size + " ");
        }
    }

    /**
     * Класс DSU (Disjoint Set Union) реализует структуру данных для объединения и поиска элементов,
     * используемую для эффективного объединения сайтов в группы.
     */
    static class DSU {
        int[] parent;  // Массив для хранения родителей сайтов

        /**
         * Конструктор для инициализации структуры DSU.
         * @param size количество сайтов.
         */
        DSU(int size) {
            parent = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;  // Каждому сайту присваиваем его собственный индекс как родителя
            }
        }

        /**
         * Метод для нахождения родителя элемента с использованием сжатия пути.
         * @param x индекс сайта.
         * @return индекс родителя сайта.
         */
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);  // Сжимаем путь, делая родителем непосредственного предка
            }
            return parent[x];
        }

        /**
         * Метод для объединения двух сайтов в одну группу.
         * @param x индекс первого сайта.
         * @param y индекс второго сайта.
         */
        void union(int x, int y) {
            parent[find(x)] = find(y);  // Объединяем группы, присваивая родителя одного из сайтов
        }
    }
}
