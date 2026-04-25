package by.it.group451051.naumchik.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        //вычисление чисел простым быстрым методом
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Вычисляет n-е число Фибоначчи за O(n) времени и O(n) памяти.
     * @param n индекс числа Фибоначчи (n >= 0)
     * @return число Фибоначчи F_n в виде BigInteger
     * @throws IllegalArgumentException если n < 0
     */
    BigInteger fastB(Integer n) {
        if (n == null || n < 0) {
            throw new IllegalArgumentException("n must be non-negative");
        }
        if (n == 0) {
            return BigInteger.ZERO;
        }
        if (n == 1) {
            return BigInteger.ONE;
        }

        // Вспомогательный массив для хранения всех чисел Фибоначчи от 0 до n
        BigInteger[] fib = new BigInteger[n + 1];
        fib[0] = BigInteger.ZERO;
        fib[1] = BigInteger.ONE;

        for (int i = 2; i <= n; i++) {
            fib[i] = fib[i - 1].add(fib[i - 2]);
        }

        return fib[n];
    }

}

