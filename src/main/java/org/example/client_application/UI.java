package org.example.client_application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/*эксперименты со swing!*/
public class UI extends JFrame {
    //элементы формы
    private JTextField messageTextField;
    private JTextField nameTextField;
    private JTextArea messageTextArea;

    private JLabel numberOfClientsLabel; //строка с количеством подключенных участников
    private JPanel bottomPanel;//нижняя панель
    private JButton sendMessageButton;//кнопка "отправить"
    private JScrollPane scrollBar;//скролл бар

    private String clientName = "";// имя клиента

    public JTextField getMessageTextField() {
        return messageTextField;
    }

    public JTextField getNameTextField() {
        return nameTextField;
    }

    public JTextArea getMessageTextArea() {
        return messageTextArea;
    }

    public JLabel getNumberOfClientsLabel() {
        return numberOfClientsLabel;
    }

    public UI(final Client client) throws FileNotFoundException, UnsupportedEncodingException {
        // Задаём настройки элементов на форме
        this.setBounds(client.getWindowXParameterFromSettingsFile(),
                client.getWindowYParameterFromSettingsFile(),
                client.getWindowWidthParameterFromSettingsFile(),
                client.getWindowHeightParameterFromSettingsFile());
        setTitle("This magnificent chat was made by Gromak Maxim");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        messageTextArea = new JTextArea();
        messageTextArea.setEditable(false);
        messageTextArea.setLineWrap(true);
        numberOfClientsLabel = new JLabel("Количество участников: ");
        bottomPanel = new JPanel(new BorderLayout());
        sendMessageButton = new JButton("Отправить");
        scrollBar = new JScrollPane(messageTextArea);
        messageTextField = new JTextField("Введите ваше сообщение: ");
        nameTextField = new JTextField("Введите ваше имя: ");

        this.add(scrollBar, BorderLayout.CENTER);
        this.add(numberOfClientsLabel, BorderLayout.NORTH);
        this.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(sendMessageButton, BorderLayout.EAST);
        bottomPanel.add(messageTextField, BorderLayout.CENTER);
        bottomPanel.add(nameTextField, BorderLayout.WEST);

        // обработчик события нажатия кнопки отправки сообщения
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // если имя клиента, и сообщение непустые, то отправляем сообщение
                if (!messageTextField.getText().trim().isEmpty() && !nameTextField.getText().trim().isEmpty()) {
                    clientName = nameTextField.getText();
                    client.sendMsg();
                    messageTextField.grabFocus();// фокус на текстовое поле с сообщением
                }
            }
        });
        // при фокусе поле сообщения очищается
        messageTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                messageTextField.setText("");
            }
        });
        // при фокусе поле имя очищается
        nameTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                nameTextField.setText("");
            }
        });
        messageTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!messageTextField.getText().trim().isEmpty() && !nameTextField.getText().trim().isEmpty()) {
                        clientName = nameTextField.getText();
                        client.sendMsg();
                        messageTextField.grabFocus();// фокус на текстовое поле с сообщением
                    }
                }
            }
        });

        // добавляем обработчик события закрытия окна клиентского приложения
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                try {
                    // здесь проверяем, что имя клиента непустое и не равно значению по умолчанию
                    if (!clientName.isEmpty() && !clientName.equals("Введите ваше имя: ")) {
                        client.getOutMessage().println(clientName + " вышел из чата!");
                    } else{
                        client.getOutMessage().println("Участник вышел из чата, так и не представившись!");
                    }

                    // отправляем служебное сообщение, которое является признаком того, что клиент вышел из чата
                    client.getOutMessage().println("##session##end##");
                    client.getOutMessage().flush();
                    client.getOutMessage().close();
                    client.getClientSocket().close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        setVisible(true);// отображаем форму
    }

    public String getClientName() {
        return clientName;
    }
}