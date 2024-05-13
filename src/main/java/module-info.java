module com.mmprogramming.mmpassignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires opencv;


    opens com.mmprogramming.mmpassignment to javafx.fxml;
    exports com.mmprogramming.mmpassignment;
}