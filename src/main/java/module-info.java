module com.mmprogramming.mmpassignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires opencv;
    requires javafx.media;
    requires java.desktop;
    requires javafx.swing;


    opens com.mmprogramming.mmpassignment to javafx.fxml;
    exports com.mmprogramming.mmpassignment;
}