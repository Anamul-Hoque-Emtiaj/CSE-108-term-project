package client;

import client.clubOwner.BuyPlayerController;
import client.clubOwner.ClubDetailsController;
import client.clubOwner.PendingRequestController;
import client.clubOwner.SearchPlayerController;
import database.Club;
import database.Player;
import javafx.application.Platform;
import util.NetworkUtil;

import java.io.IOException;
import java.util.List;

public class ClientReadThread implements Runnable{
    private Thread thr;
    private NetworkUtil networkUtil;
    private Object receivedFile;
    private List<Player> updatedBuyList;
    private String clubName;
    private Club myClub;
    private String message;
    private SearchPlayerController searchPlayer;
    private ClubDetailsController clubDetails;
    private PendingRequestController myPendingPlayers;
    private BuyPlayerController buyPlayer;
    private Boolean activeSearchPlayer;
    private Boolean activeClubDetails;
    private Boolean activeMyPendingPlayers;
    private Boolean activeBuyPlayer;

    public ClientReadThread(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        thr.start();
        activeSearchPlayer = false;
        activeClubDetails = false;
        activeBuyPlayer = false;
        activeMyPendingPlayers = false;
    }

    public void setSearchPlayer(SearchPlayerController searchPlayer) {
        this.searchPlayer = searchPlayer;
        activeSearchPlayer = true;
        activeClubDetails = false;
        activeBuyPlayer = false;
        activeMyPendingPlayers = false;
    }

    public void setClubDetails(ClubDetailsController clubDetails) {
        this.clubDetails = clubDetails;
        activeSearchPlayer = false;
        activeClubDetails = true;
        activeBuyPlayer = false;
        activeMyPendingPlayers = false;
    }

    public void setMyPendingPlayers(PendingRequestController myPendingPlayers) {
        this.myPendingPlayers = myPendingPlayers;
        activeSearchPlayer = false;
        activeClubDetails = false;
        activeBuyPlayer = false;
        activeMyPendingPlayers = true;
    }

    public void setBuyPlayer(BuyPlayerController buyPlayer) {
        this.buyPlayer = buyPlayer;
        activeSearchPlayer = false;
        activeClubDetails = false;
        activeBuyPlayer = true;
        activeMyPendingPlayers = false;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public List<Player> getUpdatedBuyList(){
        return updatedBuyList;
    }

    public String getMessage(){
        return message;
    }

    public Club getMyClub() {
        return myClub;
    }

    public Thread getThr() {
        return thr;
    }

    public void run() {
        try {
            while (true) {
                receivedFile = networkUtil.read();
                if(receivedFile instanceof String){
                    message = (String) receivedFile;
                    receivedFile = null;
                    System.out.println(clubName + " receive message: "+message);
                }else if(receivedFile instanceof Club){
                    Club c = (Club) receivedFile;
                    if(c.getName().equals(clubName)){
                        myClub = (Club) receivedFile;
                        System.out.println("Client receive club: "+myClub.getName());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if(activeSearchPlayer){
                                    searchPlayer.load(myClub);
                                }
                                if(activeClubDetails){
                                    clubDetails.load(myClub);
                                }
                            }
                        });
                    }
                    receivedFile = null;
                }else {
                    updatedBuyList = (List<Player>) receivedFile;
                    System.out.println("Client receive updatedPlayerList: "+updatedBuyList.size());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(activeBuyPlayer){
                                buyPlayer.load(updatedBuyList);
                            }
                            if(activeMyPendingPlayers){
                                myPendingPlayers.load(updatedBuyList);
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
