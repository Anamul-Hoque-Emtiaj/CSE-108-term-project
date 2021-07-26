package client.clubOwner;

import client.Main;
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
import client.ClientReadThread;
import util.NetworkUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class ClubDetailsController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
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

    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub,Stage myStage) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        this.myStage = myStage;
        try {
            clientReader.setClubDetails(this);
            networkUtil.write("clubOwner,sendMyClub");
            networkUtil.write(myClub.getName());
        } catch (  IOException e) {
            e.printStackTrace();
        }
        setMenu();
    }

    public void setMenu(){
        ObservableList menu = FXCollections.observableArrayList();
        menu.add("Club's Details");
        menu.add("Search Players");
        menu.add("Add Player");
        menu.add("Sell Requested Players");
        menu.add("Buy Players");
        menu.add("Change Password");

        menuListView.setItems(menu);

        menuListView.setStyle("-fx-font-size: 20px; -fx-font-family: 'SketchFlow Print';");
        menuListView.getSelectionModel().select("Club's Details");


        menuListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if(newValue.equals("Search Players")){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("clubOwner/playerSearching.fxml"));
                        try {
                            Parent root = loader.load();
                            PlayerSearchingController controller = (PlayerSearchingController) loader.getController();
                            controller.init(networkUtil,clientReader,myClub,myStage);
                            Scene scene = new Scene(root, 650, 550);
                            Stage stage = new Stage();
                            stage.setTitle("Search Players");
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else if(newValue.equals("Add Player")){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("clubOwner/addPlayer.fxml"));
                        try {
                            Parent root = loader.load();
                            AddPlayerController controller = (AddPlayerController) loader.getController();
                            controller.init(networkUtil,clientReader,myClub,myStage);
                            Scene scene = new Scene(root, 600, 400);
                            Stage stage = new Stage();
                            stage.setTitle("Add Player");
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else if(newValue.equals("Sell Requested Players")){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("clubOwner/pendingRequest.fxml"));
                        try {
                            Parent root = loader.load();
                            PendingRequestController controller = (PendingRequestController) loader.getController();
                            controller.init(networkUtil,clientReader,myClub,myStage);
                            Scene scene = new Scene(root, 850, 560);
                            myStage.setTitle("Sell Requested Players");
                            myStage.setScene(scene);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else if(newValue.equals("Buy Players")){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("clubOwner/buyPlayer.fxml"));
                        try {
                            Parent root = loader.load();
                            BuyPlayerController controller = (BuyPlayerController) loader.getController();
                            controller.init(networkUtil,clientReader,myClub,myStage);
                            Scene scene = new Scene(root, 850, 560);
                            myStage.setTitle("Buy Players");
                            myStage.setScene(scene);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if(newValue.equals("Change Password")){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("clubOwner/changePassword.fxml"));
                        try {
                            Parent root = loader.load();
                            ChangePasswordController controller = (ChangePasswordController) loader.getController();
                            controller.init(networkUtil,clientReader,myClub,myStage);
                            Scene scene = new Scene(root, 400, 300);
                            Stage stage = new Stage();
                            stage.setTitle("Change Password");
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void load(Club club){
        clubTitle.setText(club.getName());
        name.setText("Name: "+club.getName());
        balance.setText("Balance: " +String.format("%.2f",club.getBalance()));
        playerCount.setText("Number of Players: "+club.playerCount());
        totalSalary.setText("Players total yearly Salary: "+String.format("%.2f",club.totalYearlySalary()));

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
