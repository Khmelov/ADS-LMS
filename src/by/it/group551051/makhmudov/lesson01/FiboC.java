package by.it.group551051.makhmudov.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

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

    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        if (n <= 0) {
            return 0L;
        }
        if (m == 1) {
            return 0L;
        }
        long period = pisanoPeriod(m);
        n = n % period;
        if (n == 0) {
            n = period;
        }
        long prev = 0L;
        long curr = 1L;
        for (long i = 2; i <= n; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }
        return curr;
    }

    private static long pisanoPeriod(int m) {
        long a = 0L;
        long b = 1L;
        long period = 0L;
        do {
            long c = (a + b) % m;
            a = b;
            b = c;
            period++;
        } while (a != 0L || b != 1L);
        return period;
    }


}

