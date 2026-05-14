package by.it.group451051.sinenko.lesson14;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertTrue;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson14 {
    
    @Test(timeout = 5000)
    public void testStatesHanoiTowerC() {
        assertTrue(run("1").contains("1"));
        assertTrue(run("2").contains("1 2"));
        assertTrue(run("3").contains("1 2 4"));
        assertTrue(run("4").contains("1 4 10"));
        assertTrue(run("5").contains("1 4 8 18"));
        assertTrue(run("10").contains("1 4 38 64 252 324 340"));
        assertTrue(run("21").contains("1 4 82 152 1440 2448 14144 21760 80096 85120 116480 323232 380352 402556 669284"));
    }

    private String run(String input) {
        try {
            InputStream oldIn = System.in;
            PrintStream oldOut = System.out;

            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            System.setIn(in);
            System.setOut(new PrintStream(out));

            StatesHanoiTowerC.main(new String[]{});

            System.setIn(oldIn);
            System.setOut(oldOut);

            return out.toString().trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}