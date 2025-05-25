package model.message;

import model.connectionDataBase;
import model.user.User;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Message {

    private int id;
    private int sender_id;
    private int resever_id;
    private String message_text;
    private String send_at;
    private String is_read;

    private static String sql;
    private static Connection conn;
    private PreparedStatement pre;
    private static ResultSet re;

    public Message(int sender_id, int resever_id, String message_text, String send_at, String is_read) {
        this.sender_id = sender_id;
        this.resever_id = resever_id;
        this.message_text = message_text;
        this.send_at = send_at;
        this.is_read = is_read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getResever_id() {
        return resever_id;
    }

    public void setResever_id(int resever_id) {
        this.resever_id = resever_id;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getSend_at() {
        return send_at;
    }

    public void setSend_at(String send_at) {
        this.send_at = send_at;
    }

    public String isIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public boolean save() {
        sql = "insert into messages(sender_id,resever_id,message_text,send_at,is_read)"
                + "values(?,?,?,?,?);";

        conn = connectionDataBase.getInstance().getConntion();

        try (PreparedStatement pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            pre.setInt(1, this.sender_id);
            pre.setInt(2, this.resever_id);
            pre.setString(3, this.message_text);
            pre.setString(4, this.send_at);
            pre.setString(5, this.is_read);


            int a = pre.executeUpdate();

            if (a > 0) {

                re = pre.getGeneratedKeys();
                if (re.next()) {
                    this.id = re.getInt(1);
                }
            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static List<Message> getMessage(int sender_id, int resever_id) throws SQLException {
        sql = "select * from users where sender_id=? and resever_id=?";

        conn = connectionDataBase.getInstance().getConntion();

        try (PreparedStatement pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            pre.setInt(1, sender_id);
            pre.setInt(2, resever_id);
            List<Message> massages;
            int a = pre.executeUpdate();
            if (re.next()) {
                return (List<Message>) re;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

return null;
    }
}
