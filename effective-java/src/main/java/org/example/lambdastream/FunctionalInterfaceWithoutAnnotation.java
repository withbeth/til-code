package org.example.lambdastream;


@FunctionalInterface
public interface FunctionalInterfaceWithoutAnnotation {
    void something();

    default void defaultOp() {
        System.out.println("default method");
    }
}
