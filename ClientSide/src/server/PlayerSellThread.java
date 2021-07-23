package server;

import database.Player;
import util.NetworkUtil;

import java.util.ArrayList;
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

    public Thread getThr() {
        return thr;
    }

    @Override
    public void run() {
        try {
            List<Player> t = new ArrayList<>();
            for (Player player: playerList){
                t.add(player);
            }
           for (NetworkUtil networkUtil: networkUtilList.keySet()){
               networkUtil.write(t);
           }
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
