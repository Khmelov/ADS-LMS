package by.it.group451051.pekarskij.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerB {

    // вспомогательный класс для хранения данных о файле
    static class FileData {
        int size;
        String path;
        FileData(int size, String path) {
            this.size = size;
            this.path = path;
        }
    }

    public static void main(String[] args) {
        // получаем путь к папке src относительно пользовательской директории
        String srcDirStr = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(srcDirStr);
        
        // список для хранения результатов
        List<FileData> results = new ArrayList<>();

        if (!Files.exists(srcPath)) return;

        // обходим все файлы в папке src и её подпапках
        try (Stream<Path> walk = Files.walk(srcPath)) {
            walk.filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        // читаем содержимое файла, обрабатывая ошибки кодировки
                        String content = readContent(path);
                        if (content == null) return;

                        // файлы с тестами не участвуют в обработке
                        if (content.contains("@Test") || content.contains("org.junit.Test")) return;

                        // обрабатываем текст: удаляем комментарии, импорты, пакет, пустые строки
                        String processed = processContent(content);
                        
                        // если после обработки текст пуст, пропускаем
                        if (processed.isEmpty()) return;

                        // считаем размер в байтах
                        int size = processed.getBytes(StandardCharsets.UTF_8).length;
                        
                        // получаем относительный путь от src
                        String relativePath = srcPath.relativize(path).toString();
                        
                        results.add(new FileData(size, relativePath));
                    } catch (Exception e) {
                        // игнорируем ошибки при обработке отдельных файлов
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // сортируем результаты: сначала по размеру (возрастание), потом по пути (лексикографически)
        Collections.sort(results, (a, b) -> {
            if (a.size != b.size) return Integer.compare(a.size, b.size);
            return a.path.compareTo(b.path);
        });

        // выводим результат: размер и путь
        for (FileData f : results) {
            System.out.println(f.size + " " + f.path);
        }
    }

    // метод для чтения файла с корректной обработкой MalformedInputException
    private static String readContent(Path path) {
        try {
            // пробуем прочитать как UTF-8
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (CharacterCodingException e) {
            // если возникает MalformedInputException (наследник CharacterCodingException),
            // пробуем прочитать как ISO-8859-1 (Latin-1), которая читает любые байты
            try {
                return new String(Files.readAllBytes(path), StandardCharsets.ISO_8859_1);
            } catch (Exception ex) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    // метод обработки текста согласно заданию
    private static String processContent(String text) {
        // удаляем комментарии 
        String noComments = removeComments(text);

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new StringReader(noComments))) {
            String line;
            while ((line = br.readLine()) != null) {
                // убираем ведущие пробелы для проверки ключевых слов
                String trimmed = line.stripLeading();
                
                // удаляем строки package и import
                if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) continue;
                
                // удаляем пустые строки
                if (trimmed.isEmpty()) continue;
                
                // сохраняем строку (с исходными отступами) и добавляем перенос
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            return "";
        }

        String res = sb.toString();

        // удаляем символы с кодом < 33 в начале и конце текста
        int start = 0;
        int end = res.length();
        while (start < end && res.charAt(start) < 33) start++;
        while (end > start && res.charAt(end - 1) < 33) end--;
        
        return res.substring(start, end);
    }

    // метод удаления комментариев за 
    // корректно обрабатывает строковые литералы, чтобы не удалять "//" внутри строк
    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int len = text.length();
        boolean inBlock = false;
        boolean inString = false;
        boolean inChar = false;

        while (i < len) {
            char c = text.charAt(i);
            char next = (i + 1 < len) ? text.charAt(i + 1) : 0;

            if (inBlock) {
                if (c == '*' && next == '/') {
                    inBlock = false;
                    i += 2;
                } else {
                    i++;
                }
            } else if (inString) {
                sb.append(c);
                if (c == '\\') {
                    i++; // пропускаем экранированный символ
                    if (i < len) sb.append(text.charAt(i));
                } else if (c == '"') {
                    inString = false;
                }
                i++;
            } else if (inChar) {
                sb.append(c);
                if (c == '\\') {
                    i++; // пропускаем экранированный символ
                    if (i < len) sb.append(text.charAt(i));
                } else if (c == '\'') {
                    inChar = false;
                }
                i++;
            } else {
                if (c == '/' && next == '/') {
                    i += 2;
                    while (i < len && text.charAt(i) != '\n') i++;
                } else if (c == '/' && next == '*') {
                    inBlock = true;
                    i += 2;
                } else if (c == '"') {
                    inString = true;
                    sb.append(c);
                    i++;
                } else if (c == '\'') {
                    inChar = true;
                    sb.append(c);
                    i++;
                } else {
                    sb.append(c);
                    i++;
                }
            }
        }
        return sb.toString();
    }
}