package model.user;





import model.connectionDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


public class User {
    private int user_id;
    private String full_name;
    private String email;
    private String password;
    private Status status;
    private String phone_Number;
    private String photo;
    private String sql;

    private Connection conn;
    private PreparedStatement pre;
    private ResultSet re;

    public User(String full_name, String email, String password) {
        this.full_name = full_name;
        this.email = email;
        this.password = password;
    }

    public User(String full_name, String email, String password, String statup, String phone_Number, String photo) {
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.status = Status.OFFLINE;
        this.phone_Number = phone_Number;
        this.photo = photo;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatup() {
        return status;
    }

    public void setStatup(Status status) {
        this.status = status;
    }

    public String getPhone_Number() {
        return phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        this.phone_Number = phone_Number;
    }

    public boolean save() {
        sql = "insert into users(full_name,email,password,status,phone_number,photo)"
                + "values(?,?,?,?,?,?);";

        conn = connectionDataBase.getInstance().getConntion();

        try (PreparedStatement pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            pre.setString(1, this.full_name);
            pre.setString(2, this.email);
            pre.setString(3, this.full_name);
            pre.setString(4, this.password);
            pre.setString(5, String.valueOf(this.status));
            pre.setString(6, this.phone_Number);
            pre.setString(7, this.photo);

            int a = pre.executeUpdate();

            if (a > 0) {

                re = pre.getGeneratedKeys();
                if (re.next()) {
                    this.user_id = re.getInt("user_id");
                }
            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


    public static boolean Login(String email, String password) {
        Connection conn = connectionDataBase.getInstance().getConntion();
        String sql = "select * from users where email = ? and password = ?";

        try (PreparedStatement pre = conn.prepareStatement(sql);) {
            pre.setString(1, email);
            pre.setString(2, password);
            ResultSet re = pre.executeQuery();
            return re.next();
        } catch (SQLException ex) {
            System.out.println("Can not give Users");

        }

        return false;

    }


    public static List<User> getUserOnline() {
        Connection conn = connectionDataBase.getInstance().getConntion();
        String sql = "select * from users where status= ?";
        List<User> onlineUsers = new ArrayList<>();
        try (PreparedStatement pre = conn.prepareStatement(sql);) {
            pre.setString(1, String.valueOf(Status.ONLINE));
            ResultSet re = pre.executeQuery();
            while (re.next()) {
                User user = new User(
                        re.getString("full_name"),
                        re.getString("email"),
                        re.getString("password"),
                        re.getString("status"),
                        re.getString("phone_number"),
                        re.getString("profile_picture")
                );
                user.setUser_id(re.getInt("user_id"));
                onlineUsers.add(user);

            }
            return onlineUsers;
        } catch (SQLException e) {
            System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii" + e.getMessage());
        }
        return  null;
    }

    public  static boolean  updateStatusUser(String email,Status status)
{
    Connection conn = connectionDataBase.getInstance().getConntion();
    String sql = "update users set status= ? where email= ?";

    try (PreparedStatement pre = conn.prepareStatement(sql);) {
        pre.setString(1, String.valueOf(status));
        pre.setString(2, email);
      int a = pre.executeUpdate();
        if(a>0)
        {
        return  true;
        }

    }catch (SQLException ex)
    {
        System.out.println("can not update Status");
    }

    return  false;
}

public  static  User getUserByEmail(String email)
{

    Connection conn = connectionDataBase.getInstance().getConntion();
    User user = null;
    String sql = "select * from users where email= ?";
    List<User> onlineUsers = new ArrayList<>();
    try (PreparedStatement pre = conn.prepareStatement(sql);) {
        pre.setString(1, email);
        ResultSet re = pre.executeQuery();
        while (re.next()) {
            user= new User(
                    re.getString("full_name"),
                    re.getString("email"),
                    re.getString("password"),
                    re.getString("status"),
                    re.getString("phone_number"),
                    re.getString("profile_picture")
            );
            user.setUser_id(re.getInt("user_id"));

        }
        return  user;

    } catch (SQLException e) {
        System.out.println("can Not User By Email " + e.getMessage());
    }
    return  null;
}


    public static User getUserById(int senderId) {
        Connection conn = connectionDataBase.getInstance().getConntion();
        User user = null;
        String sql = "select * from users where user_id= ?";
        List<User> onlineUsers = new ArrayList<>();
        try (PreparedStatement pre = conn.prepareStatement(sql);) {
            pre.setInt(1, senderId);
            ResultSet re = pre.executeQuery();
            while (re.next()) {
                user= new User(
                        re.getString("full_name"),
                        re.getString("email"),
                        re.getString("password"),
                        re.getString("status"),
                        re.getString("phone_number"),
                        re.getString("profile_picture")
                );
                user.setUser_id(re.getInt("user_id"));

            }
            return  user;

        } catch (SQLException e) {
            System.out.println("can Not User By Email " + e.getMessage());
        }
        return  null;
    }


}
