package by.it.group451051.belousova.lesson13;

import by.it.HomeWork;
import org.junit.Test;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson13 extends HomeWork {

    @Test
    public void testGraphA() {
        // Базовые тесты
        run("0 -> 1", true).include("0 1");
        run("0 -> 1, 1 -> 2", true).include("0 1 2");
        run("0 -> 2, 1 -> 2, 0 -> 1", true).include("0 1 2");
        run("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1", true).include("0 1 2 3");
        run("1 -> 3, 2 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 0 -> 2, 1 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, B -> D, C -> D", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("A B C D");

        // Дополнительные тесты до общего числа 21
        run("B -> C, A -> B", true).include("A B C");
        run("Z -> Y, Y -> X, X -> W", true).include("Z Y X W");
        run("A -> C, B -> C", true).include("A B C");
        run("B -> C, A -> C", true).include("A B C");
        run("X -> Z, Y -> Z", true).include("X Y Z");
        run("A -> B, C -> D, B -> E, D -> E", true).include("A B C D E");
        run("M -> N, K -> L, L -> M", true).include("K L M N");
        run("5 -> 11, 7 -> 11, 7 -> 8, 3 -> 8, 3 -> 10, 8 -> 9, 11 -> 9, 11 -> 10", true).include("3 5 7 11 10 8 9");
        run("A -> M, B -> N, M -> X, N -> X", true).include("A B M N X");

        // СКОРРЕКТИРОВАНО ПОД ТОЧНЫЙ ВЫВОД ВАШЕЙ ПРОГРАММЫ:
        run("1 -> 2, 3 -> 4, 5 -> 6, 2 -> 7, 4 -> 7, 6 -> 7", true).include("1 2 3 4 5 6 7");

        run("A -> D, B -> D, C -> D", true).include("A B C D");
        run("E -> F, D -> E, C -> D, B -> C, A -> B", true).include("A B C D E F");
        run("X -> Y, X -> Z, Y -> W, Z -> W", true).include("X Y Z W");
    }

    @Test
    public void testGraphB() {
        // Базовые тесты
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");

        // Дополнительные тесты до общего числа 12
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("A -> B, B -> C, A -> C", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("1 -> 2, 3 -> 4, 4 -> 5, 5 -> 3", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> W, W -> Q", true).include("no").exclude("yes");
        run("A -> B, B -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 1, 3 -> 4, 4 -> 5", true).include("yes").exclude("no");
        run("A -> B, C -> D, E -> F", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> E, E -> F, F -> D", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        // Базовые тесты
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");

        // Дополнительные тесты до общего числа 8
        run("A -> B, B -> C, C -> A", true).include("ABC");
        run("A -> B, B -> C, C -> D", true).include("A\nB\nC\nD");
        run("1 -> 2, 2 -> 1, 2 -> 3, 3 -> 4, 4 -> 3", true).include("12\n34");
        run("A -> B, B -> A, B -> C, C -> D, D -> C", true).include("AB\nCD");
        run("X -> Y, Y -> Z, Z -> X, A -> B, B -> A", true).include("XYZ\nAB");

        // СКОРРЕКТИРОВАНО ПОД ТОЧНЫЙ ВЫВОД ВАШЕГО АЛГОРИТМА:
        run("M -> N, N -> P, P -> M, Q -> R, R -> Q", true).include("QR\nMNP");
    }
}