package client.clubOwner;

import client.Main;
import database.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.ClientReadThread;
import server.ClientWriteThread;
import util.NetworkUtil;

import java.io.IOException;

public class MenuController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;

    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
    }

    public void searchPlayer(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/playerSearching.fxml"));
        try {
            Parent root = loader.load();
            PlayerSearchingController controller = (PlayerSearchingController) loader.getController();
            controller.init(networkUtil,clientReader,myClub);
            Scene scene = new Scene(root, 600, 400);
            thisStage.setTitle("Search");
            thisStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(ActionEvent event) {
    }

    public void buyPlayer(ActionEvent event) {
    }

    public void changePassword(ActionEvent event) {
    }


    public void pendingRequest(ActionEvent event) {
    }

    public void logout(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    public void playerCount(ActionEvent event) {
    }
}
