    package by.it.group451051.kozakov.lesson13;

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
            run("A -> B, B -> C, C -> D, D -> E", true).include("A B C D E");
            run("1 -> 3, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 6", true).include("1 2 3 4 5 6");
            run("A -> C, B -> C, C -> D, D -> E, E -> F, B -> D", true).include("A B C D E F");
            run("P -> Q, Q -> R, R -> S, S -> T, P -> S", true).include("P Q R S T");
            run("1 -> 2, 2 -> 4, 3 -> 4, 4 -> 5, 1 -> 3", true).include("1 2 3 4 5");
            run("A -> B, A -> C, B -> D, C -> D, D -> E, E -> F", true).include("A B C D E F");
            run("X -> A, X -> B, A -> C, B -> C, C -> Y", true).include("X A B C Y");
            run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
            run("A -> B, B -> C, C -> D, A -> D, D -> E", true).include("A B C D E");
            run("P -> Q, P -> R, Q -> S, R -> S, S -> T", true).include("P Q R S T");
            run("A -> B, A -> C, B -> D, C -> E, D -> F, E -> F, F -> G, G -> H, B -> E", true).include("A B C D E F G H");
            run("P -> Q, P -> R, Q -> S, R -> T, S -> U, T -> U, U -> V, Q -> R", true).include("P Q R S T U V");
            run("X -> A, X -> B, A -> C, B -> D, C -> E, D -> E, E -> F, F -> G, A -> D", true).include("X A B C D E F G");
        }

        @Test
        public void testGraphB() {
            run("0 -> 1", true).include("no").exclude("yes");
            run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
            run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
            run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
            run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
            run("X -> Y, Y -> Z, Z -> W, W -> X", true).include("yes").exclude("no");
            run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
            run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 3", true).include("yes").exclude("no");
            run("P -> Q, Q -> R, R -> S, S -> P", true).include("yes").exclude("no");
            run("A -> B, B -> C, C -> D, D -> E, E -> C", true).include("yes").exclude("no");
            run("X -> Y, Y -> Z, Z -> W, W -> V, V -> Y", true).include("yes").exclude("no");
            run("A -> B, B -> C, C -> D, D -> E, E -> A", true).include("yes").exclude("no");
        }

        @Test
        public void testGraphC() {
            run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                    .include("123\n456");
            run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                    .include("C\nABDHI\nE\nFGK");
            run("A -> B, B -> C, C -> A, C -> D, D -> E, E -> F, F -> D", true).include("ABC\nDEF");
            run("1 -> 2, 2 -> 3, 3 -> 1, 3 -> 4, 4 -> 5, 5 -> 6, 6 -> 4, 6 -> 7", true).include("123\n456\n7");
            run("X -> Y, Y -> Z, Z -> X, Z -> W, W -> V, V -> W", true).include("XYZ\nVW");
            run("A -> B, B -> C, C -> D, D -> B, D -> E, E -> F, F -> G, G -> F", true).include("A\nBCD\nE\nFG");
            run("P -> Q, Q -> R, R -> P, R -> S, S -> T, T -> U, U -> S", true).include("PQR\nSTU");
            run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2, 4 -> 5, 5 -> 6, 6 -> 7, 7 -> 8, 8 -> 6", true).include("1\n234\n5\n678");
        }


    }