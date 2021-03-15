package ru.geekbrains.server;

import com.sun.applet2.Applet2;
import ru.geekbrains.messages.MessageDTO;
import ru.geekbrains.messages.MessageType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;

/**
 * Project java_core_l2
 *
 * @Author Alexander Grigorev
 * Created 05.03.2021
 * v1.0
 * <p>
 * Обработчик клиентов. Создается сервером на каждое подключение и получает свой сокет.
 * Работает с одним сокетом/клиентом
 * обрабатывает отправку сообщений данному конкретному клиенту и обработку сообщений, поступивших от клиента
 */
public class ClientHandler {
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private ChatServer chatServer;
    private String currentUserName = null;

    class TimerCloseConnection implements Runnable{
        private final int WAIT_TIME = 60_000;
        @Override
        synchronized public void run() {
            try {
                wait(WAIT_TIME);
                if (currentUserName == null) {
                    MessageDTO response = new MessageDTO();
                    response.setMessageType(MessageType.DISCONNECT_MESSAGE);
                    response.setBody("Connection terminated by timeout!");
                    System.out.println("Connection terminated by timeout");
                    sendMessage(response);
                    closeHandler();
                }
            } catch (InterruptedException e) {}
        }
    }

    public ClientHandler(Socket socket, ChatServer chatServer) {
        try {
            this.chatServer = chatServer;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("CH created!");
            /**
             * в отдельном потоке запускается авторизация, которая при успехе завершается и запускается бесконечный цикл чтения сообщений
             */
            new Thread(new TimerCloseConnection()).start();
            new Thread(() -> {
                try {
                    authenticate();
                    readMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * метод отправляет сообщение клиенту, который привязан к этому обработчику
     *
     * @param dto
     */
    public void sendMessage(MessageDTO dto) {
        try {
            outputStream.writeUTF(dto.convertToJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Цикл чтения и обработки сообщений
     */
    private void readMessages() throws IOException {
        try {
            while (!Thread.currentThread().isInterrupted() || socket.isConnected()) {
                String msg = inputStream.readUTF();
                MessageDTO dto = MessageDTO.convertFromJson(msg);
                dto.setFrom(currentUserName);

                switch (dto.getMessageType()) {
                    case PUBLIC_MESSAGE :
                        chatServer.broadcastMessage(dto);
                        break;
                    case PRIVATE_MESSAGE :
                        chatServer.sendPrivateMessage(dto);
                        break;
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            closeHandler();
        }
    }

    /**
     * цикл аутентификации
     * Сейчас завершится только после успеха
     */
    private void authenticate() {
        System.out.println("Authenticate started!");
        try {
            while (true) {
                String authMessage = inputStream.readUTF();
                System.out.println("received msg ");
                MessageDTO dto = MessageDTO.convertFromJson(authMessage);
                String username = chatServer.getAuthService().getUsernameByLoginPass(dto.getLogin(), dto.getPassword());
                MessageDTO response = new MessageDTO();
                if (username == null) {
                    response.setMessageType(MessageType.ERROR_MESSAGE);
                    response.setBody("Wrong login or pass!");
                    System.out.println("Wrong auth");
                } else if (chatServer.isUserBusy(username)) {
                    response.setMessageType(MessageType.ERROR_MESSAGE);
                    response.setBody("U're clone!!!");
                    System.out.println("Clone");
                } else {
                    response.setMessageType(MessageType.AUTH_CONFIRM);
                    response.setBody(username);
                    currentUserName = username;
                    chatServer.subscribe(this);
                    System.out.println("Subscribed");
                    sendMessage(response);
                    break;
                }
                sendMessage(response);
            }
        } catch (IOException e) {
//            e.printStackTrace();
            closeHandler();
        }
    }

    /**
     * метод закрытия, пока не использовали
     */
    synchronized public void closeHandler() {
        try {
            if (socket.isClosed()) return;
            chatServer.unsubscribe(this);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            socket.close();
            System.out.println("Клиент с ником \"" + currentUserName + "\" отключен.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
