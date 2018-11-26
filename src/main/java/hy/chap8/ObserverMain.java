package hy.chap8;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察着模式的改进
 */
public class ObserverMain {

    public static void main(String ... args) {
        //使用传统模式,需要new新对象，对象当然也要提前定义
        Feed f = new Feed();
        f.registerObserver(new NYTimes());
        f.registerObserver(new Guardian());
        f.registerObserver(new LeMonde());
        f.notifyObservers("The queen said her favourite book is Java 8 in Action!");

        //使用lamdba表达式的方式，不用new对象
        Feed feedLambda = new Feed();

        //直接注册行为
        feedLambda.registerObserver((String tweet) -> {
            if(tweet != null && tweet.contains("money")){
                System.out.println("Breaking news in NY! " + tweet); }
        });

        feedLambda.registerObserver((String tweet) -> {
            if(tweet != null && tweet.contains("queen")){
                System.out.println("Yet another news in London... " + tweet); }
        });

        feedLambda.notifyObservers("Money money money, give me money!");
    }


    /**
     * 定义观察者，去监控一个新闻发布主题
     */
    interface Observer {
        void inform(String tweet);//观察者去通知，有新闻发布
    }

    /**
     * 新闻发布主题，提供两个功能：1.让关注的人来注册  2.有新闻上线，就通知已经注册的人
     */
    interface Subject{
        void registerObserver(Observer o);
        void notifyObservers(String tweet);
    }

    //创造第一个观察者，关注纽约财政新闻
    static private class NYTimes implements Observer {
        @Override
        public void inform(String tweet) {
            if (tweet != null && tweet.contains("money")) {
                System.out.println("Breaking news in NY!" + tweet);
            }
        }
    }

    //创造第二个观察者，关注英国王室新闻
    static private class Guardian implements Observer {
        @Override
        public void inform(String tweet) {
            if (tweet != null && tweet.contains("queen")) {
                System.out.println("Yet another news in London... " + tweet);
            }
        }
    }

    //创造第三个观察者，关注日用品信息
    static private class LeMonde implements Observer{
        @Override
        public void inform(String tweet) {
            if(tweet != null && tweet.contains("wine")){
                System.out.println("Today cheese, wine and news! " + tweet);
            }
        }
    }

    //创造一个主题，用于注册订阅者，并发布相关消息给他们
    static private class Feed implements Subject{
        private final List<Observer> observers = new ArrayList<>();

        @Override
        public void registerObserver(Observer o) {
            this.observers.add(o);
        }

        @Override
        public void notifyObservers(String tweet) {
            observers.forEach(o -> o.inform(tweet));
        }
    }

}
