package org.example.client_application;

import org.example.server_application.Server;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;

public class ServerTest {
    final Server server = new Server();
    SettingsFileHandler sfh = new SettingsFileHandler();

    @Test
    public void test_connection() throws IOException, InterruptedException {
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();
        Thread.sleep(1000);
        sfh.initClientSocket();
        System.out.println("Connection established");

        PrintWriter outMessage = new PrintWriter(sfh.getClientSocket().getOutputStream(), true);
        outMessage.println("hellooooooo");
        Thread.sleep(2000);
        System.out.println("msg sent successfully");
        serverThread.stop();
    }
}
