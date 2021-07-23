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
import client.ClientReadThread;
import util.NetworkUtil;

import java.io.IOException;
import java.util.HashMap;

public class ClubDetailsController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;
    private int c;
    @FXML
    private VBox vbox;

    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        try {
            clientReader.setClubDetails(this);
            networkUtil.write("clubOwner,sendMyClub");
            networkUtil.write(myClub.getName());
        } catch (  IOException e) {
            e.printStackTrace();
        }
    }
    public void load(Club club){
        vbox.getChildren().remove(0,c);
        myClub = club;
        HashMap<String, Integer> playerCount = myClub.countryWisePlayerCount();
        c = playerCount.size();
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
