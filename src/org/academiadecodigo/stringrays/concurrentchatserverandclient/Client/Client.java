package org.academiadecodigo.stringrays.concurrentchatserverandclient.Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

//TODO: CREATE METHODS INSTEAD OF PUTTING EVERYTHING IN PSVM

public class Client {

    public static void main(String[] args) {

        try {

            Socket clientSocket = new Socket(InetAddress.getLoopbackAddress(), 60000);

            Thread output = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true) {

                        try {

                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                            Scanner scanner = new Scanner(System.in);

                            String chatMessage = scanner.nextLine();

                            out.println(chatMessage);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            output.start();

            Thread input = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true) {

                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                            System.out.println(in.readLine());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            input.start();

        } catch (
                IOException e) {
            e.getStackTrace();
        }
    }
}