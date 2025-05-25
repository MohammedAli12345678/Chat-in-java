package com.example.message_clinet;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
        try {
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("can not send message to clint");
            closeEverthing(socket,bufferedReader,bufferedWriter);
        }
    }

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
