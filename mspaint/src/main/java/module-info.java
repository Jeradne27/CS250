module cs250 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.graphics;

    opens cs250 to javafx.fxml;
    exports cs250;
}
