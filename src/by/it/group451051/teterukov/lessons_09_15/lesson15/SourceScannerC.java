package by.it.group451051.teterukov.lessons_09_15.lesson15;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) throws Exception {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<File> javaFiles = new ArrayList<>();
        collectJavaFiles(new File(src), javaFiles);

        List<String> texts = new ArrayList<>();
        List<String> paths = new ArrayList<>();

        for (File file : javaFiles) {
            String content = readFile(file);
            if (content == null) continue;
            if (content.contains("@Test") || content.contains("org.junit.Test")) continue;

            content = removePackageAndImports(content);
            content = removeComments(content);
            content = collapseWhitespace(content);
            content = content.trim();

            String relativePath = file.getAbsolutePath().substring(src.length());
            texts.add(content);
            paths.add(relativePath);
        }

        int n = texts.size();

        TreeMap<String, TreeSet<String>> result = new TreeMap<>();

        int maxLen = 0;
        for (String t : texts) if (t.length() > maxLen) maxLen = t.length();
        int[] prev = new int[maxLen + 1];
        int[] curr = new int[maxLen + 1];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                String a = texts.get(i);
                String b = texts.get(j);

                if (Math.abs(a.length() - b.length()) >= 10) continue;

                int dist = levenshtein(a, b, prev, curr);
                if (dist < 10) {
                    String pathI = paths.get(i);
                    String pathJ = paths.get(j);

                    if (!result.containsKey(pathI)) result.put(pathI, new TreeSet<>());
                    if (!result.containsKey(pathJ)) result.put(pathJ, new TreeSet<>());

                    result.get(pathI).add(pathJ);
                    result.get(pathJ).add(pathI);
                }
            }
        }

        for (Map.Entry<String, TreeSet<String>> entry : result.entrySet()) {
            System.out.println(entry.getKey());
            for (String copy : entry.getValue()) {
                System.out.println("\t" + copy);
            }
        }
    }

    static int levenshtein(String a, String b, int[] prev, int[] curr) {
        int la = a.length();
        int lb = b.length();

        if (la < lb) {
            String tmp = a; a = b; b = tmp;
            int t = la; la = lb; lb = t;
        }

        for (int j = 0; j <= lb; j++) prev[j] = j;

        for (int i = 1; i <= la; i++) {
            curr[0] = i;
            int rowMin = curr[0];

            for (int j = 1; j <= lb; j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(Math.min(
                                curr[j - 1] + 1,
                                prev[j] + 1),
                        prev[j - 1] + cost);
                if (curr[j] < rowMin) rowMin = curr[j];
            }

            if (rowMin >= 10) return 10;

            int[] tmp = prev; prev = curr; curr = tmp;
        }

        return prev[lb];
    }

    static void collectJavaFiles(File dir, List<File> result) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) collectJavaFiles(f, result);
            else if (f.getName().endsWith(".java")) result.add(f);
        }
    }

    static String readFile(File file) {
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.IGNORE)
                    .onUnmappableCharacter(CodingErrorAction.IGNORE);
            return decoder.decode(ByteBuffer.wrap(bytes)).toString();
        } catch (IOException e) {
            return null;
        }
    }

    static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int len = text.length();
        while (i < len) {
            int lineEnd = i;
            while (lineEnd < len && text.charAt(lineEnd) != '\n') lineEnd++;
            if (lineEnd < len) lineEnd++;
            String line = text.substring(i, lineEnd);
            String trimmed = line.stripLeading();
            if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                sb.append(line);
            }
            i = lineEnd;
        }
        return sb.toString();
    }

    static String removeComments(String text) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int len = text.length();
        while (i < len) {
            char c = text.charAt(i);

            if (c == '"') {
                sb.append(c); i++;
                while (i < len) {
                    char ch = text.charAt(i);
                    sb.append(ch);
                    if (ch == '\\' && i + 1 < len) { i++; sb.append(text.charAt(i)); }
                    else if (ch == '"') break;
                    i++;
                }
                i++; continue;
            }

            if (c == '\'') {
                sb.append(c); i++;
                while (i < len) {
                    char ch = text.charAt(i);
                    sb.append(ch);
                    if (ch == '\\' && i + 1 < len) { i++; sb.append(text.charAt(i)); }
                    else if (ch == '\'') break;
                    i++;
                }
                i++; continue;
            }

            if (c == '/' && i + 1 < len && text.charAt(i + 1) == '/') {
                while (i < len && text.charAt(i) != '\n') i++;
                continue;
            }

            if (c == '/' && i + 1 < len && text.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < len) {
                    if (text.charAt(i) == '*' && text.charAt(i + 1) == '/') { i += 2; break; }
                    i++;
                }
                continue;
            }

            sb.append(c); i++;
        }
        return sb.toString();
    }

    static String collapseWhitespace(String text) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int len = text.length();
        while (i < len) {
            char c = text.charAt(i);
            if (c < 33) {
                sb.append(' ');
                while (i < len && text.charAt(i) < 33) i++;
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }
}