package by.it.group451051.naumchik.lesson01;

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

    /**
     * Вычисляет n-е число Фибоначчи по модулю m за O(π(m)) времени.
     * Использует период Пизано.
     *
     * @param n номер числа Фибоначчи (1 <= n <= 1e18)
     * @param m модуль (2 <= m <= 1e5)
     * @return F_n mod m
     */
    long fasterC(long n, int m) {
        if (m == 1) {
            return 0; // любое число по модулю 1 равно 0, хотя m >= 2 по условию
        }

        // Находим период Пизано для m
        long period = findPisanoPeriod(m);

        // Приводим n к эквивалентному индексу внутри периода
        long r = n % period;

        // Вычисляем F_r mod m итеративно
        return fibMod(r, m);
    }

    /**
     * Находит период Пизано для заданного m.
     * Период - это наименьшее положительное k, такое что F_k ≡ 0 (mod m) и F_{k+1} ≡ 1 (mod m).
     *
     * @param m модуль
     * @return длина периода Пизано
     */
    private long findPisanoPeriod(int m) {
        long prev = 0;      // F_0 mod m
        long curr = 1;      // F_1 mod m
        long period = 0;

        // Ищем повторение пары (0, 1)
        for (long i = 0;; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;

            // Если нашли (0, 1), то период найден
            if (prev == 0 && curr == 1) {
                period = i + 1;
                break;
            }
        }
        return period;
    }

    /**
     * Вычисляет F_n mod m итеративно для небольших n (n < периода Пизано).
     *
     * @param n индекс (неотрицательный)
     * @param m модуль
     * @return F_n mod m
     */
    private long fibMod(long n, int m) {
        if (n == 0) return 0;
        if (n == 1) return 1 % m;

        long prev = 0;
        long curr = 1;
        for (long i = 2; i <= n; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }
        return curr;
    }


}

