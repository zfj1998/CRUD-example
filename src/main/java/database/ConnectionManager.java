package database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class ConnectionManager {
    private Connection conn;

    public ConnectionManager(){
        try{
            Class.forName("com.mysql.jdbc.Driver"); //加载mysq驱动
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            conn = null;
        }
        try {
            String user = "root";
            String password = "root";
            String url = "jdbc:mysql://localhost/exercise8";
            conn = (Connection) DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            e.printStackTrace();
            conn = null;
        }
    }

    public Connection getConnection(){
        return conn;
    }

}
