${toc}

# Scope

> Section 9 ~ 50

## Progress

### 240130

- 12. containerD vs docker

  - CRI and CLI
  - docker-shim removed = docker support removed

- 13. etcd basic

  - What : simple distributed kvs store
  - Note. there is significant change in v 2and v3
  - Example Value : Document, Page, etc..

- 14. etcd in k8s

  - contains whole info of pod, replocasets, secret, etc..
  - Q. 현재 상태를 나타내나? 이니면 히스토리용?
  - Q. master node 에 서비스로 존재하나?
  - kubeapi server 과만, 통신
  - HA환경에선, 여러 마스터 노드가 존재하며, 해당 마스터마다 별도 etcd 인스턴스를 갖는다.
  - 따라서, 이들과 통신하기 위해 서로가 서로를 알아야한다 using etc service configuration file
  - k8s dir 구조 = registry/pod/…

- 15. kube api server

  - the only component which interacts with etcd
  - any operations which makes changes will be updated in etcd using this api server
  - kubectl reachs to api
  - this is on a master node
  - role :
    - auth, validate req
    - handle req to scheduler and kibectl manager
  - scheduler polls the change of api?
    - Q.어떻게 알고 스케쥴링하는거지?
  - u can directly interacts with this api using REST API
  - Q. 이 api server는, 모든 컴포넌트들은 뭐가 어디에있는지 어떻게 아나? 서비스 디스커버리같은 애들 있나?
    - 적어도, etcd cluster주소는 api server시 지정해야한다

- 16. Kube ctrl manager

  - 필요한 모든 컨틀로러를 관리하는 친구
  - 노드 컨트롤러, 레플리카 셋 컨트롤러, …
  - 모든 컨트롤러는 desired된 state를 유지히기위한 모니터링 및 오페레이션을 실시 through kube api server
  - 예를 들어 노드 컨트롤러는,
    - 노드 헬스체크를 5초마다 하며
    - 대답없는 노드를 40초 기다리며,
    - 하는 기능들이 있다
  - 이 컨트롤러들은 설치시 전부설치되며, 커스터마이즈가능하다.
  - 프로세스 실행시 옵션 설정.

### 240131

- 목표 18-24
- 일단 한번 듣구, 두번째에 랩업

- 18. Kube scheduler

  - only decides which container goes where
  - node, pods을 정하기만 한다
  - 직접 생성은, kublet(captin of the ship)

- 19. kublet

  - exists in worker node
  - need to manually install!!!
  - node or pod 생성 수정 요청등이 스케줄러부터오면 핸들링.
  - 이때 CRI, img로부터 실제 컨테이너 생성.
  - 또한 정기적인 스테이터스를 모니터링후 kubapi server에게 보고

- 20. kube proxy

  - 모든 팟, 노드들은 서로 커뮤니케이션이 가능해야한다? 클러스터 상관없이? 즉, 물리적 위치 상관없이?
  - 이를 위해선 인터널 네트워킹이 필요하고, 서로를 식별할 정보가 필요.
  - 보통 아이피 이용하지만 팟은 일시적이라 자주바뀌기에 서비스 이용.
  - 서비스는 게념적인 컴포넌트고, 팟처럼 실질적인게 존재하지 않는다.
  - 단순 쿠버네티스 메모리상에만 존재.
  - 이 서비스를 이용해 아이피말고 name 이용해 통신.
  - 이를 위해선 kube proxy필요.
  - Q. 이 친구는 노드? 팟? 마다 붙어있으며, 아이피테이블 매핑을 한다?
  - how to install
  - how to customize
  - how to view process

- 21. pods recap

  - pod k8s의 최소단위.= 만들수 있는 가장 작은 단위.
  - 달리 말하면, 컨테이너를 직접 디플로이하는게 아니라, 한겹 감싼 pod이라는 객체를 이용해 디플로이한다.
  - 보통 스케일할때 하나의 노드에 새로눈 팟을 생성.
  - 힌 노드의 물리적 용량 한계기 있으면 별도 노드생성
  - pod 1: n containers 가능은 하나 보통 1:1 관계
  - n개 가질수 있는 경우는 helper containers like sidecar처럼. 하지만 관리가 어렵다?
  - 보통 pod 라이프사이클을 같이 가지고 가야하는데 이부분을 k8s가 책임짐
  - Q. 그럼 실제로 어떤 흐름으로 pod이 만들어지는가? => later section
  - Q. 로드밸런싱은 어떻게 하는가? => later section
  - 하나의 팟안에 있는 컨테이너들은 같은 스토리지, 네트워크 스페이스를 공유하기에 localhost로 서로 연결이 가능하다
  - how to deploy pods?
    - `kubectl run image —image imagename`
    - it deploys a container after creating a pod.
    - first, create a pod
    - second, deploy instance of image
  - Q. container is an instance of image?
  - kubectl get pods으로 팟 리스트, 스테이트를 볼스있다
  - Q. 그러면 어떻게 이 팟에 유저가 접근가능할까? =. later section

### 240202

> 어제 재택하며 못함.. 0131꺼 이미지 붙여넣기

- 22. pods w/ yaml

  - yaml이용해 팟 생성가능.
  - 마치 도커파일처럼.
  - 4 root elems
  - 1. apiversion
  - 2. kind. not name
    - 만드려는 쿠버 객체 타입(팟, 서비스, 레플셋,…)
  - 3. metadata
    - dict
    - k8s가 이해가능한 필드만 정의해야한다
    - name : should be unique?
    - labels like tag ex BE
      - 레이블은 뭐든지 정의 가능
  - 4. spec
    - 이미지, 자원등을 명시
    - kind마다 명시하는 것이 달라질수 있어 doc참고가 필요
    - containers
      - image
      - name
  - how to create a pod
    - `kubectl create -f pod-file-name`
    - or use `apply`
  - how to see details?
    - `kubectl describe pod-name`

- 23. demo pods w/ yaml

  - create vs run?

- 24. practice test introduction

- 25 - 28. practice test and solution

- 29. replica set
