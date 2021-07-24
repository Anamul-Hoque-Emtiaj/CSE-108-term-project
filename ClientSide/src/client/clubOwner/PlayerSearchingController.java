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
import javafx.stage.Stage;
import client.ClientReadThread;
import util.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerSearchingController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;
    private Stage myStage;
    private List<Player> playerList;

    @FXML
    private TextField name;
    @FXML
    private TextField number;
    @FXML
    private TextField salaryFrom;
    @FXML
    private TextField ageFrom;
    @FXML
    private TextField heightFrom;
    @FXML
    private TextField salaryTo;
    @FXML
    private TextField ageTo;
    @FXML
    private TextField heightTo;
    @FXML
    private ChoiceBox position;
    @FXML
    private ChoiceBox country;
    @FXML
    private ChoiceBox minimum;
    @FXML
    private ChoiceBox maximum;

    public void load() throws IOException, InterruptedException {
        networkUtil.write("clubOwner,sendMyClub");
        networkUtil.write(myClub.getName());
        Thread.sleep(100);
        this.myClub = clientReader.getMyClub();
    }

    public void init (NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub,Stage myStage) throws IOException, InterruptedException {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
        this.myStage = myStage;
        load();

        position.getItems().add("Any");
        position.getItems().add("Goalkeeper");
        position.getItems().add("Defender");
        position.getItems().add("Midfielder");
        position.getItems().add("Forward");
        position.setValue("Any");

        country.getItems().add("Any");
        for (String c: myClub.getCountryList()){
            country.getItems().add(c);
        }
        country.setValue("Any");

        minimum.getItems().add("None");
        minimum.getItems().add("WeeklySalary");
        minimum.getItems().add("Age");
        minimum.getItems().add("Height");
        minimum.setValue("None");

        maximum.getItems().add("None");
        maximum.getItems().add("WeeklySalary");
        maximum.getItems().add("Age");
        maximum.getItems().add("Height");
        maximum.setValue("None");
    }

    public void goToPreviousScene(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
    }

    public void submit(ActionEvent event) throws IOException, InterruptedException {
        load();
        String pName = "Any";
        String countryName = (String) country.getValue();
        String positionName = (String) position.getValue();
        String maxElement = (String) maximum.getValue();
        String minElement = (String) minimum.getValue();
        double min = -1000.000;
        double max = 100000000.00;
        double minSalary = min;
        double maxSalary = max;
        double minAge = min;
        double maxAge = max;
        double minHeight = min;
        double maxHeight = max;
        int pNumber = -1;
        try {
            if(!name.getText().equals("")){
                pName = name.getText().trim();
            }
            if(!number.getText().equals("")){
                pNumber = Integer.parseInt(number.getText().trim());
            }
            if(!salaryFrom.getText().equals("")){
                minSalary = Double.parseDouble(salaryFrom.getText().trim());
            }
            if(!salaryTo.getText().equals("")){
                maxSalary = Double.parseDouble(salaryTo.getText().trim());
            }
            if(!ageFrom.getText().equals("")){
                minAge = Double.parseDouble(ageFrom.getText().trim());
            }
            if(!ageTo.getText().equals("")){
                maxAge = Double.parseDouble(ageTo.getText().trim());
            }
            if(!heightFrom.getText().equals("")){
                minHeight = Double.parseDouble(heightFrom.getText().trim());
            }
            if(!heightTo.getText().equals("")){
                maxHeight = Double.parseDouble(heightTo.getText().trim());
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Search Failed");
            alert.setContentText("Invalid Input Given");
            alert.showAndWait();
            goToPreviousScene(event);
        }
        List<Player> temp1,temp2,temp3,temp4,temp5,temp6,temp7;
        if(minElement.equals("WeeklySalary")){
            temp1 = myClub.playersWithMinimumWeeklySalary();
        }else if(minElement.equals("Age")){
            temp1 = myClub.playersWithMinimumAge();
        }else if(minElement.equals("Height")){
            temp1 = myClub.playersWithMinimumHeight();
        }else{
            temp1 = new ArrayList<>();
        }
        if(maxElement.equals("WeeklySalary")){
            temp2 = myClub.playersWithMaximumWeeklySalary();
        }else if(maxElement.equals("Age")){
            temp2 = myClub.playersWithMaximumAge();
        }else if(maxElement.equals("Height")){
            temp2 = myClub.playersWithMaximumHeight();
        }else{
            temp2 = new ArrayList<>();
        }
        temp3 = new ArrayList<>();
        for(Player player: myClub.getPlayerList()) {
            if (player.getAge() >= minAge && player.getAge() <= maxAge && player.getHeight() >= minHeight && player.getHeight() <= maxHeight
                    && player.getWeeklySalary() >= minSalary && player.getWeeklySalary() <= maxSalary) {
                temp3.add(player);
            }
        }
        temp4 = new ArrayList<>();
        for(Player player: myClub.getPlayerList()){
            if(pName.equals(player.getName())){
                temp4.add(player);
            }
        }
        temp5 = new ArrayList<>();
        for(Player player: myClub.getPlayerList()){
            if(pNumber!=-1){
                temp5.add(player);
            }
        }
        temp6 = new ArrayList<>();
        for(Player player: myClub.getPlayerList()){
            if(countryName.equals(player.getCountry())){
                temp6.add(player);
            }
        }
        temp7 = new ArrayList<>();
        for(Player player: myClub.getPlayerList()){
            if(positionName.equals(player.getPosition())){
                temp7.add(player);
            }
        }

        playerList = new ArrayList<>();
        for(Player player: myClub.getPlayerList()){
            boolean t1=true,t2=true,t3,t4=true,t5=true,t6=true,t7=true;
            t3 = temp3.contains(player);
            if(!minElement.equals("None")){
                t1 = temp1.contains(player);
            }
            if(!maxElement.equals("None")){
                t2 = temp2.contains(player);
            }
            if(!pName.equals("Any")){
                t4 = temp4.contains(player);
            }
            if(pNumber!=-1){
                t5 = temp5.contains(player);
            }
            if(!countryName.equals("Any")){
                t6 = temp6.contains(player);
            }
            if(!positionName.equals("Any")){
                t7 = temp7.contains(player);
            }
            if(t1 && t2 && t3 && t4 && t5 && t6 && t7){
                playerList.add(player);
            }
        }
        if(playerList.size()==0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not found");
            alert.setHeaderText("Warning");
            alert.setContentText("No Player Found");
            alert.showAndWait();
            goToPreviousScene(event);
        }else{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("clubOwner/searchPlayer.fxml"));
            try {
                Parent root = loader.load();
                SearchPlayerController controller = (SearchPlayerController) loader.getController();
                controller.init(networkUtil,clientReader,myClub,playerList,myStage);
                Scene scene = new Scene(root, 850, 560);
                myStage.setTitle("Player's Details");
                myStage.setScene(scene);
                goToPreviousScene(event);
            } catch (IOException e) {
                goToPreviousScene(event);
                e.printStackTrace();
            }
        }
    }

    public void cancel(ActionEvent event) {
        goToPreviousScene(event);
    }
}
