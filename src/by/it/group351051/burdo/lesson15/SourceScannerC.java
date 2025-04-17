package by.it.group351051.burdo.lesson15;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Класс SourceScannerC осуществляет поиск всех Java-файлов в проекте (исключая тестовые),
 * и выводит пути к ним, соблюдая требования:
 * - Исключить из обработки файлы, содержащие аннотации тестирования.
 * - Гарантировать наличие в выводе имени файла FiboA.java (если он присутствует в структуре).
 */
public class SourceScannerC {

    /**
     * Главный метод, выполняющий рекурсивный обход и фильтрацию исходных файлов.
     *
     * @param args аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        Path basePath = locateSourceRoot();
        if (basePath == null) {
            System.err.println("Каталог src не найден.");
            return;
        }

        List<Path> candidates = collectCleanJavaFiles(basePath);
        candidates.stream()
                .map(basePath::relativize)
                .sorted(Comparator.naturalOrder())
                .forEach(System.out::println);
    }

    /**
     * Определяет и возвращает путь к каталогу src проекта.
     *
     * @return путь к src или null, если каталог не существует.
     */
    private static Path locateSourceRoot() {
        Path workingDir = Path.of(System.getProperty("user.dir"));
        Path srcDir = workingDir.endsWith("src") ? workingDir : workingDir.resolve("src");
        return Files.isDirectory(srcDir) ? srcDir : null;
    }

    /**
     * Выполняет поиск всех .java-файлов в проекте, исключая тестовые.
     * Обрабатываются только читаемые файлы, корректные по кодировке.
     *
     * @param root корневая директория src.
     * @return список путей ко всем подходящим Java-файлам.
     */
    private static List<Path> collectCleanJavaFiles(Path root) {
        List<Path> result = new ArrayList<>();

        try (Stream<Path> files = Files.walk(root)) {
            files.filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String code = Files.readString(path, StandardCharsets.UTF_8);
                            if (!code.contains("@Test") && !code.contains("org.junit.Test")) {
                                result.add(path);
                            }
                        } catch (MalformedInputException malformed) {
                            // Пропускаем файлы с проблемами кодировки
                        } catch (IOException io) {
                            // Прочие ошибки также игнорируем
                        }
                    });
        } catch (IOException e) {
            System.err.println("Ошибка при чтении дерева каталогов: " + e.getMessage());
        }

        return result;
    }
}
