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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import client.ClientReadThread;
import util.NetworkUtil;

import java.io.IOException;

public class ChangePasswordController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;
    private Stage myStage;

    @FXML
    private PasswordField oldPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Text name;

    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub,Stage myStage) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        this.myStage = myStage;
        name.setText(myClub.getName());
    }
    public void back(ActionEvent event){
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
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }
    public void submit(ActionEvent event) throws InterruptedException, IOException {
        networkUtil.write("clubOwner,sendMyClub");
        networkUtil.write(myClub.getName());
        Thread.sleep(100);
        this.myClub = clientReader.getMyClub();
        String oldPass = oldPassword.getText().trim();
        String newPass = newPassword.getText().trim();
        String confirmPass = confirmPassword.getText().trim();
        if(oldPass.equals("")||newPass.equals("")||confirmPass.equals("")){
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

        }else if(!oldPass.equals(myClub.getPassword())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Change Password");
            alert.setHeaderText("Failed to Change Password");
            alert.setContentText("Incorrect Password Given");
            alert.showAndWait();
            back(event);

        }else if(oldPass.equals(myClub.getPassword())){
            networkUtil.write("change password");
            networkUtil.write(myClub.getName()+","+newPass);
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
