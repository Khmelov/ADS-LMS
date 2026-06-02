package by.it.group451051.zhelubovskaya.lesson13;

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
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).
         // Дополнительные тесты (всего 20)
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4", true).include("0 1 2 3 4");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4", true).include("1 2 3 4");
        run("A -> B, B -> C, C -> D, D -> E", true).include("A B C D E");
        run("A -> C, A -> D, B -> C, B -> D", true).include("A B C D");
        run("X -> Y, X -> Z, Y -> W, Z -> W", true).include("X Y Z W");
        run("A -> B, A -> C, A -> D, B -> E, C -> E, D -> E", true).include("A B C D E");
        run("0 -> 5, 1 -> 5, 2 -> 5, 3 -> 5, 4 -> 5", true).include("0 1 2 3 4 5");
        run("A -> B, A -> C, B -> C", true).include("A B C");
        run("X -> Y, X -> Z, Y -> Z", true).include("X Y Z");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("0 -> 2, 0 -> 3, 1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4", true).include("0 1 2 3 4");
        run("A -> B, A -> C, B -> D, C -> D, D -> E", true).include("A B C D E");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
            // Дополнительные тесты (всего 12)
        run("1 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("A -> B, B -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("X -> Y, X -> Z, Y -> Z", true).include("no").exclude("yes");
        run("A -> B, A -> C, B -> D, C -> D, D -> A", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> E, E -> F", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 1", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");       
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
       
            // Дополнительные тесты 
        run("1->2, 2->3, 3->1", true)
            .include("123");

        run("1->2, 2->1", true)
            .include("12");

        run("1->2, 2->3, 3->2", true)
            .include("1\n23");

        run("1->2, 2->3, 3->4, 4->3", true)
            .include("1\n2\n34");

        run("A->B, B->A, B->C, C->B", true)
            .include("ABC");
        
        run("X->Y, Y->X, X->Z, Z->X", true)
            .include("XYZ");
    }
}