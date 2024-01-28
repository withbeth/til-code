package org.example.method;


import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// Item 55
public class OptionalSimulator {

    public static class Person {
        private Car car;
        public Car getCar() {
            return car;
        }
    }

    public static class Car {
        private Insurance insurance;
        public Insurance getInsurance() {
            return insurance;
        }
    }

    public static class Insurance {
        private String name;
        public String getName() {
            return name;
        }
    }

    // NPE 발생 가능
    public String getCarInsuranceName(Person person) {
        return person.getCar().getInsurance().getName();
    }

    // 매번 객체마다 null 체크
    public String getCarInsuranceNameWithNullCheck(Person person) {
        if (person == null) {
            return null;
        }
        Car car = person.getCar();
        if (car == null) {
            return null;
        }
        Insurance insurance = car.getInsurance();
        if (insurance == null) {
            return null;
        }
        return insurance.getName();
    }

    public static <E extends Comparable<E>> E maxOrException(Collection<E> c) {
        if (c.isEmpty()) {
            throw new IllegalArgumentException("collection can not be empty");
        }
        E result = null;
        for (E e : c) {
            if (result == null || e.compareTo(result) > 0) {
                result = Objects.requireNonNull(e);
            }
        }
        return result;
    }

    public static <E extends Comparable<E>> Optional<E> maxOrOptional(Collection<E> c) {
        if (c.isEmpty()) {
            return Optional.empty();
        }
        E result = null;
        for (E e : c) {
            if (result == null || e.compareTo(result) > 0) {
                result = Objects.requireNonNull(e);
            }
        }
        return Optional.of(result);
    }

    public static void main(String[] args) {
        System.out.println("maxOrOptional(List.of(1, 2,3)).orElseGet(()-> Integer.valueOf(-1)) = " + maxOrOptional(List.of(1, 2, 3)).orElseGet(() -> Integer.valueOf(-1)));
    }
}
