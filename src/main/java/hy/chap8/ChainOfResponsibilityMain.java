package hy.chap8;

import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * 责任链模式
 */
public class ChainOfResponsibilityMain {

    public static void main(String[] args) {
        //传统方式：需要定义对象，new对象
        ProcessingObject<String> p1 = new HeaderTextProcessing();
        ProcessingObject<String> p2 = new SpellCheckerProcessing();
        p1.setSuccessor(p2);
        String result1 = p1.handle("Aren't labdas really sexy?!!");
        System.out.println(result1);


        //使用lambda表达式
        UnaryOperator<String> headerProcessing = (String text) -> "From Raoul, Mario and Alan: " + text;
        UnaryOperator<String> spellCheckerProcessing = (String text) -> text.replaceAll("labda","lambda");
        Function<String, String> pipeline = headerProcessing.andThen(spellCheckerProcessing);
        String result2 = pipeline.apply("Aren't labdas really sexy?!!");
        System.out.println(result2);

    }

    static private abstract class ProcessingObject<T> {
        protected ProcessingObject<T> successor; //下一个ProcessingObject<T>

        public void setSuccessor(ProcessingObject<T> successor) {
            this.successor = successor;
        }

        public T handle(T input) {
            T r = handleWork(input); //当前类的处理结果
            if (successor != null) {
                return successor.handle(r); //把处理结果传给下一个处理器
            }
            return  r;
        }

        abstract protected T handleWork(T input);
    }

    //定义一个责任链
    static private class HeaderTextProcessing extends ProcessingObject<String> {

        protected String handleWork(String text) {
            return "From Raoul, Mario and Alan: " + text;
        }
    }

    //定义下一个责任链
    static private class SpellCheckerProcessing extends ProcessingObject<String> {
        public String handleWork(String text) {
            return text.replaceAll("labda", "lambda");
        }
    }


}
