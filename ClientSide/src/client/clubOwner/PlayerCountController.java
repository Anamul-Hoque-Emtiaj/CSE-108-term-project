package client.clubOwner;

import client.Main;
import database.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import server.ClientReadThread;
import util.NetworkUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class PlayerCountController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;
    @FXML
    private VBox vbox;

    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        try {
            networkUtil.write("clubOwner,sendMyClub");
            networkUtil.write(myClub.getName());
            Thread.sleep(100);
            this.myClub = clientReader.getMyClub();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        HashMap<String, Integer> playerCount = myClub.countryWisePlayerCount();
        for (String country: playerCount.keySet()){
            int count = playerCount.get(country);
            Label label = new Label(count+" players from "+country);
            label.setFont(Font.font("Cambria", 22));
            label.setPadding(new Insets(10, 10, 10, 10));
            vbox.getChildren().add(label);
        }
    }
    public void back(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/menu.fxml"));
        try {
            Parent root = loader.load();
            MenuController controller = (MenuController) loader.getController();
            controller.init(networkUtil,clientReader,myClub);
            Scene scene = new Scene(root, 600, 400);
            thisStage.setTitle("Club Menu");
            thisStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
