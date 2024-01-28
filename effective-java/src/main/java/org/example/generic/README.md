# Generic

## 26. Do not use Raw Type

> 로 타입은 사용하지 말라

## 27. Eliminate unchecked warnings

> 비검사 경고를 제거하라

## 28. Prefer Lists to Arrays

> 배열보다는 리스트를 사용하자

## 29. Favor generic types

> 이왕이면 제네릭 타입을 사용하자

## 30. Favor generic methods

> 이왕이면 제네릭 메서드를 사용하자

Q. 제네릭 메서드란?

## 31. Use bounded wildcards to increase API flexibility

> API 유연성을 높이려면 한정적 와일드카드를 사용하라
 
Q. 한정적 와일드카드란?

## [x] 32. Combine generics and varargs judiciously (240128)

> 제네릭과 가변인수를 함께쓸때는 신중하라

### TL;DR

- 제네릭 가변인자 매개변수는 힙오염의 가능성이 있다.
- 따라서 안전하게 사용하려면 다음 3가지 조건을 만족해야 한다.

> 1. 가변인수 호출시 생성되는 제네릭 배열에 아무것도 저장 or overwrite하지 않아야 한다.
> 2. 배열의 참조가 밖으로 노출되지 않아야 한다.
> 3. `@SafeVarargs`를 달아 메서드가 타입안정함을 컴파일러에게 알려야 한다.


### Detail

가변인수 사용시, 힙오염 발생 가능.

#### Q. 가변인수가 뭔가요?

> 메서드에 넘기는 인수의 개수를 클라이언트가 조절 가능한 기능.

```java
import java.util.Arrays;

class Foo {
    public static void foo(String... varStrings) {
        System.out.println(Arrays.toString(varStrings));
    }

    public static void main(String[] args) {
        foo("a", "b");
        foo("a", "b", "c");
        foo("a", "b", "c", "d");
    }
}
```

[ 장점 ]

> 사용성 (클라이언트 측에서 인자 숫자를 임의로 지정 가능하므로)

[ 단점 ]

> 가변인수 메서드 호출시, 해당 가변인수를 담기위한 "배열"이 자동으로 생성됨.
> 해당 배열은 메서드 내부에 노출되기에, "힙오염"위험에 노출될수 있다.
> 따라서 컴파일러는 제네릭 타입으로 가변인수 선언시 경고를 내보낸다.

#### Q. 힙오염이란?
> 매개변수 유형이 서로 다른 타입을 참조할때 발생하는 문제

#### Q. 힙오염이 생기면 어떤 문제가 생기죠? 왜 안좋은 거죠?
> 컴파일러가 자동생성한 형변환이 실패할 가능성이 생긴다.
> 즉, 제네릭이 제공하는 타입안정성에 구멍이 뚫린다.

```java
public class HeapPollution {

    public static void foo(List<String>... strings) {
        // 가변인수(varArgs) 메서드 호출시, 해당 가변 인수를 담기위한 배열이 자동으로 생성됨.

        // 참조를 상위 타입의 배열로 넘긴다.
        Object[] objects = strings;

        // 별도 타입의 객체를 참조
        objects[0] = List.of(1, 2, 3); // 힙 오염 발생

        for (int i = 0; i < strings.length; i++) {
            System.out.println("strings [" + i + "] = " + strings[i]);
        }

        // Heap오염으로 런타임시 ClassCastingExp 발생
        String s = strings[0].get(0);
    }

    public static void main(String[] args) {
        foo(List.of("foo"), List.of("var"));
    }
}
```

#### Q. 그럼 제네릭 가변인수는 절대 쓰면 안되나요?

> 자바 라이브러리에서 사용하고 있다.
> 다만, 안정하게 사용하는 기법을 이용.
> `Arrays.asList(T...a), EnumSet.of(E first, E... rest)`

#### Q. 그럼 어떻게 제네릭 가변인수를 "안전" 하게 사용 가능한가요?

> 다음 3가지 조건을 만족시 "타입 안전"하게 가변인수를 사용 가능.
> 1. 가변인수 호출시 생성되는 제네릭 배열에 아무것도 저장 or overwrite하지 않아야 한다.
> 2. 배열의 참조가 밖으로 노출되지 않아야 한다.
> 3. `@SafeVarargs`를 달아 메서드가 타입안정함을 컴파일러에게 알려야 한다.

```java
class Foo {
    @SafeVarargs // 3) 컴파일러에게 제네릭 가변인수는 사용하지만 타입Safe하다는 것을 알린다.
    public static List<String> safeVarArgs(List<String>... strings) {
        // 2)방어적 복사 이용해 참조 노출 방지
        List<String> result = new ArrayList<>();
        for (List<String> stringList : strings) {
            // 1) 제네릭 가변인수 배열에는 아무것도 저장 또는 덮어쓰지 않는다.
            result.addAll(stringList);
        }
        return result;
    }
}
```


## 33. Consider typesafe heterogeneous containers

> 타입 안전한 이종 컨테이너를 고려하라
