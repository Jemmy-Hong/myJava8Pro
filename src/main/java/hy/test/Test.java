package hy.test;

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

    }

}
