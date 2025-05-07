package by.it.group351051.belsky.lesson15;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.util.*;

/*
Создайте класс SourceScannerC с методом main,
который читает все файлы *.java из каталога src и его подкаталогов.

Каталог можно получить так:
        String src = System.getProperty("user.dir")
                       + File.separator + "src" + File.separator;

Файлы, содержащие в тексте @Test или org.junit.Test (тесты)
не участвуют в обработке.

В каждом тексте файла необходимо:
1. Удалить строку package и все импорты.
2. Удалить все комментарии за O(n) от длины текста.
3. Заменить все последовательности символов с кодом <33 на 32 (один пробел), т.е привести текст к строке.
4. Выполнить trim() для полученной строки.

В полученном наборе текстов:
1. Найти наиболее похожие тексты по метрике "расстояние Левенштейна",
   и определить копия ли это, считая копиями тексты с числом правок <10.
2. Если текст имеет копию(и), то вывести путь файла этого текста
   и в следующих строках путь(и) к копии(ям).
3. Повторить для всех файлов с копиями,
   при выводе сортировать файлы лексикографически по их пути.

Найдите способ корректно обрабатывать ошибки MalformedInputException

Оптимизируйте производительность решения (это может интересно).

Все операции не должны ничего менять на дисках (разрешено только чтение).
Работа не имеет цели найти плагиат, поэтому не нужно менять коды своих программ.

 */

public class SourceScannerC {
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

        // Список для хранения путей всех подходящих .java файлов
        List<Path> allJava = new ArrayList<>();

        try (var walk = Files.walk(userDir)) {
            // Проходим по всем файлам в директории и поддиректориях
            walk.filter(p -> p.toString().endsWith(".java"))  // Фильтруем только .java файлы
                    .forEach(p -> {
                        try {
                            // Читаем содержимое каждого файла
                            String text = Files.readString(p);

                            // Если файл не содержит аннотацию @Test или импорт org.junit.Test
                            if (!text.contains("@Test") && !text.contains("org.junit.Test")) {
                                allJava.add(p);  // Добавляем файл в список
                            }
                        } catch (MalformedInputException mie) {
                            // Ошибка кодировки — пропускаем этот файл
                        } catch (IOException e) {
                            // Прочие ошибки — пропускаем этот файл
                        }
                    });
        } catch (IOException e) {
            // Логируем ошибку, если возникла проблема при обходе директорий
            e.printStackTrace();
        }

        // После завершения обхода выводим относительные пути файлов, которые не содержат @Test
        for (Path path : allJava) {
            // Получаем относительный путь файла по отношению к директории src
            Path rel = userDir.relativize(path);
            // Выводим относительный путь файла
            System.out.println(rel);
        }
    }
}
