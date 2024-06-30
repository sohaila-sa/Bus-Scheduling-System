package BusScheduling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class databaseConnection {
	public static final String serverName = "jdbc:mysql://localhost/";
	public static final String dbName = "busscheduling";
	public static final String dbUser = "root";
	public static final String dbPassword = "";
	private static Connection connection = null;
	
	private databaseConnection() {}
	
	public static Connection getConnection() {
		 if (connection == null) {
	            try {
	                
	                Class.forName("com.mysql.cj.jdbc.Driver");
	                connection = DriverManager.getConnection(serverName+dbName, dbUser, dbPassword);
	                System.out.println("Connection established successfully.");
	            } catch (ClassNotFoundException e) {
	                System.err.println("JDBC Driver not found.");
	                e.printStackTrace();
	            } catch (SQLException e) {
	                System.err.println("Failed to establish a connection.");
	                e.printStackTrace();
	            }
	        }
		return connection;
	}

	public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("Failed to close the connection.");
                e.printStackTrace();
            }
        }
    }
	
}
