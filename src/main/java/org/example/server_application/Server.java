package org.example.server_application;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {
    private static final Logger log = Logger.getLogger(Server.class);
    private final int PORT = 25999;// порт, который будет прослушивать наш сервер
    private ArrayList<ClientHandler> clients = new ArrayList<>();// список клиентов, которые будут подключаться к серверу
    private Socket clientSocket;// сокет клиента, это некий поток, который будет подключаться к серверу по адресу и порту
    private ServerSocket serverSocket;// серверный сокет
    private ClientHandler clientHandler;//обработчик подключения клиента

    public void start() throws IOException {
        try {
            serverSocket = initServerSocket();// создаём серверный сокет на определенном порту
            System.out.println("Сервер запущен!");
            log.info(" server started ");
            while (true) {
                acceptNewClient();// таким образом ждём подключений от сервера
                clientHandler = initClientHandler();// создаём обработчик клиента, который подключился к серверу
                clients.add(clientHandler);
                log.info(" added participant. participants number: " + clients.size());
                new Thread(clientHandler).start();// каждое подключение клиента обрабатываем в новом потоке
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            stop();
            System.out.println("Сервер остановлен");
            log.warn("server stopped");
        }
    }

    private void acceptNewClient() throws IOException {
        try {
            clientSocket = serverSocket.accept();
        } catch (SocketException socketException) {
            log.warn("connection lost");
        } finally {

        }
    }

    private ClientHandler initClientHandler() {
        return new ClientHandler(clientSocket, this);
    }

    private ServerSocket initServerSocket() throws IOException {
        return new ServerSocket(PORT);
    }

    // отправляем сообщение всем клиентам
    public void sendMessageToAllClients(String msg) {
        for (ClientHandler item : clients) {
            item.sendMsg(msg);
        }
        log.info(" message sent " + msg);
    }

    // удаляем клиента из коллекции при выходе из чата
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public void stop() throws IOException {
        clientSocket.close();
        serverSocket.close();
    }
}