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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.ClientReadThread;
import util.NetworkUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PendingRequestController implements Runnable{
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;
    private List<Player> playerList;
    private Player currentPlayer;

    @FXML
    private Text height;
    @FXML
    private Text age;
    @FXML
    private Text salary;
    @FXML
    private Text club;
    @FXML
    private Text position;
    @FXML
    private Text number;
    @FXML
    private Text name;
    @FXML
    private Text country;
    @FXML
    private ImageView imageView;
    @FXML
    private ListView listView;
    @FXML
    private Button delete;
    @FXML
    private Text amount;

    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        playerList = new ArrayList<>();
        try {
            networkUtil.write("send updated buy list");
            Thread.sleep(100);
            load();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        List<Player> list = clientReader.getUpdatedBuyList();
        List<Player> temp = new ArrayList<>();
        for (Player player: list){
            if(myClub.getName().equals(player.getClub())){
                temp.add(player);
            }
        }
        playerList = temp;

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
                    delete.setText("Delete Request");
                    amount.setText(String.valueOf(currentPlayer.getAmount()));

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


    @Override
    public void run() {
        while (true){
            if (clientReader.isUpdateNeeded()){
                load();
            }
        }
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

    public void deleteRequest(ActionEvent event) throws IOException, InterruptedException {
        networkUtil.write("delete request");
        networkUtil.write(currentPlayer);
        Thread.sleep(100);
        load();
    }
}
