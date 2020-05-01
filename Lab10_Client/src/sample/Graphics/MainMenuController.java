package sample.Graphics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;
import sample.Mechanics.Game;
import sample.Mechanics.GameType;
import sample.Network.TCPClient;

import java.io.IOException;

public class MainMenuController {

    @FXML
    Button buttonLocal, buttonBrowse, buttonExit;

    public void pressButtonLocal(javafx.event.ActionEvent event) throws IOException {
        Parent localMenuParent = FXMLLoader.load(getClass().getResource("LocalMenu.fxml"));
        Scene localMenuScene = new Scene(localMenuParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(localMenuScene);
        window.show();
    }

    public void pressBrowseButton(javafx.event.ActionEvent event) throws IOException {
        TCPClient tcpClient = new TCPClient();
        tcpClient.start();

        while (!tcpClient.isGameCreated()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Game game = new Game(GameType.NetVsHuman, tcpClient.isHost());
        game.setTcpClient(tcpClient);
        InGameController.setGame(game);

        Parent localMenuParent = FXMLLoader.load(getClass().getResource("InGame.fxml"));
        Scene localMenuScene = new Scene(localMenuParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(localMenuScene);
        window.show();
    }

    public void pressButtonExit(){
        Stage stage = (Stage) buttonExit.getScene().getWindow();
        stage.close();
    }
}
