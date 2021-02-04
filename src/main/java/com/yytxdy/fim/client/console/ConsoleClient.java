package com.yytxdy.fim.client.console;

public class ConsoleClient {
    private long telephone;
    private String token;
    private String serverIp;
    private int serverPort;

    public ConsoleClient(long telephone, String token, String serverIp, int serverPort) {
        this.telephone = telephone;
        this.token = token;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void startClient() {

    }

    public static void main(String[] args) {
        new ConsoleClient(13584099554L, "888888", "127.0.0.1", 10101).startClient();
    }
}
