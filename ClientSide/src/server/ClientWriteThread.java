package server;

import util.NetworkUtil;

import java.io.IOException;

public class ClientWriteThread implements Runnable{
    private Thread thr;
    private NetworkUtil networkUtil;
    private Object message;

    public ClientWriteThread(NetworkUtil networkUtil, Object message) {
        this.message = message;
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        thr.start();
    }

    public Thread getThr() {
        return thr;
    }

    public void run() {
        try {
            networkUtil.write(message);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
