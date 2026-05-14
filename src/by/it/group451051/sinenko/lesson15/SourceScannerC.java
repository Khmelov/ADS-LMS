package by.it.group451051.sinenko.lesson15;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.nio.charset.MalformedInputException;


public class SourceScannerC {
    
    // Структура для хранения информации о файле
    static class FileInfo {
        String path;
        String content;
        
        FileInfo(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
    
    public static void main(String[] args) {
        // Получаем путь к папке src
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);
        
        // Список всех Java файлов 
        List<FileInfo> files = new ArrayList<>();
        
        try {
            // Обход всех файлов в папке src
            Files.walk(srcPath)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> processFile(p, files));
        } catch (IOException e) {
            System.err.println("Ошибка при обходе файлов: " + e.getMessage());
        }
        
        // Поиск копий
        findCopies(files);
    }
    
    // Обработка одного файла
    private static void processFile(Path path, List<FileInfo> files) {
        try {
            // Читаем файл с правильной кодировкой
            String content = readFile(path);
            
            // Пропускаем тестовые файлы
            if (isTestFile(content)) {
                return;
            }
            
            // Очищаем содержимое
            String cleaned = cleanContent(content);
            
            if (!cleaned.isEmpty()) {
                files.add(new FileInfo(path.toString(), cleaned));
            }
            
        } catch (MalformedInputException e) {
            // Пропускаем файлы с неправильной кодировкой
            System.err.println("Пропущен файл (проблема кодировки): " + path);
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + path + " - " + e.getMessage());
        }
    }
    
    //Чтение файла с обработкой кодировки
    private static String readFile(Path path) throws IOException {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            try {
                return new String(Files.readAllBytes(path), "Windows-1251");
            } catch (MalformedInputException | UnsupportedEncodingException ex) {
                return "";
            }
        }
    }
    

    //Проверка является ли файл тестовым
    private static boolean isTestFile(String content) {
        return content.contains("@Test") || content.contains("org.junit.Test");
    }
    
    //Очистка содержимого файла
    private static String cleanContent(String content) {
        content = removePackageAndImports(content);   // Удаляем package и import строки
        content = removeComments(content);            // Удаляем комментарии
        content = normalizeWhitespace(content);       // Заменяем символы с кодом <33 на пробел
        content = content.trim();                     // Trim
        
        return content;
    }
    
    //Удаление строк package и import
    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\\r?\\n");
        
        for (String line : lines) {
            String trimmed = line.trim();
            // Пропускаем package и import строки
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue;
            }
            result.append(line).append("\n");
        }
        
        return result.toString();
    }
    
    // Удаление комментариев 
    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        boolean inMultiLineComment = false;
        boolean inString = false;
        boolean inChar = false;
        int i = 0;
        int n = content.length();
        
        while (i < n) {
            char c = content.charAt(i);
            char next = (i + 1 < n) ? content.charAt(i + 1) : 0;
            
            // Обработка строковых литералов
            if (!inMultiLineComment && c == '"' && !inChar) {
                inString = !inString;
                result.append(c);
                i++;
                continue;
            }
            
            // Обработка символьных литералов
            if (!inMultiLineComment && c == '\'' && !inString) {
                inChar = !inChar;
                result.append(c);
                i++;
                continue;
            }
            
            if (inString || inChar) {
                result.append(c);
                i++;
                continue;
            }
            
            // Начало многострочного комментария /*
            if (!inMultiLineComment && c == '/' && next == '*') {
                inMultiLineComment = true;
                i += 2;
                continue;
            }
            
            // Конец многострочного комментария */
            if (inMultiLineComment && c == '*' && next == '/') {
                inMultiLineComment = false;
                i += 2;
                continue;
            }
            
            // Начало однострочного комментария //
            if (!inMultiLineComment && c == '/' && next == '/') {
                // Пропускаем до конца строки
                while (i < n && content.charAt(i) != '\n') {
                    i++;
                }
                continue;
            }
            
            // Не в комментарии - копируем
            if (!inMultiLineComment) {
                result.append(c);
            }
            i++;
        }
        
        return result.toString();
    }
    
    // Замена символов с кодом <33 на пробел
    private static String normalizeWhitespace(String content) {
        StringBuilder result = new StringBuilder();
        boolean lastWasSpace = false;
        
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c < 33) {
                if (!lastWasSpace) {
                    result.append(' ');
                    lastWasSpace = true;
                }
            } else {
                result.append(c);
                lastWasSpace = false;
            }
        }
        
        return result.toString();
    }
    
    // Поиск копий среди файлов
    private static void findCopies(List<FileInfo> files) {
        int n = files.size();
        boolean[] processed = new boolean[n];
        
        // Сортируем файлы лексикографически для вывода
        files.sort(Comparator.comparing(a -> a.path));
        
        // Для каждого файла ищем копии
        for (int i = 0; i < n; i++) {
            if (processed[i]) continue;
            
            List<Integer> copies = new ArrayList<>();
            copies.add(i);
            
            for (int j = i + 1; j < n; j++) {
                if (processed[j]) continue;
                
                // Вычисляем расстояние Левенштейна
                int distance = levenshteinDistance(files.get(i).content, files.get(j).content);
                
                if (distance < 10) {
                    copies.add(j);
                    processed[j] = true;
                }
            }
            
            // Если есть копии 
            if (copies.size() > 1) {
                processed[i] = true;
                for (int idx : copies) {
                    System.out.println(files.get(idx).path);
                }
            }
        }
    }
    
    // Расстояние Левенштейна 
    private static int levenshteinDistance(String s1, String s2) {
        // Оптимизация: если строки слишком разные по длине
        if (Math.abs(s1.length() - s2.length()) >= 10) {
            return 10; // больше порога
        }
        
        // Используем только две строки для экономии памяти
        int len1 = s1.length();
        int len2 = s2.length();
        
        if (len1 < len2) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
            len1 = s1.length();
            len2 = s2.length();
        }
        
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];
        
        // Инициализация
        for (int j = 0; j <= len2; j++) {
            prev[j] = j;
        }
        
        // Основной цикл
        for (int i = 1; i <= len1; i++) {
            curr[0] = i;
            char c1 = s1.charAt(i - 1);
            
            for (int j = 1; j <= len2; j++) {
                char c2 = s2.charAt(j - 1);
                int cost = (c1 == c2) ? 0 : 1;
                
                curr[j] = Math.min(
                    Math.min(prev[j] + 1, curr[j - 1] + 1),
                    prev[j - 1] + cost
                );
                
                // Ранний выход если уже превысили порог
                if (curr[j] >= 10 && i == len1 && j == len2) {
                    return 10;
                }
            }
            
            // Меняем местами
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        return prev[len2];
    }
}