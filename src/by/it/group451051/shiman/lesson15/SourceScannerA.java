package by.it.group451051.shiman.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerA {

    public static void main(String[] args) {
        String srcDir = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(srcDir);

        List<FileInfo> fileInfos = new ArrayList<>();

        try (var walk = Files.walk(srcPath)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            // Читаем содержимое файла (используем кодировку по умолчанию, как в тесте)
                            String content = Files.readString(p);

                            // Пропускаем файлы, содержащие тестовые аннотации
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Удаляем строку package и все импорты
                            String withoutPackageAndImports = removePackageAndImports(content);

                            // Удаляем управляющие символы в начале и конце (код <33)
                            String trimmed = trimControlChars(withoutPackageAndImports);

                            // Вычисляем размер в байтах (UTF-8)
                            int size = trimmed.getBytes(java.nio.charset.StandardCharsets.UTF_8).length;

                            // Относительный путь с системным разделителем (как ожидает тест)
                            String relativePath = srcPath.relativize(p).toString();

                            fileInfos.add(new FileInfo(size, relativePath));
                        } catch (MalformedInputException e) {
                            // Игнорируем ошибки MalformedInputException (пропускаем файл)
                        } catch (IOException e) {
                            // Игнорируем прочие ошибки ввода-вывода
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортировка: по размеру (возрастание), затем по пути лексикографически
        fileInfos.sort(Comparator.comparingInt(FileInfo::size)
                .thenComparing(FileInfo::path));

        // Вывод результатов
        for (FileInfo info : fileInfos) {
            System.out.println(info.size() + " " + info.path());
        }
    }

    private static String removePackageAndImports(String content) {
        String[] lines = content.split("\\R"); // разбиваем по всем переносам строк
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package ") && !trimmedLine.startsWith("import ")) {
                sb.append(line).append("\n");
            }
        }
        // Удаляем последний лишний перевод строки, если он есть
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    private static String trimControlChars(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        int start = 0;
        while (start < s.length() && s.charAt(start) < 33) {
            start++;
        }
        int end = s.length() - 1;
        while (end >= start && s.charAt(end) < 33) {
            end--;
        }
        return s.substring(start, end + 1);
    }

    private record FileInfo(int size, String path) {
    }
}