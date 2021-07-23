package client.clubOwner;

import database.Club;
import database.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import client.ClientReadThread;
import util.NetworkUtil;

import java.io.IOException;

public class SellRequestController {
    @FXML
    private TextField amount;
    @FXML
    private Text name;
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;
    private Player player;

    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub,Player player){
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        this.player = player;
        name.setText(player.getName());
    }

    public void submit(ActionEvent event) throws InterruptedException, IOException {
        String pAmount = amount.getText().trim();
        if(Double.parseDouble(pAmount)>=0){
            networkUtil.write("clubOwner,sellRequest");
            networkUtil.write(myClub.getName()+","+player.getName()+","+pAmount);
            Thread.sleep(100);
            if(clientReader.getMessage().equals("already requested")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Request Denied!!");
                alert.setHeaderText("Warning");
                alert.setContentText("Already requested for sell");
                alert.showAndWait();
            }else if(clientReader.getMessage().equals("request accepted")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Request Accepted!!");
                alert.setHeaderText("Confirmation");
                alert.setContentText("Player requested for sell");
                alert.showAndWait();
            }

        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Request Denied!!");
            alert.setHeaderText("Warning");
            alert.setContentText("Invalid Amount given");
            alert.showAndWait();
        }
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    public void cancel(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }
}
