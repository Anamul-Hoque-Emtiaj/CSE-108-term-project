package server;

import util.NetworkUtil;

import java.io.IOException;


public class ServerThread implements Runnable{
    private Thread thr;
    private NetworkUtil networkUtil;

    public ServerThread( NetworkUtil networkUtil) {
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
