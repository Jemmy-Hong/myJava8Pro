package hy.chap6;

import java.util.*;

import static hy.chap6.Dish.menu;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

public class Partitioning {

    public static void main(String ... args) {
        System.out.println("Dishes partitioned by vegetarian： " + partitionByVegeterian());
        System.out.println("Vegetarian Dishes by type: " + vegetarianDishesByType());
        System.out.println("Most caloric dishes by vegetarian: " + mostCaloricPartitionedByVegetarian());
    }

    //按照素菜和非素菜来分区
    private static Map<Boolean, List<Dish>> partitionByVegeterian() {
        return menu.stream().collect(partitioningBy(Dish::isVegetarian));
    }

    //先分区再分组
    private static Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType() {
        return menu.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));
    }

    //找到每个分区中热量最高的菜
    private static Object mostCaloricPartitionedByVegetarian() {
        return menu.stream().collect(
                partitioningBy(Dish::isVegetarian,
                        collectingAndThen(maxBy(comparingInt(Dish::getCalories)),Optional::get))
        );
    }
}
