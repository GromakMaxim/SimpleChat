package org.example.server_application;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private static final Logger log = Logger.getLogger(Server.class);
    private final int PORT = 25999;// порт, который будет прослушивать наш сервер
    private CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();// список клиентов, которые будут подключаться к серверу

    public void start() throws IOException {
        Socket clientSocket = null;
        ServerSocket serverSocket = null;
        ClientHandler clientHandler;//обработчик подключения клиента
        try {
            serverSocket = new ServerSocket(PORT);// создаём серверный сокет на определенном порту
            System.out.println("Сервер запущен!");
            log.info(" server started ");
            while (true) {
                try {// таким образом ждём подключений от сервера
                    clientSocket = serverSocket.accept();// сокет клиента, это некий поток, который будет подключаться к серверу по адресу и порту
                } catch (SocketException socketException) {
                    log.warn("connection lost");
                } finally {

                }
                clientHandler = new ClientHandler(clientSocket, this);// создаём обработчик клиента, который подключился к серверу
                clients.add(clientHandler);
                log.info(" added participant. participants number: " + clients.size());
                new Thread(clientHandler).start();// каждое подключение клиента обрабатываем в новом потоке
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            stop(clientSocket, serverSocket);
            System.out.println("Сервер остановлен");
            log.warn("server stopped");
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

    public void stop(Socket clientSocket, ServerSocket serverSocket) throws IOException {
        clientSocket.close();
        serverSocket.close();
    }
}