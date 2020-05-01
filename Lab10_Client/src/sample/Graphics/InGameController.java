package sample.Graphics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.Mechanics.Game;

import java.io.IOException;

public class InGameController {
    private static Game game;
    private Thread gameThread;
    private static boolean localTurn;
    private static boolean humanFinished;

    @FXML
    Canvas canvasGame;

    @FXML
    Label labelPlayer, labelTurn, labelColor;

    public void initialize() {
        System.out.println("In game Controller initialized");
        humanFinished = false;
        game.setInGameController(this);
        game.setCanvas(canvasGame);
        gameThread = new Thread(game);
        gameThread.start();
    }

    public static void setGame(Game game) {
        InGameController.game = game;
    }

    public void pressButtonLeave(javafx.event.ActionEvent event) throws IOException {
        Parent localMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene localMenuScene = new Scene(localMenuParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(localMenuScene);
        window.show();

        gameThread.stop();
    }

    public static boolean isLocalTurn() {
        return localTurn;
    }

    public static void setLocalTurn(boolean localTurn) {
        InGameController.localTurn = localTurn;
    }

    public void mouseClickOnCanvas(javafx.scene.input.MouseEvent event) {
        if (!isLocalTurn()){
            return;
        }

        double mx = event.getX();
        double my = event.getY();

        int selectedX = -1;
        int selectedY = -1;
        boolean chosen = false;

        int indexX = 0;
        for (double x : game.getProjectX()){

            int indexY = 0;
            for (double y : game.getProjectY()){
                if (Math.sqrt((x-mx)*(x-mx) + (y-my)*(y-my)) < game.getCircleRadius() &&
                        game.getBoard().getBoard()[indexY][indexX] == -1){
                    selectedX = indexX;
                    selectedY = indexY;
                    chosen = true;
                    break;
                }
                indexY++;
            }

            if (chosen){
                break;
            }

            indexX++;
        }

        if (chosen){
            int playerTurn = game.getPlayerTurn();
            game.getPlayers()[playerTurn].setSelected(selectedX, selectedY);
            humanFinished = true;
        }
    }

    public static boolean isHumanFinished() {
        return humanFinished;
    }

    public static void setHumanFinished(boolean humanFinished) {
        InGameController.humanFinished = humanFinished;
    }

    public Label getLabelPlayer() {
        return labelPlayer;
    }

    public Label getLabelTurn() {
        return labelTurn;
    }

    public Label getLabelColor() {
        return labelColor;
    }
}
