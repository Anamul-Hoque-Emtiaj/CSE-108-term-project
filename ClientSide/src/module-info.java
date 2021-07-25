module myjfx {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    opens client to javafx.graphics, javafx.fxml, javafx.base;
    opens client.starting to javafx.base, javafx.fxml, javafx.graphics;
    opens client.clubOwner to javafx.base, javafx.fxml, javafx.graphics;
    opens client.admin to javafx.base, javafx.fxml, javafx.graphics;
}