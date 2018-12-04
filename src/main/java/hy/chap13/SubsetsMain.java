package hy.chap13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SubsetsMain {

    public static void main(String ... args) {
        List<List<Integer>> subs = subsets(Arrays.asList(1, 4, 9));
        subs.forEach(System.out::println);
    }

    public static List<List<Integer>> subsets(List<Integer> l) {
        if (l.isEmpty()) {
            List<List<Integer>> ans = new ArrayList<>();
            ans.add(Collections.emptyList());
            return ans;
        }

        Integer first = l.get(0);
        List<Integer> rest = l.subList(1,l.size());
        List<List<Integer>> subans = subsets(rest);
        List<List<Integer>> sunans2 = insertAll(first, subans);
        return contact(subans, sunans2);
    }

    /**
     * 使用copyList 避免了对输入参数lists的修改
     * @param first
     * @param lists
     * @return
     */
    public static List<List<Integer>> insertAll(Integer first, List<List<Integer>> lists) {
        List<List<Integer>> result = new ArrayList<>();
        for (List<Integer> l : lists) {
            List<Integer> copyList = new ArrayList<>(); //输入参数不必增加first这个元素
            copyList.add(first);
            copyList.addAll(l);
            result.add(copyList);
        }
        return result;
    }

    static List<List<Integer>> contact(List<List<Integer>> a, List<List<Integer>> b) {
        //使用新的变量链接两个值,不对输入的参数 a 和 b 做任何的更改
        List<List<Integer>> r = new ArrayList<>(a);
        r.addAll(b);
        return r;
    }

}
