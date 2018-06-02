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
@WebServlet("/del")
public class CtrlDel extends HttpServlet{
    @Override
    public void doPost (HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        String name = request.getParameter("name");
        String extn = request.getParameter("extn");
        int flag = 0;
        try{
            PreparedStatement stmt = null;
            Connection conn = new ConnectionManager().getConnection();
            if (conn != null) {
                String sql = "delete from user where name = ? and extn = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1,name);
                stmt.setString(2,extn);
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
