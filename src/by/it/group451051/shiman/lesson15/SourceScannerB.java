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

public class SourceScannerB {

    private static class FileInfo {
        int size;
        String path;
        FileInfo(int size, String path) {
            this.size = size;
            this.path = path;
        }
    }

    public static void main(String[] args) {
        String srcDir = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(srcDir);

        List<FileInfo> fileInfos = new ArrayList<>();

        try (var walk = Files.walk(srcPath)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String content = Files.readString(p);

                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            String noComments = removeComments(content);
                            String noPkgImports = removePackageAndImports(noComments);
                            String trimmed = trimControlChars(noPkgImports);
                            String withoutEmpty = removeEmptyLines(trimmed);

                            int size = withoutEmpty.getBytes(java.nio.charset.StandardCharsets.UTF_8).length;
                            String relativePath = srcPath.relativize(p).toString();

                            fileInfos.add(new FileInfo(size, relativePath));
                        } catch (MalformedInputException e) {
                            // игнорируем
                        } catch (IOException e) {
                            // игнорируем
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Исправленная сортировка с явным указанием типа
        fileInfos.sort(Comparator.comparingInt((FileInfo a) -> a.size)
                .thenComparing(a -> a.path));

        for (FileInfo info : fileInfos) {
            System.out.println(info.size + " " + info.path);
        }
    }

    private static String removeComments(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder out = new StringBuilder();
        int n = s.length();
        int i = 0;
        boolean inString = false;
        boolean inChar = false;
        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean escape = false;

        while (i < n) {
            char c = s.charAt(i);
            char next = (i + 1 < n) ? s.charAt(i + 1) : 0;

            if (!inString && !inChar && !inLineComment && !inBlockComment) {
                if (c == '/' && next == '/') {
                    inLineComment = true;
                    i += 2;
                    continue;
                }
                if (c == '/' && next == '*') {
                    inBlockComment = true;
                    i += 2;
                    continue;
                }
            }

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    out.append('\n');
                } else if (c == '\r') {
                    inLineComment = false;
                    out.append('\r');
                    if (i + 1 < n && s.charAt(i + 1) == '\n') {
                        out.append('\n');
                        i++;
                    }
                }
                i++;
                continue;
            }

            if (inBlockComment) {
                if (c == '*' && next == '/') {
                    inBlockComment = false;
                    i += 2;
                    continue;
                }
                i++;
                continue;
            }

            if (!inString && !inChar && c == '"') {
                inString = true;
                out.append(c);
                i++;
                continue;
            }
            if (!inString && !inChar && c == '\'') {
                inChar = true;
                out.append(c);
                i++;
                continue;
            }

            if (inString) {
                out.append(c);
                if (!escape && c == '"') {
                    inString = false;
                }
                escape = (c == '\\' && !escape);
                i++;
                continue;
            }

            if (inChar) {
                out.append(c);
                if (!escape && c == '\'') {
                    inChar = false;
                }
                escape = (c == '\\' && !escape);
                i++;
                continue;
            }

            out.append(c);
            i++;
        }
        return out.toString();
    }

    private static String removePackageAndImports(String s) {
        String[] lines = s.split("\\R");
        StringBuilder sb = new StringBuilder();
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

    private static String trimControlChars(String s) {
        if (s == null || s.isEmpty()) return s;
        int start = 0;
        while (start < s.length() && s.charAt(start) < 33) start++;
        int end = s.length() - 1;
        while (end >= start && s.charAt(end) < 33) end--;
        return s.substring(start, end + 1);
    }

    private static String removeEmptyLines(String s) {
        if (s == null || s.isEmpty()) return s;
        String[] lines = s.split("\\R");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                sb.append(line).append("\n");
            }
        }
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}