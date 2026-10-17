package by.it.holupova.lesson01;

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

        long pisPeriod = getPisPeriod(m);   //ищу период Пизано для m

        long eqN = n % pisPeriod;  //нахожу n в пределах периода

        return fibMod(eqN, m); //вычисляю число Фибоначчи по m для eqN
    }
        //метод для поиска периода Пизано
        private long getPisPeriod(int m) {
            long a = 0, b = 1, c;
            for (int i = 0; i < 6 * m; i++) { //период не превышает 6m
                c = (a+b) % m; //новое число Фибоначчи
                a = b;
                b = c;
                if (a==0 && b==1)  {
                    return i+1;
                }
            }
            return 6L * m; //для подстраховки вернули верхнюю границу
        }
              //метод вычисления остатка
        private long fibMod(long n, int m) {
        if (n == 0) return 0;
        if (n == 1) return 1 % m;

        long a = 0, b= 1, c = 0;
        for (long i = 2; i <= n; i++) {
            c = (a+b) % m;
            a = b;
            b = c;
        }
        return c;
    }


}

