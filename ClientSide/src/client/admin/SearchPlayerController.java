package client.admin;

import client.ClientReadThread;
import client.Main;
import client.clubOwner.AddPlayerController;
import client.clubOwner.BuyPlayerController;
import client.clubOwner.PlayerEditController;
import client.clubOwner.SellRequestController;
import database.Club;
import database.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.NetworkUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SearchPlayerController {
    private NetworkUtil networkUtil;
    private AdminReadThread clientReader;
    private Stage myStage;
    private List<Player> playerList;
    private Player currentPlayer;

    @FXML
    private ImageView imageView;
    @FXML
    private ListView<String> listView;
    @FXML
    private Text country;
    @FXML
    private Text name;
    @FXML
    private Text number;
    @FXML
    private Text position;
    @FXML
    private Text club;
    @FXML
    private Text salary;
    @FXML
    private Text age;
    @FXML
    private Text height;
    @FXML
    private ListView menuListView;




    public void init(NetworkUtil networkUtil, AdminReadThread clientReader, List<Player>list,Stage myStage) throws IOException {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.playerList = list;
        this.myStage = myStage;
        currentPlayer = new Player();
        clientReader.setSearchPlayer(this);
        setMenu();
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
        menuListView.getSelectionModel().select("Search Players");


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

    public void load(List<Player> allPlayerList){
        List<Player> t = new ArrayList<>();
        ObservableList names = FXCollections.observableArrayList();
        for (Player player: playerList){
            for (Player p: allPlayerList){
                if(player.getName().equals(p.getName())){
                    names.add(player.getName());
                    t.add(p);
                }
            }
        }
        playerList = t;
        listView.setItems(names);
        listView.setStyle("-fx-font-size: 16px; -fx-font-family: 'SketchFlow Print';");

        if(names.size()>0){
            int in = 0;
            if(names.contains(currentPlayer.getName())){
                in = names.indexOf(currentPlayer.getName());
            }
            listView.getSelectionModel().select(in);
            String pName = (String) names.get(in);
            for(Player player: playerList){
                if(player.getName().equals(pName)){
                    currentPlayer = player;
                    break;
                }
            }
            age.setText(String.valueOf("Age: "+currentPlayer.getAge()));
            position.setText("Position: "+currentPlayer.getPosition());
            height.setText("Height: "+String.valueOf(currentPlayer.getHeight()));
            number.setText("Number: "+String.valueOf(currentPlayer.getNumber()));
            salary.setText("Weekly Salary: "+String.valueOf(currentPlayer.getWeeklySalary()));
            club.setText("Club: "+currentPlayer.getClub());
            country.setText("Country: "+currentPlayer.getCountry());
            name.setText("Name: "+currentPlayer.getName());

            try {
                File img = new File(System.getProperty("user.dir")+"\\src\\client\\img\\"+currentPlayer.getImageName());
                Image image = new Image(new FileInputStream(img));
                imageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            age.setText(null);
            position.setText(null);
            height.setText(null);
            number.setText(null);
            salary.setText(null);
            club.setText(null);
            country.setText(null);
            name.setText(null);
            imageView.setImage(null);
        }

        listView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    for(Player player: playerList){
                        if(player.getName().equals(newValue)){
                            currentPlayer=player;
                            break;
                        }
                    }
                    age.setText(String.valueOf("Age: "+currentPlayer.getAge()));
                    position.setText("Position: "+currentPlayer.getPosition());
                    height.setText("Height: "+String.valueOf(currentPlayer.getHeight()));
                    number.setText("Number: "+String.valueOf(currentPlayer.getNumber()));
                    salary.setText("Weekly Salary: "+String.valueOf(currentPlayer.getWeeklySalary()));
                    club.setText("Club: "+currentPlayer.getClub());
                    country.setText("Country: "+currentPlayer.getCountry());
                    name.setText("Name: "+currentPlayer.getName());

                    try {
                        File img = new File(System.getProperty("user.dir")+"\\src\\client\\img\\"+currentPlayer.getImageName());
                        Image image = new Image(new FileInputStream(img));
                        imageView.setImage(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
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
