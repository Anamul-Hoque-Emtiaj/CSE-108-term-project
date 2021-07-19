package client.starting;

import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import server.ClientReadThread;
import util.NetworkUtil;

import java.io.IOException;
import java.util.Optional;

public class StartingController {
    public final String SERVER_ADDRESS = "127.0.0.1";
    public final int SERVER_PORT = 44444;
    private NetworkUtil networkUtil;
    public void connect(){
        try {
            this.networkUtil = new NetworkUtil(SERVER_ADDRESS, SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void exit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Warning!!");
        alert.setContentText("You have wanted to Exit");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()==ButtonType.OK){
            try {
                networkUtil.write("exit");
                networkUtil.closeConnection();
                Node node = (Node) event.getSource();
                Stage thisStage = (Stage) node.getScene().getWindow();
                thisStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
