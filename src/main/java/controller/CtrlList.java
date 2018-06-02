package controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;
import com.google.gson.Gson;

import bean.User;
import database.ConnectionManager;

@WebServlet("/list")
public class CtrlList extends HttpServlet{
    @Override
    public void doGet(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException {
        Connection conn = new ConnectionManager().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        String table = "user";

        //获取请求次数
        String draw = "0";
        draw = request.getParameter("draw");
        //数据起始位置
        String start = request.getParameter("start");
        //数据长度
        String length = request.getParameter("length");

        //总记录数
        String recordsTotal = "0";

        //过滤后记录数
        String recordsFiltered = "";

        //定义列名
        String[] cols = {"","name", "position", "salary", "start_date", "office", "extn"};
        //获取客户端需要那一列排序
        String orderColumn = "0";
        orderColumn = request.getParameter("order[0][column]");
        orderColumn = cols[Integer.parseInt(orderColumn)];
        //获取排序方式 默认为asc
        String orderDir = "asc";
        orderDir = request.getParameter("order[0][dir]");

        //获取用户过滤框里的字符
        String searchValue = request.getParameter("search[value]");

        List<String> sArray = new ArrayList<String>();
        if (!searchValue.equals("")) {
            sArray.add(" name like '%" + searchValue + "%'");
            sArray.add(" position like '%" + searchValue + "%'");
            sArray.add(" salary like '%" + searchValue + "%'");
            sArray.add(" start_date like '%" + searchValue + "%'");
            sArray.add(" office like '%" + searchValue + "%'");
            sArray.add(" extn like '%" + searchValue + "%'");
        }

        String individualSearch = "";
        if (sArray.size() == 1) {
            individualSearch = sArray.get(0);
        } else if (sArray.size() > 1) {
            for (int i = 0; i < sArray.size() - 1; i++) {
                individualSearch += sArray.get(i) + " or ";
            }
            individualSearch += sArray.get(sArray.size() - 1);
        }

        List<User> users = new ArrayList<User>();
        if (conn != null) {
            try{
                String recordsFilteredSql = "select count(1) as recordsFiltered from " + table;
                stmt = conn.createStatement();
                //获取数据库总记录数
                String recordsTotalSql = "select count(1) as recordsTotal from " + table;
                rs = stmt.executeQuery(recordsTotalSql);
                while (rs.next()) {
                    recordsTotal = rs.getString("recordsTotal");
                }


                String searchSQL = "";
                String sql = "SELECT * FROM " + table;
                if (individualSearch != "") {
                    searchSQL = " where " + individualSearch;   //模糊匹配
                }
                sql += searchSQL;
                recordsFilteredSql += searchSQL;
                sql += " order by " + orderColumn + " " + orderDir;
                recordsFilteredSql += " order by " + orderColumn + " " + orderDir;
                sql += " limit " + start + ", " + length;


                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    users.add(new User(rs.getString("name"),
                            rs.getString("position"),
                            rs.getString("salary"),
                            rs.getString("start_date"),
                            rs.getString("office"),
                            rs.getString("extn")));
                }

                if (searchValue != "") {
                    rs = stmt.executeQuery(recordsFilteredSql);
                    while (rs.next()) {
                        recordsFiltered = rs.getString("recordsFiltered");
                    }
                } else {
                    recordsFiltered = recordsTotal;
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e){
                e.printStackTrace();
                users = null;
                recordsFiltered="0";
                recordsTotal="0";
                draw="0";
            }
        }


        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("data", users);
        info.put("recordsTotal", recordsTotal);
        info.put("recordsFiltered", recordsFiltered);
        info.put("draw", draw);
        String json = new Gson().toJson(info);

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(json);
    }
}
