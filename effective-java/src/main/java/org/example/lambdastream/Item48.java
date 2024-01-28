package org.example.lambdastream;

import java.math.BigInteger;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.math.BigInteger.*;

public class Item48 {

    private static Stream<BigInteger> primes() {
        return Stream.iterate(TWO, BigInteger::nextProbablePrime);
    }

    // 메르센 소수 20개 찾기
    static void findMersenneBySequentialStream() {
        primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
                .filter(mersenne -> mersenne.isProbablePrime(50))
                .limit(20)
                .forEach(System.out::println);
    }

    static void findMersenneByParallelStream() {
        primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
                .parallel() // 손쉬운 병렬화
                .filter(mersenne -> mersenne.isProbablePrime(50))
                .limit(20)
                .forEach(System.out::println);
    }

    static long findPiBySequentialStream(long n) {
        return LongStream.rangeClosed(2, n)
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }

    static long findPiByParallelStream(long n) {
        return LongStream.rangeClosed(2, n)
                .parallel()
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }


    public static void main(String[] args) {
        // findMersenneBySequentialStream();
        // findMersenneByParallelStream();

        //System.out.println("findPiBySequentialStream(5_000_000) = " + findPiBySequentialStream(5_000_000));
        //System.out.println("findPiByParallelStream(5_000) = " + findPiByParallelStream(5_000_000));
    }


}
