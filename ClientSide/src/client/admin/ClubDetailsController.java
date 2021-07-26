package client.admin;

import client.ClientReadThread;
import client.Main;
import client.clubOwner.*;
import database.Club;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.NetworkUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class ClubDetailsController {
    private NetworkUtil networkUtil;
    private AdminReadThread clientReader;
    private Club myClub;
    private Stage myStage;
    private int c;
    @FXML
    private VBox vbox;
    @FXML
    private Text clubTitle;
    @FXML
    private ListView menuListView;
    @FXML
    private Text name;
    @FXML
    private Text balance;
    @FXML
    private Text playerCount;
    @FXML
    private Text totalSalary;

    public void init(NetworkUtil networkUtil, AdminReadThread clientReader, Club myClub,Stage myStage) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        this.myStage = myStage;
        setMenu();
        load();
    }

    public void setMenu(){
        ObservableList menu = FXCollections.observableArrayList();
        menu.add("Info");
        menu.add("Search Players");
        menu.add("Search Club");
        menu.add("Sell Requested Players");
        menu.add("Add Club");
        menu.add("Change Auth Info");

        menuListView.setItems(menu);

        menuListView.setStyle("-fx-font-size: 20px; -fx-font-family: 'SketchFlow Print';");
        menuListView.getSelectionModel().select("Search Club");


        menuListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if(newValue.equals("Search Players")){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("admin/playerSearching.fxml"));
                        try {
                            Parent root = loader.load();
                            client.admin.PlayerSearchingController controller = (PlayerSearchingController) loader.getController();
                            controller.init(networkUtil,clientReader,myStage);
                            Scene scene = new Scene(root, 650, 550);
                            Stage stage = new Stage();
                            stage.setTitle("Search Players");
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else if(newValue.equals("Add Club")){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("admin/addClub.fxml"));
                        try {
                            Parent root = loader.load();
                            AddClubController controller = (AddClubController) loader.getController();
                            controller.init(networkUtil,clientReader,myStage);
                            Scene scene = new Scene(root, 275, 235);
                            Stage stage = new Stage();
                            stage.setTitle("Add Club");
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else if(newValue.equals("Sell Requested Players")){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("admin/pendingRequest.fxml"));
                        try {
                            Parent root = loader.load();
                            client.admin.PendingRequestController controller = (PendingRequestController) loader.getController();
                            controller.init(networkUtil,clientReader,myStage);
                            Scene scene = new Scene(root, 850, 560);
                            myStage.setTitle("Sell Requested Players");
                            myStage.setScene(scene);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else if(newValue.equals("Search Club")){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("admin/searchClub.fxml"));
                        try {
                            Parent root = loader.load();
                            SearchClubController controller = (SearchClubController) loader.getController();
                            controller.init(networkUtil,clientReader,myStage);
                            Scene scene = new Scene(root, 275, 235);
                            Stage stage = new Stage();
                            stage.setTitle("Search Club");
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if(newValue.equals("Change Auth Info")){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("admin/changePassword.fxml"));
                        try {
                            Parent root = loader.load();
                            client.admin.ChangePasswordController controller = (ChangePasswordController) loader.getController();
                            controller.init(networkUtil, clientReader,myStage);
                            Scene scene = new Scene(root, 400, 300);
                            Stage stage = new Stage();
                            stage.setTitle("Change Auth Info");
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if(newValue.equals("Info")){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("admin/info.fxml"));
                        try {
                            Parent root = loader.load();
                            InfoController controller = (InfoController) loader.getController();
                            controller.init(networkUtil,clientReader,myStage);
                            Scene scene = new Scene(root, 850, 560);
                            myStage.setTitle("Info");
                            myStage.setScene(scene);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void load(){
        name.setText("Name: "+myClub.getName());
        balance.setText("Balance: "+ String.format("%.2f",myClub.getBalance()));
        playerCount.setText("Number of Players: "+myClub.playerCount());
        totalSalary.setText("Players total yearly Salary: "+String.format("%.2f",myClub.totalYearlySalary()));

        vbox.getChildren().remove(0,c);
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

    public void logOut(ActionEvent event) throws IOException {
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
}
