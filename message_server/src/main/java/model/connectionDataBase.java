package model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Muhammed_PC
 */
public class connectionDataBase {
    private static connectionDataBase instance;
    private Connection connection;
    private final  String URL = "jdbc:mysql://localhost:3306/chat_db";
    private String USERNAME = "root";
    private String PASSWORD = "12345678";

    private connectionDataBase()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(connectionDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static connectionDataBase getInstance()
    {
        if(instance==null)
        {
            instance  = new connectionDataBase();
        }
        return instance;
    }

    public Connection getConntion()
    {
        if(connection ==null )
        {
            try {
                connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            } catch (SQLException ex) {
                Logger.getLogger(connectionDataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return connection;
    }

    public void  getClose()
    {

        try {
            if(connection !=null || connection.isClosed() )
            {

                connection.close();
                System.out.println("Connection Colsing ...");

            }
        } catch (SQLException ex) {
            Logger.getLogger(connectionDataBase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Can Not Connection Closing  ...!");
        }

    }




}

