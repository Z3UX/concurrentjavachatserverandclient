package org.academiadecodigo.stringrays.concurrentchatserverandclient.Server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Server server;
    private Socket clientSocket;
    private String nick;

    public ClientHandler(Server server, Socket clientSocket, String nick) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.nick = nick;
    }

    @Override
    public void run() {
        server.getClients().add(this);
        while (!Thread.currentThread().isInterrupted()) {
            chatServer();
        }
    }

    public String getNick() {
        return nick;
    }

    public void messageToClient(String nick, String chatMessage) {

        PrintWriter out;

        try {

            out = new PrintWriter(clientSocket.getOutputStream(), true);

            out.println(nick + ": " + chatMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void chatServer() {

        BufferedReader in;
        String chatMessage;

        try {

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            chatMessage = in.readLine();

            if (chatMessage == null) {
                server.getClients().remove(this);
                close(clientSocket);
                Thread.currentThread().interrupt();
                return;
            }

            if (chatMessage.substring(0, 1).equals("/")) {

                String[] command = chatMessage.split("\\s+");

                if (command.length > 1 && command[0].toLowerCase().equals("/nick")) {
                    nick = command[1];
                    messageToClient("NICK CHANGED TO", nick);
                    return;
                }

                if (command.length > 1 && command[0].toLowerCase().equals("/whisper")) {
                    server.whisper(command[1].toLowerCase(), nick, chatMessage.replace("/whisper " + command[1] + " ", ""));
                    return;
                }
                messageToClient("ERROR", "Invalid command.");
                return;
            }

            //TODO CREATE A SYNCRONIZED BROADCAST AND A BUFFER OF MESSAGES TO SEND
            server.broadcast(nick, chatMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
