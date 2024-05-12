module com.mmprogramming.mmpassignment {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mmprogramming.mmpassignment to javafx.fxml;
    exports com.mmprogramming.mmpassignment;
}