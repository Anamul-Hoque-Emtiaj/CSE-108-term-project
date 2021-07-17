package client.clubOwner;

import client.Main;
import database.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.ClientReadThread;
import server.ClientWriteThread;
import util.NetworkUtil;

import java.io.IOException;

public class PlayerSearchingController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;

    @FXML
    private TextField name;
    @FXML
    private TextField number;
    @FXML
    private TextField salaryFrom;
    @FXML
    private TextField ageFrom;
    @FXML
    private TextField heightFrom;
    @FXML
    private TextField salaryTo;
    @FXML
    private TextField ageTo;
    @FXML
    private TextField heightTo;
    @FXML
    private ComboBox position;
    @FXML
    private ComboBox country;
    @FXML
    private ChoiceBox minimum;
    @FXML
    private ChoiceBox maximum;


    public void init (NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        String clubName = myClub.getName();
        try {
            writeToServer("clubOwner,sendMyClub");
            Thread.sleep(50);
            String s = (String) clientReader.getReceivedFile();
            writeToServer(clubName);
            Thread.sleep(50);
            this.myClub = (Club) clientReader.getReceivedFile();
            System.out.println(this.myClub.getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void writeToServer(String message){
        new ClientWriteThread(networkUtil,message);
    }


    public void submit(ActionEvent event) {
    }

    public void cancel(ActionEvent event) {
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
