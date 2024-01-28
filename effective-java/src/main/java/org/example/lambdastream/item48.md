# Summary
---
계산 정확도, 성능 개선 검증이 된후, 스트림 파이프라인 병렬화를 진행하라.

# Why?
---
Java8이후, 동시성 프로그램을 작성하기 쉬워졌다 (stream() -> parrellStream())
하지만 여전히 올바르고 빠르게 작성하는 일은 여전히 어렵다.

즉, 손쉬운 병렬화가 가능해졌지만, parrelStream()이 쓰레드 안전성을 제공하지 않기에,
동시성 프로그래밍시 주의해야 하는 안정성과, 응답성은 여전히 고려해야 한다.

# Example
---



# 그럼 어떨때 병렬스트림을 쓸 수 있죠?
---
병렬처리 및 동시성 제어로 인한 오버헤드를 고려해야 하기에, 다음과 같은 사항을 고려하자.

## 데이터 소스 원소 개수
- `소스 데이터 원소 개수 N`이 충분히 많아야 한다. (최소 만개 이상)

## 각 원소마다 수행할 함수 연산 비용
- 각 원소마다 수행할 `연산 비용 Q`이 충분히 비싸야 한다.

## 쉽게 split하며 참조지역성이 뛰어난 자료구조
- 내부적으로 Fork-join 프레임워크를 이용하기에, `쉽게 split` 가능하며 `참조지역성`이 뛰어난 `자료구조` 여야한다.

### Good Example
`Array, ArrayList, HashMap, HashSet`
- 데이터를 원하는 크기로 나누어 다수의 스레드로 손쉽게 나눌수 있다.
- 이웃한 원소의 참조들이 메모리에 연속되어 저장되어있기에 참조지역성이 뛰어나다.

## 쉽게 결과를 merge가능한 종단연산
-  `쉽게 결과를 merge가능한` `종단 연산` 이여야 한다.

### Good Example
- `reduce, anyMatch, allMatch, nonMatch`
- 모든 원소를 하나로 합치거나, 조건에 맞으면 바로 반환되기에.

### Bad Example
- `grouping, collect`
- 가변 축소를 수행하는 메서드는 병렬화에 적합하지 않다.

##  `stateful한 연산이 없어야 한다`
- parallelStream()은 스레드 안전성을 제공하지 않기에.


# Reference
- https://www.infoq.com/presentations/effective-java-third-edition/