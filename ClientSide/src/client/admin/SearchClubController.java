package client.admin;

import javafx.stage.Stage;
import util.NetworkUtil;

public class SearchClubController {
    private NetworkUtil networkUtil;
    private AdminReadThread clientReader;
    private Stage myStage;

    public void init(NetworkUtil networkUtil, AdminReadThread clientReader,Stage myStage) {
        this.networkUtil = networkUtil;
        this.clientReader = clientReader;
        this.myStage = myStage;
    }
}
