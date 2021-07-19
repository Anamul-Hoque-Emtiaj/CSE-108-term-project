package client;

import client.clubOwner.MenuController;
import client.starting.StartingController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("starting/starting.fxml"));
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();*/

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("starting/starting.fxml"));
        try {
            Parent root = loader.load();
            StartingController controller = (StartingController) loader.getController();
            controller.connect();
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("Club Menu");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
