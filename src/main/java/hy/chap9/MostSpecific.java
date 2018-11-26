package hy.chap9;

public class MostSpecific {

    public static void main(String[] args) {
        new C().hello();//调用B. B继承A，因此B更加具体.
        new E().hello();//调用B. D是实现类，更加具体，因此调用D，而D中方法的实现在B中，因为它继承B.
        new G().hello();//调用F. F是实现类，最具体，方法的实现也在具体的类中.
    }

    static interface A {
        public default void hello() {
            System.out.println("Hello from A");
        }
    }

    static interface B extends A {
        public default void  hello() {
            System.out.println("Hello from B");
        }
    }

    static class C implements B, A {}

    static class D implements A{}

    static class E extends D implements B, A{}

    static class F implements B, A {
        public void hello() {
            System.out.println("Hello from F");
        }
    }

    static class G extends F implements B, A{}
}
