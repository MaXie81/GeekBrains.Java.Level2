package network;

import java.io.IOException;

public class ChatMessageService implements MessageService {
    private String host;
    private int port;
    private NetworkService networkService;              // отправить сообщение
    private MessageProcessor processor;                 // получить и вывести сообщение в Клиенте

    public ChatMessageService(String host, int port, MessageProcessor processor) {
        this.host = host;
        this.port = port;
        this.processor = processor;
        connectToServer();
    }

    private void connectToServer() {
        try {
            this.networkService = new NetworkService(host, port, this);
            System.out.println("Клиент подключен к Серверу.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String msg) {
        networkService.writeMessage(msg);
    }

    @Override
    public void receiveMessage(String msg) {
        processor.processMessage(msg);
    }
}
