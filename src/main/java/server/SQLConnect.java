package server;

import java.sql.*;

public class SQLConnect {
    private static Connection connection;
    private static Statement statement;

    SQLConnect(){
        try {
            createDB();
            createUser();
            connect();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            disconnect();
        }
    }

    private static void createUser() {
        try {
            statement.executeUpdate("INSERT INTO Users (User,Pass) VALUES ('qwe','qwe');");
            System.out.println("user qwe exists");
            statement.executeUpdate("INSERT INTO Users (User,Pass) VALUES ('111','111');");
            System.out.println("user 111 exists");
            statement.executeUpdate("INSERT INTO Users (User,Pass) VALUES ('222','222');");
            System.out.println("user 222 exists");

        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("users already exists");
        }
    }

    private static void createDB() {
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Users (" +
                    "ID   INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL, " +
                    "User STRING  UNIQUE NOT NULL, Pass STRING  NOT NULL);");
            System.out.println("DB exists");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkAutorisation(String login, String pass) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT ID,User,Pass FROM Users WHERE User = ? AND Pass = ?;");
            ps.setString(1,login);
            ps.setString(2,pass);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void connect() throws Exception{
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:cloud.db");
        statement = connection.createStatement();
//        createDB();
//        createUser();
    }

    private static void disconnect(){
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
