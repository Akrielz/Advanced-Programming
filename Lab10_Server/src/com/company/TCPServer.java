package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class TCPServer {
    public static final int PORT = 4069;
    public boolean inQueue;
    public boolean gameSecured;
    Socket lastClient;

    HashMap<Socket, Socket> partners;

    public TCPServer() throws IOException {
        inQueue = false;
        lastClient = null;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                System.out.println("Waiting for a client ...");
                Socket socket = serverSocket.accept();
                new ClientThread(socket, this).start();
                System.out.println("Starting connection with the client ...");
            }
        } catch (IOException e) {
            System.err.println("Ooops... " + e);
        }
    }
}