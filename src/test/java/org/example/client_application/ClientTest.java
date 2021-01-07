package org.example.client_application;

import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTest {
    SettingsFileHandler sfh = mock(SettingsFileHandler.class);
    @Test
    public void test_initClient() throws IOException, InterruptedException {
        when(sfh.getClientSocket()).thenReturn(new Socket("localhost", 25999));
        new Client(sfh);
        System.out.println("app started");
    }
}