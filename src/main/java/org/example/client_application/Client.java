package org.example.client_application;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import static java.util.Calendar.*;

public class Client {
    private final UI graphicalInterface;
    private Scanner inMessage;// входящее сообщение
    private PrintWriter outMessage;// исходящее сообщение

    public Client(final SettingsFileHandler settingsFileHandler) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
            try {
                inMessage = new Scanner(settingsFileHandler.getClientSocket().getInputStream());
                outMessage = new PrintWriter(settingsFileHandler.getClientSocket().getOutputStream(), true);
            } catch (IOException e) {
                System.err.println("ConnectException: Ошибка подключения, сервер недоступен");
                Thread.sleep(5000);
            }
        this.graphicalInterface = new UI(this, settingsFileHandler);
        // в отдельном потоке начинаем работу с сервером
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (inMessage.hasNext()) {// если есть входящее сообщение считываем его
                            String inMes = inMessage.nextLine();
                            String clientsInChat = "Клиентов в чате = ";
                            if (inMes.indexOf(clientsInChat) == 0) {
                                graphicalInterface.getNumberOfClientsLabel().setText(getCurrentDateString() + ": " + inMes);
                            } else {
                                graphicalInterface.getMessageTextArea().append(getCurrentDateString() + ": " + inMes);// выводим сообщение
                                graphicalInterface.getMessageTextArea().append("\n");// добавляем строку перехода
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
        String messageStr = graphicalInterface.getNameTextField().getText() + ": " + graphicalInterface.getMessageTextField().getText();
        outMessage.println(messageStr);// отправляем сообщение
        graphicalInterface.getMessageTextField().setText("");
    }

    private String getCurrentDateString() {
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd-HH:m:s");
        return df.format(calendar.getTime());
    }
    public PrintWriter getOutMessage() {
        return outMessage;
    }
}