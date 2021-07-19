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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.ClientReadThread;
import server.ClientWriteThread;
import util.NetworkUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SearchPlayerController {
    @FXML
    private Button sell;
    @FXML
    private Button delete;
    @FXML
    private Button edit;
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
        load();
    }

    public void load(){

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
                    age.setText(String.valueOf("Age: "+currentPlayer.getAge()));
                    position.setText("Position: "+currentPlayer.getPosition());
                    height.setText("Height: "+String.valueOf(currentPlayer.getHeight()));
                    number.setText("Number: "+String.valueOf(currentPlayer.getNumber()));
                    salary.setText("Weekly Salary: "+String.valueOf(currentPlayer.getWeeklySalary()));
                    club.setText("Club: "+currentPlayer.getClub());
                    country.setText("Country: "+currentPlayer.getCountry());
                    name.setText("Name: "+currentPlayer.getName());
                    sell.setText("Sell");
                    edit.setText("Edit");
                    delete.setText("Delete");

                    try {
                        System.out.println(currentPlayer.getImageName());
                        File img = new File(System.getProperty("user.dir")+"\\src\\client\\img\\"+currentPlayer.getImageName());
                        Image image = new Image(new FileInputStream(img));
                        imageView.setImage(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public void back(ActionEvent event) {
        try {
            networkUtil.write("clubOwner,sendMyClub");
            networkUtil.write(myClub.getName());
            Thread.sleep(100);
            this.myClub = clientReader.getMyClub();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

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
            thisStage.setTitle("Edit Player Info");
            thisStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Warning!!");
        alert.setContentText("Deleting this player");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()==ButtonType.OK){

            networkUtil.write("clubOwner,deletePlayer");
            networkUtil.write(currentPlayer);
            playerList.remove(currentPlayer);
            load();
        }
    }

    public void sell(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/sellRequest.fxml"));
        try {
            Parent root = loader.load();
            SellRequestController controller = (SellRequestController) loader.getController();
            controller.init(networkUtil,clientReader,myClub,currentPlayer,playerList);
            Scene scene = new Scene(root, 330, 250);
            Stage stage = new Stage();
            stage.setTitle("Sell Player");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
