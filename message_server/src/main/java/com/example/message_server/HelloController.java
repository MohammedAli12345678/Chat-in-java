package com.example.message_server;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController {
//    @FXML
//    private Button Button_send;
//    @FXML
//     private TextField tf_main;
//    @FXML
//    private ScrollPane sp_main;
//    @FXML
//    private VBox vBox_main;
//
//    private  Server server;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//
//        try
//        {
//            server = new Server(new ServerSocket(6700));
//        }catch (IOException E){
//            E.printStackTrace();
//            System.out.println("Error creating Servrtr");
//        }
//
//        vBox_main.heightProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
//                sp_main.setVvalue(100);
//            }
//        });
//        server.receivMessageFromCilent(vBox_main);
//
//        Button_send.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                String messageToSend = tf_main.getText();
//                if(!messageToSend.isEmpty())
//                {
//                    HBox hBox = new HBox();
//                    hBox.setAlignment(Pos.CENTER_RIGHT);
//                    hBox.setPadding(new Insets(5,5,5,10));
//
//                    Text text = new Text(messageToSend);
//                    TextFlow textFlow = new TextFlow(text);
//
//                    textFlow.setStyle("-fx-color:rgb(239,242,255);"+
//                            "-fx-background-color:rgb(15,125,242);"+
//                            "-fx-background-radius:20px;");
//
//                    textFlow.setPadding(new Insets(5,10,5,10));
//                    text.setFill(Color.color(0.934,0.945,0.996));
//
//                    hBox.getChildren().add(textFlow);
//                    vBox_main.getChildren().add(hBox);
//
//                    server.sendMessageToClient(messageToSend);
//                    tf_main.clear();
//                }
//            }
//        });
//    }
//
//    public  static void addLebal(String messageFromClient,VBox vBox)
//    {
//        HBox hBox = new HBox();
//        hBox.setAlignment(Pos.CENTER_LEFT);
//        hBox.setPadding(new Insets(5,5,5,10));
//
//        Text text = new Text(messageFromClient);
//        TextFlow textFlow = new TextFlow(text);
//
//        textFlow.setStyle(
//                "-fx-background-color:rgb(233,233,235);"+
//                "-fx-background-radius:20px;");
//
//
//        textFlow.setPadding(new Insets(5,10,5,10));
//
//
//        hBox.getChildren().add(textFlow);
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                vBox.getChildren().add(hBox);
//            }
//        });
//
//    }
}

