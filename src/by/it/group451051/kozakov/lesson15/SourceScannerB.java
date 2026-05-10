package by.it.group451051.kozakov.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerB {

    static class FileInfo {
        String path;
        String text;
        int size;

        FileInfo(String path, String text) {
            this.path = path;
            this.text = text;
            this.size = text.getBytes(StandardCharsets.UTF_8).length;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<FileInfo> files = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String content = safeRead(path);
                            if (content == null) {
                                System.err.println("Не удалось прочитать файл: " + path);
                                return;
                            }
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                System.err.println("Пропущен тест: " + path);
                                return;
                            }
                            String normalized = normalize(content);
                            if (normalized.isEmpty()) {
                                System.err.println("Файл пустой после нормализации: " + path);
                                return;
                            }
                            String relativePath = root.relativize(path).toString();
                            files.add(new FileInfo(relativePath, normalized));
                            System.out.println("Обработан файл: " + relativePath); // Отладка
                        } catch (IOException e) {
                            System.err.println("Ошибка чтения файла: " + path + " - " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка сканирования каталога: " + e.getMessage());
            return;
        }

        // Сортировка и вывод
        files.sort((f1, f2) -> {
            int sizeCompare = Integer.compare(f1.size, f2.size);
            return sizeCompare != 0 ? sizeCompare : f1.path.compareTo(f2.path);
        });

        for (FileInfo file : files) {
            System.out.printf("%d %s%n", file.size, file.path);
        }
    }

    // Чтение файла с обработкой MalformedInputException
    static String safeRead(Path path) throws IOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }

    // Нормализация текста: удаление package, импортов, комментариев, символов <33 и пустых строк
    static String normalize(String text) {
        // Удаление комментариев
        String noComments = removeComments(text);

        // Удаление package и импортов
        String[] lines = noComments.split("\n", -1);
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.stripLeading();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue;
            }
            sb.append(line).append('\n');
        }

        // Удаление символов <33 в начале и конце
        String result = sb.toString();
        result = result.replaceAll("^[\\x00-\\x20\\x7F]+", "");
        result = result.replaceAll("[\\x00-\\x20\\x7F]+$", "");

        // Удаление пустых строк
        result = result.replaceAll("(?m)^\\s*$", "");

        return result.trim();
    }

    // Удаление комментариев (однострочных и многострочных)
    static String removeComments(String s) {
        StringBuilder out = new StringBuilder();
        int n = s.length();
        boolean inString = false, inChar = false, lineComment = false, blockComment = false, escape = false;

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i), next = (i + 1 < n) ? s.charAt(i + 1) : '\0';

            if (lineComment && c == '\n') {
                lineComment = false;
                out.append(c);
                continue;
            } else if (lineComment) {
                continue;
            }

            if (blockComment && c == '*' && next == '/') {
                blockComment = false;
                i++;
                continue;
            } else if (blockComment) {
                continue;
            }

            if (inString) {
                out.append(c);
                if (escape) escape = false;
                else if (c == '\\') escape = true;
                else if (c == '"') inString = false;
                continue;
            }

            if (inChar) {
                out.append(c);
                if (escape) escape = false;
                else if (c == '\\') escape = true;
                else if (c == '\'') inChar = false;
                continue;
            }

            if (c == '"') {
                inString = true;
                out.append(c);
                continue;
            }

            if (c == '\'') {
                inChar = true;
                out.append(c);
                continue;
            }

            if (c == '/' && next == '/') {
                lineComment = true;
                i++;
                continue;
            }

            if (c == '/' && next == '*') {
                blockComment = true;
                i++;
                continue;
            }

            out.append(c);
        }

        return out.toString();
    }
}