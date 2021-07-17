package client.clubOwner;

import client.Main;
import database.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.ClientReadThread;
import server.ClientWriteThread;
import util.NetworkUtil;

import java.io.IOException;

public class LoginController {
    public final String SERVER_ADDRESS = "127.0.0.1";
    public final int SERVER_PORT = 33333;
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;

    @FXML
    private PasswordField password;
    @FXML
    private TextField name;

    public void connect(){
        try {
            this.networkUtil = new NetworkUtil(SERVER_ADDRESS, SERVER_PORT);
            this.clientReader = new ClientReadThread(networkUtil);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeToServer(String message){
        new ClientWriteThread(networkUtil,message);
    }

    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Failed");
        alert.setHeaderText("Error");
        alert.setContentText("The username and password you provided is not correct.");
        alert.showAndWait();
    }

    public void login(ActionEvent event) throws InterruptedException {
        connect();
        String uName = name.getText();
        uName.trim();
        String uPassword = password.getText();
        uPassword.trim();
        if(!uName.equals("")&& !uPassword.equals("")){
            writeToServer("clubOwner,login");
            Thread.sleep(100);
            String r = (String) clientReader.getReceivedFile();
            if(r.equals("provide your name and password")){
                writeToServer(uName+","+uPassword);
                Thread.sleep(100);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Error");
                alert.setContentText("Server Error Occurred.");
                alert.showAndWait();
                Node node = (Node) event.getSource();
                Stage thisStage = (Stage) node.getScene().getWindow();
                thisStage.close();
            }
            r = (String) clientReader.getReceivedFile();
            if(r.equals("login successful")){
                writeToServer("send my club");
                Thread.sleep(100);
                myClub = (Club) clientReader.getReceivedFile();
                System.out.println(myClub.getName());
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
            }else{
                showAlert();
                Node node = (Node) event.getSource();
                Stage thisStage = (Stage) node.getScene().getWindow();
                thisStage.close();
            }
        }else{
            showAlert();
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
