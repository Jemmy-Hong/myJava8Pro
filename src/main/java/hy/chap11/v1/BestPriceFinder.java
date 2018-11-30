package hy.chap11.v1;

import hy.chap11.ExchangeService;
import hy.chap11.ExchangeService.Money;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BestPriceFinder {

    private final List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll")/*,
            new Shop("ShopEasy")*/);

   //定长线程池
    private final Executor executor = Executors.newFixedThreadPool(shops.size(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    });

    //使用顺序流得到价格字符串
    public List<String> findPricesSequential(String product) {
        return shops.stream()
                .map(shop -> shop.getName() + " price is " + shop.getPrice(product))
                .collect(Collectors.toList());
    }

    //使用并行流得到字符串
    public List<String> findPricesParallel(String product) {
        return shops.parallelStream()
                .map(shop -> shop.getName() + " price is " + shop.getPrice(product))
                .collect(Collectors.toList());
    }

    //使用CompletableFuture实现异步处理并行流
    public List<String> findPricesFuture(String product) {
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getName() + " price is " + shop.getPrice(product), executor))
                .collect(Collectors.toList());

        List<String> prices = priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        return prices;
    }

    /**
     * 和并两个独立的CompletableFuture对象
     *
     * @param product
     * @return
     */
    public List<String> findPricesInUSD(String product) {
        List<CompletableFuture<Double>> pricesFutures = new ArrayList<>();
        for (Shop shop : shops) {
            // Start of Listing 10.20.
            // Only the type of futurePriceInUSD has been changed to
            // CompletableFuture so that it is compatible with the
            // CompletableFuture::join operation below.
            CompletableFuture<Double> futurePriceInUSD = CompletableFuture.supplyAsync(() -> shop.getPrice(product))
                    .thenCombine(CompletableFuture.supplyAsync( //合并上一个CompletableFuture产生的结果
                            () -> ExchangeService.getRate(ExchangeService.Money.EUR, ExchangeService.Money.USD)),
                            (price, rate) -> price * rate);
            pricesFutures.add(futurePriceInUSD);
        }
        // Drawback: The shop is not accessible anymore outside the loop,
        // so the getName() call below has been commented out.
        List<String> prices = pricesFutures.stream()
                .map(CompletableFuture::join)
                .map(price -> /*shop.getName() +*/ " price is " + price)
                .collect(Collectors.toList());
        return prices;
    }

    /**
     * 使用java7的分支合并框架，来实现上一个功能
     * @param product
     * @return
     */
    public List<String> findPricesInUSDJava7(String product) {
        //定义一个可缓存线程池
        ExecutorService excutor = Executors.newCachedThreadPool();

        //首次先处理汇率问题
        List<Future<Double>> priceFutures = new ArrayList<>();

        for (Shop shop: shops) {//循环处理每一个商品的价格，把处理的结果累加到上面的集合中
            final Future<Double> futureRate = excutor.submit(new Callable<Double>() {
                @Override
                public Double call() throws Exception {
                    return ExchangeService.getRate(Money.EUR, Money.USD);
                }
            });

            //这个Future来处理上一个Future产生的结果，并返回一个Future<Double>
            Future<Double> futurePriceInUSD = excutor.submit(new Callable<Double>() {
                @Override
                public Double call() throws Exception {
                    try {
                        double priceInEUR = shop.getPrice(product);
                        return priceInEUR * futureRate.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            });
            priceFutures.add(futurePriceInUSD);
        }

        //把价格的信息拿出来，处理成每个商品的字符串集合
        List<String> prices = new ArrayList<>();
        for (Future<Double> priceFuture : priceFutures) {
            try {
                prices.add(/*shop.getName() +*/  " price is  " + priceFuture.get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return prices;
    }

    /**
     * 这个方法在循环里用thenApply方法获得了name属性的值
     * @param product
     * @return
     */
    public List<String> findPricesInUSD2(String product){
        List<CompletableFuture<String>> priceFutures = new ArrayList<>();
        for (Shop shop: shops) {
            // Here, an extra operation has been added so that the shop name
            // is retrieved within the loop. As a result, we now deal with
            // CompletableFuture<String> instances.
            //一个额外的方法被添加进来，现在name的值在循环中能获得了。我们现在来处理CompletableFuture<String>实例
            CompletableFuture<String> futurePriceInUSD =
                    CompletableFuture.supplyAsync(() -> shop.getPrice(product))
                    .thenCombine(
                            CompletableFuture.supplyAsync(
                                    () -> ExchangeService.getRate(Money.EUR, Money.USD)),
                                    (price, rate) -> price * rate
                    ).thenApply(price -> shop.getName() + " price is " + price); //用这个方法就可以得到name的值了,前几个都不可以
            priceFutures.add(futurePriceInUSD);
        }

        List<String> prices = priceFutures
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        return prices;
    }

    /**
     *
     * @param product
     * @return
     */
    public List<String> findPricesInUSD3(String product) {
        //Here, the for loop has been replaced by a mapping function...
        //这里的loop循环被一个mapping方法替代
        Stream<CompletableFuture<String>> priceFuturesStream = shops
                .stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product))
                        .thenCombine(
                                CompletableFuture.supplyAsync(() -> ExchangeService.getRate(Money.EUR, Money.USD)),
                                (price, rate) -> price * rate)
                        .thenApply(price -> shop.getName() + " price is " + price));

        // However, we should gather the CompletableFutures into a List so that the asynchronous
        // operations are triggered before being "joined."
        // 虽然，我们应该把CompletableFutures聚集到一个List中，以便异步操作在joined之前被触发
        List<CompletableFuture<String>> priceFutures = priceFuturesStream.collect(Collectors.toList());
        List<String> prices = priceFutures
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        return prices;
    }


}
