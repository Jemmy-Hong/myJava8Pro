package hy.chap4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamVsCollection {

    public static void main(String ... args) {
        List<String> names = Arrays.asList("Java8","Lambdas", "In", "Action");
        Stream<String> s = names.stream();
        s.forEach(System.out::println);

        //下面的代码会报错因为stream只会被消费一次
        // uncommenting this line will result in an IllegalStateException
        // because streams can be consumed only once
        //s.forEach(System.out::println);
    }
}
