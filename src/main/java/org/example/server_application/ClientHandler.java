package org.example.server_application;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// реализуем интерфейс Runnable, который позволяет работать с потоками
public class ClientHandler implements Runnable {
    private Server server;// экземпляр нашего сервера
    private PrintWriter outMessage;// исходящее сообщение
    private Scanner inMessage;// входящее собщение
    private static int clients_count = 0;// количество клиента в чате, статичное поле

    // конструктор, который принимает клиентский сокет и сервер
    public ClientHandler(Socket socket, Server server) {
        try {
            clients_count++;
            this.server = server;
            this.outMessage = new PrintWriter(socket.getOutputStream());
            this.inMessage = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Переопределяем метод run(), который вызывается когда происходит new Thread(client_application).start();
    @Override
    public void run() {
        try {
            // сервер отправляет сообщение
            server.sendMessageToAllClients("Новый участник вошёл в чат!");
            server.sendMessageToAllClients("Клиентов в чате = " + clients_count);
            while (true) {
                if (inMessage.hasNext()) {// Если от клиента пришло сообщение
                    String clientMessage = inMessage.nextLine();
                    // если клиент отправляет данное сообщение, то цикл прерывается и клиент выходит из чата
                    if (clientMessage.equalsIgnoreCase("##session##end##")) {
                        break;
                    }
                    server.sendMessageToAllClients(clientMessage);// отправляем данное сообщение всем клиентам
                }
                Thread.sleep(100);// останавливаем выполнение потока на 100 мс
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            this.close();
        }
    }

    // отправляем сообщение
    public void sendMsg(String msg) {
        try {
            outMessage.println(msg);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // клиент выходит из чата
    public void close() {
        server.removeClient(this);// удаляем клиента из списка
        clients_count--;
        server.sendMessageToAllClients("Клиентов в чате = " + clients_count);
    }
}
