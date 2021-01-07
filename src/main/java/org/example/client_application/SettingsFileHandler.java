package org.example.client_application;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class SettingsFileHandler {
    private final Socket clientSocket;// клиентский сокет
    private final String clientSettingsFilePath = "src/main/java/org/example/client_application/settings/client_settings.txt";

    public Socket getClientSocket() {
        return clientSocket;
    }

    public SettingsFileHandler() throws IOException {
        this.clientSocket = initClientSocket();
    }

    Socket initClientSocket() throws IOException {
        while (true) {
            try {
                return new Socket(getServerHostParameterFromSettingsFile(), Integer.parseInt(getServerPortParameterFromSettingsFile()));//1-localhost, 2-данные из файла
            } catch (ConnectException connectException) {
                System.err.println("ConnectException: сервер недоступен!");
            }
        }
    }

    //прочитать порт сервера
    String getServerPortParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        String server_port = "server_port";
        checkSettingsFileExistenceOrCreateDefaultFile();
        return readSettingsFileAndGetParameter(server_port);
    }

    //прочитать хост
    String getServerHostParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        String server_host = "server_host";
        checkSettingsFileExistenceOrCreateDefaultFile();
        return readSettingsFileAndGetParameter(server_host);
    }

    //прочитать параметр окна
    int getWindowXParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        String window_x = "window_x";
        checkSettingsFileExistenceOrCreateDefaultFile();
        return Integer.parseInt(readSettingsFileAndGetParameter(window_x));
    }

    //прочитать параметр окна
    int getWindowYParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        String window_y = "window_y";
        checkSettingsFileExistenceOrCreateDefaultFile();
        return Integer.parseInt(readSettingsFileAndGetParameter(window_y));
    }

    //прочитать ширину окна
    int getWindowWidthParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        String window_width = "window_width";
        checkSettingsFileExistenceOrCreateDefaultFile();
        return Integer.parseInt(readSettingsFileAndGetParameter(window_width));
    }

    //прочитать высоту окна
    int getWindowHeightParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
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
    String readSettingsFileAndGetParameter(String parameter) {
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
                .replace("\r", "")
                .replace("\n", "")
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
    void createDefaultSettingsFileIfNotExist(String path) throws FileNotFoundException, UnsupportedEncodingException {
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