package by.it.group451051.pekarskij.lesson15;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {
    // хранит результат обработки одного файла
    static class FileStat {
        int size; String path;
        FileStat(int s, String p) { size = s; path = p; }
    }

    public static void main(String[] args) throws IOException {
        String srcDir = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(srcDir);
        List<FileStat> list = new ArrayList<>();

        // обходим все файлы в src
        Files.walk(srcPath).filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".java")).forEach(path -> {
            try {
                String content;
                try {
                    content = Files.readString(path, StandardCharsets.UTF_8);
                } catch (CharacterCodingException e) {
                    return; // игнорируем MalformedInputException при несовместимой кодировке
                }

                // пропускаем тесты
                if (content.contains("@Test") || content.contains("org.junit.Test")) return;

                // удаляем package и import за один проход
                StringBuilder sb = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new StringReader(content))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String t = line.stripLeading();
                        if (!t.startsWith("package ") && !t.startsWith("import ")) {
                            sb.append(line).append('\n');
                        }
                    }
                }

                // убираем символы < 33 с краев
                String txt = sb.toString();
                int l = 0, r = txt.length();
                while (l < r && txt.charAt(l) < 33) l++;
                while (r > l && txt.charAt(r - 1) < 33) r--;

                String res = txt.substring(l, r);
                int bytes = res.getBytes(StandardCharsets.UTF_8).length;
                list.add(new FileStat(bytes, srcPath.relativize(path).toString()));
            } catch (IOException e) { /* игнорируем ошибки чтения */ }
        });

        // сортировка: по размеру, затем по пути
        list.sort((a, b) -> a.size != b.size ? Integer.compare(a.size, b.size) : a.path.compareTo(b.path));
        for (FileStat f : list) System.out.println(f.size + " " + f.path);
    }
}