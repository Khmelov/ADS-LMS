package by.it.group451051.teterukov.lessons_09_15.lesson15;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

    public static void main(String[] args) throws Exception {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        List<File> javaFiles = new ArrayList<>();
        collectJavaFiles(new File(src), javaFiles);

        List<int[]> sizeIndexList = new ArrayList<>();
        List<String> paths = new ArrayList<>();
        List<Integer> sizes = new ArrayList<>();

        for (File file : javaFiles) {
            String content = readFile(file);
            if (content == null) continue;

            if (content.contains("@Test") || content.contains("org.junit.Test")) continue;

            content = removePackageAndImports(content);

            content = trimLow(content);

            int size = content.getBytes(StandardCharsets.UTF_8).length;

            String relativePath = file.getAbsolutePath().substring(src.length());

            sizeIndexList.add(new int[]{size, paths.size()});
            paths.add(relativePath);
            sizes.add(size);
        }

        sizeIndexList.sort((a, b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            return paths.get(a[1]).compareTo(paths.get(b[1]));
        });

        for (int[] entry : sizeIndexList) {
            System.out.println(entry[0] + " " + paths.get(entry[1]));
        }
    }

    static void collectJavaFiles(File dir, List<File> result) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                collectJavaFiles(f, result);
            } else if (f.getName().endsWith(".java")) {
                result.add(f);
            }
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

    static String trimLow(String text) {
        int start = 0;
        int end = text.length();
        while (start < end && text.charAt(start) < 33) start++;
        while (end > start && text.charAt(end - 1) < 33) end--;
        return text.substring(start, end);
    }
}