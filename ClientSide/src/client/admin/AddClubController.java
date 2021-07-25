package client.admin;

import javafx.stage.Stage;
import util.NetworkUtil;

public class AddClubController {
    private NetworkUtil networkUtil;
    private AdminReadThread clientReader;

    public void init(NetworkUtil networkUtil, AdminReadThread clientReader) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
    }
}
