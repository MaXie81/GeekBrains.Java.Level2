package ru.geekbrains;

import ru.geekbrains.messages.MessageDTO;
import ru.geekbrains.messages.MessageType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private ChatServer chatServer;

    public String getUser() {
        return user;
    }

    private String user;

    public ClientHandler(Socket socket, ChatServer chatServer) {
        try {
            this.chatServer = chatServer;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("CH created!");

            new Thread(() -> {
                authenticate();
                readMessages();
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(MessageDTO dto) {
        try {
            outputStream.writeUTF(dto.convertToJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessages() {
        try {
            while (true) {
                String msg = inputStream.readUTF();
                MessageDTO dto = MessageDTO.convertFromJson(msg);
                boolean flgPrivate = false;
                dto.setFrom(user);

                switch (dto.getMessageType()) {
                    case PUBLIC_MESSAGE : flgPrivate = false;
                    break;
                    case PRIVATE_MESSAGE : flgPrivate = true;
                    break;
                }
                chatServer.sendMessage(dto, flgPrivate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void authenticate() {
        System.out.println("Authenticate started!");
        try {
            while (true) {
                String authMessage = inputStream.readUTF();
                System.out.println("received msg ");
                MessageDTO dto = MessageDTO.convertFromJson(authMessage);
                String username = chatServer.getAuthService().getUsernameByLoginPass(dto.getLogin(), dto.getPassword());
                MessageDTO send = new MessageDTO();
                if (username == null) {
                    send.setMessageType(MessageType.ERROR_MESSAGE);
                    send.setBody("Wrong login or pass!");
                    System.out.println("Wring auth");
                    sendMessage(send);
                } else {
                    send.setMessageType(MessageType.AUTH_CONFIRM);
                    send.setBody(username);
                    user = username;
                    chatServer.subscribe(this);
                    System.out.println("Subscribed");
                    sendMessage(send);
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeHandler() {
        try {
            chatServer.unsubscribe(this);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
