package client.admin;

import client.Main;
import database.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.NetworkUtil;

import java.io.IOException;
import java.util.Optional;

public class SearchClubController {
    private NetworkUtil networkUtil;
    private AdminReadThread clientReader;
    private Stage myStage;

    @FXML
    private TextField name;

    public void init(NetworkUtil networkUtil, AdminReadThread clientReader,Stage myStage) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myStage = myStage;
    }

    public void submit(ActionEvent event) throws IOException, InterruptedException {
        String clubName = name.getText().trim();
        networkUtil.write("search Club");
        networkUtil.write(clubName);
        Thread.sleep(50);
        if(clientReader.getMessage().equals("valid club")){
            Club myClub = clientReader.getMyClub();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("admin/clubDetails.fxml"));
            try {
                Parent root = loader.load();
                ClubDetailsController controller = (ClubDetailsController) loader.getController();
                controller.init(networkUtil,clientReader,myClub,myStage);
                Scene scene = new Scene(root, 850, 560);
                myStage.setTitle("Club Details");
                myStage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Node node = (Node) event.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();
        }else if(clientReader.getMessage().equals("invalid club")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Search Club");
            alert.setHeaderText("Warning!!");
            alert.setContentText("No Club Found With This Name");
            alert.showAndWait();
            Node node = (Node) event.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();
        }

    }

    public void cancel(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }
}
