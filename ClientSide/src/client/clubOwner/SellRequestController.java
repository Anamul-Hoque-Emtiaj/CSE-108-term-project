package client.clubOwner;

import client.Main;
import database.Club;
import database.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.ClientReadThread;
import util.NetworkUtil;

import java.io.IOException;
import java.util.List;

public class SellRequestController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private List<Player> playerList;
    private Club myClub;
    private Player player;

    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub,Player player,List<Player>list){
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        this.player = player;
        playerList = list;
    }
    public void submit(ActionEvent event) {
    }

    public void cancel(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/searchPlayer.fxml"));
        try {
            Parent root = loader.load();
            SearchPlayerController controller = (SearchPlayerController) loader.getController();
            controller.init(networkUtil,clientReader,myClub,playerList);
            Scene scene = new Scene(root, 600, 400);
            thisStage.setTitle("Player's Details");
            thisStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
