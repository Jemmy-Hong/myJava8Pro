package hy.chap6;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collector.Characteristics.*;

public class PartitionPrimeNumbers {

    public static void main(String ... args) {
        System.out.println("Numbers partitioned in prime and non-prime: " + partitionPrimes(100));
        System.out.println("Numbers partitioned in prime and non-prime: " + partitionPrimesWithCustomCollector(100));
    }

    //把自然数n前面的质数都列出来
    public static Map<Boolean, List<Integer>> partitionPrimes(int n) {
        return IntStream.rangeClosed(2, n).boxed()
                .collect(partitioningBy(candidate -> isPrime(candidate)));
    }

    //判断质数的方法
    public static boolean isPrime(int candidate) {
        return IntStream.rangeClosed(2, candidate -1)
                .limit((long) Math.floor(Math.sqrt((double) candidate)) - 1)   //取平方根以前的数字，缩小范围
                .noneMatch(i -> candidate % i == 0);  //看看平方根以前的数字能不能被candidate整除
    }

    //使用自定义的收集器判断质数
    public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollector(int n) {
        return IntStream.rangeClosed(2, n).boxed().collect(new PrimeNumbersCollector());
    }


    //优化后的判断质数的方法
    public static boolean isPrime(List<Integer> primes, Integer candidate) {
        double candidateRoot = Math.sqrt((double) candidate);
        return takeWhile(primes, i -> i<= candidateRoot).stream().noneMatch(i -> candidate % i == 0);
        //return primes.stream().takeWhile(i -> i <= candidateRoot).noneMatch(i -> candidate % i == 0);//这个方法只有java9才能通过编译
    }

    /**
     * 给定一个判断条件p,一旦符合，就返符合条件那个数以前的数字
     */
    public static <A> List<A> takeWhile(List<A> list, Predicate<A> p){
        int i = 0;
        for (A item : list) {
            if (!p.test(item)) {
                return  list.subList(0, i);
            }
            i++;
        }
        return list;
    }



    //自定义收集器

    public static class PrimeNumbersCollector implements Collector<Integer,Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {

        //创建收集器: 创建了一个HashMap收集器，并初始化了两个ArrayList准备存放元素
        @Override
        public Supplier<Map<Boolean, List<Integer>>> supplier() {
            return () -> new HashMap<Boolean, List<Integer>>(){{
                put(true, new ArrayList<Integer>());
                put(false, new ArrayList<Integer>());
            }};
        }

        //实现规约过程(实际上是分组)，isPrime返回了true和false，这样就实现了分组
        @Override
        public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
            return (Map<Boolean, List<Integer>> acc, Integer candidate) -> {
                acc.get(isPrime(acc.get(true), candidate)).add(candidate);
            };
        }

        //把分流计算的结果汇总到一起
        @Override
        public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
            return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
                map1.get(true).addAll(map2.get(true)); //把后一个结果全部加到上一个结果的后面
                map1.get(false).addAll(map2.get(false));
                return map1;
            };
        }

        //combiner的结果就是我们需要的，所以这个finisher不用做任何转换
        @Override
        public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
            return i -> i;
        }

        //给自己的收集器定义一些提示：有序，优化，指示已经完成...
        @Override
        public Set<Characteristics> characteristics() {
            return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH));
        }

        public Map<Boolean, List<Integer>> partitionPrimesWithInlineCollector(int n) {
            return Stream.iterate(2, i -> i + 1).limit(n)
                        .collect(
                                () -> new HashMap<Boolean, List<Integer>>(){{  //收集器
                                    put(true, new ArrayList<Integer>());
                                    put(false, new ArrayList<Integer>());
                                }},
                                (acc, candidate) -> {    //规约过程
                                    acc.get( isPrime(acc.get(true),candidate) ).add(candidate);
                                },
                                (map1, map2) -> {    //汇总结果
                                    map1.get(true).addAll(map2.get(true));
                                    map1.get(false).addAll(map2.get(false));
                                }
                        );
        }
    }

}
