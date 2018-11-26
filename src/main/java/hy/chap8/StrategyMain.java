package hy.chap8;

public class StrategyMain {

    public static void main(String ... args) {

        //cld school 需要创建对象
        Validator v1 = new Validator(new IsNumeric());
        System.out.println(v1.validate("aaaa"));
        Validator v2 = new Validator(new IsAllLowerCase());
        System.out.println(v2.validate("bbbb"));

        //with lambdas 不用创建对象,直接的lambda表达式就是接口实例
        Validator v3 = new Validator((String s) -> s.matches("\\d+"));
        System.out.println(v3.validate("aaaa"));
        Validator v4 = new Validator((String s) -> s.matches("[a-z]]+"));
        System.out.println(v4.validate("bbbb"));

    }

    //接口方法
    interface ValidationStrategy {
        public boolean execute(String s);
    }

    static private class IsAllLowerCase implements ValidationStrategy {
        @Override
        public boolean execute(String s) {
            return s.matches("[a-z]+");
        }
    }

    static private class IsNumeric implements ValidationStrategy {
        @Override
        public boolean execute(String s) {
            return s.matches("\\d+");
        }
    }

    static private class Validator{
       private final ValidationStrategy strategy;

        public Validator(ValidationStrategy v) {
            this.strategy = v;
        }

        public boolean validate(String s) {
            return strategy.execute(s);
        }
    }

}
