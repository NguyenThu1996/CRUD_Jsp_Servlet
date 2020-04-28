package nguyenthu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
	private static String DB_URL = "jdbc:mysql://localhost:3306/manageruser";
	private static String USER_NAME = "root";
	private static String PASSWORD = "";

	public static Connection getJDBCConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			return DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // Load Database

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;
	}

	public static void main(String[] args) {
		Connection con = getJDBCConnection();
		if (con != null) {
			System.out.println("Dang nhap thanh cong");
		} else {
			System.out.println("dang nhap that bai");
		}
	}
}
