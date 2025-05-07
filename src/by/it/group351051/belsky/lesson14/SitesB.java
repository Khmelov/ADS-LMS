package by.it.group351051.belsky.lesson14;

import java.util.*;

public class SitesB {
    static class DSU {
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> size = new HashMap<>();

        // Поиск корня множества с применением пути сжатия
        String find(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);  // Если вершина ещё не существует, инициализируем её как саму себя
                size.put(x, 1);  // Изначально размер множества - 1
            }
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));  // Рекурсивно находим корень
            }
            return parent.get(x);
        }

        // Объединение двух множеств
        void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);
            if (!rootX.equals(rootY)) {
                // Объединяем меньшее множество в большее
                if (size.get(rootX) < size.get(rootY)) {
                    String temp = rootX;
                    rootX = rootY;
                    rootY = temp;
                }
                parent.put(rootY, rootX);  // Устанавливаем rootX как корень для rootY
                size.put(rootX, size.get(rootX) + size.get(rootY));  // Обновляем размер объединённого множества
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();

        // Чтение вводимых строк до команды "end"
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.equals("end")) {
                break;  // Завершаем, если введена команда "end"
            }
            String[] sites = line.split("\\+");  // Разделяем два сайта по символу "+"
            dsu.union(sites[0], sites[1]);  // Объединяем сайты в одно множество
        }

        // Подсчёт количества элементов в каждом множестве
        Map<String, Integer> groups = new HashMap<>();
        for (String site : dsu.parent.keySet()) {
            String root = dsu.find(site);  // Находим корень для сайта
            groups.put(root, groups.getOrDefault(root, 0) + 1);  // Увеличиваем размер компоненты
        }

        // Сортируем размеры компонент по убыванию
        List<Integer> result = new ArrayList<>(groups.values());
        result.sort(Collections.reverseOrder());

        // Выводим размеры компонент
        for (int size : result) {
            System.out.print(size + " ");
        }
    }
}
