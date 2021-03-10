public interface IServerClient {
    void send(String str);
    String receive();
    boolean closeDataExchange(String str);
    void openConnection();
    void closeConnection();
}
