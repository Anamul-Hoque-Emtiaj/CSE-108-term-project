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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import client.ClientReadThread;
import util.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AddPlayerController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;

    @FXML
    private ChoiceBox position;
    @FXML
    private Text fileName;
    @FXML
    private TextField name;
    @FXML
    private TextField country;
    @FXML
    private TextField number;
    @FXML
    private TextField salary;
    @FXML
    private TextField age;
    @FXML
    private TextField height;
    @FXML
    private Text club;


    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        club.setText(myClub.getName());
        position.getItems().add("Not Selected");
        position.getItems().add("Goalkeeper");
        position.getItems().add("Defender");
        position.getItems().add("Midfielder");
        position.getItems().add("Forward");
        position.setValue("Not Selected");
    }

    public void goPreviousScene(ActionEvent event){
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
        } catch (IOException e1) {
            e1.printStackTrace();
        }
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void submit(ActionEvent event) {
        try {
            String pName = name.getText().trim();
            String pClub = myClub.getName();
            String pCountry = country.getText().trim();
            String pPosition = (String) position.getValue();
            String pImageName = fileName.getText();
            Double pAge = Double.parseDouble(age.getText().trim());
            Double pHeight = Double.parseDouble(height.getText().trim());
            Double pSalary = Double.parseDouble(salary.getText().trim());
            Integer pNumber = Integer.parseInt(number.getText().trim());
            Player player = new Player(pName,pCountry,pAge,pHeight,pClub,pPosition,pNumber,pSalary,pImageName);
            if(!pName.equals("") && !pCountry.equals("") && !pPosition.equals("Not Selected")){
                networkUtil.write("Add player");
                networkUtil.write(player);
                Thread.sleep(100);
                if(clientReader.getMessage().equals("Player Added successfully")){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Successful");
                    alert.setHeaderText("Add Player");
                    alert.setContentText("Player Added successfully");
                    alert.showAndWait();

                }else if(clientReader.getMessage().equals("Adding failed")){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("failed");
                    alert.setHeaderText("Warning!!");
                    alert.setContentText("Player with this name already exist");
                    alert.showAndWait();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("failed");
                alert.setHeaderText("Warning!!");
                alert.setContentText("Invalid Input Given");
                alert.showAndWait();
            }
        }catch (Exception e){
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Input Given");
            alert.setContentText("Plz provide valid Input");
            alert.showAndWait();
        }finally {
            goPreviousScene(event);
        }
    }

    public void cancel(ActionEvent event) {
        goPreviousScene(event);
    }
}
