package by.it.group351051.barysiuk.lesson15;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

public class SourceScannerC {
    public static void main(String[] args) {
        Path userDir = Path.of(System.getProperty("user.dir"));
        if (!userDir.getFileName().toString().equals("src")) {
            userDir = userDir.resolve("src");
        }
        if (!Files.exists(userDir) || !Files.isDirectory(userDir)) {
            System.err.println("Not found or not directory: " + userDir);
            return;
        }

        List<Path> allJava = new ArrayList<>();
        try (var walk = Files.walk(userDir)) {
            walk.filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String text = Files.readString(p);
                            if (!text.contains("@Test") && !text.contains("org.junit.Test")) {
                                allJava.add(p);
                            }
                        } catch (MalformedInputException mie) {
                            // некорректная кодировка - пропускаем
                        } catch (IOException e) {
                            // прочие ошибки - пропускаем
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Path path : allJava) {
            Path rel = userDir.relativize(path);
            System.out.println(rel);
        }
    }
}