module com.example.message_server {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.message_server to javafx.fxml;
    exports com.example.message_server;
}