package client.admin;

import client.ClientReadThread;
import database.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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


    public void init(NetworkUtil networkUtil, AdminReadThread clientReader) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
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
            Node node = (Node) event.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();
        }else if(!newPass.equals(confirmPass)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Change Password");
            alert.setHeaderText("Failed to Change Password");
            alert.setContentText("New Password and confirm Password did not match");
            alert.showAndWait();
            Node node = (Node) event.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();

        }else if(!oldPass.equals(adminPassword)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Change Password");
            alert.setHeaderText("Failed to Change Password");
            alert.setContentText("Incorrect Password Given");
            alert.showAndWait();
            Node node = (Node) event.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();

        }else if(oldPass.equals(adminPassword)){
            networkUtil.write("change password");
            networkUtil.write(adminName+","+newPass);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Change Password");
            alert.setHeaderText("Successful");
            alert.setContentText("Password Changed Successfully");
            alert.showAndWait();
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
