package org.example.general;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

class Item62Test {

    private String getCompoundKey(String className, int seq) {
        return className + "#" + seq;
    }

    private String getClassNameFromKey(String compoundKey) {
        return compoundKey.split("#")[0];
    }

    private String getSeqFromKey(String compoundKey) {
        return compoundKey.split("#")[1];
    }

    private static class CompoundKey {
        private static final String SEPARATOR = "#";
        private final String className;
        private final int seq;
        private final String value;

        private CompoundKey(String className, int seq) {
            validateClassName(className);
            validateSeq(seq);
            this.className = className;
            this.seq = seq;
            this.value =  className + SEPARATOR + seq;
        }

        public static CompoundKey from(String className, int seq) {
            // Flyweight 패턴 적용 가능
            return new CompoundKey(className, seq);
        }

        private void validateClassName(String className) {
            Objects.requireNonNull(className);
            if (className.contains(SEPARATOR)) {
                throw new IllegalArgumentException("# 포함하지 마세여");
            }
        }

        private void validateSeq(int seq) {
            if (seq < 0) {
                throw new IllegalArgumentException("0보다 작으면 안되여");
            }
        }

        public String getClassName() {
            return className;
        }

        public int getSeq() {
            return seq;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CompoundKey that = (CompoundKey) o;
            return seq == that.seq && className.equals(that.className);
        }

        @Override
        public int hashCode() {
            return Objects.hash(className, seq);
        }

        @Override
        public String toString() {
            return "CompoundKey{" +
                    "className='" + className + '\'' +
                    ", seq=" + seq +
                    '}';
        }
    }


    @Test
    void testStringCompoundKey() {
        Set<String> compoundKeys = IntStream.rangeClosed(1, 10)
                .mapToObj(seq -> getCompoundKey(Item62.class.getSimpleName(), seq))
                .collect(toUnmodifiableSet());
        for (String compoundKey : compoundKeys) {
            System.out.println(compoundKey);
            System.out.println(getClassNameFromKey(compoundKey));
            System.out.println(getSeqFromKey(compoundKey));
        }
    }

    @Test
    void testCompoundKeyClass() {
        Set<CompoundKey> compoundKeys = IntStream.rangeClosed(1, 10)
                .mapToObj(seq -> CompoundKey.from(Item62.class.getSimpleName(), seq))
                .collect(toUnmodifiableSet());
        for (CompoundKey compoundKey : compoundKeys) {
            System.out.println(compoundKey);
            System.out.println(compoundKey.getValue());
            System.out.println(compoundKey.getClassName());
            System.out.println(compoundKey.getSeq());
        }
    }
}