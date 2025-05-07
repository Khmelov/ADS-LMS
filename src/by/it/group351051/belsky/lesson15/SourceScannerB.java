package by.it.group351051.belsky.lesson15;

import java.io.IOException;
import java.nio.file.*;

public class SourceScannerB {
    public static void main(String[] args) {
        // Получаем текущую рабочую директорию
        Path userDir = Path.of(System.getProperty("user.dir"));

        // Если текущая рабочая директория не является папкой "src", то добавляем "src" в путь
        if (!userDir.getFileName().toString().equals("src")) {
            userDir = userDir.resolve("src");
        }

        // Проверяем, существует ли директория "src" и является ли она директорией
        if (!Files.exists(userDir) || !Files.isDirectory(userDir)) {
            System.err.println("Не найдена или не является директорией: " + userDir);
            return;  // Выход, если директория не найдена или не является директорией
        }

        try (var walk = Files.walk(userDir)) {
            // Закрываем файловую систему автоматически, используя try-with-resources
            Path finalUserDir = userDir;  // Для использования в лямбда-выражении

            // Проходим по всем файлам в директории и поддиректориях
            walk.filter(path -> path.toString().endsWith(".java"))  // Фильтруем только .java файлы
                    .forEach(path -> {
                        try {
                            // Читаем содержимое каждого файла
                            String content = Files.readString(path);

                            // Если файл не содержит аннотацию @Test или импорт org.junit.Test
                            if (!content.contains("@Test") && !content.contains("org.junit.Test")) {
                                // Получаем относительный путь файла по отношению к директории src
                                Path rel = finalUserDir.relativize(path);
                                // Выводим относительный путь файла
                                System.out.println(rel);
                            }
                        } catch (IOException e) {
                            // Игнорируем ошибки при чтении файла
                        }
                    });
        } catch (IOException e) {
            // Логируем ошибку, если возникла проблема при обходе директорий
            e.printStackTrace();
        }
    }
}
