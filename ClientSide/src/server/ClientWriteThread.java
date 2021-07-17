package server;

import util.NetworkUtil;

import java.io.IOException;

public class ClientWriteThread implements Runnable{
    private Thread thr;
    private NetworkUtil networkUtil;
    private String message;

    public ClientWriteThread(NetworkUtil networkUtil, String message) {
        this.message = message;
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            networkUtil.write(message);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
