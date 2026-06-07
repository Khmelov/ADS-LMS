package by.it.group451051.naumchik.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerC {

    // Вспомогательный класс для хранения информации о файле и его очищенном тексте
    private static class JavaFile {
        final String path;
        final String cleanedText;

        JavaFile(String path, String cleanedText) {
            this.path = path;
            this.cleanedText = cleanedText;
        }
    }

    public static void main(String[] args) {
        String srcPathStr = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(srcPathStr);

        if (!Files.exists(srcPath)) {
            return;
        }

        List<JavaFile> javaFiles = new ArrayList<>();

        // Настраиваем декодер для игнорирования MalformedInputException
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE);

        try (Stream<Path> walk = Files.walk(srcPath)) {
            List<Path> paths = walk
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .collect(Collectors.toList());

            for (Path path : paths) {
                try {
                    byte[] bytes = Files.readAllBytes(path);
                    String content = decoder.decode(ByteBuffer.wrap(bytes)).toString();

                    // Исключаем тестовые файлы
                    if (content.contains("@Test") || content.contains("org.junit.Test")) {
                        continue;
                    }

                    String cleaned = cleanCode(content);
                    javaFiles.add(new JavaFile(path.toAbsolutePath().toString(), cleaned));

                } catch (CharacterCodingException e) {
                    // Хэндлинг ошибок декодирования, если REPLACE не отработал
                } catch (IOException e) {
                    // Пропускаем проблемные файлы при чтении
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортируем файлы лексикографически по пути для корректного вывода
        javaFiles.sort(Comparator.comparing(f -> f.path));

        // Структура для хранения копий: файл -> список его копий
        Map<String, List<String>> copiesMap = new TreeMap<>();

        // Попарное сравнение файлов
        for (int i = 0; i < javaFiles.size(); i++) {
            JavaFile fileA = javaFiles.get(i);
            for (int j = 0; j < javaFiles.size(); j++) {
                if (i == j) continue;

                JavaFile fileB = javaFiles.get(j);

                // Оптимизация: если разница длин >= 10, расстояние Левенштейна точно >= 10
                if (Math.abs(fileA.cleanedText.length() - fileB.cleanedText.length()) >= 10) {
                    continue;
                }

                // Считаем точное расстояние Левенштейна с ограничением (bounded Levenshtein)
                if (getBoundedLevenshteinDist(fileA.cleanedText, fileB.cleanedText, 10) < 10) {
                    copiesMap.computeIfAbsent(fileA.path, k -> new ArrayList<>()).add(fileB.path);
                }
            }
        }

        // Вывод результатов
        for (Map.Entry<String, List<String>> entry : copiesMap.entrySet()) {
            System.out.println(entry.getKey());
            List<String> copies = entry.getValue();
            // Сортируем пути копий лексикографически
            Collections.sort(copies);
            for (String copyPath : copies) {
                System.out.println(copyPath);
            }
        }
    }

    /**
     * Очистка кода согласно бизнес-логике за O(n)
     */
    private static String cleanCode(String content) {
        // 1. Удаляем строки package и import
        // Регулярное выражение находит строки, начинающиеся с package/import и до точки с запятой
        String noImports = content.replaceAll("(?m)^\\s*(package|import)\\s+[^;]+;\\s*$", "");

        // 2. Удаляем комментарии (однострочные // и многострочные /* */) за O(n)
        String noComments = removeComments(noImports);

        // 3 & 4. Заменяем все символы < 33 (управляющие, пробелы, переносы) на код 32 (пробел)
        // Схлопываем идущие подряд пробелы в один, после чего делаем trim()
        char[] chars = noComments.toCharArray();
        StringBuilder sb = new StringBuilder(chars.length);
        boolean inSpace = false;

        for (char c : chars) {
            if (c < 33) {
                if (!inSpace) {
                    sb.append(' ');
                    inSpace = true;
                }
            } else {
                sb.append(c);
                inSpace = false;
            }
        }

        return sb.toString().trim();
    }

    /**
     * Удаление комментариев конечным автоматом за честный O(n) без тяжелых регулярных выражений
     */
    private static String removeComments(String code) {
        StringBuilder sb = new StringBuilder();
        int len = code.length();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        boolean inString = false;

        for (int i = 0; i < len; i++) {
            char c = code.charAt(i);

            if (inBlockComment) {
                if (c == '*' && i + 1 < len && code.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i++; // Пропускаем '/'
                }
            } else if (inLineComment) {
                if (c == '\n' || c == '\r') {
                    inLineComment = false;
                    sb.append(c); // Сохраняем перенос строки для корректной логики
                }
            } else if (inString) {
                sb.append(c);
                if (c == '\\' && i + 1 < len) {
                    sb.append(code.charAt(i + 1));
                    i++; // Пропускаем экранированный символ
                } else if (c == '"') {
                    inString = false;
                }
            } else {
                if (c == '/' && i + 1 < len && code.charAt(i + 1) == '/') {
                    inLineComment = true;
                    i++;
                } else if (c == '/' && i + 1 < len && code.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i++;
                } else {
                    sb.append(c);
                    if (c == '"') {
                        inString = true;
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * Оптимизированный алгоритм Левенштейна с ограничением по порогу (Threshold/Bounded)
     * Использует две строки вместо матрицы для экономии памяти O(min(M,N))
     */
    private static int getBoundedLevenshteinDist(String s, String t, int threshold) {
        if (s.equals(t)) return 0;

        int n = s.length();
        int m = t.length();

        if (Math.abs(n - m) >= threshold) return threshold;

        int[] p = new int[n + 1];
        int[] d = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (int j = 1; j <= m; j++) {
            char tJ = t.charAt(j - 1);
            d[0] = j;

            int min = d[0];
            for (int i = 1; i <= n; i++) {
                int cost = (s.charAt(i - 1) == tJ) ? 0 : 1;
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
                if (d[i] < min) {
                    min = d[i];
                }
            }

            // Если на текущей итерации минимальное число правок уже превысило лимит,
            // дальнейший расчет этой пары строк бессмысленен.
            if (min >= threshold) {
                return threshold;
            }

            int[] temp = p;
            p = d;
            d = temp;
        }

        return p[n];
    }
}
