package com.txl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/work?useUnicode=true&characterEncoding=utf8&ServerTimezone=GMT&useSSL=false&serverTimezone=UTC";
    private static String userName = "root";
    private static String password = "root";
    private Connection conn;
    private Statement stmt;

    public Connect() {
        try {
            Class.forName(driverName);//加载数据库
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);//使用DriverManger获取数据库连接
    }

    public void dispose() {
        try {
            if (conn != null) {
                conn.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}