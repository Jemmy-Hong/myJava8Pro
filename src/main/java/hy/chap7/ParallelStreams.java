package hy.chap7;

import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ParallelStreams {

    //传统的迭代方法计算
    public static long iterativeSum(long n) {
        long result = 0;
        for (int i = 0; i <= n; i++) {
            result += i;
        }
        return result;
    }

    //使用顺序流计算
    public static long sequentialSum(long n) {
        return Stream.iterate(1L, i -> i + 1).limit(n).reduce(Long::sum).get();
    }

    //使用并行流计算
    public static long parallelSum(long n){
        return Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(Long::sum).get();
    }

    //使用LongStream的rangeClosed方法预先界定数字范围
    public static long rangedSum(long n) {
        return LongStream.rangeClosed(1, n).reduce(Long::sum).getAsLong();
    }

    //规定范围并使用并行流
    public static long parallelRangedSum(long n) {
        return LongStream.rangeClosed(1, n).parallel().reduce(Long::sum).getAsLong();
    }

    //有副作用的累加方法
    public static long sideEffectSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).forEach(accumulator::add);
        return accumulator.total;
    }

    //带有副作用的并行流(结果不准确，foreach在访问流中元素的时候，改变了状态，而这个元素还要用)
    public  static long sideEffectParallelSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);//foreach方法每次都改变accumulator的状态
        return accumulator.total;
    }

    //创建一个累加器,只是简单把自己的属性累加
    public static class Accumulator {
        private long total = 0;

        public void add(long value) {
            total += value;
        }
    }
}
