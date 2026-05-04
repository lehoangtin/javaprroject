package com.parking.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/quanlynhaxe"; 
    private static final String USER = "root";
    private static final String PASS = "Tin@1234";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Kết nối Database thành công!");
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại: " + e.getMessage());
        }
        return conn;
    }
    public static void main(String[] args) {
        getConnection();
    }
}
