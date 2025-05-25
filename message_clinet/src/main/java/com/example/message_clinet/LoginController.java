package com.example.message_clinet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    private void onLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter email and password.");
            return;
        }

        try {
            Socket socket = new Socket("localhost", 6700);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // إرسال بيانات تسجيل الدخول للسيرفر
            writer.write("LOGIN:" + email + ":" + password);
            writer.newLine();
            writer.flush();

            // استقبال الرد من السيرفر
            String response = reader.readLine();

            if ("SUCCESS".equals(response)) {
                AppState.currentEmail = email;
                AppState.socket = socket;
                AppState.bufferedWriter = writer;
                AppState.reader = reader;

                // تحميل واجهة الشات
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Scene scene = new Scene(loader.load());

                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Chat");
                stage.show();
            } else {
                errorLabel.setText("Invalid email or password.");
            }

        } catch (IOException e) {
            errorLabel.setText("Cannot connect to server.");
            e.printStackTrace();
        }
    }
}