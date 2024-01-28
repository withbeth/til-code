package org.example.concurrency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskExecutionWebServer {
    private static final int THREAD_NO = 100;
    private static final Executor executor = Executors.newFixedThreadPool(THREAD_NO);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(80);
        while (true) {
            // blocks until a connection is made.
            final Socket connection = serverSocket.accept();

            // Task 정의
            Runnable task = () -> {
                try {
                    handleRequest(connection);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };

            // Task 수행 임의
            executor.execute(task);
        }

    }

    private static void handleRequest(Socket connection) throws IOException {

        BufferedReader in
            = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        System.out.println("handle request : " + in.readLine());
    }

}
