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
import client.ClientReadThread;
import util.NetworkUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SearchPlayerController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;
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
    private Button sell;
    @FXML
    private Button delete;
    @FXML
    private ListView menuListView;
    @FXML
    private Text clubTitle;
    @FXML
    private Button edit;


    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub,List<Player>list,Stage myStage) throws IOException {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        this.playerList = list;
        this.myStage = myStage;
        currentPlayer = new Player();
        clientReader.setSearchPlayer(this);
        networkUtil.write("clubOwner,sendMyClub");
        networkUtil.write(myClub.getName());
        clubTitle.setText(myClub.getName());
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
        menuListView.getSelectionModel().select("Search Players");

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
                    }else if(newValue.equals("Club's Details")){
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("clubOwner/clubDetails.fxml"));
                        try {
                            Parent root = loader.load();
                            ClubDetailsController controller = (ClubDetailsController) loader.getController();
                            controller.init(networkUtil,clientReader,myClub,myStage);
                            Scene scene = new Scene(root, 850, 560);
                            myStage.setTitle("Club's Details");
                            myStage.setScene(scene);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void load(Club c){
        myClub = c;
        List<Player> t = new ArrayList<>();
        ObservableList names = FXCollections.observableArrayList();
        for (Player player: playerList){
            for (Player p: myClub.getPlayerList()){
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
            sell.setText("Sell");
            edit.setText("Edit");
            delete.setText("Delete");

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
            sell.setText(null);
            edit.setText(null);
            delete.setText(null);
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
                    sell.setText("Sell");
                    edit.setText("Edit");
                    delete.setText("Delete");

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

    public void edit(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/playerEdit.fxml"));
        try {
            Parent root = loader.load();
            PlayerEditController controller = (PlayerEditController) loader.getController();
            controller.init(networkUtil,clientReader,myClub,currentPlayer);
            Scene scene = new Scene(root, 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Edit Player Info");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(ActionEvent event) throws IOException, InterruptedException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Warning!!");
        alert.setContentText("Deleting this player");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()==ButtonType.OK){

            networkUtil.write("clubOwner,deletePlayer");
            networkUtil.write(currentPlayer);
            playerList.remove(currentPlayer);
        }
    }

    public void sell(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clubOwner/sellRequest.fxml"));
        try {
            Parent root = loader.load();
            SellRequestController controller = (SellRequestController) loader.getController();
            controller.init(networkUtil,clientReader,myClub,currentPlayer);
            Scene scene = new Scene(root, 320, 235);
            Stage stage = new Stage();
            stage.setTitle("Sell Player");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
