package client.clubOwner;

import database.Club;
import javafx.event.ActionEvent;
import server.ClientReadThread;
import server.ClientWriteThread;
import util.NetworkUtil;

public class MenuController {
    private NetworkUtil networkUtil;
    private ClientReadThread clientReader;
    private Club myClub;

    public void init(NetworkUtil networkUtil, ClientReadThread clientReader, Club myClub) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myClub = myClub;
    }

    public void clubDetails(ActionEvent event) {
    }

    public void searchPlayer(ActionEvent event) {
    }

    public void addPlayer(ActionEvent event) {
    }

    public void buyPlayer(ActionEvent event) {
    }

    public void changePassword(ActionEvent event) {
    }

    public void exit(ActionEvent event) {
    }

    public void sellRequest(ActionEvent event) {
    }

    public void pendingRequest(ActionEvent event) {
    }
}
