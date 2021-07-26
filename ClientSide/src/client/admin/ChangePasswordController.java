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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.NetworkUtil;

import java.io.IOException;

public class ChangePasswordController {
    private NetworkUtil networkUtil;
    private AdminReadThread clientReader;
    private Stage myStage;
    private String adminName;
    private String adminPassword;

    @FXML
    private PasswordField oldPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private TextField name;

    public void back(ActionEvent event){
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
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    public void init(NetworkUtil networkUtil, AdminReadThread clientReader,Stage myStage) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myStage = myStage;
        try {
            networkUtil.write("change admin info");
            Thread.sleep(50);
            String[] t = clientReader.getMessage().split(",");
            adminName = t[0];
            adminPassword = t[1];
            name.setText(adminName);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void submit(ActionEvent event) throws InterruptedException, IOException {
        String oldPass = oldPassword.getText().trim();
        String newPass = newPassword.getText().trim();
        String confirmPass = confirmPassword.getText().trim();
        adminName = name.getText().trim();
        if(oldPass.equals("")||newPass.equals("")||confirmPass.equals("")||adminName.equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Change Password");
            alert.setHeaderText("Failed to Change Password");
            alert.setContentText("Invalid Input Given");
            alert.showAndWait();
            back(event);
        }else if(!newPass.equals(confirmPass)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Change Password");
            alert.setHeaderText("Failed to Change Password");
            alert.setContentText("New Password and confirm Password did not match");
            alert.showAndWait();
            back(event);

        }else if(!oldPass.equals(adminPassword)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Change Password");
            alert.setHeaderText("Failed to Change Password");
            alert.setContentText("Incorrect Password Given");
            alert.showAndWait();
            back(event);

        }else if(oldPass.equals(adminPassword)){
            networkUtil.write("change password");
            networkUtil.write(adminName+","+newPass);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Change Password");
            alert.setHeaderText("Successful");
            alert.setContentText("Password Changed Successfully");
            alert.showAndWait();
            back(event);
        }
    }

    public void cancel(ActionEvent event) {
        back(event);
    }
}
