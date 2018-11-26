package hy.chap9;

import java.util.Arrays;
import java.util.List;

public class Game {

    public static void main(String[] args) {
        List<Resizable> resizableShaps = Arrays.asList(new Square(), new Triangle(), new Ellipse());
        Utils.paint(resizableShaps);
    }

}
