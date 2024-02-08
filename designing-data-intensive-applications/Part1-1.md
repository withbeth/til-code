# Part1 1장 신뢰할수있고, 확장 가능하며, 유지보수하기 쉬운 어플리케이션 

## 들어가며…

> 1. 요즘 나오는 요구사항들은 "데이터중심적"이며, 이를 위해선 표준 구성요소들을 해당 요구사항에 맞게 적절히 "조합"할줄 알아야 한다.
> 2. 또한, "신뢰할수 없는 부품"들을 조합하여, "신뢰할수 있는 시스템"을 구축해야 한다.

* 더이상 많은 앱들은 CPU 계산중심이 아닌, 데이터 중심적.
* 데이터 중심적 앱들이 공통으로 필요한 표준 구성요소들은 다음과 같다.
    * DB, Cache, search index, stream processing, batch processing
    * 데이터 저장 및 읽기 성능 향상 >> DB, Cache
    * 데이터 검색 및 다양한 방법으로 필터링할수 있게 제공 >> search index
    * 비동기 처리를 위해 다른 프로세스로 메시지 보내기 >> stream processing
    * 주기적으로 대량 데이터 분석 >> batch processing
* 다만, 요구사항과 다양한 시나리오에 따라 어떤 요소의 어떤 툴을 어떻게 조합해야할지는 어려운 문제.
    * 각 구성요소에도 다양한 제품이 존재하며, 이에 대한 특성을 알고 있어야 한다.
    * 이러한 제품들을 어떻게 조합해서 요구사항을 만족시킬 것인지에 대한 디자인 능력이 필요하다.
* 그렇다면, 이 책에서는 어떤 것을 배울 수 있나?
    * 데이터 시스템의 
        * 1. 원칙
        * 2. 실용성
        * 3. 이를 활용한 데이터 중심 앱 개발 방법
        * 4. 표준 구성요소(도구, 제품)의 공통점, 차별점, 특성 설명
    * 신뢰할수 없는 여러 부품으로, 신뢰할수 있는 시스템을 구축하는 기법들. TODO

## 이 장에서는 무엇을 배울 수 있나?

* 신뢰성, 확장성, 유지보수성의 “명확한” 의미
* 이를 고려하는 방법

## 다음 장에서는 무엇을 배울 수 있나?

* 데이터 중심 앱 개발시 고려해야할 설계 결정사항을, “계층별”로 고려.


## 데이터 시스템에 대한 생각

> 1. 여러 구성요소들을 "조합"해, 데이터 시스템을 설계해야 하는데, 이때 고려할 문제들이 많다.
> 2. 대표적으로 3가지 (신뢰성, 확장성, 유지보수성) 존재.

* DB, 캐시, 큐등 여러 도구들을 왜 “데이터 시스템” 이라는 포괄적 용어로 묶는가?
    * 1. DB, 캐시, 큐등 전통적인 범주에 fit하지 않는 도구들이 등장했기에.
        * DB처럼 지속성 보장 MQ >> Kafka
        * MQ로 사용하는 DataStore >> Redis
    * 2. 단일 도구로는 만족할수 없는 요구사항이 등장했기에
        * 따라서, 단일 도구에서 효율적으로 달성할수 있는 태스크를 나누고,
        * 다양한 도구들을 앱 코드를 이용해 연결.
* 따라서, 이제 개발자는 앱 개발자뿐만 아니라, 데이터 시스템 설계자이다.
    * 복합 데이터 시스템을 설계하고, 외부 클라이언트가 일관된 결과를 보게끔 제공.
    * 각 도구 결합시 구현 세부사항은 캡슐화.
* 이러한 데이터 시스템 설계시 까다로운 문제가 많이 생긴다.
    * CAP
    * + 기존시스템 의존성
    * + 조직내 기술 숙련도
    * + 조직내 제약 사항
    * + 스케줄
* 이 책에서는, 대부분이 중요하다고 생각하는 3가지 관심사에 focus.
    * 1) 신뢰성 (Reliability)
        * 어떠한 결함, 에러가 있더라도 시스템은 올바르게 작동해야 한다.
            * 올바르게 작동 = 원하는 성능에서 정확한 기능 수행
            * HW, SW, Human error
    * 2) 확장성 (Scalability)
        * 데이터, 트래픽, 복잡도가 증가하여도 이를 적절히 처리할수 있어야 한다.
    * 3) 유지보수성 (Maintainability)
        * 손쉬운 변경 (OCP)

## 신뢰성

* 신뢰성
    * 신뢰성이란, “무언가 잘못되더라도 올바르게 동작함”
    * 잘못될 수 있는 일 = 결함 (fault)
    * 결함을 예측 및 대처 가능한 시스템 = 내결합성 (fault-tolerant) 또는 탄력성(resilient)을 지닌 시스템
        * 정확하게는 “특정 유형”의 내결합성을 가졌다고 말하는게 적절.
    * 결함 (fault) != 장애 (failure)
        * 결함 = 사양에서 벗어난 시스템의 한 구성 요소
        * 장애 = 필요한 서비스를 제공하지 못하는 상태
        * 결합 확률을 0으로 줄이는 것은 불가능하기에,
        * 결합으로 인해 장애가 발생하지 못하게끔 내결합성 구조를 설계하는 것이 가장 좋다. TODO
* HW 결함
    * HW결함이 얼마나 자주 일어나는지.
    * 따라서, 단일 장비 신뢰성보다, SW 내결함성 기술 이용 + HW 중복을 이용해 장치 손실에 대한 시스템으로 옮겨가고 있다.
    * = 당연한 애기.
* SW 에러
    * SW오류 (버그)는 특정 상황에 의해 발생하기까지 나타나지 않는다. (그래서 Bug)
    * SW오류 예
        * 연쇄장애
        * 공유자원 과도 사용 프로세스로 생기는 문제
        * 속도저하로 반응이 없거나 잘못된 응답 반환하는 서비스
    * SW오류의 “신속한” 해결책은 없다.
    * 따라서, 시스템의 가정과 상호작용에 대한 깊은 고려, 빈틈없는 테스트, 프로세스 격리, 프로덕션에서의 시스템 동작 측정~분석이 필요하다.
    * 불변조건이 존재한다면, 해당 불변조건이 불일치할시 경고를 발생시킬수도 있다.
        * 예) MQ에 수신한 메시지 수 ==  송신된 메시지 수

* Human 에러
    * 휴먼에러는 일어난다. (일어날 일은 일어난다 from 테넷)
    * 그렇다면, 휴먼에러가 일어남에도 어떻게 시스템을 신뢰성 있게 만들수 있을까?
    * 다양한 접근방식
        * 오류 최소화 방향 설계
        * 실수로 장애가 발생할수 있는 부분 분리
        * 빠른 복구 가능성
        * 모니터링 (원격측정 = telemetry)
            * 로켓 발사후, 원격 측정은 일어나는 일을 추적하고 장애 해석을 위해 필수적.
* 신뢰성은 얼마나 중요한가?
    * “어떤” 어플리케이션이라도, 사용자에 대한 책임(responsibility)이 있다.
        * 원자력 발전소나, 항공 관제 SW이 아니더라도.


## 확장성 

* 확장성 = 증가하는 시스템 부하에 대처하는 능력
    * 일차원적이지 않다. (NG 예 : "X는 확장가능하다")

* 확장성을 논한다는 것 
    * = "시스템이 특정방식으로 커지면 이를 대처하기위한 옵션은 무엇인가?" 와 같은 질문을 고려한다는 뜻.

* "부하" 기술하기
    * 측정하지 않은 것은 개선할 수 없다.
    * 부하를 기술(describe)하기 위한 지표는 어떤 것이 있고, 어떻게 선택하면 좋은가?
    * 부하는, 부하 매개변수(load param)으로 나타낼 수 있다.
    * 또한, 적절한 부하 매개변수 선택은 설계에 따라 달라진다.
    * 그럼, 부하 매개변수의 예는 무엇인가?
        * RPS
        * DB READ/WRITE ratio
        * active user number
        * cache hit ratio
        * ...
    * 이러한 부하매개변수 숫자는, 평균이 중요할수도, 소수의 극단적인 경우가 중요할 수도 있다.
        * 예: 트위터 셀레브리티 문제


* "성능" 기술하기

    * 부하 매개변수를 기술하면, 해당 숫자가 증가할때 어떤 일이 일어나는지 "조사"할 수 있다.
        * 조사방법
            * 1) 부하 매개변수 증가 & 자원 KEEP 시의 성능 영향
            * 2) 부하 매개변수 증가시 성능을 유지하고 싶다면, 어떤 자원을 얼마나 늘려야 하나.
    * 이 질문에 대답하기 위해선 성능 수치가 필요하다.
        * 처리량 (throughput) 
            * = 초당 처리가능한 레코드수 
            * = or 일정 데이터 집합으로 작업 수행시 걸리는 전체 시간
        * 응답시간 (response time) << 온라인 시스템에서 중요한 성능 수치
            * = "클라이언트"가 요청보내고 응답을 받는 사이의 시간 
            * 실제 서비스 시간 + 네트워크 지연 + 큐 지연등 모든 지연을 포함.
        * 지연시간 (latency) 
            * = 요청이 처리되길 기다리는 시간(pause time)
    * 응답시간은 매번 다르다. 
        * 이유 = 프로세스 컨텍스트 스위칭, 네트워크 패킷 손실, TCP재전송, GC pause, 디스크에서 읽기를 강제하는 page fault, ...
        * 따라서, 분포로 생각해야 한다.
        * 평균보단 백분위(percentile)이 좋다.
            * 사용자가 "보통" 얼마나 오랫동안 기다리는지는 중앙값이 좋은 지표.
                * 중앙값 (median) p50
                    * 응답시간 목록을 가장 빠른 시간부터 정렬하여 중간 지점
                    * 사용자 50% 중앙값 미만. 사용자 50% 중앙값 오버
            * 특이 값(outliner)가 얼마나 좋지 않은지 알고 싶다면, 상위 백분위 파악
                * p95, p99, p999(99.9분위)
                * 예: 요청의 95% (p95)가 특정 기준치보다 빠르다면, 해당 기준치가 응답 시간 기준치.
                * p95 응답시간이 1.5초라면, 
                    * 100개 중 95개는 1.5초 미만
                    * 100개 중 5개는 1.5초 이상
            * 상위 백분위는 UX에 직접 영향을 주기에 중요하다! (tail latency and HOB problem)
                * 보통 응답시간이 가장 느린 유저는, 고래일 확률이 있다 (많은 구매를 해서, 고객중에 가장 많은 데이터를 갖고 있다)
                * 하지만, 이 최상위 백분위를 위해 최적화하는 작업은 코스트 퍼포먼스가 안맞을 수 있다.
                * 또한 최상위 백분위는 임의의 이벤트에 쉽게 영향을 받아서 어렵다.
            * 백분위는 SLO(목표), SLA(협약서)에 자주 등장한다.
                * SLA 예
                    * 응답시간 중앙값이 200ms미만이고, 99분위가 1초미만인경우, 정상서비스 상태로 간주한다.
                    * 서비스 제공시간(uptime)은 99.% 이상이어야 한다.
            * 큐 대기 지연(queueing delay)는 높은 백분위에서 응답시간의 상당부분을 차지.
                * 왜? 서버는 병렬로 소수 작업만 처리 가능하기에 (CPU코어수에 제한됨), 소수의 느린 요청 처리만으로, 후속 요청 처리가 지체된다.
                    * = Head Of Line Blocking Problem
                * 따라서, 성능 테스트를 위해 인위적으로 부하 생성하는 경우, 클라이언트는 다음 요청 보내기 전에 이전 요청이 완료되길 기다리면 안된다.
                    * = HOLB 문제를 고려해서 성능 테스트를 해야 한다.
            * 하나의 요청처리 위해 여러 호출이 필요한경우, 단 하나의 느린 호출결과가 최종 사용자 요청을 느리게 한다.
                * = 병렬처리시 특히 이문제가 고려된다.

* 부하 "대응" 접근 방식
    * 성능 측정을 위한 부하 매개변수를 얻었으니, 이제 어떻게 확장성을 늘릴수 있을지 생각.
    * 즉, 부하 매개변수가 어느정도 증가해도 성능을 유지하려면 어떻게 해야 하나?
    *  보통, scale up, scale out.
    * 또한, 탄력적(elastic) 시스템 여부.
    * DB같은, state를 갖는 데이터 시스템을 분산시키는 것은 통상 어렵다.
    * 따라서, 고가용성 요구가 있을때까지, 단일 노드에 DB를 유지한다. (최근엔 다르다? Q. AWS는 어떻게 하나?)
    * 대개 시스템 아키텍처는 해당 앱에 특화되어 있다. = 모든 상황에 맞는 만능 구조는 없다.
        * 아키를 결정하는 요소
            * READ/WRITE VOLUMN
            * 저장할 데이터의 양
            * 데이터 복잡도
            * 응답시간 요구사항등..
        * 주요 동작이 무엇인지, 특장점을 파악해야 한다.
        * 이러한 특장점은 부하매개변수를 통해 개선이 가능하다.
    * 다만, 익숙한 패턴으로 나열된 범용적인 구성요소로 구축한다.
    * 따라서, 이책에서는 이러한 구성요소와 패턴에 대해서 설명한다. TODO

## 유지보수성 

* SW비용의 대부분은 유지보수에서 발생.
    * 버그 수정, 장애조사, 새로운 플랫폼 적응, 기술채무 상환, 새로운 기능 추가, ...

* 그럼 이를 해결할수 있는 좋은 방법이 있는가?
    * 고통을 최소화하는 시스템 설계 원칙 3가지는 있다.
    * 1) 운용성 : 운영팀이 원할하게 운영할수 있도록 "쉽게" 만들어라
    * 2) 단순성 : 복잡도를 최대한 제거하여 시스템을 이해하기 "쉽게" 만들어라
    * 3) 발전성 : 시스템을 "쉽게" 변경할수 있도록 만들어라
        * 유연성(extensibility)
        * 수정 가능성(modifiability)
        * 적응성(plasticity)

* 아니, 그래서 어떻게 하냐구요...

* 운용성 : 운영팀이 원할하게 운영할수 있도록 "쉽게"
    * 운영팀의 책임
        * 시스템 모니터링
            * 서비스 복원
            * 장애, 성능 저하 추적
        * 배포, 설정관리 등을 위한 모범사례와 도구 마련
        * ...
    * 답은 반복되는 태스크를 "쉽게" 수행하도록 만들어, 고부가가치 활동에 집중할수 있게 해야 한다.
        * "자동화"
        * "좋은 문서"
        * 이해하기 쉬운 운영 모델 제공
        * 예측 가능하게 동작하고, 예기치 않은 상황 최소화

* 단순성 : 복잡도를 최대한 제거하여 엔지니어가 시스템을 이해하기 "쉽게".
    * 복잡도의 증상
        * 강한 커플링, 복잡한 의존성, 임시방편 문제 해결 사례등...
    * 복잡도를 최대한 제거하라 = 단순성이 시스템의 핵심 목표여야 한다.
    * 단순하게 만들어라 != 기능을 줄여라
        * 단순하게 만들어라 = 우발적(accidental) 복잡도를 줄여라.
        * 우발적 복잡도 = SW가 풀어야할 문제에 있지 않다. 구현에 있다.
        * 즉, 단순 기능을 줄이는 것 말고, 그것을 구현해나갈때 생겨나는 복잡도를 줄이라는 소리다.
    * 그럼 우발적 복잡도를 어떻게 줄이나요?
        * 답은 "추상화"
        * 책 전반에 걸쳐 추상화를 살펴볼것이다 (큰 시스템 일부를, 잘 정의되고 재사용 가능한 구성요소로 추출) TODO

* 발전성 : 변화를 “쉽게” 만들기
    * 변화에 적응하기 위한 프레임워크
        * 애자일, TDD
    * 이 책에서는 대규모 데이터 시스템 수준에서 민첩성(발전성)을 높이는 방법을 찾는다. TODO 

## 정리

- 앱이 유용하려면 다양한 요구사항을 만족해야 한다.
    - 기능적 요구사항
    - 비기능적 요구사항(신뢰성, 확장성, 유지보수성, ...) << 여기에 대해 알아보았다

- 신뢰성 : 결함이 발생해도 시스템이 올바르게 동작해야 한다.
    -  결함에는 HW,SW, 휴먼 에러등이 있으며, 이를 위해 특정 유형의 내결합성을 갖도록 설계 해야 한다.

- 확장성 : 부하가 증가해도 성능을 유지해야 한다.
    - 이를 위해 부하와 성능 수치를 기술하고, 해당 수치를 통해 부하가 증가해도 성능을 유지하도록 설계해야 한다.

- 유지보수성 : 손쉬운 변경이 가능해야 한다
    - 이를 위해 최대한 복잡도를 제거하고 단순해야 하며, 반복 태스크는 최대한 "쉽게" 수행 가능해야 한다.
    - "추상화", "자동화", "좋은 문서"

## My thought 

- 실질적으로 문제 정의만 하고 있고, 답은 이 챕터에서 주고 있지 않다.
- 따라서 책을 읽고난 후, 이 챕터에서 던져논 떡밥(문제 정의 및 해결법)이 회수 되었나 확인이 필요 할 것 같다.
    - 예: 우발적 복잡도, 그래서 어떻게 줄이는데?
    - 예: 발전성을 어떻게 향상시킬수 있는데?


## QnA


* Q. 로켓분야에서 빌려온 용어인 telemetry 원격측정, 어떻게 하고 있는 건가?
* Q. 여기서 말하는 신뢰성, 확장성, 유지보수성과 CAP의 관계?
* Q. 정산, 주문, 전시도메인에서 중요시 하는 부하 매개변수, 성능 수치?
* Q. 우발적 복잡도를 줄여본 사례?
* Q. 책에 언급하는 유지보수성을 위해 하고 있는 활동?
* Q. DB같이 state를 갖는 데이터 시스템을 분산시키는 것은 통상 어려워 최대한 단일 노드에 DB를 유지한다고 하는데, AWS는 어떻게 DB를 분산시켜 서비스를 제공하나?  단순 Replication, Sharding 기법들을 애기하고 있는 건가?
