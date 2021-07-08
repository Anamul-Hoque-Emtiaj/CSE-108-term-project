package server;

import util.NetworkUtil;

import java.io.IOException;
import java.util.HashMap;

public class ServerReadThread implements Runnable{
    private Thread thr;
    private NetworkUtil networkUtil;
    public HashMap<String, NetworkUtil> clientMap;

    public ServerReadThread(HashMap<String, NetworkUtil> map, NetworkUtil networkUtil) {
        this.clientMap = map;
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = networkUtil.read();
            }
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            try {
                networkUtil.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
