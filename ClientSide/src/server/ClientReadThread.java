package server;

import util.NetworkUtil;

import java.io.IOException;

public class ClientReadThread implements Runnable{
    private Thread thr;
    private NetworkUtil networkUtil;
    private Object receivedFile;

    public ClientReadThread(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        thr.start();
    }

    public Thread getThr() {
        return thr;
    }

    public Object getReceivedFile() {
        return receivedFile;
    }

    public void run() {
        try {
            while (true) {
                receivedFile = networkUtil.read();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
