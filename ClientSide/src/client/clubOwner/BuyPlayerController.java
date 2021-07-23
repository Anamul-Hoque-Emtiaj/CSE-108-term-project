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

    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        currentPlayer = new Player();
        try {
            clientReader.setBuyPlayer(this);
            networkUtil.write("send updated buy list");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(List<Player> playerList){
        ObservableList names = FXCollections.observableArrayList();
        for (Player player: playerList){
            if(!player.getClub().equals(myClub.getName())){
                names.add(player.getName());
            }
        }
        listView.setItems(names);

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
            buy.setText("Buy Player");
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
                    buy.setText("Buy Player");
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
}
