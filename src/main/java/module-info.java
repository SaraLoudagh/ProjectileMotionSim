module com.example.projectilemotionsim {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projectilemotionsim to javafx.fxml;
    exports com.example.projectilemotionsim;
}