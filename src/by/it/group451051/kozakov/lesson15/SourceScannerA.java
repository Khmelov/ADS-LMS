package by.it.group451051.kozakov.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerA {

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
                                System.err.println("Не удалось прочитать: " + path);
                                return;
                            }
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                System.err.println("Пропущен тест: " + path);
                                return;
                            }
                            String normalized = normalize(content);
                            String relativePath = root.relativize(path).toString(); // Не заменяем \ на /
                            files.add(new FileInfo(relativePath, normalized));
                            System.out.println("Обработан: " + relativePath); // Отладка
                        } catch (IOException e) {
                            System.err.println("Ошибка чтения: " + path + " - " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка сканирования: " + e.getMessage());
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

    // Нормализация текста: удаление package, импортов и символов <33
    static String normalize(String text) {
        String[] lines = text.split("\n", -1);
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

        return result.trim();
    }
}