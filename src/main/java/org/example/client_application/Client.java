package org.example.client_application;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import static java.util.Calendar.YEAR;

public class Client {
    private final UI ui;
    private static final String SERVER_HOST = "localhost";// адрес сервера
    private static final int SERVER_PORT = 25999;// порт
    private Socket clientSocket;// клиентский сокет
    private Scanner inMessage;// входящее сообщение
    private PrintWriter outMessage;// исходящее сообщение

    public Scanner getInMessage() {
        return inMessage;
    }

    public PrintWriter getOutMessage() {
        return outMessage;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    // конструктор
    public Client() {
        try {
            // подключаемся к серверу
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            inMessage = new Scanner(clientSocket.getInputStream());
            outMessage = new PrintWriter(clientSocket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ui = new UI(this);
        // в отдельном потоке начинаем работу с сервером
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (getInMessage().hasNext()) {// если есть входящее сообщение считываем его
                            String inMes = getInMessage().nextLine();
                            String clientsInChat = "Клиентов в чате = ";
                            if (inMes.indexOf(clientsInChat) == 0) {
                                ui.getNumberOfClientsLabel().setText(getCurrentDateString()+": "+inMes);
                            } else {
                                ui.getMessageTextArea().append(getCurrentDateString()+": " + inMes);// выводим сообщение
                                ui.getMessageTextArea().append("\n");// добавляем строку перехода
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }).start();
    }

    // отправка сообщения
    public void sendMsg() {
        // формируем сообщение для отправки на сервер
        String messageStr = ui.getNameTextField().getText()+": " + ui.getMessageTextField().getText();
        outMessage.println(messageStr);// отправляем сообщение
        ui.getMessageTextField().setText("");
    }

    private String getCurrentDateString(){
        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return (year + "." + month + "." + day + "//"+hour+":"+minute+ " ");
    }
}
