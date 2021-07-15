module myjfx {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    opens client to javafx.graphics, javafx.fxml, javafx.base;
    opens client.others to javafx.base, javafx.fxml, javafx.graphics;
}