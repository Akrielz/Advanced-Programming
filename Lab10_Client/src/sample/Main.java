package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Graphics/MainMenu.fxml"));
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        //TCPClient tcpClient1 = new TCPClient();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
