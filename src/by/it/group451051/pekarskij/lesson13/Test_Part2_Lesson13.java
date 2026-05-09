package by.it.group451051.pekarskij.lesson13;

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
        // дополнительные тесты
        run("0 -> 1, 2 -> 3", true).include("0 1 2 3");
        run("0 -> 1, 2 -> 3, 4 -> 5", true).include("0 1 2 3 4 5");
        run("0 -> 1, 0 -> 2, 0 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, A -> D, A -> E", true).include("A B C D E");
        run("1 -> 4, 2 -> 4, 3 -> 4", true).include("1 2 3 4");
        run("0 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("0 -> 2, 0 -> 3, 1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4", true).include("0 1 2 3 4");
        run("1 -> 0, 2 -> 0, 3 -> 0, 4 -> 0", true).include("1 2 3 4 0");
        run("0 -> 3, 1 -> 3, 2 -> 3, 3 -> 4, 3 -> 5", true).include("0 1 2 3 4 5");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4", true).include("0 1 2 3 4");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 6", true).include("0 1 2 3 4 5 6");
        run("A1 -> A2, A1 -> B1, A2 -> B2, B1 -> B2", true).include("A1 A2 B1 B2");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        // дополнительные тесты
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3", true).include("no").exclude("yes");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        run("0 -> 1, 2 -> 3, 4 -> 5", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 2, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        // дополнительные тесты
        run("0->1, 1->2, 2->0, 2->3, 3->4, 4->5, 5->3, 5->6, 6->7, 7->6", true)
                .include("012\n345\n67");
        run("A->B, B->C, C->A, D->E, E->F, F->D", true)
                .include("ABC")
                .include("DEF");
        run("A->B, B->A, C->D, D->C, E->F, F->E, A->C, C->E", true)
                .include("AB")
                .include("CD")
                .include("EF");
        run("0->1, 1->2, 2->0, 2->3, 3->4, 4->3", true)
            .include("012")
            .include("34");
        run("A->B, B->C, C->D, D->E, E->C, D->F, F->G, G->F, G->H, H->I, I->G", true)
                .include("A")
                .include("B")
                .include("CDE")
                .include("FG")
                .include("HI");
        run("0->1, 1->2, 2->0, 1->3, 3->4, 4->5, 5->3, 2->4, 0->5, 6->7, 7->8, 8->6, 5->7", true)
                .include("012")
                .include("345")
                .include("678");
    }


}