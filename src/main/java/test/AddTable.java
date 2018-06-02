package test;

import javax.swing.plaf.nimbus.State;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class AddTable {
    private Connection conn;

    public AddTable(){
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

    public Boolean addTable(String tablename){
        Boolean result = false;
        try{
            Statement stm = conn.createStatement();
            String addTable = "create table "+tablename+" (name varchar(20), id varchar(100));";
            stm.executeUpdate(addTable);
            stm.close();
            conn.close();
            result = true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return result;
    }

    public Boolean alterTable(String tablename){
        Boolean result = false;
        try{
            Statement stm = conn.createStatement();
            String addTable = "ALTER TABLE "+tablename+" ADD newcolumn varchar(20);";
            stm.executeUpdate(addTable);
            stm.close();
            conn.close();
            result = true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return result;
    }

    public Boolean createTrigger(){
        Boolean result = false;
        try{
            Statement stm = conn.createStatement();
            String addTable = "CREATE  TRIGGER tri_insert before insert on " +
                    "hello for each row begin " +
                    "if new.name='c' then " +
                    "update hello set name='a' where name='b'; " +
                    "end if; " +
                    "end";
            stm.executeUpdate(addTable);
            stm.close();
            conn.close();
            result = true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return result;
    }

    public static void main(String[] strings){
        AddTable addTable = new AddTable();
        System.out.println(addTable.createTrigger());
        //System.out.println(addTable.addTable("hello"));
        //System.out.println(addTable.alterTable("hello"));
    }
}
