package by.it.group451052.nasonova.lesson15;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Path.of(src);
        List<FileData> files = new ArrayList<>();

        try (var walk = Files.walk(root)) {
            walk.forEach(path -> {
                if (path.toString().endsWith(".java")) {
                    try {
                        String content = safeRead(path);
                        if (content == null) {
                            return;
                        }

                        if (content.contains("@Test") || content.contains("org.junit.Test")) {
                            return;
                        }

                        String normalized = normalize(content);
                        String relativePath = root.relativize(path).toString().replace('\\', '/');
                        files.add(new FileData(relativePath, normalized));
                    } catch (IOException ignored) {
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Collections.sort(files, Comparator.comparing(f -> f.path));

        int n = files.size();
        boolean[] printed = new boolean[n];

        for (int i = 0; i < n; i++) {
            List<String> copies = new ArrayList<>();

            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }

                int diff = levenshtein(files.get(i).text, files.get(j).text, 9);
                if (diff < 10) {
                    copies.add(files.get(j).path);
                }
            }

            if (!copies.isEmpty()) {
                System.out.println(files.get(i).path);
                for (String copy : copies) {
                    System.out.println(copy);
                }
            }
        }
    }

    static String safeRead(Path path) throws IOException {
        try {
            return Files.readString(path);
        } catch (MalformedInputException e) {
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                StringBuilder sb = new StringBuilder();
                String line;
                boolean first = true;
                while ((line = reader.readLine()) != null) {
                    if (!first) {
                        sb.append('\n');
                    }
                    sb.append(line);
                    first = false;
                }
                return sb.toString();
            } catch (MalformedInputException ex) {
                return null;
            }
        }
    }

    static String normalize(String text) {
        String noComments = removeComments(text);
        String[] lines = noComments.split("\n", -1);
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.stripLeading();
            if (trimmed.startsWith("package ")) {
                continue;
            }
            if (trimmed.startsWith("import ")) {
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

        boolean inString = false;
        boolean inChar = false;
        boolean lineComment = false;
        boolean blockComment = false;
        boolean escape = false;

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            char next = (i + 1 < n) ? s.charAt(i + 1) : '\0';

            if (lineComment) {
                if (c == '\n') {
                    lineComment = false;
                    out.append(c);
                }
                continue;
            }

            if (blockComment) {
                if (c == '*' && next == '/') {
                    blockComment = false;
                    i++;
                }
                continue;
            }

            if (inString) {
                out.append(c);
                if (escape) {
                    escape = false;
                } else if (c == '\\') {
                    escape = true;
                } else if (c == '"') {
                    inString = false;
                }
                continue;
            }

            if (inChar) {
                out.append(c);
                if (escape) {
                    escape = false;
                } else if (c == '\\') {
                    escape = true;
                } else if (c == '\'') {
                    inChar = false;
                }
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

            out.append(c);
        }

        return out.toString();
    }

    static int levenshtein(String a, String b, int limit) {
        int la = a.length();
        int lb = b.length();

        if (Math.abs(la - lb) > limit) {
            return limit + 1;
        }

        if (la > lb) {
            String t = a;
            a = b;
            b = t;
            la = a.length();
            lb = b.length();
        }

        int[] prev = new int[lb + 1];
        int[] cur = new int[lb + 1];

        for (int j = 0; j <= lb; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= la; i++) {
            cur[0] = i;
            int minInRow = cur[0];
            char ca = a.charAt(i - 1);

            for (int j = 1; j <= lb; j++) {
                int cost = (ca == b.charAt(j - 1)) ? 0 : 1;

                int del = prev[j] + 1;
                int ins = cur[j - 1] + 1;
                int sub = prev[j - 1] + cost;

                int best = del < ins ? del : ins;
                if (sub < best) {
                    best = sub;
                }

                cur[j] = best;
                if (best < minInRow) {
                    minInRow = best;
                }
            }

            if (minInRow > limit) {
                return limit + 1;
            }

            int[] temp = prev;
            prev = cur;
            cur = temp;
        }

        return prev[lb];
    }
}