module com.example.ocrtune {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.ocrtune to javafx.fxml;
    exports com.example.ocrtune;
}