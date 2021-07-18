package server;

import database.Club;
import database.Player;
import util.NetworkUtil;

import java.io.IOException;
import java.util.List;

public class ClientReadThread implements Runnable{
    private Thread thr;
    private NetworkUtil networkUtil;
    private Object receivedFile;
    private static List<Player> updatedBuyList;
    private Club myClub;
    private String message;
    private static boolean updateNeeded;

    public ClientReadThread(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        thr.start();
    }

    public List<Player> getUpdatedBuyList(){
        updateNeeded = false;
        return updatedBuyList;
    }

    public String getMessage(){
        return message;
    }

    public Club getMyClub() {
        return myClub;
    }

    public boolean isUpdateNeeded() {
        return updateNeeded;
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
                    System.out.println(message);
                }else if(receivedFile instanceof Club){
                    myClub = (Club) receivedFile;
                }else {
                    updateNeeded = true;
                    updatedBuyList = (List<Player>) receivedFile;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
