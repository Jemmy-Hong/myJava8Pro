package hy.chap7;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WordCount {

    public static final String SENTENCE =
            " Nel   mezzo del cammin  di nostra  vita " +
                    "mi  ritrovai in una  selva oscura" +
                    " che la  dritta via era   smarrita ";

    public static void main(String[] args) {
        System.out.println("Found " + countWordsIteratively(SENTENCE) + " words"); //普通的迭代计算，不分流
        System.out.println("Found " + countWords(SENTENCE) + " words"); //分流计算
    }

    //迭代计算String的长度
    public static int countWordsIteratively(String s) {
        int counter = 0;
        boolean lastSpace = true;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {//如果是空格就设置成true
                lastSpace = true;
            }else {
                if (lastSpace) counter++;
                lastSpace = Character.isWhitespace(c); //如果不是空格就设置成false
            }
        }
        return counter;
    }


    public static int countWords(String s) {
       // Stream<Character> stream = IntStream.range(0, s.length()).mapToObj(SENTENCE::charAt).parallel();

        WordCounterSpliterator spliterator = new WordCounterSpliterator(s);
        Stream<Character> stream = StreamSupport.stream(spliterator, true); //并行处理
        return countWords(stream);
    }


    private static int countWords(Stream<Character> stream) {
        WordCounter wordCounter = stream.reduce(new WordCounter(0, true),
                WordCounter::accumulate, //分流计算
                WordCounter::combine); //计算组合
        return wordCounter.getCounter();
    }


    /**
     * 自定义的wordcounter
     */
    private static class WordCounter {
        private final int counter;
        private final boolean lastSpace;

        public WordCounter(int counter, boolean lastSpace) {
            this.counter = counter;
            this.lastSpace = lastSpace;
        }

        /**
         * 方法说明accumulate是用来处理单个字符的，当遇到空白的时候，就表明是处理到目前为止是一个字符串
         * 如果没遇到空格，就继续加1，来统计一句话中的字符个数。
         * @param c
         * @return
         */
        public WordCounter accumulate(Character c) { //递归方法
            if (Character.isWhitespace(c)) {//空白符的情况
                return lastSpace ? this : new WordCounter(counter, true); //空白符是最后一个字符，用当前类处理，不是的话new新类
            } else { //非空白符的情况
                return lastSpace ? new WordCounter(counter + 1, false) : this; //是最后一个字符，new新类，不是用当前类
            }
        }

        //组合结果
        public WordCounter combine(WordCounter wordCounter) {
            return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
        }

        public int getCounter() {
            return counter;
        }
    }

    /**
     * 自定义拆分器
     */
    private static class WordCounterSpliterator implements Spliterator<Character> {

        private final String string;
        private int currentChar = 0;

        public WordCounterSpliterator(String string) { //构造函数
            this.string = string;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Character> action) {//传入对字符串中，每个字符处理的方法
            action.accept(string.charAt(currentChar++));
            return currentChar < string.length();
        }

        @Override
        public Spliterator<Character> trySplit() {
            int currentSize = string.length() - currentChar;
            if (currentSize < 10) { //如果小于10个字符，就不要拆分任务了，直接用本类处理就好了
                return null;
            }

            for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) { //从一半的位置开始处理，但是一半的位置可能是字符不是空格
                if (Character.isWhitespace(string.charAt(splitPos))) { //如果是空格
                    Spliterator<Character> spliterator = new WordCounterSpliterator(string.substring(currentChar, splitPos));
                    currentChar = splitPos;
                    return spliterator;//不断的递归的处理过程
                }
            }

            return null;
        }

        @Override
        public long estimateSize() {
            return string.length() - currentChar;
        }

        @Override
        public int characteristics() {
            return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
        }
    }

}
