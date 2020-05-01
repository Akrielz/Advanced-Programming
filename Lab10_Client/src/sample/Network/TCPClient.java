package sample.Network;

import sample.Graphics.InGameController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient extends Thread {
    public static final int PORT = 4069;
    public static String SERVER_ADRESS = "127.0.0.1";
    private boolean gameCreated = false;

    public void run () {
        try (
                Socket socket = new Socket(SERVER_ADRESS, PORT);
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                )
        ) {
            while(true) {
                //Sending Message
                String request;

                if (!gameCreated) {
                    request = "browse";
                }
                else{
                    while(!InGameController.isHumanFinished()){
                        try {
                            Thread.sleep(30);
                            //System.out.println("Player not ready");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    request = "move ";
                }
                out.println(request);

                //Receiving Answer
                String response = in.readLine();
                System.out.println("Received: " + response);

                switch (response){
                    case "exit confirmed":
                        System.out.println("The client is shutting down!");
                        break;

                    case "host":
                        System.out.println("Game created!");
                        gameCreated = true;
                        break;

                    case "join":
                        System.out.println("Game found");
                        gameCreated = true;
                        break;
                }

                if (response.equals("exit confirmed")){
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("No server listening... " + e);
        }
    }

    public boolean isGameCreated() {
        return gameCreated;
    }
}
