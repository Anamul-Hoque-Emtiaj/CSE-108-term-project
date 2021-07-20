package client.clubOwner;

import client.Main;
import database.Club;
import database.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import server.ClientReadThread;
import server.ClientWriteThread;
import util.NetworkUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PlayerEditController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;
    private Player player;

    @FXML
    private Text name;
    @FXML
    private Text club;
    @FXML
    private Text country;
    @FXML
    private Text position;
    @FXML
    private TextField age;
    @FXML
    private TextField height;
    @FXML
    private TextField salary;
    @FXML
    private TextField number;
    @FXML
    private Text fileName;

    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub,Player player){
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        this.player = player;
        name.setText(player.getName());
        club.setText(player.getClub());
        country.setText(player.getCountry());
        position.setText(player.getPosition());
        age.setText(String.valueOf(player.getAge()));
        height.setText(String.valueOf(player.getHeight()));
        salary.setText(String.valueOf(player.getWeeklySalary()));
        number.setText(String.valueOf(player.getNumber()));
        fileName.setText(player.getImageName());
    }

    public void chooseFile(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif","*.jfif")
        );
        fc.setTitle("Attach an image");
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                String imgDir = "\\src\\client\\img\\";
                Path from = Paths.get(selectedFile.toURI());
                Path to = Paths.get(System.getProperty("user.dir")+imgDir+selectedFile.getName());
                Path copied = Files.copy(from, to);;
                if(copied!=null){
                    fileName.setText(String.valueOf(copied.getFileName()));
                    System.out.println("copied file Name: "+String.valueOf(copied.getFileName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void submit(ActionEvent event) throws Exception{
       try {
           String pFileName = fileName.getText();
           Double pAge = Double.parseDouble(age.getText().trim());
           Double pHeight = Double.parseDouble(height.getText().trim());
           Double pSalary = Double.parseDouble(salary.getText().trim());
           Integer pNumber = Integer.parseInt(number.getText().trim());
           player.setImageName(pFileName);
           player.setAge(pAge);
           player.setHeight(pHeight);
           player.setWeeklySalary(pSalary);
           player.setNumber(pNumber);

           networkUtil.write("clubOwner,editPlayer");
           networkUtil.write(player);

       }catch (Exception e){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Error");
           alert.setHeaderText("Search Failed");
           alert.setContentText("Invalid Input Given");
           alert.showAndWait();
       }finally {
           Node node = (Node) event.getSource();
           Stage thisStage = (Stage) node.getScene().getWindow();
           thisStage.close();
       }
    }

    public void cancel(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }
}
