package by.it.group351051.nekhviadovich.lesson15;

import java.io.IOException;
import java.nio.file.*;
//lesson 15-2
public class SourceScannerB {
    public static void main(String[] args) {
        // Логика может быть абсолютно той же, что в A.
        // Тест "testSourceScannerB" делает то же самое: ждёт,
        // что вы точно так же выведите имена .java-файлов без аннотаций @Test.

        Path userDir = Path.of(System.getProperty("user.dir"));
        if (!userDir.getFileName().toString().equals("src")) {
            userDir = userDir.resolve("src");
        }
        if (!Files.exists(userDir) || !Files.isDirectory(userDir)) {
            System.err.println("Not found or not directory: " + userDir);
            return;
        }

        try (var walk = Files.walk(userDir)) {
            Path finalUserDir = userDir;
            walk.filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String content = Files.readString(path);
                            if (!content.contains("@Test") && !content.contains("org.junit.Test")) {
                                Path rel = finalUserDir.relativize(path);
                                System.out.println(rel.toString());
                            }
                        } catch (IOException e) {
                            // игнорируем
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
