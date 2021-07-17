package client.starting;

import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartingController {

    public void exit(ActionEvent event) {

    }

    public void adminLogin(ActionEvent event) {
    }

    public void clubOwnerLogin(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/login.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root, 300, 214);
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
