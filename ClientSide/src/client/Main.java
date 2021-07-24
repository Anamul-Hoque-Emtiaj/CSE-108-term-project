package client;

import client.starting.StartingController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import util.NetworkUtil;

import java.io.IOException;
import java.util.Optional;

import static java.lang.Thread.sleep;

public class Main extends Application {
    private Stage stage;
    public final String SERVER_ADDRESS = "127.0.0.1";
    public final int SERVER_PORT = 44444;
    private NetworkUtil networkUtil;
    public void connect(){
        try {
            this.networkUtil = new NetworkUtil(SERVER_ADDRESS, SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        connect();
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("starting/starting.fxml"));
        try {
            Parent root = loader.load();
            StartingController controller = (StartingController) loader.getController();
            controller.connect(networkUtil);
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("Club Menu");
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setOnCloseRequest(e-> {
                try {
                    closingAlert();
                } catch (IOException | InterruptedException ioException) {
                    ioException.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closingAlert() throws IOException, InterruptedException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Exit");
        alert.setHeaderText("Warning!!");
        alert.setContentText("You have wanted to Exit");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()==ButtonType.OK){
            networkUtil.write("exit");
            Thread.sleep(50);
            networkUtil.closeConnection();
            stage.close();
            Platform.exit();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
