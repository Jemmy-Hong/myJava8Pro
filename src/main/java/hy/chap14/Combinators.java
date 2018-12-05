package hy.chap14;

import java.util.function.Function;

public class Combinators {

    public static void main(String[] args) {

        System.out.println(repeat(3, (Integer x) -> 2 * x).apply(10));

    }

    /**
     * 组合方法：把方法g和f组合起来，返回另一个方法
     * @param g
     * @param f
     * @param <A>
     * @param <B>
     * @param <C>
     * @return
     */
    static <A, B, C> Function<A,C> compose(Function<B, C> g, Function<A, B> f){
        return x -> g.apply(f.apply(x));
    }

    /**
     * 递归获得质数
     * @param n
     * @param f
     * @param <A>
     * @return
     */
    static <A> Function<A, A> repeat(int n, Function<A, A> f) {
        return n == 0 ? x -> x : compose(f, repeat(n -1, f)); //x代表一个方法
    }
}
