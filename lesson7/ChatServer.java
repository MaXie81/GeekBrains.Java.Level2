package ru.geekbrains;

import ru.geekbrains.auth.AuthService;
import ru.geekbrains.auth.PrimitiveAuthService;
import ru.geekbrains.messages.MessageDTO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ChatServer {
    private static final int PORT = 65500;
    private List<ClientHandler> clientHandlers;
    private AuthService authService;

    public ChatServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server started");
            authService = new PrimitiveAuthService();
            authService.start();
            clientHandlers = new LinkedList<>();
            while(true) {
                System.out.println("Waiting for connection...");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessage(MessageDTO dto, boolean flgPrivate) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (flgPrivate && !dto.getTo().equals(clientHandler.getUser())) continue;
            clientHandler.sendMessage(dto);
        }
    }

    public synchronized void subscribe(ClientHandler c) {
        clientHandlers.add(c);
    }

    public synchronized void unsubscribe(ClientHandler c) {
        clientHandlers.remove(c);
    }

    public AuthService getAuthService() {
        return authService;
    }
}
