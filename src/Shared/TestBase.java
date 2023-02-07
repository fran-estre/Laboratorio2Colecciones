package Shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestBase {
    public static void main(String[] args) {
        String user = "postgres";
        String password = "1409";
        String url = "jdbc:postgresql://localhost:5432/MyDatabase";
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("se conecto a la base");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
