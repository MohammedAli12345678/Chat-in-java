package com.example.message_clinet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class AppState {

    public  static  String currentEmail;
    public static Socket socket;
    public  static BufferedWriter bufferedWriter;
    public static BufferedReader reader;
}
