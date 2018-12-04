package hy.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Test {

    public static void main(String ... args) {

        //一下内容是java7的新特性

        int int_num = 1_00_00_000;
        System.out.println("int num: " + int_num);

        long long_num = 1_00_00_000;
        System.out.println("long num: " + long_num);

        float float_num = 2.10_001F;
        System.out.println("float num: " + float_num);

        double double_num = 2.10_12_001;
        System.out.println("double num: " + double_num);

        //获得自己电脑的处理器数量
        System.out.println(Runtime.getRuntime().availableProcessors());

        IntStream.range(0, "jie mao wan wan".length()).mapToObj("jie mao wan wan"::charAt).forEach(
          c -> System.out.print(c)
        );
        System.out.println("test line...");
        System.out.println(Long.MAX_VALUE/1_000_000);

        testList();
    }

    public static void testList(){
        List<Integer> nums = Arrays.asList(1,4,9,13);
        List<Integer> subList = nums.subList(1,nums.size());
        System.out.println(subList);
    }

}
