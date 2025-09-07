package by.it.group351051.barysiuk.lesson15;

import java.io.IOException;
import java.nio.file.*;

public class SourceScannerB {
    public static void main(String[] args) {
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
                            // Ignore
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}