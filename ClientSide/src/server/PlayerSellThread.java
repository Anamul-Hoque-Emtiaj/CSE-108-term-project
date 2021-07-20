package server;

import database.Player;
import util.NetworkUtil;

import java.util.HashMap;
import java.util.List;

public class PlayerSellThread implements Runnable{
    private HashMap<NetworkUtil,String> networkUtilList;
    private List<Player> playerList;
    private Thread thr;

    public PlayerSellThread(HashMap<NetworkUtil,String>  networkUtilList, List<Player> playerList) {
        this.networkUtilList = networkUtilList;
        this.playerList = playerList;
        this.thr = new Thread(this);
        thr.start();
    }

    @Override
    public void run() {
        try {
           for (NetworkUtil networkUtil: networkUtilList.keySet()){
               networkUtil.write(playerList);
           }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
