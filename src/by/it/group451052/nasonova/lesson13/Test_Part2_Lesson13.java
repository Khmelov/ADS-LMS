package by.it.group451052.nasonova.lesson13;

import by.it.HomeWork;
import org.junit.Test;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson13 extends HomeWork {

    @Test
    public void testGraphA() {
        run("0 -> 1", true).include("0 1");
        run("0 -> 1, 1 -> 2", true).include("0 1 2");
        run("0 -> 2, 1 -> 2, 0 -> 1", true).include("0 1 2");
        run("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1", true).include("0 1 2 3");
        run("1 -> 3, 2 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 0 -> 2, 1 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, B -> D, C -> D", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("A B C D");
        run("A -> C, B -> C", true).include("A B C");
        run("A -> D, B -> D, C -> D", true).include("A B C D");
        run("A -> B, C -> D", true).include("A B C D");
        run("X -> Y, A -> B", true).include("A B X Y");
        run("A -> B, A -> C, B -> E, C -> D", true).include("A B C D E");
        run("3 -> 5, 1 -> 2, 2 -> 4", true).include("1 2 3 4 5");
        run("AA -> AB, AA -> AC, AB -> AD, AC -> AD", true).include("AA AB AC AD");
        run("b -> c, a -> c", true).include("a b c");
        run("M -> N, M -> O, N -> P, O -> P, P -> Q", true).include("M N O P Q");
        run("0 -> 3, 1 -> 3, 2 -> 4", true).include("0 1 2 3 4");
        run("Z -> a, Y -> a", true).include("Y Z a");
        run("A -> E, B -> E, C -> F, D -> F", true).include("A B C D E F");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("A -> B, C -> D", true).include("no").exclude("yes");
        run("A -> A", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0, 3 -> 4", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> X, A -> B", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> E, E -> F", true).include("no").exclude("yes");
        run("K -> L, L -> M, M -> K, M -> N", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("A->B, B->C", true)
                .include("A\nB\nC");
        run("A->B, B->C, C->A", true)
                .include("ABC");
        run("A->B, B->A, B->C, C->D, D->C", true)
                .include("AB\nCD");
        run("1->2, 2->1, 2->3, 3->4, 4->5, 5->3", true)
                .include("12\n345");
        run("A->B, B->C, C->B, C->D", true)
                .include("A\nBC\nD");
        run("A->B, B->A, B->C, C->D, D->E, E->D", true)
                .include("AB\nC\nDE");
    }

}