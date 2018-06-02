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
@WebServlet("/edit")
public class CtrlEdit extends HttpServlet{
    @Override
    public void doPost (HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        String name = request.getParameter("name");
        String position = request.getParameter("position");
        String salary = request.getParameter("salary");
        String start_date = request.getParameter("start_date");
        String office = request.getParameter("office");
        String extn = request.getParameter("extn");

        Connection conn = new ConnectionManager().getConnection();
        PreparedStatement stmt = null;
        int flag = 0;
        try{
            if (conn != null) {
                String sql = "update user set position = ?,salary = ?,start_date = ?,office = ?,extn = ? where name = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(6, name);
                stmt.setString(1, position);
                stmt.setString(2, salary);
                stmt.setString(3, start_date);
                stmt.setString(4, office);
                stmt.setString(5, extn);
                flag = stmt.executeUpdate();
            }
            stmt.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
            flag = 0;
        }

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(flag);
    }
}
