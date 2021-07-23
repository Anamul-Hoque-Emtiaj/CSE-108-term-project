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
    private List<String> clubCountryList;
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
        clubCountryList = new ArrayList<>();
        thr.start();
    }

    public Thread getThr() {
        return thr;
    }

    @Override
    public void run() {
        try {
            for (Club club: clubList){
                if(club.getName().equals(clubName)){
                    myClub = club;
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
                    for (Player player: playerList){
                        if(player.getClub().equals(myClub.getName())){
                            if(!clubCountryList.contains(player.getCountry())){
                                clubCountryList.add(player.getCountry());
                            }
                        }
                    }
                    myClub.setCountryList(clubCountryList);
                    networkUtil.write(myClub);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
