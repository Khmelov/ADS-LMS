package by.it.group351051.samsonova.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи с вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

/*числа Фиббоначи: 0, 1, 1, 2, 3, 5, 8, 13 и т.д.*/
/*F(n) = F(n-1) + F(n-2) при n >= 2*/
public class FiboB {

    private long startTime = System.currentTimeMillis();

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    public static void main(String[] args) {

        //вычисление чисел простым быстрым методом
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }



    BigInteger fastB(Integer n) {
        //здесь нужно реализовать вариант с временем O(n) и памятью O(n)
        /*необходимо проверить n<2 и n=0*/
        if (n==0) return BigInteger.ZERO;
        if (n==1) return BigInteger.ONE;
        //далее необходимо создать массив fibonacci, в котором будут заполняться числа от 0 до n

        BigInteger[] fibonacci = new BigInteger[n+1];
        fibonacci[0] = BigInteger.ZERO;
        fibonacci[1] = BigInteger.ONE;
        //заполню массив по формуле фибоначчи
        //F(n) = F(n-1) + F(n-2) при n >= 2
        //необходимо использовать метод add, так как в случае с большими числами
        //нельзя использовать арифметические операциии типа "+"
       for (int i = 2; i <= n; i++) {
           fibonacci[i] = fibonacci[i-1].add(fibonacci[i-2]);
       }

       //результат:
            return fibonacci[n];
    }

}

