package client.admin;

import client.admin.PendingRequestController;
import client.admin.SearchPlayerController;
import database.Club;
import database.Player;
import javafx.application.Platform;
import util.NetworkUtil;

import java.util.List;

public class AdminReadThread implements Runnable{
    private Thread thr;
    private NetworkUtil networkUtil;
    private Object receivedFile;
    private List<Player> updatedPlayerList;
    private Club myClub;
    private String message;
    private SearchPlayerController searchPlayer;
    private InfoController infoController;
    private PendingRequestController myPendingPlayers;
    private Boolean activeSearchPlayer;
    private Boolean activeInfo;
    private Boolean activeMyPendingPlayers;

    public AdminReadThread(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        thr.start();
        activeSearchPlayer = false;
        activeInfo = false;
        activeMyPendingPlayers = false;
    }

    public void setSearchPlayer(SearchPlayerController searchPlayer) {
        this.searchPlayer = searchPlayer;
        activeSearchPlayer = true;
        activeInfo = false;
        activeMyPendingPlayers = false;
    }

    public void setInfo(InfoController infoController) {
        this.infoController = infoController;
        activeSearchPlayer = false;
        activeInfo = true;
        activeMyPendingPlayers = false;
    }

    public void setMyPendingPlayers(PendingRequestController myPendingPlayers) {
        this.myPendingPlayers = myPendingPlayers;
        activeSearchPlayer = false;
        activeInfo = false;
        activeMyPendingPlayers = true;
    }

    public List<Player> getUpdatedPlayersList(){
        return updatedPlayerList;
    }

    public String getMessage(){
        return message;
    }

    public Club getMyClub() {
        return myClub;
    }

    @Override
    public void run() {
        try {
            while (true) {
                receivedFile = networkUtil.read();
                if(receivedFile instanceof String){
                    message = (String) receivedFile;
                    System.out.println("Admin receive message: "+message);
                    receivedFile = null;
                }else if(receivedFile instanceof Club){
                    myClub = (Club) receivedFile;
                    System.out.println("Admin receive club: "+myClub.getName());
                    receivedFile = null;
                }else {
                    updatedPlayerList = (List<Player>) receivedFile;
                    System.out.println("Admin receive updatedPlayerList: "+updatedPlayerList.size());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(activeMyPendingPlayers){
                                myPendingPlayers.load(updatedPlayerList);
                            }
                            if(activeSearchPlayer){
                                searchPlayer.load(updatedPlayerList);
                            }
                            if(activeInfo){
                                infoController.load(updatedPlayerList);
                            }
                        }
                    });
                }
                receivedFile = null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
