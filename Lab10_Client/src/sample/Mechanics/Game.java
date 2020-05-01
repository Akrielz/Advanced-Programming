package sample.Mechanics;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import sample.Graphics.InGameController;
import sample.Network.TCPClient;

import java.awt.*;

public class Game implements Runnable {
    private int totalTurns;
    private int playerTurn;
    private Player[] players;
    private Board board;
    private GameType gameType;
    private boolean isStoped;
    private Canvas canvas;
    private double circleRadius;
    private InGameController inGameController;

    TCPClient tcpClient;

    double []projectX;
    double []projectY;

    public InGameController getInGameController() {
        return inGameController;
    }

    public void setInGameController(InGameController inGameController) {
        this.inGameController = inGameController;
    }

    public Game(GameType gameType, boolean isHost) {
        isStoped = false;
        board = new Board();
        players = new Player[2];
        circleRadius = 20;

        if (isHost){
            InGameController.setLocalTurn(true);
        }
        else{
            InGameController.setLocalTurn(false);
        }

        projectX = new double[board.getBoardSize()];
        projectY = new double[board.getBoardSize()];

        this.gameType = gameType;
        totalTurns = 0;
        playerTurn = 0;

        if (gameType == GameType.LocalVsHuman){
            players[0] = new PlayerLocal();
            players[1] = new PlayerLocal();
        }

        if (gameType == GameType.LocalVsAI){
            players[0] = new PlayerLocal();
            players[1] = new PlayerAI();
        }

        if (gameType == GameType.NetVsHuman){
            if (isHost){
                players[0] = new PlayerLocal();
                players[1] = new PlayerNet();
            }
            else{
                players[0] = new PlayerNet();
                players[1] = new PlayerLocal();
                playerTurn = 1;
            }
        }
        players[0].setGame(this);
        players[1].setGame(this);
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void drawBoard(){
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setStroke(Color.GRAY);

        int offset = 20;

        int height = (int)canvas.getHeight() - offset*2;
        int width  = (int)canvas.getWidth() -  offset*2;
        double stepHeight = (double)height / (board.getBoardSize()-1);
        double stepWidth  = (double)width  / (board.getBoardSize()-1);

        double yCur = offset;
        for (int y = 0; y < board.getBoardSize(); y++){
            projectY[y] = yCur;

            double xCur = offset;
            for (int x = 0; x < board.getBoardSize(); x++){
                projectX[x] = xCur;
                graphicsContext.strokeLine(xCur-stepWidth, yCur, xCur, yCur);
                graphicsContext.strokeLine(xCur, yCur-stepHeight, xCur, yCur);
                graphicsContext.strokeLine(xCur, yCur, xCur+stepWidth, yCur);
                graphicsContext.strokeLine(xCur, yCur, xCur, yCur+stepHeight);

                xCur += stepWidth;
            }
            yCur += stepHeight;
        }
    }

    public void drawMove(int line, int col){
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        if (playerTurn == 0){

            graphicsContext.setStroke(Color.BLACK);
            graphicsContext.setFill(Color.WHITE);
        }
        else{
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setFill(Color.BLACK);
        }

        graphicsContext.fillOval(projectX[col]-circleRadius/2, projectY[line]-circleRadius/2, circleRadius , circleRadius);
        graphicsContext.strokeOval(projectX[col]-circleRadius/2, projectY[line]-circleRadius/2, circleRadius , circleRadius);
    }

    @Override
    public final void run() {
        int[][] insideBoard = board.getBoard();
        drawBoard();

        while (!isStoped){
            String playerColor = "White";
            if (playerTurn == 1){
                playerColor = "Black";
            }

            int move = players[playerTurn].getMove(insideBoard);
            int line = move/15;
            int col = move%15;
            System.out.println("P[" + playerTurn + "]: " + line + " " + col);
            insideBoard[line][col] = playerTurn;

            drawMove(line, col);
            if (checkWinner(move)){
                System.out.println("Player[" + playerTurn + "] is victorious!");
                break;
            }

            playerTurn = 1-playerTurn;
            totalTurns ++;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkWinner(int move) {
        int line = move/15;
        int col = move%15;

        int[][] insideBoard = board.getBoard();
        int color = insideBoard[line][col];

        int sum = 0;
        for (int i = -4; i < 5; i++){
            if (col+i < 0 || col+i >= insideBoard.length) {
                sum = 0;
                continue;
            }
            if (insideBoard[line][col+i] == color){
                sum ++;
            }
            else{
                sum = 0;
            }
            if (sum == 5) {
                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                double x1 = projectX[col+i];
                double x2 = projectX[col+i-4];
                double y1 = projectY[line];
                double y2 = projectY[line];
                graphicsContext.setStroke(Color.BLUE);
                graphicsContext.strokeLine(x1, y1, x2, y2);
                return true;
            }
        }

        sum = 0;
        for (int i = -4; i < 5; i++){
            if (line+i < 0 || line+i >= insideBoard.length) {
                sum = 0;
                continue;
            }
            if (insideBoard[line+i][col] == color){
                sum ++;
            }
            else{
                sum = 0;
            }
            if (sum == 5) {
                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                double x1 = projectX[col];
                double x2 = projectX[col];
                double y1 = projectY[line+i];
                double y2 = projectY[line+i-4];
                graphicsContext.setStroke(Color.BLUE);
                graphicsContext.strokeLine(x1, y1, x2, y2);
                return true;
            }
        }

        sum = 0;
        for (int i = -4; i < 5; i++){
            if (line+i < 0 || line+i >= insideBoard.length || col+i < 0 || col+i >= insideBoard.length) {
                sum = 0;
                continue;
            }
            if (insideBoard[line+i][col+i] == color){
                sum ++;
            }
            else{
                sum = 0;
            }
            if (sum == 5) {
                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                double x1 = projectX[col+i];
                double x2 = projectX[col+i-4];
                double y1 = projectY[line+i];
                double y2 = projectY[line+i-4];
                graphicsContext.setStroke(Color.BLUE);
                graphicsContext.strokeLine(x1, y1, x2, y2);
                return true;
            }
        }

        sum = 0;
        for (int i = -4; i < 5; i++){
            if (line+i < 0 || line+i >= insideBoard.length || col-i < 0 || col-i >= insideBoard.length) {
                sum = 0;
                continue;
            }
            if (insideBoard[line+i][col-i] == color){
                sum ++;
            }
            else{
                sum = 0;
            }
            if (sum == 5) {
                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                double x1 = projectX[col-i];
                double x2 = projectX[col-i+4];
                double y1 = projectY[line+i];
                double y2 = projectY[line+i-4];
                graphicsContext.setStroke(Color.BLUE);
                graphicsContext.strokeLine(x1, y1, x2, y2);
                return true;
            }
        }

        return false;
    }

    public void stop(){
        isStoped = true;
    }

    public void setCanvas(Canvas canvasGame) {
        canvas = canvasGame;
    }

    public double[] getProjectX() {
        return projectX;
    }

    public double[] getProjectY() {
        return projectY;
    }

    public double getCircleRadius() {
        return circleRadius;
    }

    public void setTcpClient(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
        tcpClient.setGame(this);
    }

    public TCPClient getTcpClient() {
        return tcpClient;
    }
}
