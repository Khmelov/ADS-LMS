package by.it.group551051.shulvinski.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.math.BigInteger;

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    // Для решения этой задачи необходимо использовать период Пизано

    long getPisanoPeriod(int m){
        long prev = 0;
        long curr = 1;

        for(int i = 0; i < 6L * m; i++){
            long t = curr;
            curr = (curr + prev) % m; // вычисление следующего остатка последовательносии фибоначчи
            prev = t;

            if (prev == 0 && curr == 1){
                return i + 1; // последовательность остатков зациклилась - найден период
            }
        }
        return 0;
    }
    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации

        long pisanoPeriod = getPisanoPeriod(m); // нахождение периода пизано по модулю m

        n = n % pisanoPeriod; // сокращение до размера периода для оптимизации вычислений

        if (n < 2){
            return n; // базовый случай
        }
        long prev = 0;
        long curr = 1;

        for (long i = 0; i < n - 1; i++){
            long t = curr;
            curr = (curr + prev) % m; // вычисление числа фибоначчи по модулю m
            prev = t;
        }
        return curr; // результатом является остаток от деления
    }


}

