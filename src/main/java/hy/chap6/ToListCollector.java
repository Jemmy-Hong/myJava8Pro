package hy.chap6;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import static java.util.stream.Collector.Characteristics.*;


/**
 * 创建自定义的toList收集器
 */
public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

    //创建收集器
    @Override
    public Supplier<List<T>> supplier() {
        return () -> new ArrayList<T>(); //创建一个ArrayList用于接收
    }

    //规定规约过程
    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return (list, item) -> list.add(item);//给list不断添加一个新元素item
    }

    //把分流计算的结果合并起来
    @Override
    public BinaryOperator<List<T>> combiner() {//组合结果
        return (list1, list2) ->
        {
            list1.addAll(list2);
            return list1;
        };
    }

    //完成的时候，不用做转换操作
    @Override
    public Function<List<T>, List<T>> finisher() {
        return i -> i;
    }

    //给自己定义的收集器添加一些信息
    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH, CONCURRENT));
    }
}
