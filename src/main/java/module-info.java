module com.example.projectilemotionsim {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.projectilemotionsim to javafx.fxml;
    exports com.example.projectilemotionsim;
}