package com.example.message_clinet;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    String [] selectedUser = {null};
    Map<String,String> nameToEmail = new HashMap<>();
    @FXML
    private Button Button_send;
    @FXML
    private TextField tf_main;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private VBox vBox_main;

    private   Client client;
    @FXML
    private ListView<String>onlineUserList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = new Client(AppState.socket);
        System.out.println("Connected to Server");
        receiveOnlineUsers();

        Button_send.setVisible(false);
        tf_main.setVisible(false);
        onlineUserList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedUser[0] = nameToEmail.get(newValue);
                Button_send.setVisible(true);
                tf_main.setVisible(true);
            }
        });

        vBox_main.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                sp_main.setVvalue(100);
            }
        });
        client.receivMessageServer(vBox_main);

        Button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String messageToSend = tf_main.getText();
                if(!messageToSend.isEmpty() && selectedUser[0] !=null)
                {
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5,5,5,10));

                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);

                    textFlow.setStyle("-fx-color:rgb(239,242,255);"+
                            "-fx-background-color:rgb(15,125,242);"+
                            "-fx-background-radius:20px;");

                    textFlow.setPadding(new Insets(5,10,5,10));
                    text.setFill(Color.color(0.934,0.945,0.996));

                    hBox.getChildren().add(textFlow);
                    vBox_main.getChildren().add(hBox);

                    client.sendMessageToServer("SEND:"+AppState.currentEmail
                            +":"+selectedUser[0]+":"+messageToSend);
                    tf_main.clear();
                }
            }
        });
    }


    public  static void addLebal(String messageFromServer,VBox vBox)
    {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle(
                "-fx-background-color:rgb(233,233,235);"+
                        "-fx-background-radius:20px;");


        textFlow.setPadding(new Insets(5,10,5,10));


        hBox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });

    }

    private void receiveOnlineUsers() {
        new Thread(() -> {
            try {
                String line;
                while ((line = AppState.reader.readLine()) != null) {
                    if (line.equals("SUCCESS")) {
                        System.out.println("Sussccful Loing");
                        continue; // تجاوز رسالة الدخول الناجح
                    } else if (line.startsWith("USER:")) {
                        /*
                        String userFullName = line.substring(5);
                        Platform.runLater(() -> onlineUserList.getItems().add(userFullName));
                        */
                         String [] parts = line.split(":");
                         String fullName = parts[1];
                         String email = parts[2];
                         nameToEmail.put(fullName,email);
                         Platform.runLater(()-> {
                             if (!AppState.currentEmail.equals(email) && !onlineUserList.getItems().contains(fullName)) {
                                 onlineUserList.getItems().add(fullName);
                             }

                         });




                    }else if(line.startsWith("REMOVE_USER:"))
                    {
                        String removedEmail = line.substring("REMOVE_USER:".length());
                        Platform.runLater(()->{
                            String nameTpRemove = null;
                            for(Map.Entry<String,String>entry:nameToEmail.entrySet())
                            {
                                if(entry.getValue().equals(removedEmail))
                                {
                                    nameTpRemove = entry.getKey();
                                    break;
                                }
                            }
                            if(nameTpRemove !=null)
                            {
                                onlineUserList.getItems().remove(nameTpRemove);
                                nameToEmail.remove(nameTpRemove);
                            }

                        });
                    }
                    else if (line.equals("END")) {
                        continue; // انتهت القائمة
                    } else {
                        // هنا تقدر تستقبل رسائل المحادثة لاحقًا
                        System.out.println("رسالة أخرى: " + line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private  void logoutuser(ActionEvent event) throws IOException {
        String line ;
       AppState.bufferedWriter.write("LOGOUT:"+AppState.currentEmail);
       AppState.bufferedWriter.newLine();
       AppState.bufferedWriter.flush();
       line =  AppState.reader.readLine();
       if(line !=null && line.startsWith("SUCCUSSFULLLOGOUT:"))
       {
           AppState.reader.close();
           AppState.bufferedWriter.close();
           AppState.socket.close();
           FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
           Scene scene = new Scene(loader.load());

           Stage stage = (Stage) Button_send.getScene().getWindow();  // إعادة استخدام نفس النافذة
           stage.setScene(scene);
           stage.setTitle("Login");
           stage.show();
       }else
       {
           System.out.println("The Line iS Null ");
       }



    }


}