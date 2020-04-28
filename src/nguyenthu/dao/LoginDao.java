package nguyenthu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import nguyenthu.bean.LoginBean;
import nguyenthu.util.DBConnect;

public class LoginDao {

	public String authenticateUser(LoginBean loginBean) {
		String username = loginBean.getUsername();
		String password = loginBean.getPassword();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String usernameDB = "";
		String passwordDB = "";
		String roleDB = "";

		try {
			con = DBConnect.getJDBCConnection();
			String sql = " Select username, password, role from user";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery(sql);
			
			while (rs.next()) {
				
				usernameDB = rs.getString("username");
				passwordDB = rs.getString("password");
				roleDB = rs.getString("role");
				
				if (username.equals(usernameDB) && password.equals(passwordDB) && roleDB.equals("Admin"))
					
					return "Admin_Role";
				if (username.equals(usernameDB) && password.equals(passwordDB) && roleDB.equals("Editor"))
					return "Editor_Role";
				if (username.equals(usernameDB) && password.equals(passwordDB) && roleDB.equals("User"))
					return "User_Role";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Invalid user credentials";

	}

}
