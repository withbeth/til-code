# Enum

## Item34. Prefer `enum` over `int` constants

> int상수대신 열거타입 사용하라

### Tl;DR

- ENUM이 더 읽기 쉽고 타입 안전하며 추상화도 가능함.
- 따라서, 필요 원소를 컴파일 타임에 알수 있는 상수집합이라면 ENUM.

- Enum에서, 하나의 메소드가 상수별로 다르게 동작해야한다면, 상수별 메서드 구현.
- Enum에서, 일부 같은 동작을 공유한다면, 전략 열거 타입 패턴 사용.

### 정수 상수의 단점

- Main : 타입안정성 보장 안됨,
- Other : 테스트의 어려움, 가독성 저하, 상수 집합을 확장하거나 수정하기 어려움

### One more thing

#### Enum은 어떻게 초기화 될까?

[ 사전 지식 ]
일반적인 경우,

클래스 로드 시점 :

1. 클래스의 인스턴스 생성 -- 생성 first
2. 클래스의 정적 메소드 호출
3. 클래스의 정적 변수 할당
4. 클래스의 정적 변수 초기화

인스턴스 초기화 순서 :

1. 정적 블록
2. 정적 변수
3. 생성자

명세서에서는 암시적인 필드가 초기화 될때 생성된다고 함.
결론적으로 인스턴스 생성이 먼저고 정적 변수 초기화는 나중에 이루어짐.

[ 열거 타입의 경우 ]

> 열거타입의 경우, 클래스 로드 시점에 JVM Method 영역에 할당되며 heap 영역에 인스턴화 됨.
>
> item3에 싱글톤 설명에서 잠시 나옴

Refer :
https://github.com/EffectiveStudy/study-history/blob/main/6%EC%9E%A5-%EC%97%B4%EA%B1%B0%20%ED%83%80%EC%9E%85%EA%B3%BC%20%EC%95%A0%EB%84%88%ED%85%8C%EC%9D%B4%EC%85%98/item34.md

# Annotation