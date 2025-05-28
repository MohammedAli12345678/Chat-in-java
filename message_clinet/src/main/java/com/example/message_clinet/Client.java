package com.example.message_clinet;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import javafx.application.Platform;


public class Client {


    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket)
    {

        this.socket = socket;
try {
    this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
}catch (IOException e)
{
    System.out.println("can not " + e.getMessage());
}

    }

    public void sendMessageToServer(String messageToServer)
    {
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            System.out.println("⚠️ لا يمكن الإرسال: الاتصال مغلق");
            return;
        }
        try {
            if (socket != null && !socket.isClosed() && socket.isConnected()) {
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            } else {
                System.out.println("لا يمكن الإرسال: الاتصال مغلق");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("can not send message to clint");
            closeEverthing(socket,bufferedReader,bufferedWriter);
        }
    }
    /*

    public void receivMessageServer(VBox vBox)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected())
                {
                    try {
                        String messageFromServer = bufferedReader.readLine();

                        if (messageFromServer == null) {
                            System.out.println("Server closed the connection.");
                            closeEverthing(socket, bufferedReader, bufferedWriter);
                            break; // أخرج من الحلقة
                        }
                        if (messageFromServer.startsWith("MSG_FROM:")) {
                            String[] parts = messageFromServer.split(":", 3);
                            String sender = parts[1];
                            String message = parts[2];
                            HelloController.addLebal(sender + ": " + message, vBox);
                        }

//                        HelloController.addLebal(messageFromServer,vBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("can not revrive");
                        closeEverthing(socket,bufferedReader,bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }
    */

    public void receivMessageServer(VBox vBox) {
        new Thread(() -> {
            try {
                String messageFromServer;
                while ((messageFromServer = bufferedReader.readLine()) != null) {
                    if (messageFromServer.startsWith("MSG_FROM:")) {
                        String[] parts = messageFromServer.split(":", 3);
                        if(parts.length <  3)
                        {
                            System.out.println("Invaled the value in send sERVER :       "  + messageFromServer);
                            continue;
                        }
                        String sender = parts[1];
                        String message = parts[2];
                        HelloController.addLebal(sender + ": " + message, HelloController.instance.getvBox_main());

                    } else if (messageFromServer.startsWith("SUCCESSFUL_SEND_MESSAGE:")) {
                        String[] parts = messageFromServer.split(":", 3);
                        String sender = parts[1];
                        String message = parts[2];
                        String display = sender.equals(AppState.currentEmail) ? "Me: " + message : sender + ": " + message;
                        Platform.runLater(() -> HelloController.addLebal(display, HelloController.instance.getvBox_main()));

                    } else if (messageFromServer.startsWith("USER:")) {
                        String[] parts = messageFromServer.split(":");
                        String fullName = parts[1];
                        String email = parts[2];
                        int id = Integer.parseInt(parts[3]);
                        System.out.println("fullName :" + fullName +"     email:" + email  +"    id: "  + id);
                        HelloController.nameToEmail.put(fullName, email);
                        HelloController.IdToEmail.put(email, id);
                        Platform.runLater(() -> {
                            if (!AppState.currentEmail.equals(email) && !HelloController.onlineUserListStatic.getItems().contains(fullName)) {
                                HelloController.onlineUserListStatic.getItems().add(fullName);
                            }
                        });

                    } else if (messageFromServer.startsWith("REMOVE_USER:")) {
                        String removedEmail = messageFromServer.substring("REMOVE_USER:".length());
                        Platform.runLater(() -> {
                            String nameToRemove = null;
                            for (Map.Entry<String, String> entry : HelloController.nameToEmail.entrySet()) {
                                if (entry.getValue().equals(removedEmail)) {
                                    nameToRemove = entry.getKey();
                                    break;
                                }
                            }
                            if (nameToRemove != null) {
                                HelloController.onlineUserListStatic.getItems().remove(nameToRemove);
                                HelloController.nameToEmail.remove(nameToRemove);
                            }
                        });

                    } else if (messageFromServer.equals("END") || messageFromServer.equals("END_MESSAGES")) {
                        continue;
                    } else {
                        System.out.println("رسالة غير معروفة: " + messageFromServer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void closeEverthing(Socket scoket,BufferedReader bufferedReader,BufferedWriter bufferedWriter)
    {
        try {
            if(bufferedReader !=null)
                bufferedReader.close();
            if(bufferedWriter !=null)
                bufferedWriter.close();
            if(scoket !=null)
                scoket.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
