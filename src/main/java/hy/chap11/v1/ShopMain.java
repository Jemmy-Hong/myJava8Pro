package hy.chap11.v1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ShopMain {

    public static void main(String ... args) {
        Shop shop = new Shop("BestShop");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("my favourite prouct");
        long invocationTime = ((System.nanoTime() - start) /1_000_000);
        System.out.println("Invocation returned after " + invocationTime + " msecs");

        //DO some more tasks, like querying other shops
        doSomethingElse();

        try {
            Double price = futurePrice.get();
            System.out.printf("price is %.2f%n", price);
        } catch ( ExecutionException |InterruptedException e) {
            throw new RuntimeException();
        }

        long retrievalTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Price returned after " + retrievalTime + " msecs");

    }


    private static void doSomethingElse() {
        System.out.println("Doing something else...");
    }
}
