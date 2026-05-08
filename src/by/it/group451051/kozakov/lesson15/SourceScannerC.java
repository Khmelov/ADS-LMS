package by.it.group451051.kozakov.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class SourceScannerC {

    static class FileData {
        String path;
        String text;

        FileData(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<FileData> files = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String content = safeRead(path);
                            if (content == null || content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }
                            String normalized = normalize(content);
                            String relativePath = root.relativize(path).toString().replace('\\', '/');
                            files.add(new FileData(relativePath, normalized));
                        } catch (IOException ignored) {}
                    });
        } catch (IOException e) {
            System.err.println("Ошибка при сканировании каталога: " + e.getMessage());
            return;
        }

        // Группируем файлы по названиям
        Map<String, List<FileData>> filesByName = new HashMap<>();
        for (FileData file : files) {
            String fileName = extractFileName(file.path);
            filesByName.computeIfAbsent(fileName, k -> new ArrayList<>()).add(file);
        }

        // Ищем копии только внутри каждой группы
        for (Map.Entry<String, List<FileData>> entry : filesByName.entrySet()) {
            String fileName = entry.getKey();
            List<FileData> group = entry.getValue();
            int n = group.size();
            Set<Integer> printed = new HashSet<>();

            for (int i = 0; i < n; i++) {
                if (printed.contains(i)) continue;
                String baseAuthor = extractAuthor(group.get(i).path);
                if (baseAuthor.equals("unknown")) continue;

                List<Integer> copies = new ArrayList<>();

                for (int j = i + 1; j < n; j++) {
                    if (printed.contains(j)) continue;
                    String copyAuthor = extractAuthor(group.get(j).path);
                    if (copyAuthor.equals("unknown")) continue;

                    int diff = levenshtein(group.get(i).text, group.get(j).text, 9);
                    if (diff < 10) {
                        copies.add(j);
                        printed.add(j);
                    }
                }

                if (!copies.isEmpty()) {
                    System.out.println("┌───────────────────────────────────────────────");
                    System.out.printf("│ Группа одинаковых файлов (%s, %s):%n", fileName, baseAuthor);
                    System.out.println("├───────────────────────────────────────────────");

                    for (int copyIndex : copies) {
                        int diff = levenshtein(group.get(i).text, group.get(copyIndex).text, 9);
                        String copyAuthor = extractAuthor(group.get(copyIndex).path);
                        System.out.printf("│ %s (%d)%n", copyAuthor, diff);
                    }

                    for (int k = 0; k < copies.size(); k++) {
                        for (int l = k + 1; l < copies.size(); l++) {
                            int diff = levenshtein(group.get(copies.get(k)).text, group.get(copies.get(l)).text, 9);
                            String authorK = extractAuthor(group.get(copies.get(k)).path);
                            String authorL = extractAuthor(group.get(copies.get(l)).path);
                            if (!authorK.equals(authorL)) {
                                System.out.printf("│ %s и %s (%d)%n", authorK, authorL, diff);
                            }
                        }
                    }

                    System.out.println("└───────────────────────────────────────────────");
                }
            }
        }
    }

    // Извлекает имя файла из пути
    static String extractFileName(String path) {
        int lastSlash = path.lastIndexOf('/');
        return path.substring(lastSlash + 1);
    }

    // Извлекает фамилию/ник из пути (например, "kozakov")
    static String extractAuthor(String path) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("by/it/group451051/([^/]+)/");
        java.util.regex.Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "unknown";
    }

    static String safeRead(Path path) throws IOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }

    static String normalize(String text) {
        String noComments = removeComments(text);
        String[] lines = noComments.split("\n", -1);
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.stripLeading();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                continue;
            }
            sb.append(line).append('\n');
        }

        StringBuilder normalized = new StringBuilder();
        boolean prevSpace = false;
        for (int i = 0; i < sb.length(); i++) {
            char ch = sb.charAt(i);
            if (ch < 33) {
                if (!prevSpace) {
                    normalized.append(' ');
                    prevSpace = true;
                }
            } else {
                normalized.append(ch);
                prevSpace = false;
            }
        }

        return normalized.toString().trim();
    }

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

    static int levenshtein(String a, String b, int limit) {
        int la = a.length(), lb = b.length();
        if (Math.abs(la - lb) > limit) return limit + 1;
        if (la > lb) return levenshtein(b, a, limit);

        int[] prev = new int[lb + 1];
        for (int j = 0; j <= lb; j++) prev[j] = j;

        for (int i = 1; i <= la; i++) {
            int[] cur = new int[lb + 1];
            cur[0] = i;
            char ca = a.charAt(i - 1);

            for (int j = 1; j <= lb; j++) {
                int cost = (ca == b.charAt(j - 1)) ? 0 : 1;
                cur[j] = Math.min(Math.min(prev[j] + 1, cur[j - 1] + 1), prev[j - 1] + cost);
            }
            prev = cur;
        }

        return prev[lb];
    }
}