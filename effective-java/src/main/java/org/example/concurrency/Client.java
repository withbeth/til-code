package org.example.concurrency;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        InetAddress localHost = InetAddress.getLocalHost();

        for (int i = 0; i < 100; i++) {

            // create client socket using ip and port num
            final Socket connection = new Socket(localHost, 80);

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())));

            System.out.print("서버로 보낼 message: ");

            String data = in.readLine();
            out.println(data);
            out.flush();

            connection.close();

        }

    }

}
