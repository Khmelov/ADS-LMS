package by.it.group451051.shiman.lesson15;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class SourceScannerC {

    private static class FileContent {
        final String path;
        final String normalizedText;

        FileContent(String path, String normalizedText) {
            this.path = path;
            this.normalizedText = normalizedText;
        }
    }

    public static void main(String[] args) {
        String srcDir = System.getProperty("user.dir") + java.io.File.separator + "src" + java.io.File.separator;
        Path srcPath = Paths.get(srcDir);

        List<FileContent> files = new ArrayList<>();

        try (var walk = Files.walk(srcPath)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = Files.readString(p);
                            // пропускаем тесты
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }
                            String noComments = removeComments(content);
                            String noPackageImports = removePackageAndImports(noComments);
                            String replaced = replaceControlChars(noPackageImports);
                            String trimmed = replaced.trim();

                            if (trimmed.isEmpty()) {
                                return; // пустые файлы не интересны
                            }
                            String relativePath = srcPath.relativize(p).toString();
                            files.add(new FileContent(relativePath, trimmed));
                        } catch (MalformedInputException e) {
                            // игнорируем
                        } catch (IOException e) {
                            // игнорируем
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // строим граф копий (расстояние <10)
        int n = files.size();
        List<Set<Integer>> graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            graph.add(new HashSet<>());
        }

        for (int i = 0; i < n; i++) {
            String textI = files.get(i).normalizedText;
            for (int j = i + 1; j < n; j++) {
                String textJ = files.get(j).normalizedText;
                int dist = levenshteinWithThreshold(textI, textJ, 9); // <10
                if (dist < 10) {
                    graph.get(i).add(j);
                    graph.get(j).add(i);
                }
            }
        }

        // вывод: для каждого файла, у которого есть хотя бы одна копия
        // сортируем файлы по пути (лексикографически)
        List<FileContent> sortedFiles = new ArrayList<>(files);
        sortedFiles.sort(Comparator.comparing(f -> f.path));

        for (FileContent file : sortedFiles) {
            int idx = files.indexOf(file); // находим индекс (можно было сохранить при сортировке, но мы пересоздадим список индексов)
            // переделаем: лучше создать массив индексов
        }

        // более удобно: сначала создаём список пар (путь, индекс)
        // а для вывода понадобятся копии
        // создадим массив путей для быстрого доступа
        String[] paths = new String[n];
        for (int i = 0; i < n; i++) {
            paths[i] = files.get(i).path;
        }

        // сортируем индексы по пути
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) indices[i] = i;
        Arrays.sort(indices, Comparator.comparing(i -> paths[i]));

        for (int idx : indices) {
            Set<Integer> neighbours = graph.get(idx);
            if (neighbours.isEmpty()) continue;
            // выводим путь текущего файла
            System.out.println(paths[idx]);
            // собираем пути копий (все соседи), сортируем
            List<String> copies = new ArrayList<>();
            for (int nb : neighbours) {
                copies.add(paths[nb]);
            }
            copies.sort(Comparator.naturalOrder());
            for (String cp : copies) {
                System.out.println(cp);
            }
        }
    }

    // удаление комментариев (однострочных и многострочных) с учётом строк и символов
    private static String removeComments(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder out = new StringBuilder();
        int n = s.length();
        int i = 0;
        boolean inString = false, inChar = false, inLineComment = false, inBlockComment = false;
        boolean escape = false;

        while (i < n) {
            char c = s.charAt(i);
            char next = (i + 1 < n) ? s.charAt(i + 1) : 0;

            if (!inString && !inChar && !inLineComment && !inBlockComment) {
                if (c == '/' && next == '/') {
                    inLineComment = true;
                    i += 2;
                    continue;
                }
                if (c == '/' && next == '*') {
                    inBlockComment = true;
                    i += 2;
                    continue;
                }
            }

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    out.append('\n');
                } else if (c == '\r') {
                    inLineComment = false;
                    out.append('\r');
                    if (i + 1 < n && s.charAt(i + 1) == '\n') {
                        out.append('\n');
                        i++;
                    }
                }
                i++;
                continue;
            }

            if (inBlockComment) {
                if (c == '*' && next == '/') {
                    inBlockComment = false;
                    i += 2;
                    continue;
                }
                i++;
                continue;
            }

            if (!inString && !inChar && c == '"') {
                inString = true;
                out.append(c);
                i++;
                continue;
            }
            if (!inString && !inChar && c == '\'') {
                inChar = true;
                out.append(c);
                i++;
                continue;
            }

            if (inString) {
                out.append(c);
                if (!escape && c == '"') {
                    inString = false;
                }
                escape = (c == '\\' && !escape);
                i++;
                continue;
            }

            if (inChar) {
                out.append(c);
                if (!escape && c == '\'') {
                    inChar = false;
                }
                escape = (c == '\\' && !escape);
                i++;
                continue;
            }

            out.append(c);
            i++;
        }
        return out.toString();
    }

    // удаление строк package и import
    private static String removePackageAndImports(String s) {
        String[] lines = s.split("\\R");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                sb.append(line).append("\n");
            }
        }
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    // замена символов с кодом <33 на пробел (32)
    private static String replaceControlChars(String s) {
        if (s == null || s.isEmpty()) return s;
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] < 33) {
                chars[i] = ' ';
            }
        }
        return new String(chars);
    }

    // расстояние Левенштейна с порогом (если превышает threshold, возвращаем threshold+1)
    private static int levenshteinWithThreshold(String a, String b, int threshold) {
        if (a == null || b == null) return Math.abs((a == null ? 0 : a.length()) - (b == null ? 0 : b.length()));
        int lenA = a.length();
        int lenB = b.length();
        if (Math.abs(lenA - lenB) > threshold) return threshold + 1;

        // оптимизация: работаем с короткой строкой как с "b"
        if (lenA > lenB) {
            String tmp = a;
            a = b;
            b = tmp;
            lenA = a.length();
            lenB = b.length();
        }

        // если длина a нулевая, расстояние равно lenB
        if (lenA == 0) return lenB > threshold ? threshold + 1 : lenB;

        int[] dpPrev = new int[lenA + 1];
        int[] dpCurr = new int[lenA + 1];
        for (int i = 0; i <= lenA; i++) dpPrev[i] = i;

        for (int j = 1; j <= lenB; j++) {
            dpCurr[0] = j;
            int minInRow = dpCurr[0];
            for (int i = 1; i <= lenA; i++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                dpCurr[i] = Math.min(Math.min(dpCurr[i - 1] + 1, dpPrev[i] + 1), dpPrev[i - 1] + cost);
                if (dpCurr[i] < minInRow) minInRow = dpCurr[i];
            }
            if (minInRow > threshold) {
                return threshold + 1;
            }
            // swap
            int[] temp = dpPrev;
            dpPrev = dpCurr;
            dpCurr = temp;
        }
        return dpPrev[lenA];
    }
}