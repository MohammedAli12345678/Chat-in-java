module com.example.message_clinet {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.message_clinet to javafx.fxml;
    exports com.example.message_clinet;
}