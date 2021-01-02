package org.example.client_application;

import java.io.*;
import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import static java.util.Calendar.*;

public class Client {
    private final UI graphicalInterface;
    private final String clientSettingsFilePath = "src/main/java/org/example/client_application/settings/client_settings.txt";
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
    public Client() throws FileNotFoundException, UnsupportedEncodingException {
        try {
            // подключаемся к серверу
            clientSocket = new Socket(getServerHostParameterFromSettingsFile(), Integer.parseInt(getServerPortParameterFromSettingsFile()));//1-localhost, 2-данные из файла
            inMessage = new Scanner(clientSocket.getInputStream());
            outMessage = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.graphicalInterface = new UI(this);
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
        int year = calendar.get(YEAR);
        int month = calendar.get(MONTH);
        int day = calendar.get(DAY_OF_MONTH);
        int hour = calendar.get(HOUR_OF_DAY);
        int minute = calendar.get(MINUTE);
        return (year + "." + month + "." + day + "//" + hour + ":" + minute + " ");
    }

    //прочитать порт сервера
    private String getServerPortParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        String server_port = "server_port";
        checkSettingsFileExistenceOrCreateDefaultFile();
        return readSettingsFileAndGetParameter(server_port);
    }

    //прочитать хост
    private String getServerHostParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        String server_host = "server_host";
        checkSettingsFileExistenceOrCreateDefaultFile();
        return readSettingsFileAndGetParameter(server_host);
    }

    //прочитать параметр окна
    public int getWindowXParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        String window_x = "window_x";
        checkSettingsFileExistenceOrCreateDefaultFile();
        return Integer.parseInt(readSettingsFileAndGetParameter(window_x));
    }
    //прочитать параметр окна
    public int getWindowYParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        String window_y = "window_y";
        checkSettingsFileExistenceOrCreateDefaultFile();
        return Integer.parseInt(readSettingsFileAndGetParameter(window_y));
    }
    //прочитать ширину окна
    public int getWindowWidthParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        String window_width = "window_width";
        checkSettingsFileExistenceOrCreateDefaultFile();
        return Integer.parseInt(readSettingsFileAndGetParameter(window_width));
    }

    //прочитать высоту окна
    public int getWindowHeightParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        String window_height = "window_height";
        checkSettingsFileExistenceOrCreateDefaultFile();
        return Integer.parseInt(readSettingsFileAndGetParameter(window_height));
    }

    //проверить наличие файла с настройкам в заданной директории
    private void checkSettingsFileExistenceOrCreateDefaultFile() throws FileNotFoundException, UnsupportedEncodingException {
        File settingsFile = new File(clientSettingsFilePath);
        if (!settingsFile.exists()) {//сомневаюсь между выбором exists() и isFile()
            createDefaultSettingsFileIfNotExist(clientSettingsFilePath);
        }
    }

    //достаём заданный параметр из файла с настройками
    private String readSettingsFileAndGetParameter(String parameter) {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fin = new FileInputStream(clientSettingsFilePath)) {
            int i = -1;
            while ((i = fin.read()) != -1) {//читаем все символы
                sb.append((char) i);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        String fileString = sb.toString();
        String[] fileLines = fileString
                .replace(" ", "")//очистить пробелы, переносы строки и пр
                .replace("\r","")
                .replace("\n","")
                .trim()
                .split(";");//дробим файл по разделителям
        for (String line : fileLines) {
            if (line.split(":")[0].equalsIgnoreCase(parameter)) {//в каждой строке ищем опцию (слева от двоеточия)
                return line.split(":")[1];//если нашли, вытаскиваем её аргумент(справа от двоеточия)
            }
        }
        return "";
    }

    //создадим файл с настройками по умолчанию
    private void createDefaultSettingsFileIfNotExist(String path) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        //укажем настройки по умолчанию
        writer.println("server_port:25999;");//порт
        writer.println("server_host:localhost;");//
        writer.println("window_x:600;");//параметры для окна
        writer.println("window_y:300;");
        writer.println("window_width:500;");
        writer.println("window_height:500;");
        writer.close();
    }
}
