package org.example.server_application;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final Logger log = Logger.getLogger(Server.class);
    // порт, который будет прослушивать наш сервер
    static final int PORT = 25999;
    // список клиентов, которые будут подключаться к серверу
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    public Server() {
        // сокет клиента, это некий поток, который будет подключаться к серверу
        // по адресу и порту
        Socket clientSocket = null;
        // серверный сокет
        ServerSocket serverSocket = null;
        try {
            // создаём серверный сокет на определенном порту
            serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер запущен!");
            log.info(" server started ");
            // запускаем бесконечный цикл
            while (true) {
                // таким образом ждём подключений от сервера
                clientSocket = serverSocket.accept();
                // создаём обработчик клиента, который подключился к серверу
                // this - это наш сервер
                ClientHandler client = new ClientHandler(clientSocket, this);
                clients.add(client);
                log.info(" added participant ");
                log.info(" participants number: " + clients.size());
                // каждое подключение клиента обрабатываем в новом потоке
                new Thread(client).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                // закрываем подключение
                clientSocket.close();
                System.out.println("Сервер остановлен");
                log.warn("server stopped");
                serverSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
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

}