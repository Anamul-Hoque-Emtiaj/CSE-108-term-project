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
import util.NetworkUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PendingRequestController implements Runnable{
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;
    private List<Player> playerList;
    private Player currentPlayer;
    private boolean endThread;

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
        endThread = false;
        try {
            networkUtil.write("send updated buy list");
            Thread.sleep(100);
            load();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        System.out.println("loaded");
        age.setText(null);
        position.setText(null);
        height.setText(null);
        number.setText(null);
        salary.setText(null);
        club.setText(null);
        country.setText(null);
        name.setText(null);
        delete.setText(null);
        amount.setText(null);
        imageView.setImage(null);
        currentPlayer = null;

        playerList = clientReader.getUpdatedBuyList();

        ObservableList names = FXCollections.observableArrayList();
        for (Player player: playerList){
            if(player.getClub().equals(myClub.getName())){
                names.add(player.getName());
            }
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
                    if(currentPlayer!=null){
                        age.setText(String.valueOf("Age: "+currentPlayer.getAge()));
                        position.setText("Position: "+currentPlayer.getPosition());
                        height.setText("Height: "+String.valueOf(currentPlayer.getHeight()));
                        number.setText("Number: "+String.valueOf(currentPlayer.getNumber()));
                        salary.setText("Weekly Salary: "+String.valueOf(currentPlayer.getWeeklySalary()));
                        club.setText("Club: "+currentPlayer.getClub());
                        country.setText("Country: "+currentPlayer.getCountry());
                        name.setText("Name: "+currentPlayer.getName());
                        delete.setText("Delete Request");
                        amount.setText("Price: "+String.valueOf(currentPlayer.getAmount()));

                        try {
                            System.out.println(currentPlayer.getImageName());
                            File img = new File(System.getProperty("user.dir")+"\\src\\client\\img\\"+currentPlayer.getImageName());
                            Image image = new Image(new FileInputStream(img));
                            imageView.setImage(image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }


    @Override
    public void run() {
        try {
            while (true){
                if(clientReader.isUpdateNeeded()){
                    System.out.println("need update");
                    load();
                    clientReader.setUpdateNeeded(false);
                }
                if(endThread){
                    break;
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void back(ActionEvent event) {
        endThread = true;
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Warning!!");
        alert.setContentText("Deleting this player's sell request");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()==ButtonType.OK){

            networkUtil.write("delete request");
            networkUtil.write(currentPlayer);
            Thread.sleep(100);
            load();
        }

    }
}
