package server;

import java.sql.*;

public class SQLConnect {
    private static Connection connection;
    private static Statement statement;
    //private static Logger logger = LoggerFactory.getLogger(SQLConnect.class);

    SQLConnect(){
        try {
            createDB();

            createUser();

            connect();
            checkAutorisation("LoginTest","111");
            checkAutorisation("1LoginTest","222");

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
            //logger.info("Tests user create in BD");

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
            //logger.info("BD create successful");

        } catch (SQLException e) {
            e.printStackTrace();
            //logger.error("Cannot create BD");
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
                //logger.info("User: "+Color.ANSI_BLUE.getColor()+login+Color.ANSI_RESET.getColor()+" authorized");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //logger.error(e.getSQLState());
        }
        //logger.error("User "+login+" cannot authorized");
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
