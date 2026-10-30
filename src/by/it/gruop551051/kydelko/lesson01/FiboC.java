package by.it.gruop551051.kydelko.lesson01;

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
        long period = pisano(m);
        long r = n % period;

        long a = 0, b = 1;               // F(0), F(1)
        for (long i = 0; i < r; i++) {
            long c = (a + b) % m;
            a = b;
            b = c;
        }
        return a;                        // F(r) mod m




    }
    private long pisano(int m) {
        long a = 0, b = 1;
        for (long i = 0; ; i++) {
            long c = (a + b) % m;
            a = b;
            b = c;
            if (a == 0 && b == 1) {      // вернулись к началу
                return i + 1;
            }
        }
    }

}

