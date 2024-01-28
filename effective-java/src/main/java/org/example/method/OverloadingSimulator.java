package org.example.method;

import java.math.BigInteger;
import java.util.*;

public class OverloadingSimulator {

    static String classify(Set<?> s) {
        return "Set";
    }

    static String classify(List<?> s) {
        return "List";
    }

    static String classify(Collection<?> s) {
        return "Collection";
    }

    public static void main(String[] args) {
        Collection<?>[] collections = {
                new HashSet<String>(),
                new ArrayList<BigInteger>(),
                new HashMap<String, String>().values()
        };

        for (Collection<?> c : collections) {
            // 오버로딩된 classify중, 어느 메서드를 호출할지는 "컴파일타입"에 결정된다.
            // 컴파일타임에, c는 항상 Collection<?> 타입이므로, "Collection" 반환 메서드가 호출된다.
            System.out.println("classify(c) = " + classify(c));
        }

        Set<Integer> set = new TreeSet<>();
        List<Integer> list = new ArrayList<>();
        for (int i = -3; i < 3; i++) {
            set.add(i);
            list.add(i);
        }
        for (int i = 0; i < 3; i++) {
            // call remove(Object)
            set.remove(i);

            // Problem  : call remove(index) instead of remove(Object)
            // Reason   : List.remove가 다중정의 되어 있으므로.
            // Solution : Integer로 형변환. (List.remove((Integer) i)
            list.remove(i);
        }

        System.out.println("set = " + set);
        System.out.println("list = " + list);
    }
}
