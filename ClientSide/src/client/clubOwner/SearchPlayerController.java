package client.clubOwner;

import client.Main;
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
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.ClientReadThread;
import util.NetworkUtil;

import java.io.IOException;
import java.util.List;

public class SearchPlayerController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;
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


    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub,List<Player>list){
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        this.playerList = list;

        currentPlayer = playerList.get(0);
        age.setText(String.valueOf(currentPlayer.getAge()));
        position.setText(currentPlayer.getPosition());
        height.setText(String.valueOf(currentPlayer.getHeight()));
        number.setText(String.valueOf(currentPlayer.getNumber()));
        salary.setText(String.valueOf(currentPlayer.getWeeklySalary()));
        club.setText(currentPlayer.getClub());
        country.setText(currentPlayer.getCountry());
        name.setText(currentPlayer.getName());
        try {

            Image image = new Image(Main.class.getResourceAsStream("img/"+currentPlayer.getImageName()));
            imageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObservableList names = FXCollections.observableArrayList();
        for (Player player: playerList){
            names.add(player.getName());
        }

        listView.setItems(names);

        listView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    for(Player player: playerList){
                        if(player.getName().equals(newValue)){
                            currentPlayer=player;
                            break;
                        }
                    }
                    age.setText(String.valueOf(currentPlayer.getAge()));
                    position.setText(currentPlayer.getPosition());
                    height.setText(String.valueOf(currentPlayer.getHeight()));
                    number.setText(String.valueOf(currentPlayer.getNumber()));
                    salary.setText(String.valueOf(currentPlayer.getWeeklySalary()));
                    club.setText(currentPlayer.getClub());
                    country.setText(currentPlayer.getCountry());
                    name.setText(currentPlayer.getName());
                    try {

                        Image image = new Image(Main.class.getResourceAsStream("img/"+currentPlayer.getImageName()));
                        imageView.setImage(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
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

    public void edit(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/playerEdit.fxml"));
        try {
            Parent root = loader.load();
            PlayerEditController controller = (PlayerEditController) loader.getController();
            controller.init(networkUtil,clientReader,myClub,currentPlayer,playerList);
            Scene scene = new Scene(root, 600, 400);
            thisStage.setTitle("Edit Player");
            thisStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(ActionEvent event) {
    }

    public void sell(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/sellRequest.fxml"));
        try {
            Parent root = loader.load();
            SellRequestController controller = (SellRequestController) loader.getController();
            controller.init(networkUtil,clientReader,myClub,currentPlayer,playerList);
            Scene scene = new Scene(root, 330, 250);
            thisStage.setTitle("Sell Player");
            thisStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
