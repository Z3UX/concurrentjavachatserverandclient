package org.academiadecodigo.stringrays.concurrentchatserverandclient.Server;

public class Main {

    public static void main(String[] args) {

        Server server = new Server(1000, 60000);

        server.start();
    }
}

