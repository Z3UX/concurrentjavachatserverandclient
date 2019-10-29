package org.academiadecodigo.stringrays.concurrentchatserverandclient.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int portNumber;
    private LinkedList<ClientHandler> clients = new LinkedList<>();
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int maxConnections;
    private int i = 0;

    public Server(int maxConnections, int portNumber) {
        this.maxConnections = maxConnections;
        this.portNumber = portNumber;
    }

    public void start() {

        ExecutorService fixedPool = Executors.newFixedThreadPool(maxConnections);

        try {

            serverSocket = new ServerSocket(portNumber);

            while (true) {

                clientSocket = serverSocket.accept();

                fixedPool.execute(new ClientHandler(this, clientSocket, "User#" + i++));
            }
        } catch (
                IOException e) {
            e.getStackTrace();
        }
    }

    public LinkedList getClients() {
        return clients;
    }


    public synchronized void broadcast(String nick, String chatMessage) {

        for (ClientHandler client : clients) {
            if (!client.getNick().equals(nick)) {
                client.messageToClient(nick, chatMessage);
            }
        }
        System.out.println(nick + ": " + chatMessage);
    }

    public void whisper(String toNick, String fromNick, String chatMessage) {

        for (ClientHandler client : clients) {
            if (client.getNick().toLowerCase().equals(toNick)) {
                client.messageToClient("WHISPER FROM " + fromNick, chatMessage);
            }
        }
    }
}