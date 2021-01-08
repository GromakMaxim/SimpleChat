package org.example.client_application;

import org.junit.Test;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientTest {
    SettingsFileHandler sfh = mock(SettingsFileHandler.class);

    @Test(expected = ConnectException.class)
    public void test_initClientWithNoServer() throws IOException, InterruptedException {
        when(sfh.initClientSocket()).thenReturn(new Socket("localhost", 25999));
        when(sfh.getClientSocket()).thenReturn(new Socket("localhost", 25999));
        new Client(sfh);
        System.out.println("app started but there is no server for connection");
    }
}