package by.it.group451051.zhelubovskaya.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    private static final int MAX_EDIT_DISTANCE = 10;

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<FileContent> fileContents = new ArrayList<>();

        Files.walk(srcPath)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        processFile(path, srcPath, fileContents);
                    } catch (IOException e) {
                        // ignore
                    }
                });

        // Группировка копий
        List<List<FileContent>> groups = new ArrayList<>();
        boolean[] processed = new boolean[fileContents.size()];

        for (int i = 0; i < fileContents.size(); i++) {
            if (processed[i]) continue;
            
            List<FileContent> group = new ArrayList<>();
            group.add(fileContents.get(i));
            processed[i] = true;
            
            for (int j = i + 1; j < fileContents.size(); j++) {
                if (processed[j]) continue;
                
                int distance = levenshteinDistance(fileContents.get(i).content, fileContents.get(j).content);
                if (distance < MAX_EDIT_DISTANCE) {
                    group.add(fileContents.get(j));
                    processed[j] = true;
                }
            }
            
            if (group.size() > 1) {
                groups.add(group);
            }
        }

        // Сортировка групп и вывод
        for (List<FileContent> group : groups) {
            group.sort(Comparator.comparing(a -> a.relativePath));
            for (int i = 0; i < group.size(); i++) {
                System.out.println(group.get(i).relativePath);
            }
            System.out.println(); // пустая строка между группами
        }
    }

    private static void processFile(Path path, Path srcPath, List<FileContent> fileContents) throws IOException {
        String relativePath = srcPath.relativize(path).toString();

        // Чтение файла
        String content;
        try {
            byte[] bytes = Files.readAllBytes(path);
            content = new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            try {
                byte[] bytes = Files.readAllBytes(path);
                content = new String(bytes, Charset.forName("windows-1251"));
            } catch (Exception ex) {
                return;
            }
        }

        // Проверка на тесты
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        // 1. Удаляем package и импорты
        content = removePackageAndImports(content);

        // 2. Удаляем комментарии
        content = removeComments(content);

        // 3. Заменяем символы <33 на пробел
        content = replaceControlCharsToSpace(content);

        // 4. Trim
        content = content.trim();

        if (content.isEmpty()) return;

        fileContents.add(new FileContent(relativePath, content));
    }

    private static String removePackageAndImports(String content) {
        String[] lines = content.split("\\r?\\n");
        StringBuilder sb = new StringBuilder();
        boolean packageFound = false;
        boolean importFound = false;
        boolean started = false;

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package ")) {
                packageFound = true;
                continue;
            }
            if (trimmed.startsWith("import ")) {
                importFound = true;
                continue;
            }
            if (!started && (packageFound || importFound)) {
                if (!trimmed.isEmpty() && !trimmed.startsWith("@") && !trimmed.startsWith("//") && !trimmed.startsWith("/*")) {
                    started = true;
                }
            }
            if (!packageFound && !importFound) {
                sb.append(line).append("\n");
            } else if (started) {
                sb.append(line).append("\n");
            }
        }

        if (!packageFound && !importFound) {
            return content;
        }
        return sb.toString();
    }

    private static String removeComments(String content) {
        StringBuilder sb = new StringBuilder();
        boolean inMultiLineComment = false;
        boolean inString = false;
        int i = 0;
        int n = content.length();

        while (i < n) {
            char c = content.charAt(i);
            
            if (c == '"' && !inMultiLineComment) {
                inString = !inString;
                sb.append(c);
                i++;
                continue;
            }
            
            if (!inString) {
                if (!inMultiLineComment && i + 1 < n && c == '/' && content.charAt(i + 1) == '*') {
                    inMultiLineComment = true;
                    i += 2;
                    continue;
                }
                if (inMultiLineComment && i + 1 < n && c == '*' && content.charAt(i + 1) == '/') {
                    inMultiLineComment = false;
                    i += 2;
                    continue;
                }
                if (!inMultiLineComment && i + 1 < n && c == '/' && content.charAt(i + 1) == '/') {
                    while (i < n && content.charAt(i) != '\n') {
                        i++;
                    }
                    continue;
                }
            }
            
            if (!inMultiLineComment) {
                sb.append(c);
            }
            i++;
        }
        
        return sb.toString();
    }

    private static String replaceControlCharsToSpace(String content) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c < 33) {
                sb.append(' ');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // Расстояние Левенштейна с оптимизацией (только если длина различается не сильно)
    private static int levenshteinDistance(String a, String b) {
        int lenA = a.length();
        int lenB = b.length();
        
        // Если длина отличается больше чем на MAX_EDIT_DISTANCE, сразу возвращаем большое число
        if (Math.abs(lenA - lenB) >= MAX_EDIT_DISTANCE) {
            return MAX_EDIT_DISTANCE;
        }
        
        // Используем два ряда для оптимизации памяти
        int[] prev = new int[lenB + 1];
        int[] curr = new int[lenB + 1];
        
        for (int j = 0; j <= lenB; j++) {
            prev[j] = j;
        }
        
        for (int i = 1; i <= lenA; i++) {
            curr[0] = i;
            for (int j = 1; j <= lenB; j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(
                    Math.min(curr[j - 1] + 1, prev[j] + 1),
                    prev[j - 1] + cost
                );
            }
            // Ранний выход, если минимальное значение уже превышает порог
            int min = Integer.MAX_VALUE;
            for (int val : curr) {
                if (val < min) min = val;
            }
            if (min >= MAX_EDIT_DISTANCE) {
                return MAX_EDIT_DISTANCE;
            }
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        return prev[lenB];
    }

    private static class FileContent {
        String relativePath;
        String content;

        FileContent(String relativePath, String content) {
            this.relativePath = relativePath;
            this.content = content;
        }
    }
}