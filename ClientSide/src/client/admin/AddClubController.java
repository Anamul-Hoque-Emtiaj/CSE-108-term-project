package client.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.NetworkUtil;

import java.io.IOException;

public class AddClubController {
    private NetworkUtil networkUtil;
    private AdminReadThread clientReader;

    @FXML
    private TextField name;

    public void init(NetworkUtil networkUtil, AdminReadThread clientReader) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
    }

    public void submit(ActionEvent event) throws IOException {
        String clubName = name.getText().trim();
        networkUtil.write("add club");
        networkUtil.write(clubName);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Club");
        alert.setHeaderText("Successful");
        alert.setContentText("Club Added Successfully");
        alert.showAndWait();
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    public void cancel(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }
}
