package server;

import database.Club;
import database.Player;
import util.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SendUpdatedClubThread implements Runnable{
    private Club myClub;
    private List<Player> clubPlayerList;
    private List<Player> clubPlayerPendingList;
    private NetworkUtil networkUtil;
    private String clubName;
    private List<Player> playerList;
    private List<Club> clubList;
    private List<Player> pendingPlayerList;
    private Thread thr;

    public SendUpdatedClubThread(NetworkUtil networkUtil, String clubName, List<Player> playerList, List<Club> clubList, List<Player> pendingPlayerList) {
        this.clubName = clubName;
        this.playerList = playerList;
        this.clubList = clubList;
        this.pendingPlayerList = pendingPlayerList;
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        clubPlayerList = new ArrayList<>();
        clubPlayerPendingList = new ArrayList<>();
        myClub = new Club(clubName);
        thr.start();
    }

    @Override
    public void run() {

        for (Club club: clubList){
            if(club.getName().equals(clubName)){
                myClub = club;
            }
        }
        for (Player player: playerList){
            if(player.getClub().equals(myClub.getName())){
                clubPlayerList.add(player);
            }
        }
        myClub.setPlayerList(clubPlayerList);
        for (Player player: pendingPlayerList){
            if(player.getClub().equals(myClub.getName())){
                clubPlayerPendingList.add(player);
            }
        }
        myClub.setPendingList(clubPlayerPendingList);
        try {
            networkUtil.write(myClub);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}