package controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;

import database.ConnectionManager;
@WebServlet("/add")
public class CtrlAdd extends HttpServlet{
    @Override
    public void doPost (HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException{
        String name = request.getParameter("name");
        String position = request.getParameter("position");
        String salary = request.getParameter("salary");
        String start_date = request.getParameter("start_date");
        String office = request.getParameter("office");
        String extn = request.getParameter("extn");
        int flag=0;
        try{
            PreparedStatement stmt = null;
            Connection conn = new ConnectionManager().getConnection();
            if (conn != null) {
                String sql = "insert into user values (?,?,?,?,?,?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1,name);
                stmt.setString(2,position);
                stmt.setString(3,salary);
                stmt.setString(4,start_date);
                stmt.setString(5,office);
                stmt.setString(6,extn);

                flag = stmt.executeUpdate();    //更新条数
            }
            stmt.close();
            conn.close();
        } catch (SQLException e){
            flag = 0;
        }

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(flag);
    }
}
