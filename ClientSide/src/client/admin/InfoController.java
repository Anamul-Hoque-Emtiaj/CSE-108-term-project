package client.admin;

import client.Main;
import database.Club;
import database.Player;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class InfoController {
    private NetworkUtil networkUtil;
    private AdminReadThread clientReader;
    private Stage myStage;
    private int c1;
    private int c2;

    @FXML
    private ListView menuListView;
    @FXML
    private Text playerCount;
    @FXML
    private VBox countryVbox;
    @FXML
    private VBox clubVbox;
    @FXML
    private Text countryCount;
    @FXML
    private Text clubCount;

    public void init(NetworkUtil networkUtil, AdminReadThread clientReader,Stage myStage) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myStage = myStage;
        clientReader.setInfo(this);
        setMenu();
        try {
            networkUtil.write("send all players list");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        menuListView.getSelectionModel().select("Info");


        menuListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if(newValue.equals("Search Players")){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("admin/playerSearching.fxml"));
                        try {
                            Parent root = loader.load();
                            PlayerSearchingController controller = (PlayerSearchingController) loader.getController();
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
                            PendingRequestController controller = (PendingRequestController) loader.getController();
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
                            ChangePasswordController controller = (ChangePasswordController) loader.getController();
                            controller.init(networkUtil, clientReader,myStage);
                            Scene scene = new Scene(root, 400, 300);
                            Stage stage = new Stage();
                            stage.setTitle("Change Auth Info");
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void load(List<Player> playerList){
        List<String> club = new ArrayList<>();
        List<String> country = new ArrayList<>();
        for(Player player: playerList){
            if(!club.contains(player.getClub())){
                club.add(player.getClub());
            }
            if(!country.contains(player.getCountry())){
                country.add(player.getCountry());
            }
        }
        playerCount.setText("Number of Players: "+playerList.size());
        clubCount.setText("Number of Clubs: "+ club.size());
        countryCount.setText("Number of Countries: "+country.size());

        clubVbox.getChildren().remove(0,c1);
        countryVbox.getChildren().remove(0,c2);
        c1 = club.size();
        c2 = country.size();
        for (String c: country){
            int count = 0;
            for (Player player: playerList){
                if(player.getCountry().equals(c)){
                    count++;
                }
            }
            Label label = new Label(c+": "+count);
            label.setFont(Font.font("Cambria", 20));
            label.setPadding(new Insets(10, 10, 10, 10));
            countryVbox.getChildren().add(label);
        }
        for (String c: club){
            int count = 0;
            for (Player player: playerList){
                if(player.getClub().equals(c)){
                    count++;
                }
            }
            Label label = new Label(c+": "+count);
            label.setFont(Font.font("Cambria", 20));
            label.setPadding(new Insets(10, 10, 10, 10));
            clubVbox.getChildren().add(label);
        }
    }

    public void logOut(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log out");
        alert.setHeaderText("Warning!!");
        alert.setContentText("You have wanted to log out");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()==ButtonType.OK){
            Node node = (Node) event.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();
        }
    }
}
