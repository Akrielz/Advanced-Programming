package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientThread extends Thread {
    private Socket socket = null ;
    TCPServer tcpServer = null;
    public ClientThread (Socket socket, TCPServer tcpServer) {
        this.socket = socket ;
        this.tcpServer = tcpServer;
    }

    public void run () {
        try {
            while(true) {
                //Receiving Request
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String request = in.readLine();

                //Sending back Answer
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                System.out.println("Received: " + request);

                String answer;
                switch (request){
                    case "browse":
                        if (tcpServer.inQueue){
                            answer = "join";
                            tcpServer.partners.put(socket, tcpServer.lastClient);
                            tcpServer.partners.put(tcpServer.lastClient, socket);
                            tcpServer.gameSecured = true;
                            tcpServer.inQueue = false;
                            break;
                        }

                        tcpServer.inQueue = true;
                        tcpServer.lastClient = socket;
                        while (!tcpServer.gameSecured){
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        answer = "host";

                    case "exit":
                        answer = "exit confirmed";
                        break;

                    default:
                        answer = "unhandadled situation";
                        break;
                }

                if (request.substring(0, 4).equals("move")){
                    int size = request.length();
                    answer = "exit confirmed";
                }

                out.println(answer);
                out.flush();

                if (request.equals("exit")){
                    System.out.println("Connection terminated with " + socket.toString());
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        } finally {
            try {
                socket.close(); // or use try-with-resources
            } catch (IOException e) { System.err.println (e); }
        }
    }
}