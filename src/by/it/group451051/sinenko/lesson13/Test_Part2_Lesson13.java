package by.it.group451051.sinenko.lesson13;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson13 {

    // Метод для запуска тестов для GraphA
    private Result runGraphA(String input) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        try {
            GraphA.main(new String[]{});
        } catch (Exception e) {
        }
        
        System.setOut(originalOut);
        System.setIn(System.in);
        
        String output = outputStream.toString().trim();
        return new Result(output);
    }
    
    // Метод для запуска тестов для GraphB
    private Result runGraphB(String input) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        try {
            GraphB.main(new String[]{});
        } catch (Exception e) {
        }
        
        System.setOut(originalOut);
        System.setIn(System.in);
        
        String output = outputStream.toString().trim();
        return new Result(output);
    }
    
    // Метод для запуска тестов для GraphC
    private Result runGraphC(String input) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        
        try {
            GraphC.main(new String[]{});
        } catch (Exception e) {
        }
        
        System.setOut(originalOut);
        System.setIn(System.in);
        
        String output = outputStream.toString().trim();
        return new Result(output);
    }
    
    //класс для проверки результатов тестов
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
        
        Result exclude(String unexpected) {
            assertTrue("Не ожидалось: " + unexpected + "\nПолучено: " + output,
                    !output.contains(unexpected));
            return this;
        }
    }
    
    // тесты для Graph A
    @Test
    public void testGraphA() {
        runGraphA("0 -> 1").include("0 1");
        runGraphA("0 -> 1, 1 -> 2").include("0 1 2");
        runGraphA("0 -> 2, 1 -> 2, 0 -> 1").include("0 1 2");
        runGraphA("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1").include("0 1 2 3");
        runGraphA("1 -> 3, 2 -> 3, 2 -> 3, 0 -> 1, 0 -> 2").include("0 1 2 3");
        runGraphA("0 -> 1, 0 -> 2, 0 -> 2, 1 -> 3, 1 -> 3, 2 -> 3").include("0 1 2 3");
        runGraphA("A -> B, A -> C, B -> D, C -> D").include("A B C D");
        runGraphA("A -> B, A -> C, B -> D, C -> D, A -> D").include("A B C D");
        runGraphA("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4").include("0 1 2 3 4");
        runGraphA("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4").include("0 1 2 3 4");
        runGraphA("A -> B, B -> C, C -> D, D -> E").include("A B C D E");
        runGraphA("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4").include("1 2 3 4");
    }
    
    //тесты для Graph B
    @Test
    public void testGraphB() {
        runGraphB("0 -> 1").include("no").exclude("yes");
        runGraphB("0 -> 1, 1 -> 2").include("no").exclude("yes");
        runGraphB("0 -> 1, 1 -> 2, 2 -> 0").include("yes").exclude("no");
        runGraphB("0 -> 1, 1 -> 2, 2 -> 3").include("no").exclude("yes");
        runGraphB("0 -> 1, 1 -> 2, 2 -> 1").include("yes").exclude("no");
        runGraphB("0 -> 1, 1 -> 0").include("yes").exclude("no");
        runGraphB("A -> B, B -> C, C -> A").include("yes").exclude("no");
        runGraphB("A -> B, B -> C, C -> D, D -> B").include("yes").exclude("no");
        runGraphB("0 -> 1, 2 -> 3").include("no").exclude("yes");
        runGraphB("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1").include("yes").exclude("no");
    }
    
    // тесты для Graph C
    @Test
    public void testGraphC() {
        runGraphC("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4")
                .include("123\n456");
        runGraphC("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G")
                .include("C\nABDHI\nE\nFGK");
        runGraphC("1->2, 2->3, 3->1")
                .include("123");
        runGraphC("1->2, 2->3, 3->4, 4->2")
                .include("1\n234");
        runGraphC("A->B, B->C, C->A, A->D, D->E, E->D")
                .include("ABC\nDE");
        runGraphC("0->1, 1->2, 2->0, 1->3, 3->4, 4->3")
                .include("012\n34");
        runGraphC("0->1, 1->2, 2->3, 3->0, 4->5, 5->4")
                .include("0123\n45");
    }
}