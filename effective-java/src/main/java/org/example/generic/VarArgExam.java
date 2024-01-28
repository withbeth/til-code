package org.example.generic;

import java.util.ArrayList;
import java.util.List;

public class VarArgExam {

    public static void heapPollution(List<String>... strings) {
        // 가변인수(varArgs) 메서드 호출시, 해당 가변 인수를 담기위한 배열이 자동으로 생성됨.

        // 참조를 상위 타입의 배열로 넘긴다.
        Object[] objects = strings;

        // 별도 타입의 객체를 참조
        objects[0] = List.of(1, 2, 3); // 힙 오염 발생

        for (int i = 0; i < strings.length; i++) {
            System.out.println("strings [" + i + "] = " + strings[i]);
        }

        String s = strings[0].get(0);
    }

    @SafeVarargs
    public static List<String> safeVarArgs(List<String>... strings) {
        // 2)방어적 복사 이용해 참조 노출 방지
        List<String> result = new ArrayList<>();
        for (List<String> stringList : strings) {
            // 1) 제네릭 가변인수 배열에는 아무것도 저장 또는 덮어쓰지 않는다.
            result.addAll(stringList);
        }
        return result;
    }

    public static void main(String[] args) {
        heapPollution(List.of("foo"), List.of("var"));
    }
}
