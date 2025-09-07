package by.it.group351051.barysiuk.lesson15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SourceScannerA {
    public static void main(String[] args) {
        Path userDir = Path.of(System.getProperty("user.dir"));
        if (!"src".equals(userDir.getFileName().toString())) {
            userDir = userDir.resolve("src");
        }

        if (!Files.exists(userDir) || !Files.isDirectory(userDir)) {
            System.err.println("Not found or not directory: " + userDir);
            return;
        }

        try (var walk = Files.walk(userDir)) {
            final Path finalUserDir = userDir;
            walk.filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String content = Files.readString(path);
                            if (!content.contains("@Test") && !content.contains("org.junit.Test")) {
                                Path relative = finalUserDir.relativize(path);
                                System.out.println(relative.toString());
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