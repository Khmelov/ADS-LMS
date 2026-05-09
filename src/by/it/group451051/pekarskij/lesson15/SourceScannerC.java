package by.it.group451051.pekarskij.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) throws IOException {
        Path srcDir = Paths.get(System.getProperty("user.dir"), "src");
        if (!Files.exists(srcDir)) return;

        // храним очищенные тексты и пути к файлам
        Map<Path, String> cleanedMap = new LinkedHashMap<>();

        // обходим директорию и собираем подходящие файлы
        try (var stream = Files.walk(srcDir)) {
            stream.filter(Files::isRegularFile)
                  .filter(p -> p.toString().endsWith(".java"))
                  .forEach(p -> {
                      try {
                          String raw = readFile(p);
                          if (raw == null) return; // пропускаем файлы с битой кодировкой
                          if (raw.contains("@Test") || raw.contains("org.junit.Test")) return;

                          String cleaned = clean(raw);
                          if (!cleaned.isEmpty()) {
                              cleanedMap.put(p, cleaned);
                          }
                      } catch (Exception e) { /* игнорируем ошибки чтения */ }
                  });
        }

        // ищем пары файлов с расстоянием левенштейна < 10
        Map<Path, List<Path>> copies = new HashMap<>();
        List<Path> paths = new ArrayList<>(cleanedMap.keySet());
        int n = paths.size();

        for (int i = 0; i < n; i++) {
            Path p1 = paths.get(i);
            String s1 = cleanedMap.get(p1);
            for (int j = i + 1; j < n; j++) {
                Path p2 = paths.get(j);
                String s2 = cleanedMap.get(p2);

                // быстрая отсечка по разнице длин
                if (Math.abs(s1.length() - s2.length()) >= 10) continue;

                if (levenshteinBounded(s1, s2, 10) < 10) {
                    copies.computeIfAbsent(p1, k -> new ArrayList<>()).add(p2);
                    copies.computeIfAbsent(p2, k -> new ArrayList<>()).add(p1);
                }
            }
        }

        // сортируем файлы-источники лексикографически по пути
        List<Path> sortedKeys = new ArrayList<>(copies.keySet());
        sortedKeys.sort(Comparator.comparing(Path::toString));

        // выводим результат в требуемом формате
        for (Path key : sortedKeys) {
            System.out.println(key);
            for (Path copy : copies.get(key)) {
                System.out.println(copy);
            }
        }
    }

    // читаем файл, корректно обрабатывая ошибки кодировки
    static String readFile(Path p) throws IOException {
        try {
            return Files.readString(p, StandardCharsets.UTF_8);
        } catch (CharacterCodingException e) {
            try {
                return Files.readString(p, Charset.forName("CP1251"));
            } catch (CharacterCodingException e2) {
                // fallback на однобайтовую кодировку, которая не падает
                return new String(Files.readAllBytes(p), StandardCharsets.ISO_8859_1);
            }
        }
    }

    // выполняем все этапы очистки текста
    static String clean(String text) {
        // удаляем строки package и import
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = br.readLine()) != null) {
                String t = line.stripLeading();
                if (!t.startsWith("package ") && !t.startsWith("import ")) {
                    sb.append(line).append('\n');
                }
            }
        } catch (IOException e) { return ""; }

        // удаляем комментарии
        String noComments = removeComments(sb.toString());

        // заменяем последовательности символов < 33 на один пробел
        StringBuilder res = new StringBuilder();
        boolean spacePending = false;
        for (int i = 0; i < noComments.length(); i++) {
            char c = noComments.charAt(i);
            if (c < 33) {
                spacePending = true;
            } else {
                if (spacePending) {
                    res.append(' ');
                    spacePending = false;
                }
                res.append(c);
            }
        }
        // применяем trim
        return res.toString().trim();
    }

    // конечный автомат для удаления комментариев за O(n)
    static String removeComments(String src) {
        StringBuilder out = new StringBuilder();
        int i = 0, len = src.length();
        boolean inBlock = false, inString = false, inChar = false;

        while (i < len) {
            char c = src.charAt(i);
            char next = i + 1 < len ? src.charAt(i + 1) : 0;

            if (inBlock) {
                if (c == '*' && next == '/') { i += 2; inBlock = false; }
                else i++;
            } else if (inString) {
                out.append(c);
                if (c == '\\' && i + 1 < len) { out.append(src.charAt(i + 1)); i += 2; continue; }
                if (c == '"') inString = false;
                i++;
            } else if (inChar) {
                out.append(c);
                if (c == '\\' && i + 1 < len) { out.append(src.charAt(i + 1)); i += 2; continue; }
                if (c == '\'') inChar = false;
                i++;
            } else {
                if (c == '/' && next == '/') { i += 2; while (i < len && src.charAt(i) != '\n') i++; }
                else if (c == '/' && next == '*') { i += 2; inBlock = true; }
                else if (c == '"') { inString = true; out.append(c); i++; }
                else if (c == '\'') { inChar = true; out.append(c); i++; }
                else { out.append(c); i++; }
            }
        }
        return out.toString();
    }

    // оптимизированное расстояние левенштейна с ранним выходом
    static int levenshteinBounded(String s1, String s2, int max) {
        int n = s1.length(), m = s2.length();
        if (Math.abs(n - m) > max) return max + 1;

        int[] dp = new int[m + 1];
        for (int j = 0; j <= m; j++) dp[j] = j;

        for (int i = 1; i <= n; i++) {
            int prev = dp[0];
            dp[0] = i;
            int minInRow = i;

            for (int j = 1; j <= m; j++) {
                int temp = dp[j];
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[j] = Math.min(prev + cost, Math.min(dp[j] + 1, dp[j - 1] + 1));
                prev = temp;
                if (dp[j] < minInRow) minInRow = dp[j];
            }
            // если минимальное значение в строке превысило лимит, дальше считать нет смысла
            if (minInRow > max) return max + 1;
        }
        return dp[m];
    }
}