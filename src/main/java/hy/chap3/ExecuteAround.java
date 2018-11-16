package hy.chap3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExecuteAround {

    public static void main(String ... args) throws IOException{
        // method we want to refactor to make more flexible
        String result = processFileLimited();
        System.out.println(result);

        System.out.println("---");

        String oneline = processFile((BufferedReader b) -> b.readLine());
        System.out.println(oneline);

        String twoLines = processFile((BufferedReader b) -> b.readLine() + b.readLine());
        System.out.println(twoLines);


    }


    public static String processFileLimited() throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new FileReader("src\\main\\java\\hy\\chap3\\data.txt"))) {
            return br.readLine();

        }
    }

    public static String processFile(BuffererReaderProcessor p) throws  IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("src\\main\\java\\hy\\chap3\\data.txt"))){ //java7的新特性，括号里面的资源自动关闭
            return p.process(br);
        }
    }

    public interface BuffererReaderProcessor {
         String process(BufferedReader b) throws IOException;
    }
}
