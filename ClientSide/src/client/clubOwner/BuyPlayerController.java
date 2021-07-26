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
import java.util.List;
import java.util.Optional;

public class BuyPlayerController{

    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;
    private Player currentPlayer;
    private Stage myStage;

    @FXML
    private ListView listView;
    @FXML
    private ImageView imageView;
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
    private Button buy;
    @FXML
    private Text amount;
    @FXML
    private ListView menuListView;
    @FXML
    private Text clubTitle;

    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub,Stage myStage) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        this.myStage = myStage;
        currentPlayer = new Player();
        try {
            clientReader.setBuyPlayer(this);
            networkUtil.write("send updated buy list");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        menuListView.getSelectionModel().select("Buy Players");

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

    public void load(List<Player> playerList){
        ObservableList names = FXCollections.observableArrayList();
        for (Player player: playerList){
            if(!player.getClub().equals(myClub.getName())){
                names.add(player.getName());
            }
        }
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
            buy.setText("Buy");
            amount.setText("Price: "+String.valueOf(currentPlayer.getAmount()));

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
            buy.setText(null);
            amount.setText(null);
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
                    buy.setText("Buy");
                    amount.setText("Price: "+String.valueOf(currentPlayer.getAmount()));

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

    public void buy(ActionEvent event) throws IOException, InterruptedException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Buy Player");
        alert.setHeaderText("Confirm");
        alert.setContentText("Buying "+currentPlayer.getName()+" at "+currentPlayer.getAmount());
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()==ButtonType.OK){
            if(myClub.getBalance()>=currentPlayer.getAmount()){
                networkUtil.write("buy Player");
                networkUtil.write(myClub.getName());
                networkUtil.write(currentPlayer);
                Thread.sleep(100);
                if(clientReader.getMessage().equals("Successful")){
                    currentPlayer.setClub(myClub.getName());
                    Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                    alert1.setTitle("Buy Player");
                    alert1.setHeaderText("Successful");
                    alert1.setContentText("Buying Player successful");
                    alert1.showAndWait();

                }else if(clientReader.getMessage().equals("failed")){
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Buy Player");
                    alert1.setHeaderText("Failed");
                    alert1.setContentText("Cannot buy this player");
                    alert1.showAndWait();
                }
            }else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Buy Player");
                alert1.setHeaderText("Failed");
                alert1.setContentText("You don't have enough balance for buying this player");
                alert1.showAndWait();
            }
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
