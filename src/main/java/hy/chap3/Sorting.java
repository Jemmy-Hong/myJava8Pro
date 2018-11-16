package hy.chap3;

import hy.entity.Apple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import static java.util.Comparator.comparing;

public class Sorting {

    public static void main(String ... args) {

        List<Apple> inventory = new ArrayList<>();
        inventory.addAll(Arrays.asList(new Apple(80,"green"), new Apple(155, "green"), new Apple(120, "red")));

        //sort方法需要一个借口的实现，而接口的实现是可以用lamdba表达式的

        // 1
        //第一种方式：直接new接口的实例
        inventory.sort(new AppleComparator());
        System.out.println(inventory);

        // reshuffling things a little
        inventory.set(1, new Apple(30, "green"));

        // 2
        // 第二种方式：使用匿名函数
        // [Apple{color='green', weight=30}, Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        inventory.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple a1, Apple a2) {
                return a1.getWeight().compareTo(a2.getWeight());
            }
        });
        System.out.println(inventory);

        // reshuffling things a little
        inventory.set(1, new Apple(20, "red"));

        // 3
        // 第三种方式：使用lamdba表达式
        // [Apple{color='red', weight=20}, Apple{color='green', weight=30}, Apple{color='green', weight=155}]
        inventory.sort((a1,a2) -> a1.getWeight().compareTo(a2.getWeight()));
        System.out.println(inventory);

        // reshuffling things a little
        inventory.set(1, new Apple(10, "red"));

        //4
        // 第四种方式：使用lamdba表达式的方法传递方式
        // [Apple{color='red', weight=10}, Apple{color='red', weight=20}, Apple{color='green', weight=155}]
        inventory.sort(comparing(Apple::getWeight));
        System.out.println(inventory);

    }

    static class AppleComparator implements Comparator<Apple> {
        @Override
        public int compare(Apple a1, Apple a2) {
            return a1.getWeight().compareTo(a2.getWeight());
        }
    }

}
