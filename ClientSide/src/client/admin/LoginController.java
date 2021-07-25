package client.admin;

import client.ClientReadThread;
import client.Main;
import database.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.NetworkUtil;

import java.io.IOException;

public class LoginController {
    public final String SERVER_ADDRESS = "127.0.0.1";
    public final int SERVER_PORT = 44444;
    private NetworkUtil networkUtil;
    private AdminReadThread clientReader;

    @FXML
    private PasswordField password;
    @FXML
    private TextField name;


    public void connect(){
        try {
            this.networkUtil = new NetworkUtil(SERVER_ADDRESS, SERVER_PORT);
            this.clientReader = new AdminReadThread(networkUtil);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Failed");
        alert.setHeaderText("Error");
        alert.setContentText("The username and password you provided is not correct.");
        alert.showAndWait();
    }

    public void login(ActionEvent event) throws Exception {
        connect();
        String uName = name.getText();
        uName.trim();
        String uPassword = password.getText();
        uPassword.trim();
        if(!uName.equals("")&& !uPassword.equals("")){
            networkUtil.write("admin,login");
            networkUtil.write(uName+","+uPassword);
            Thread.sleep(200);
            String r = clientReader.getMessage();
            if(r.equals("login successful")){
                Node node = (Node) event.getSource();
                Stage myStage = (Stage) node.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("admin/info.fxml"));
                try {
                    Parent root = loader.load();
                    InfoController controller = (InfoController) loader.getController();
                    controller.init(networkUtil,clientReader,myStage);
                    Scene scene = new Scene(root, 850, 560);
                    myStage.setTitle("Info");
                    myStage.setScene(scene);
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
