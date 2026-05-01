package by.it.group451051.markhel.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerC {

    private static final int COPY_THRESHOLD = 10;

    static class ProcessedFile {
        final String relativePath;
        final String content;

        ProcessedFile(String relativePath, String content) {
            this.relativePath = relativePath;
            this.content = content;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Path.of(src);

        List<ProcessedFile> processedFiles = new ArrayList<>();

        try (var walk = Files.walk(srcPath)) {
            walk.parallel()
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        String content = readFileWithFallback(p);
                        if (content == null) return;

                        // Пропускаем тесты
                        if (content.contains("@Test") || content.contains("org.junit.Test")) {
                            return;
                        }

                        String withoutComments = removeComments(content);
                        String withoutPackageAndImports = removePackageAndImports(withoutComments);
                        String normalized = normalizeWhitespace(withoutPackageAndImports);
                        String finalContent = normalized.trim();

                        String relativePath = srcPath.relativize(p).toString();
                        processedFiles.add(new ProcessedFile(relativePath, finalContent));
                    });
        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталога src: " + e.getMessage());
            return;
        }

        // Поиск копий
        Map<String, List<String>> copiesMap = new HashMap<>();
        int n = processedFiles.size();
        for (int i = 0; i < n; i++) {
            ProcessedFile a = processedFiles.get(i);
            for (int j = i + 1; j < n; j++) {
                ProcessedFile b = processedFiles.get(j);
                int distance = limitedLevenshtein(a.content, b.content, COPY_THRESHOLD);
                if (distance < COPY_THRESHOLD) {
                    copiesMap.computeIfAbsent(a.relativePath, k -> new ArrayList<>()).add(b.relativePath);
                    copiesMap.computeIfAbsent(b.relativePath, k -> new ArrayList<>()).add(a.relativePath);
                }
            }
        }

        // Вывод
        List<String> filesWithCopies = new ArrayList<>(copiesMap.keySet());
        Collections.sort(filesWithCopies);
        for (String file : filesWithCopies) {
            List<String> copies = copiesMap.get(file);
            Collections.sort(copies);
            System.out.println(file);
            for (String copy : copies) {
                System.out.println(copy);
            }
        }
    }

    private static String readFileWithFallback(Path path) {
        Charset[] charsets = {StandardCharsets.UTF_8, Charset.forName("Windows-1251")};
        for (Charset cs : charsets) {
            try {
                return Files.readString(path, cs);
            } catch (MalformedInputException e) {
                // пробуем следующую кодировку
            } catch (IOException e) {
                System.err.println("Ошибка чтения " + path + ": " + e.getMessage());
                return null;
            }
        }
        System.err.println("Не удалось прочитать файл " + path + " ни в одной из кодировок");
        return null;
    }

    private static String removeComments(String code) {
        StringBuilder result = new StringBuilder();
        boolean inString = false;
        boolean inChar = false;
        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean escape = false;
        int len = code.length();
        for (int i = 0; i < len; i++) {
            char c = code.charAt(i);
            if (inBlockComment) {
                if (c == '*' && i + 1 < len && code.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i++;
                }
                continue;
            }
            if (inLineComment) {
                if (c == '\n' || c == '\r') {
                    inLineComment = false;
                    result.append(c);
                }
                continue;
            }
            if (inString) {
                result.append(c);
                if (!escape && c == '"') {
                    inString = false;
                }
                escape = !escape && c == '\\';
                continue;
            }
            if (inChar) {
                result.append(c);
                if (!escape && c == '\'') {
                    inChar = false;
                }
                escape = !escape && c == '\\';
                continue;
            }
            if (c == '"') {
                inString = true;
                result.append(c);
            } else if (c == '\'') {
                inChar = true;
                result.append(c);
            } else if (c == '/' && i + 1 < len) {
                char next = code.charAt(i + 1);
                if (next == '/') {
                    inLineComment = true;
                    i++;
                } else if (next == '*') {
                    inBlockComment = true;
                    i++;
                } else {
                    result.append(c);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static String removePackageAndImports(String code) {
        StringBuilder sb = new StringBuilder();
        String[] lines = code.split("\n");
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

    private static String normalizeWhitespace(String s) {
        StringBuilder result = new StringBuilder();
        boolean lastWasControl = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 33) {
                if (!lastWasControl) {
                    result.append(' ');
                    lastWasControl = true;
                }
            } else {
                result.append(c);
                lastWasControl = false;
            }
        }
        return result.toString();
    }

    private static int limitedLevenshtein(String a, String b, int limit) {
        int m = a.length();
        int n = b.length();
        if (Math.abs(m - n) > limit) return limit + 1;
        if (m == 0) return n;
        if (n == 0) return m;

        int[] dp = new int[n + 1];
        for (int j = 0; j <= n; j++) dp[j] = j;

        for (int i = 1; i <= m; i++) {
            int prev = dp[0];
            dp[0] = i;
            int minInRow = dp[0];
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[j] = prev;
                } else {
                    dp[j] = 1 + Math.min(Math.min(dp[j - 1], dp[j]), prev);
                }
                prev = temp;
                if (dp[j] < minInRow) minInRow = dp[j];
            }
            if (minInRow > limit) return limit + 1;
        }
        return dp[n];
    }
}
