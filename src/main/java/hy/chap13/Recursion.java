package hy.chap13;

import java.util.stream.LongStream;

public class Recursion {

    public static void main(String ... args) {
        System.out.println(factorialIterative(5));
        System.out.println(factorialRecursive(5));
        System.out.println(factorialStreams(5));
        System.out.println(factorialTailRecursive(5));
    }

    /**
     * 使用迭代 (不存在副作用)
     * @param n
     * @return
     */
    public static int factorialIterative(int n) {
        int r = 1;
        for (int i = 1; i <= n; i++) {
            r*=i;
        }
        return r;
    }

    /**
     *使用递归，不断的压入方法栈，比较耗资源，而且有副作用(改变了n的值)
     * @param n
     * @return
     */
    public static long factorialRecursive(long n) {
        return n == 1 ? n : n * factorialRecursive(n-1);
    }

    /**
     * 使用Stream，形式上没有副作用。
     * @param n
     * @return
     */
    public static long factorialStreams(long n) {
        return LongStream.rangeClosed(1, n)
                .reduce(1, (long a, long b) -> a * b);
    }

    /**
     * 使用不压方法栈的递归:尾部调用
     * @param n
     * @return
     */
    public static long factorialTailRecursive(long n) {
        return factorialHelper(1, n);
    }
    public static long factorialHelper(long acc, long n) {
        return n == 1 ? acc : factorialHelper(acc * n, n-1);
    }
}
