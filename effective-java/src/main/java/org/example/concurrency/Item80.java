package org.example.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Item80 {

    public static void main(String[] args) {
        /**
         *  무제한(Unbounded) 큐에서 작동하는 단일 작업자 스레드를 사용하는 실행자 생성.
         *  작업은 순차적으로 실행되도록 보장.
         *  하나 이상의 작업이 활성화 되지 않는다.
         *  실행 중 장애로 단일 스레드 종료시, 새 스레드 생성하여 작업 수행.
         */
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 실행할 작업 넘기기 (Runnable)
        executorService.execute(() -> System.out.println("hello, executor framework"));

        // executor 종료시키기 (종료 안되면 VM 자체가 종료 안됨)
        executorService.shutdown();
    }

}
