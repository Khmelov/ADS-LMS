package by.it.group451051.markhel.lesson13;

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

        // Дополнительные тесты (всего 20)
        run("1 -> 2, 2 -> 3, 3 -> 4", true).include("1 2 3 4");
        run("A -> B, B -> C, C -> D, D -> E", true).include("A B C D E");
        run("0 -> 2, 2 -> 4, 0 -> 1, 1 -> 3, 3 -> 4", true).include("0 1 2 3 4");
        run("1 -> 3, 2 -> 3, 2 -> 4, 3 -> 5, 4 -> 5", true).include("1 2 3 4 5");
        run("A -> B, A -> C, B -> D, C -> D, D -> E", true).include("A B C D E");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 4 -> 5", true).include("0 1 2 3 4 5");
        run("X -> Y, X -> Z, Y -> W, Z -> W", true).include("X Y Z W");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 6", true).include("1 2 3 4 5 6");
        run("0 -> 5, 0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("0 1 2 3 4 5");
        run("A -> C, B -> C, C -> D, D -> E, E -> F, A -> B", true).include("A B C D E F");
        run("1 -> 4, 2 -> 4, 3 -> 4, 4 -> 5, 5 -> 6", true).include("1 2 3 4 5 6");
        run("0 -> 3, 1 -> 3, 2 -> 3, 3 -> 4, 4 -> 5, 0 -> 1", true).include("0 1 2 3 4 5");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");

        // Дополнительные тесты (всего 12)
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 3", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 2, 2 -> 3, 3 -> 0", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");

        // Дополнительные тесты (всего 8)
        run("0->1, 1->0, 2->3, 3->2, 4->5, 5->4", true)
                .include("01\n23\n45");
        run("A->B, B->C, C->A, D->E, E->D, F->G, G->F, H->I, I->H", true)
                .include("ABC\nDE\nFG\nHI");
        run("1->2, 2->3, 3->1, 4->5, 5->4, 6->7, 7->6, 8->8", true)
                .include("123\n45\n67\n8");
        run("0->1, 1->2, 2->0, 3->4, 4->3, 5->5", true)
                .include("012\n34\n5");
        run("X->Y, Y->X, Z->Z, W->V, V->W", true)
                .include("VW\nXY\nZ");
        run("0->1, 1->2, 2->3, 3->0, 4->5, 5->6, 6->4, 7->8, 8->7, 9->9", true)
                .include("0123\n456\n78\n9");
    }
}