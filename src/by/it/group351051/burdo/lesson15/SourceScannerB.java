package by.it.group351051.burdo.lesson15;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Класс предназначен для обхода всех Java-файлов в папке проекта,
 * исключая файлы, содержащие тестовые аннотации.
 * Результатом работы программы является список относительных путей
 * ко всем валидным .java-файлам.
 */
public class SourceScannerB {

    /**
     * Точка входа в программу.
     * Выполняет поиск и фильтрацию исходных файлов по заданным критериям.
     *
     * @param args аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        Path base = resolveSrcPath();
        if (base == null) {
            System.err.println("Исходная папка не найдена.");
            return;
        }

        try (Stream<Path> files = Files.walk(base)) {
            files
                    .filter(SourceScannerB::isJavaSource)
                    .filter(SourceScannerB::notATestFile)
                    .map(base::relativize)
                    .sorted(Comparator.naturalOrder())
                    .forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Ошибка при обходе директории: " + e.getMessage());
        }
    }

    /**
     * Проверяет, является ли путь Java-файлом.
     *
     * @param path путь к файлу
     * @return true, если файл оканчивается на ".java"
     */
    private static boolean isJavaSource(Path path) {
        return path.toString().endsWith(".java");
    }

    /**
     * Проверяет, содержит ли файл тестовые аннотации.
     *
     * @param file путь к Java-файлу
     * @return true, если файл не содержит аннотаций тестирования
     */
    private static boolean notATestFile(Path file) {
        try {
            String text = Files.readString(file, StandardCharsets.UTF_8);
            return !(text.contains("@Test") || text.contains("org.junit.Test"));
        } catch (IOException e) {
            // Проблемы при чтении файла — просто исключаем его
            return false;
        }
    }

    /**
     * Определяет путь к папке src. Если рабочая директория уже является src, возвращается она.
     * Иначе добавляется подкаталог "src".
     *
     * @return путь к директории src или null, если она не существует
     */
    private static Path resolveSrcPath() {
        Path current = Path.of(System.getProperty("user.dir"));
        Path srcPath = current.getFileName().toString().equals("src") ? current : current.resolve("src");
        return Files.isDirectory(srcPath) ? srcPath : null;
    }
}
