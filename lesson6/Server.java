import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements IServerClient{
    final int PORT = 8189;
    final String EXIT_PHRASE = "/end";
    ServerSocket serverSocket;

    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    String str;
    boolean flgInitClose;

    public static void main(String[] args) {new Server().run();}

    public void run() {
        try {
            System.out.println("Запуск Сервера...");
            serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер запущен");
            while (true) {
                System.out.println("Ожидание подключения Клиента...");
                openConnection();
                System.out.println("Режим обмена данными...");
                flgInitClose = false;

                Thread thread = new Thread(() -> doSend());
                thread.start();
                doReceive();

                thread.interrupt();
                closeConnection();

                if (flgInitClose) break;
            }
            serverSocket.close();
            System.out.println("Сервер остановлен");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void doSend() {
        BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                if (scan.ready()) {
                    str = scan.readLine();
                    send(str);
                    if (str.equalsIgnoreCase(EXIT_PHRASE)) flgInitClose = true;
                }
                Thread.sleep(500);
            }
        } catch (Exception e) {}
    }

    void doReceive() {
        while (true) {
            str = receive();
            if (closeDataExchange(str)) {
                break;
            }
        }
    }

    @Override
    public void send(String str) {
        try {
            dos.writeUTF(str);
            System.out.println("Отправлено сообщение: " + str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String receive() {
        try {
            str = dis.readUTF();
            System.out.println("Получено сообщение: " + str);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean closeDataExchange(String str) {
        if (!str.equalsIgnoreCase(EXIT_PHRASE)) return false;
        if (!flgInitClose) send(str);
        System.out.println("Получена команда на завершение режима обмена данными");
        return true;
    }

    @Override
    public void openConnection() {
        try {
            socket = serverSocket.accept();
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("Соединение открыто");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        try {
            dis.close();
            dos.close();
            socket.close();
            System.out.println("Соединение закрыто");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
