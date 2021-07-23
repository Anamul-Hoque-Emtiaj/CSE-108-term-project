package client.clubOwner;

import client.Main;
import database.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import client.ClientReadThread;
import util.NetworkUtil;

import java.io.IOException;
import java.util.Optional;

public class MenuController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;

    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        try {
            networkUtil.write("clubOwner,sendMyClub");
            networkUtil.write(myClub.getName());
            Thread.sleep(100);
            this.myClub = clientReader.getMyClub();
        }catch (Exception e){
            System.out.println(e);
        }
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
            Scene scene = new Scene(root, 650, 550);
            thisStage.setTitle("Search");
            thisStage.setScene(scene);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/addPlayer.fxml"));
        try {
            Parent root = loader.load();
            AddPlayerController controller = (AddPlayerController) loader.getController();
            controller.init(networkUtil,clientReader,myClub);
            Scene scene = new Scene(root, 600, 400);
            thisStage.setTitle("Add Player");
            thisStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buyPlayer(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/buyPlayer.fxml"));
        try {
            Parent root = loader.load();
            BuyPlayerController controller = (BuyPlayerController) loader.getController();
            controller.init(networkUtil,clientReader,myClub);
            Scene scene = new Scene(root, 600, 400);
            thisStage.setTitle("Add Player");
            thisStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/changePassword.fxml"));
        try {
            Parent root = loader.load();
            ChangePasswordController controller = (ChangePasswordController) loader.getController();
            controller.init(networkUtil,clientReader,myClub);
            Scene scene = new Scene(root, 400, 300);
            Stage stage = new Stage();
            stage.setTitle("Change Password");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void pendingRequest(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/pendingRequest.fxml"));
        try {
            Parent root = loader.load();
            PendingRequestController controller = (PendingRequestController) loader.getController();
            controller.init(networkUtil,clientReader,myClub);
            Scene scene = new Scene(root, 600, 400);
            thisStage.setTitle("Add Player");
            thisStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log out");
        alert.setHeaderText("Warning!!");
        alert.setContentText("You have wanted to log out");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()==ButtonType.OK){
            networkUtil.write("logout");
            networkUtil.write(myClub.getName());
            Node node = (Node) event.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();
        }
    }

    public void clubDetails(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/clubDetails.fxml"));
        try {
            Parent root = loader.load();
            ClubDetailsController controller = (ClubDetailsController) loader.getController();
            controller.init(networkUtil,clientReader,myClub);
            Scene scene = new Scene(root, 600, 400);
            thisStage.setTitle("Add Player");
            thisStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
