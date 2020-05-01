package sample.Graphics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.Mechanics.Game;
import sample.Mechanics.GameType;

import java.io.IOException;

public class LocalMenuController {

    @FXML
    Button buttonHuman, buttonAI, buttonBack;

    public void pressButtonHuman(javafx.event.ActionEvent event) throws IOException {
        Game game = new Game(GameType.LocalVsHuman, true);
        InGameController.setGame(game);

        Parent localMenuParent = FXMLLoader.load(getClass().getResource("InGame.fxml"));
        Scene localMenuScene = new Scene(localMenuParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(localMenuScene);
        window.show();
    }

    public void pressButtonAI(javafx.event.ActionEvent event) throws IOException {
        Game game = new Game(GameType.LocalVsAI, true);
        InGameController.setGame(game);

        Parent localMenuParent = FXMLLoader.load(getClass().getResource("InGame.fxml"));
        Scene localMenuScene = new Scene(localMenuParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(localMenuScene);
        window.show();
    }

    public void pressButtonBack(javafx.event.ActionEvent event) throws IOException {
        Parent localMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene localMenuScene = new Scene(localMenuParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(localMenuScene);
        window.show();
    }
}
