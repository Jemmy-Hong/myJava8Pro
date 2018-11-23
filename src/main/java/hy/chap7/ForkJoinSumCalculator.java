package hy.chap7;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

import static hy.chap7.ParallelStreamsHarness.FORK_JOIN_POOL;

/**
 * 自定义分支-合并框架
 */
public class ForkJoinSumCalculator extends RecursiveTask<Long> {

    public static final long THRESHOLD = 10_000;

    private final long[] numbers;
    private final int start;
    private final int end;

    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    public ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    /**
     * 框架核心方法
     * @return
     */
    @Override
    protected Long compute() {
        int length = end -start;
        if(length < THRESHOLD) { //如果长度小于10000，就直接计算，不要拆分任务了
            return computeSequentially();
        }

        //拆分任务
        //前半段
        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length/2);//先把任务对半分，刚开始的时候start是0
        leftTask.join();//拆分执行

        //后半段
        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length/2, end);
        Long rightResult = rightTask.compute();//执行
        Long leftResult = leftTask.join();

        //合并结果
        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }

    public static long forkJoinSum(long n) {
        long[] numbers = LongStream.rangeClosed(1, n).toArray();
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
        return FORK_JOIN_POOL.invoke(task);//用池子来执行任务
    }
}
