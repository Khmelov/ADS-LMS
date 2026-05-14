package by.it.group451051.sinenko.lesson15;

import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson15Test {

    private static List<String> samples;

    @Test(timeout = 5000)
    public void testSourceScannerC() {
        run("").include("FiboA.java");
    }
    
    private Result run(String input) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        try {
            SourceScannerC.main(new String[]{});
        } catch (Exception e) {
        }
        
        System.setOut(originalOut);
        System.setIn(System.in);
        
        String output = outputStream.toString().trim();
        return new Result(output);
    }
    
    private static class Result {
        private final String output;
        
        Result(String output) {
            this.output = output;
        }
        
        Result include(String expected) {
            assertTrue("Ожидалось: " + expected + "\nПолучено: " + output,
                    output.contains(expected));
            return this;
        }
    }
    
    private static List<String> lazyWalk() {
        if (samples == null) {
            samples = new ArrayList<>();
            Path root = Path.of(System.getProperty("user.dir")
                                + File.separator + "src" + File.separator);
            try (var walk = Files.walk(root)) {
                walk.forEach(
                        p -> {
                            if (p.toString().endsWith(".java")) {
                                try {
                                    String s = Files.readString(p);
                                    if (!s.contains("@Test") && !s.contains("org.junit.Test")) {
                                        samples.add(root.relativize(p).toString());
                                    }
                                } catch (IOException e) {
                                    if (System.currentTimeMillis() < 0) {
                                        System.err.println(p);
                                    }
                                }
                            }
                        }
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return samples;
    }
}