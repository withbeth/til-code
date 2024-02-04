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

## 31. Use bounded wildcards to increase API flexibility (240204)

> API 유연성을 높이려면 한정적 와일드카드를 사용하라

### TL;DR

> 유연성을 극대화하려면 원소의 생산자, 소비자용 입력 매개변수에 와일드카드 타입을 사용하라.
>
> PECS (Producer-Extends, Consumer-Super)

### WHO is WHO?

- 누가 생산자? `void push(Collection<? extends E> src)`
- 누가 소비자? `void pop(Collection<? super E> dst)`

### Q. 기준?

> 필드.

- 매개변수로 인해, 필드가 "생산"될 경우,
- 매개변수로 인해, 필드를 "소비"할경우,

### Q. 왜 생산자는 하위타입을 받나?

- 입력받는 객체를 통해, 상위 객체에 추가 되므로.
- 반대로, 소비자는 공급받는 객체를, 상위 객체에 추가하여 소비하므로, 상위타입을 받는다.

- 예: `Number one = new Integer(1)`

### One more thing

입력 매개변수로서 E vs ?

- 공개API에, 메서드 선언에 타입매개변수가 하나라면, 와일드카드 is better.
- Collection에 와일드카드있다면 null밖에 안들어가니, helper를.

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
>
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

## 33. Consider typesafe heterogeneous containers p198-205 (240204)

> 타입 안전한 이종 컨테이너를 고려하라

### TL;DR

- 일반적인 제네릭형태에서는, 한 컨테이너가 다룰 수 있는 `타입매개변수 수`가 고정되어있다.
  - 예: `Set<E>, Map<K,V>, ThreadLocal<T>, AtomicReference<T>`
  - 이때, 매개변수화 되는 대상은, 원소가 아닌, 컨테이너 자신.
- 하지만, 컨테이너 자체가 아닌, KEY를 타입매개변수로 바꾸면, 위 제약이 없는, 타입 안전한 이종 컨테이너를 만들수 있다.
- 타입안전한 이종컨테이너는, Class를 KEY로 쓰며, 이렇게 쓰이는 Class객체를 타입 토큰이라 한다.
- 또한 직접 구현한 키 타입도 쓸수 있다.

### Problem

> 한 컨테이너가 다를 수 있는 타입매개 변수 수가 고정되어있다.

### Solution

> 컨테이너 자체가 아닌, KEY를 타입매개변수로 이용하는, 타입 안정 이종 컨테이너 사용.

### Q. 타입안정한 이종 컨테이너란?

> KEY를 타입매개변수로 사용하는 컨테이너.
> Class객체를 KEY로 쓰며, 이렇게 쓰이는 Class객체를 타입 토큰이라 한다.

### Q. 그럼 타입 토큰이란?

> 컴파일 타입 정보와 런타임 타입 정보를 알아내기 위해, 메서드들이 주고 받는 class 리터럴
> 예: String.class, Integer.class

- class 리터럴의 타입은, class가 아닌 `Class<T>`이다.
  - = class의 클래스는, Generic.
- String.class의 타입은, `Class<String>`
- Integer.class의 타입은, `Class<Integer>`

### Q. class 리터럴이란?

From [Java tutorial - DataTypes](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html):

> There's a special kind of literal called a class literal,
> formed by taking a type name and appending ".class";
> for example, String.class.
> This refers to the object (of type Class) that represents the type itself.

- 타입 자체를 나타내는 클래스 타입 객체를 나타낸다.
- 즉, `Class<T>`를 클래스 리터럴을 이용해 획득 가능.

From [Java tutorial - Class Literals as Runtime-Type Tokens](https://docs.oracle.com/javase/tutorial/extra/generics/literals.html):

> One of the changes in JDK 5.0 is that the class java.lang.Class is generic
> for something other than a container class.
> Now that Class has a type parameter T, you might well ask, what does T stand for?
> It stands for the type that the Class object is representing.
>
> For example,
> the type of String.class is `Class<String>`,
> and the type of Serializable.class is `Class<Serializable>`.
> This can be used to improve the type safety of your reflection code.
>
> In particular, since the newInstance() method in Class now returns a T,
> you can get more precise types when creating objects reflectively.

- JDK5부터, class 클래스는 Generic해지며, 타입 파라미터를 가질수 있게 되었다.
- 이를 통해, 리플렉션사용시 타입안전성을 향상시킬수 있다.

### Q. `Class<T>`가 뭐죠..?

- 특정 타입 Class의 인스턴스.
- 특정 클래스의 런타임 속성을 조회, 조작, 검사할수 있는 불변 객체
- JVM의 클래스로더에서, 클래스 파일 로딩 완료시, 해당 클래스 타입 불변 Class 객체를 생성하여 Heap에 저장.

- 이 객체는, 클래스 리터럴을 이용해 획득 가능.

  - 클래스.class
  - 인스턴스.getClass()
  - Class.forName("클래스명")

- Reflection API의 엔트리 포인트로도 사용된다.
  - 해당 Class 객체를 이용해 Constructor, Method, Field 인스턴스 접근 및 조작 가능.
  - refer [Java tutorial](https://docs.oracle.com/javase/tutorial/reflect/class/index.html)

From [JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) :

> Instances of the class Class represent classes and interfaces in a running Java application.
> An enum is a kind of class and an annotation is a kind of interface.
> The primitive Java types, and the keyword void are also represented as Class objects.

- 즉, 특정 타입 Class의 인스턴스.

> Class has no public constructor.
> Instead Class objects are constructed automatically by the Java Virtual Machine as classes are loaded and by calls to the defineClass method in the class loader.

- Class 클래스는 public 생성자 존재하지 않고, JVM의 class loader에 의해 로드된다.

> It is also possible to get the Class object for a named type (or for void) using a class literal.
> See Section 15.8.2 of The Java™ Language Specification. For example:

- 이 Class 객체는, 클래스 리터럴을 이용해 획득 가능하다.
  - `System.out.println("The name of class Foo is: "+Foo.class.getName());`

### HOW?

> See org/example/generic/Item33TypeSafeHeterogeneousContainer.java

### One more thing

1. 실체화 불가능한 타입은, 해당 이종 컨테이너를 사용할수 없다.

> 이를 해결하기 위해, SuperTypeToken 패턴을 사용할수 있다.

> In Spring, 'ParameterizedTypeReference' is used to solve this problem.

2. bounded type token을 사용하려면, Annotation API가 어떻게 하는지 확인 (Item39)

```java
    /**
     * Returns this element's annotation for the specified type if such an annotation is present, else null.
     * Throws: NullPointerException – if the given annotation class is null
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        Objects.requireNonNull(annotationClass);
        return annotationClass.cast(declaredAnnotations().get(annotationClass));
    }

```
