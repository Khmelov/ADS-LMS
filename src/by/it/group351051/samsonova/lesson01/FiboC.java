package by.it.group351051.samsonova.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m.
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 10;
        int m = 2;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    long fasterC(long n, int m) {
        //Решение сложно найти интуитивно
        //возможно потребуется дополнительный поиск информации
        //см. период Пизано


        //числа могут быть слишком большими, алгоритму понадобится много времени
        //чтобы пройти через все числа
        //задача в том, как сделать так, чтобы не проходить через все числа
        //решение заключается в периоде (последовательности)
        //формула: F(n) mod m = F(n%период) mod m
        //свойства: F(0) mod m = 0
        // F(1) mod m = 1
        //первое, что нужно сделать, это вычислить период Пизано
        //длина периода пизано:
        int periodPisano = getPeriodPisano(m);

        //уменьшить n с помощью периода
        int reducedN = (int) (n % periodPisano);

        return getFibonacciMod(reducedN, m);
    }

    private int getPeriodPisano(int m) {
        //начинать нужно с первых двух остатков: F(0) % m и F(1) % m по свойству
        int previous = 0;
        int current = 1;

        //цикл
        for (int i = 0; i < m * m; i++) { //период не может быть больше m*m
            int next = (previous + current) % m; //остаток следующего числа
            previous = current; //сдвиг остатков
            current = next;     //новый остаток - текущий

            //если встретили 0 и 1, то тогда найдена длина
            if (previous == 0 && current == 1) {
                return i + 1; //длина периода
            }
        }
        return 0;
    }

    private long getFibonacciMod(int n, int m) {
        //если n = 0, то F(0) = 0
        if (n == 0) return 0;

        //если n = 1, то F(1) = 1
        if (n == 1) return 1;

        //нач. значения для итерации по свойству
        long previous = 0;
        long current = 1;

        //нужно вычислить остатки для всех чисел от F(2) до F(n)
        for (int i = 2; i <= n; i++) {
            long next = (previous + current) % m;
            previous = current;
            current = next;
        }

        //остаток от F(n) % m
        return current;
    }

}

