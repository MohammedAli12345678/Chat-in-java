package com.example.message_server;



import model.message.Message;
import model.user.Status;
import model.user.User;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String email;
    private  int id;
    List<User> onlineUsers;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            closeEverything();
        }
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {
                String message = reader.readLine();

                if (message != null && message.startsWith("LOGIN:")) {
                    String[] parts = message.split(":");
                    String email = parts[1];
                    String password = parts[2];

                    boolean success = User.Login(email, password);

                    if (success) {
                        this.email = email;
                      this.id = User.getUserByEmail(email).getUser_id();
                        // تحديث الحالة إلى Online
                        User.updateStatusUser(email, Status.ONLINE);

                        // إرسال SUCCESS
                        writer.write("SUCCESS:"+this.id);
                        writer.newLine();
                         writer.flush();
                        // إرسال قائمة الأعضاء المتصلين
                        onlineUsers = User.getUserOnline();

                        for (User u : onlineUsers) {
                            writer.write("USER:" + u.getFull_name() + ":" + u.getEmail() +":"+u.getUser_id());
                            writer.newLine();
                            writer.flush();
                        }

                        writer.write("END");
                        writer.newLine();
                        writer.flush();
                        for(ClientHandler client : Server.clientHandlers)
                        {
                            if(client !=this && client.email !=null)
                            {
                                client.writer.write("USER:"+User.getUserByEmail(email).getFull_name()+":" + email + ":"  +User.getUserByEmail(email).getUser_id());
                                client.writer.newLine();
                                client.writer.flush();
                            }
                        }

                    } else {
                        writer.write("FAIL");
                        writer.newLine();
                        writer.flush();
                    }
                }else if(message.startsWith("SEND:"))
                {
                    // get the massage Of Client
                    String[] parts  = message.split(":",4);
                    String senderEmail = parts[1];
                    String recevierEmail = parts[2];
                    String messageText = parts[3];
                    // get the sender and receicer
                    User sender = User.getUserByEmail(senderEmail);
                    User receiver = User.getUserByEmail(recevierEmail);

                    // Save Message in dataBase
                    Message msg = new Message(
                            sender.getUser_id(),
                            receiver.getUser_id(),
                            messageText,
                            java.time.LocalDateTime.now().toString(),
                            "false"
                    );
                    msg.save();

                    for(ClientHandler client : Server.clientHandlers)
                    {
                        if(client.email != null && (client.email.equals(recevierEmail) || client.email.equals(senderEmail)))
                        {
                            client.writer.write("MSG_FROM:" + senderEmail + ":" + messageText);
                            client.writer.newLine();
                            client.writer.flush();
                        }
                    }


                }else if(message != null && message.startsWith("LOGOUT:"))
                {
                    String email = message.substring("LOGOUT:".length());
                    Boolean ss = User.updateStatusUser(email,Status.OFFLINE);
                    if(ss)
                    {
                        onlineUsers = User.getUserOnline();
                        writer.write("SUCCUSSFULLLOGOUT:" +email);
                        writer.newLine();
                        writer.flush();
                        for(ClientHandler clientHandler : Server.clientHandlers)
                        {
                            if (clientHandler != this && clientHandler.email !=null) {
                                clientHandler.writer.write("REMOVE_USER:" + email);
                                clientHandler.writer.newLine();
                                clientHandler.writer.flush();

                            }
                        }

                    }
                    closeEverything();

                }
                else if(message!=null && message.startsWith("GET_MESSAGE:"))
                {
                    List<Message> messages;
                    String [] param = message.split(":");
                    String senderId = param[1];
                    String reciver_id = param[2];
                    try {
                        messages = Message.getMessage(Integer.parseInt(senderId), Integer.parseInt(reciver_id));
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                    if(messages!=null)
                    {
                        for(Message m : messages) {
                            String senderEmail = User.getUserById(m.getSender_id()).getEmail();
                            writer.write("SUCCESSFUL_SEND_MESSAGE:"+senderEmail+":" + m.getMessage_text());
                            writer.newLine();
                            writer.flush();
                        }
                        writer.write("END_MESSAGES");
                        writer.newLine();
                        writer.flush();
                    }

                }
            }
        } catch (IOException e) {
            closeEverything();
        }
    }

    private void closeEverything() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
