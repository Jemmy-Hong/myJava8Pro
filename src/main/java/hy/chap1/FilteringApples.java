package hy.chap1;

import hy.entity.Apple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class FilteringApples {

    public static void main(String[] args) {

        List<Apple> inventory = Arrays.asList(new Apple(80,"green"),
                                              new Apple(155,"green"),
                                              new Apple(120, "red"));

        //获取绿苹果(传统方式)
        //正确结果： [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        //List<Apple> greenApples = filterApples(inventory, FilteringApples::isGreenApple);
        //System.out.println(greenApples);

        //获取重的苹果(传统方式)
        //正确结果： [Apple{color='green', weight=155}]
        //List<Apple> heavyApples = filterApples(inventory, FilteringApples::isHeavyApple);
        //System.out.println(heavyApples);

        //获取绿苹果(使用lamda表达式)
        //正确结果： [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        //List<Apple> greenApples2 = filterApples(inventory, (Apple a) -> "green".equals(a.getColor()));
        //System.out.println(greenApples2);

        //获取重苹果(使用lamda表达式)
        //正确结果： [Apple{color='green', weight=155}]
        //List<Apple> heavyApples2 = filterApples(inventory, (Apple a) -> a.getWeight() > 150);
        //System.out.println(heavyApples2);

        //获取重量小于80，颜色是棕色的苹果
        List<Apple> weirdApples = filterApples(inventory, (Apple a) -> a.getWeight() < 80 || "brown".equals(a.getColor()));
        System.out.println(weirdApples);

    }

    public static List<Apple> filterGreenApples(List<Apple> inventory){
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if ("green".equals(apple.getColor())){
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterHeavyApples(List<Apple> inventory){
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory){
            if (apple.getWeight() > 150) {
                result.add(apple);
            }
        }
        return result;
    }

    public static boolean isGreenApple(Apple apple) {
        return "green".equals(apple.getColor());
    }

    public static boolean isHeavyApple(Apple apple) {
        return apple.getWeight() > 150;
    }


    public static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p){
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (p.test(apple)){
                result.add(apple);
            }
        }

        return result;

    }


}
