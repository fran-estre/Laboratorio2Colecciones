package Shared;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestBase {
    public static void main(String[] args) throws UnknownHostException {
        String user = "postgres";
        String password = "1409";
        String url = "jdbc:postgresql://localhost:5432/MyDatabase";
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("se conecto a la base");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        ping();


    }
    public static void  ping() throws UnknownHostException {
        byte[] buffer = new byte[SerializationHandler.SIZE + SerializationHandler.HEADER];
        try{
        InetAddress address =InetAddress.getByName("192.168.0.102");
        DatagramPacket datagramPacket1=new DatagramPacket(buffer, buffer.length);
        datagramPacket1.setAddress(address);
            System.out.println(datagramPacket1.getAddress().toString());
    } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
