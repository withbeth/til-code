package org.example.method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Item50Test {

    private static class Person {
        private final String name;
        private int age;

        private Person(String name) {
            this.name = name;
        }
    }

    @Test
    void listCopyOfBehavior() {
        Person brian = new Person("brian");
        brian.age = 35;

        List<Person> mutableList = new ArrayList<>();
        mutableList.add(brian);

        brian.age = 40;


        Assertions.assertEquals(40, mutableList.get(0).age);


    }

}