package by.it.group451051.zhelubovskaya.lesson15;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<FileInfo> fileInfos = new ArrayList<>();

        Files.walk(srcPath)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        processFile(path, srcPath, fileInfos);
                    } catch (IOException e) {
                        // ignore
                    }
                });

        // Сортировка по размеру, затем по пути
        fileInfos.sort((a, b) -> {
            if (a.size != b.size) {
                return Integer.compare(a.size, b.size);
            }
            return a.relativePath.compareTo(b.relativePath);
        });

        // Вывод
        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.relativePath);
        }
    }

    private static void processFile(Path path, Path srcPath, List<FileInfo> fileInfos) throws IOException {
        // Относительный путь с ОБРАТНЫМИ слешами для Windows (как в тесте)
        String relativePath = srcPath.relativize(path).toString();

        // Чтение файла с игнорированием ошибок кодировки
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

        // Удаляем package и импорты
        content = removePackageAndImports(content);

        // Удаляем символы с кодом <33 в начале и конце
        content = trimControlChars(content);

        // Получаем размер в байтах
        int size = content.getBytes(StandardCharsets.UTF_8).length;

        fileInfos.add(new FileInfo(size, relativePath));
    }

    private static String removePackageAndImports(String content) {
        String[] lines = content.split("\\r?\\n");
        StringBuilder sb = new StringBuilder();
        boolean packageFound = false;
        boolean importFound = false;
        boolean firstNonPackageImport = false;

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
            // После удаления package и всех импортов добавляем оставшиеся строки
            if (packageFound || importFound) {
                if (!firstNonPackageImport && !trimmed.isEmpty()) {
                    firstNonPackageImport = true;
                }
                if (firstNonPackageImport) {
                    sb.append(line).append("\n");
                }
            } else {
                sb.append(line).append("\n");
            }
        }

        // Если не было package и импортов, возвращаем исходный текст
        if (!packageFound && !importFound) {
            return content;
        }

        return sb.toString();
    }

    private static String trimControlChars(String content) {
        int start = 0;
        int end = content.length() - 1;

        while (start <= end && content.charAt(start) < 33) {
            start++;
        }
        while (end >= start && content.charAt(end) < 33) {
            end--;
        }

        if (start > end) {
            return "";
        }
        return content.substring(start, end + 1);
    }

    private static class FileInfo {
        int size;
        String relativePath;

        FileInfo(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }
}