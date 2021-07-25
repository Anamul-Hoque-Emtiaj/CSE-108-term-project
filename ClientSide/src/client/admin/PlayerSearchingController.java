package client.admin;

import client.ClientReadThread;
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
import util.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerSearchingController {
    private NetworkUtil networkUtil;
    private AdminReadThread clientReader;
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
    @FXML
    private ChoiceBox club;


    public void init (NetworkUtil networkUtil, AdminReadThread clientReader, Stage myStage) throws IOException, InterruptedException {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myStage = myStage;
        List<Player> allPlayers;
        List<String> clubList = new ArrayList<>();
        List<String> countryList = new ArrayList<>();
        networkUtil.write("send all players list");
        Thread.sleep(50);
        allPlayers = clientReader.getUpdatedPlayersList();

        for (Player player: allPlayers){
            if(!clubList.contains(player.getClub())){
                clubList.add(player.getClub());
            }
            if(!countryList.contains(player.getCountry())){
                countryList.add(player.getCountry());
            }
        }

        position.getItems().add("Any");
        position.getItems().add("Goalkeeper");
        position.getItems().add("Defender");
        position.getItems().add("Midfielder");
        position.getItems().add("Forward");
        position.setValue("Any");

        country.getItems().add("Any");
        for (String c: countryList){
            country.getItems().add(c);
        }
        country.setValue("Any");

        club.getItems().add("Any");
        for (String c: clubList){
            club.getItems().add(c);
        }
        club.setValue("Any");

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
        List<Player> allPlayers;
        networkUtil.write("send all players list");
        Thread.sleep(50);
        allPlayers = clientReader.getUpdatedPlayersList();
        String pName = "Any";
        String countryName = (String) country.getValue();
        String positionName = (String) position.getValue();
        String maxElement = (String) maximum.getValue();
        String minElement = (String) minimum.getValue();
        String clubName = (String) club.getValue();
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
        List<Player> temp1,temp2,temp3,temp4,temp5,temp6,temp7,temp8;
        temp1 = new ArrayList<>();
        if(minElement.equals("WeeklySalary")){
            double mini=1000000000,temp;
            for(Player player: allPlayers){
                temp = player.getWeeklySalary();
                if(temp<mini)
                    mini=temp;
            }
            for(Player player: allPlayers){
                if(player.getWeeklySalary()==mini){
                    temp1.add(player);
                }
            }
        }else if(minElement.equals("Age")){
            double mini=100000000,temp;
            for(Player player: allPlayers){
                temp = player.getAge();
                if(temp<mini)
                    mini=temp;
            }
            for(Player player: allPlayers){
                if(player.getAge()==mini){
                    temp1.add(player);
                }
            }
        }else if(minElement.equals("Height")){
            double mini=100000000,temp;
            for(Player player: allPlayers){
                temp = player.getHeight();
                if(temp<mini)
                    mini=temp;
            }
            for(Player player: allPlayers){
                if(player.getHeight()==mini){
                    temp1.add(player);
                }
            }
        }
        temp2 = new ArrayList<>();
        if(maxElement.equals("WeeklySalary")){
            double maxi=-1,temp;
            for(Player player: allPlayers){
                temp = player.getWeeklySalary();
                if(temp>maxi)
                    maxi=temp;
            }
            for(Player player: allPlayers){
                if(player.getWeeklySalary()==maxi){
                    temp2.add(player);
                }
            }
        }else if(maxElement.equals("Age")){
            double maxi=-1,temp;
            for(Player player: allPlayers){
                temp = player.getAge();
                if(temp>maxi)
                    maxi=temp;
            }
            for(Player player: allPlayers){
                if(player.getAge()==maxi){
                    temp2.add(player);
                }
            }
        }else if(maxElement.equals("Height")){
            double maxi=-1,temp;
            for(Player player: allPlayers){
                temp = player.getHeight();
                if(temp>maxi)
                    maxi=temp;
            }
            for(Player player: allPlayers){
                if(player.getHeight()==maxi){
                    temp2.add(player);
                }
            }
        }
        temp3 = new ArrayList<>();
        for(Player player: allPlayers) {
            if (player.getAge() >= minAge && player.getAge() <= maxAge && player.getHeight() >= minHeight && player.getHeight() <= maxHeight
                    && player.getWeeklySalary() >= minSalary && player.getWeeklySalary() <= maxSalary) {
                temp3.add(player);
            }
        }
        temp4 = new ArrayList<>();
        for(Player player: allPlayers){
            if(pName.equals(player.getName())){
                temp4.add(player);
            }
        }
        temp5 = new ArrayList<>();
        for(Player player: allPlayers){
            if(pNumber!=-1){
                temp5.add(player);
            }
        }
        temp6 = new ArrayList<>();
        for(Player player: allPlayers){
            if(countryName.equals(player.getCountry())){
                temp6.add(player);
            }
        }
        temp7 = new ArrayList<>();
        for(Player player: allPlayers){
            if(positionName.equals(player.getPosition())){
                temp7.add(player);
            }
        }
        temp8 = new ArrayList<>();
        for(Player player: allPlayers){
            if(clubName.equals(player.getClub())){
                temp8.add(player);
            }
        }

        playerList = new ArrayList<>();
        for(Player player: allPlayers){
            boolean t1=true,t2=true,t3,t4=true,t5=true,t6=true,t7=true,t8=true;
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
            if(!clubName.equals("Any")){
                t8 = temp8.contains(player);
            }
            if(t1 && t2 && t3 && t4 && t5 && t6 && t7 && t8){
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
            loader.setLocation(Main.class.getResource("admin/searchPlayer.fxml"));
            try {
                Parent root = loader.load();
                SearchPlayerController controller = (SearchPlayerController) loader.getController();
                controller.init(networkUtil,clientReader,playerList,myStage);
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
