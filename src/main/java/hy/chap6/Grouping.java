package hy.chap6;

import java.util.*;

import static hy.chap6.Dish.dishTags;
import static hy.chap6.Dish.menu;
import static java.util.stream.Collectors.*;

public class Grouping {

    enum CaloricLevel { DIET, NORMAL, FAT };

    public static void main(String[] args) {

        //根据名字判断每个方法的名字

        System.out.println("Dishes grouped by type: " + groupDishesByType());
        System.out.println("Dish names grouped by type: " + groupDishNamesByType());
      //  System.out.println("Dish tags grouped by type: " + groupDishTagsByType());
        System.out.println("Caloric dishes grouped by type: " + groupCaloricDishesByType());
        System.out.println("Dishes grouped by caloric level: " + groupDishesByCaloricLevel());
        System.out.println("Dishes grouped by type and caloric level: " + groupDishedByTypeAndCaloricLevel());
        System.out.println("Count dishes in groups: " + countDishesInGroups());
        System.out.println("Most caloric dishes by type: " + mostCaloricDishesByType());//返回的是Optional
        System.out.println("Most caloric dishes by type: " + mostCaloricDishesByTypeWithoutOprionals()); //返回的是Dish
        System.out.println("Sum calories by type: " + sumCaloriesByType());
        System.out.println("Caloric levels by type: " + caloricLevelsByType());
    }

    //简单的groupingBy
    private static Map<Dish.Type, List<Dish>> groupDishesByType(){
        return menu.stream().collect(groupingBy(Dish::getType));
    }

    //分组后返回菜的名字
    private static Map<Dish.Type, List<String>> groupDishNamesByType(){
        return menu.stream().collect(groupingBy(Dish::getType,mapping(Dish::getName,toList())));
    }

    //这个方法需要java9的api，暂时不能通过编译
    /*private static Map<Dish.Type, Set<String>> groupDishTagsByType() {
        return menu.stream().collect(groupingBy(Dish::getType, flatMapping(dish -> dishTags.get( dish.getName() ).stream(), toSet())));
    }*/


    //第二种方法暂时不能通过编译
    private static Map<Dish.Type, List<Dish>> groupCaloricDishesByType(){
        return menu.stream().filter(dish -> dish.getCalories() > 500).collect(groupingBy(Dish::getType));
       // return menu.stream().collect(groupingBy(Dish::getType, filtering(dish -> dish.getCalories() > 500, toList())));
    }

    //根据热量水平分组
    private static Map<CaloricLevel, List<Dish>> groupDishesByCaloricLevel() {
        return menu.stream().collect(
                groupingBy(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <=700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                })
        );
    }

    //先根据类型分组，再根据热量水平分组
    private static Map<Dish.Type, Map<CaloricLevel, List<Dish>>> groupDishedByTypeAndCaloricLevel() {
        return menu.stream().collect(
                groupingBy(Dish::getType,
                            groupingBy((Dish dish) -> {
                                if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                                else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                                else return CaloricLevel.FAT;
                            })
                )
        );
    }

    //统计每个类型里面有几个菜
    private static Map<Dish.Type, Long> countDishesInGroups() {
        return menu.stream().collect(groupingBy(Dish::getType, counting()));
    }

    //获得每个菜下面热量最高的一道菜
    private static Map<Dish.Type, Optional<Dish>> mostCaloricDishesByType() {
        return menu.stream().collect(
                groupingBy(Dish::getType,
                        reducing((Dish d1, Dish d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)));
    }

    //不用Optional,直接获取每个类型下热量最高的菜
    private static Map<Dish.Type, Dish> mostCaloricDishesByTypeWithoutOprionals() {
        return menu.stream().collect(
                groupingBy(Dish::getType,
                        collectingAndThen(
                                reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2),
                                Optional::get)));
    }

    //计算每个类型中,菜的总热量
    private static Map<Dish.Type, Integer> sumCaloriesByType() {
        return menu.stream().collect(groupingBy(Dish::getType,
                summingInt(Dish::getCalories)));
    }

    //统计每个类型中，有几种热量类型（高，低，平常）
    private static Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType() {
        return menu.stream().collect(
                groupingBy(Dish::getType,mapping(
                        dish -> {
                            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT;
                        },
                        toSet()
                ))
        );
    }

}
