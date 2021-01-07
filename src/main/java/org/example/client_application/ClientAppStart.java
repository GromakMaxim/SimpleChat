package org.example.client_application;

import java.io.IOException;

public class ClientAppStart {
    public static void main(String[] args) throws IOException, InterruptedException {
        new Client(new SettingsFileHandler());
    }
}
