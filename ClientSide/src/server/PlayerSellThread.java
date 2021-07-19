package server;

import database.Player;
import util.NetworkUtil;

import java.util.List;

public class PlayerSellThread implements Runnable{
    private List<NetworkUtil> networkUtilList;
    private List<Player> playerList;
    private Thread thr;

    public PlayerSellThread(List<NetworkUtil> networkUtilList, List<Player> playerList) {
        this.networkUtilList = networkUtilList;
        this.playerList = playerList;
        this.thr = new Thread(this);
        thr.start();
    }

    @Override
    public void run() {
        try {
           for (NetworkUtil networkUtil: networkUtilList){
               networkUtil.write(playerList);
           }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
