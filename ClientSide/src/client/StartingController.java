package client;

import server.ClientReadThread;
import server.ClientWriteThread;
import util.NetworkUtil;

import java.io.IOException;

public class StartingController {
    private boolean isConnected = false;
    public final String SERVER_ADDRESS = "127.0.0.1";
    public final int SERVER_PORT = 33333;
    private NetworkUtil networkUtil;
    private ClientReadThread r;

    public void connect(){
        try {
            networkUtil = new NetworkUtil(SERVER_ADDRESS, SERVER_PORT);
            r = new ClientReadThread(networkUtil);
            this.isConnected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void writeToServer(String message){
        if(!isConnected)
            connect();
        new ClientWriteThread(networkUtil,message);
    }
}
