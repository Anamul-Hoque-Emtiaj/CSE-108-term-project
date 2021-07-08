package server;

import util.NetworkUtil;

import java.io.IOException;

public class ClientWriteThread implements Runnable{
    private Thread thr;
    private NetworkUtil networkUtil;

    public ClientWriteThread(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                networkUtil.write("Hi");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                networkUtil.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
