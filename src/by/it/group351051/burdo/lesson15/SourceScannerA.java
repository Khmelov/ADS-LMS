package by.it.group351051.burdo.lesson15;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * Класс для обхода всех файлов в каталоге `src` и его подкаталогах,
 * с последующей фильтрацией Java-файлов, не содержащих тестовых аннотаций.
 *
 * Файлы с аннотациями @Test или содержащие "org.junit.Test" в тексте
 * не обрабатываются и исключаются из вывода.
 */
public class SourceScannerA {

    /**
     * Главный метод программы. Ищет все Java-файлы без тестов
     * и выводит их относительные пути в консоль.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        // Определяем рабочий каталог
        Path srcDir = Path.of(System.getProperty("user.dir"));

        // Если текущая директория — не "src", добавляем подкаталог "src"
        if (!"src".equals(srcDir.getFileName().toString())) {
            srcDir = srcDir.resolve("src");
        }

        // Проверка, что каталог существует и это действительно директория
        if (!Files.isDirectory(srcDir)) {
            System.err.println("Исходная папка не найдена: " + srcDir);
            return;
        }

        // Рекурсивный обход всех файлов и вывод подходящих путей
        try (Stream<Path> paths = Files.walk(srcDir)) {
            paths
                    .filter(SourceScannerA::isValidJavaFile)   // фильтруем только нужные Java-файлы
                    .map(srcDir::relativize)                   // получаем относительный путь
                    .sorted()                                  // сортируем для стабильного вывода
                    .forEach(System.out::println);             // выводим
        } catch (IOException e) {
            System.err.println("Ошибка при обходе директории: " + e.getMessage());
        }
    }

    /**
     * Проверяет, является ли файл допустимым Java-файлом без тестовых аннотаций.
     *
     * @param path путь к файлу
     * @return {@code true}, если файл имеет расширение .java и не содержит тестовых аннотаций
     */
    private static boolean isValidJavaFile(Path path) {
        // Проверяем расширение файла
        if (!path.toString().endsWith(".java")) return false;

        try {
            // Читаем содержимое файла в UTF-8
            String content = Files.readString(path, StandardCharsets.UTF_8);
            // Ищем аннотации, указывающие на тестовый файл
            return !(content.contains("@Test") || content.contains("org.junit.Test"));
        } catch (IOException e) {
            // Игнорируем файлы, которые не удалось прочитать
            return false;
        }
    }
}
