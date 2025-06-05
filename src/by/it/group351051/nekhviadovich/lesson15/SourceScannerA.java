package by.it.group351051.nekhviadovich.lesson15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
//lesson 15-1
public class SourceScannerA {
    public static void main(String[] args) {
        // 1) Определяем рабочую директорию.
        Path userDir = Path.of(System.getProperty("user.dir"));
        // Если папка не "src", то добавляем "src"
        if (!"src".equals(userDir.getFileName().toString())) {
            userDir = userDir.resolve("src");
        }

        // Проверяем, что каталог существует
        if (!Files.exists(userDir) || !Files.isDirectory(userDir)) {
            System.err.println("Not found or not directory: " + userDir);
            return;
        }

        // 2) Рекурсивно обходим файлы
        try (var walk = Files.walk(userDir)) {
            final Path finalUserDir = userDir;
            walk.filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            // Читаем содержимое
                            String content = Files.readString(path);
                            // Пропускаем файлы с аннотациями тестов
                            if (!content.contains("@Test") && !content.contains("org.junit.Test")) {
                                // Выводим путь, относительный от finalUserDir
                                Path relative = finalUserDir.relativize(path);
                                System.out.println(relative.toString());
                            }
                        } catch (IOException e) {
                            // Если не читается - игнорируем
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
