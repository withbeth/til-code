package org.example.lambdastream;

public class Item42 {

    public static void foo(FunctionalInterfaceWithoutAnnotation bar) {
        bar.something();
    }

    public static void main(String[] args) {
        // @FunctionalInterface가 없어도,
        // 디폴트메서드가 존재해도,
        // 추상메서드를 하나만 갖는다면 FunctionalInterface로 인식되어,
        // 람다를 통해 동작 파라미터화를 할수 있는지 검증
        Item42.foo(() -> System.out.println("call from lambda"));
    }
}
