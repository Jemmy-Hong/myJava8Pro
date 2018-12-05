package hy.chap14;

import java.util.function.Consumer;

public class PersistentTrainJourney {

    public static void main(String ... args) {

        TrainJourney tj1 = new TrainJourney(40, new TrainJourney(30, null));
        TrainJourney tj2 = new TrainJourney(20, new TrainJourney(50, null));

        TrainJourney appended = append(tj1, tj2);
        visit(appended, tj -> { System.out.print(tj.price + " - "); });
        System.out.println();

        //lamdba表达式的方式，规避了副作用
        // A new TrainJourney is created without altering tj1 and tj2.
        TrainJourney appended2 = append(tj1, tj2);
        visit(appended2, tj -> { System.out.print(tj.price + " - "); });
        System.out.println();

        //对象被改变，但是对外部不可见
        // tj1 is altered but it's still not visible in the results.
        TrainJourney linked = link(tj1, tj2);
        visit(linked, tj -> { System.out.print(tj.price + " - "); });
        System.out.println();

        //由于改变了对象状态，引起了死循环
        // ... but here, if this code is uncommented, tj2 will be appended
        // at the end of the already altered tj1. This will cause a
        // StackOverflowError from the endless visit() recursive calls on
        // the tj2 part of the twice altered tj1.
        /*TrainJourney linked2 = link(tj1, tj2);
        visit(linked2, tj -> { System.out.print(tj.price + " - "); });
        System.out.println();*/

    }

    static class TrainJourney {
        public int price;
        public TrainJourney onward;

        public TrainJourney(int p, TrainJourney t) {
            price = p;
            onward = t;
        }
    }

    static TrainJourney link(TrainJourney a, TrainJourney b) {
        if (a == null) {
            return b;
        }
        TrainJourney t = a;
        while (t.onward != null) {
            t = t.onward;
        }
        t.onward = b;
        return a;
    }

    static TrainJourney append(TrainJourney a, TrainJourney b) {
        return a == null ? b : new TrainJourney(a.price, append(a.onward, b));
    }

    /**
     * 使用函数式来实现上个方法
     * @param journey
     * @param c
     */
    static void visit(TrainJourney journey, Consumer<TrainJourney> c) {
        if (journey != null) {
            c.accept(journey);
            visit(journey.onward, c);
        }
    }
}
