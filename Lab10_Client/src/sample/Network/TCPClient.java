package sample.Network;

import sample.Graphics.InGameController;
import sample.Mechanics.Game;

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
    private boolean isHost;
    private Game game;
    private boolean decoded = false;
    private int move;

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
                    int move = game.getPlayers()[game.getPlayerTurn()].getSelectedY()*game.getBoard().getBoardSize() +
                            game.getPlayers()[game.getPlayerTurn()].getSelectedX();
                    request = "move " + String.valueOf(move);
                    System.out.println("DEBUG: " + request);
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
                        isHost = true;
                        gameCreated = true;
                        break;

                    case "join":
                        System.out.println("Game found");
                        isHost = false;
                        gameCreated = true;
                        break;
                }

                if (request.substring(0, 4).equals("move")){
                    int size = request.length();
                    move = Integer.parseInt(request.substring(5, size));
                    decoded = true;
                }

                if (response.equals("exit confirmed")){
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isGameCreated() {
        return gameCreated;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isDecoded() {
        return decoded;
    }

    public void setDecoded(boolean decoded) {
        this.decoded = decoded;
    }

    public int getMove() {
        return move;
    }
}
