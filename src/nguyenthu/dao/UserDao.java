package nguyenthu.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import java.sql.PreparedStatement;
import nguyenthu.bean.UserBean;
import nguyenthu.util.DBConnect;

public class UserDao {
	public static UserBean getUserByUsername(String username) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserBean user = new UserBean();
		try {
			con = DBConnect.getJDBCConnection();
			String sql = "SELECT ID, NAME, AGE, ADDRESS, USERNAME, PASSWORD, EMAIL FROM USER Where USERNAME= '"
					+ username + "'";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				user.setFullName(rs.getString("name"));
				user.setId(rs.getInt("id"));
				user.setAge(rs.getInt("age"));
				user.setAddress(rs.getString("address"));
				user.setPassword(rs.getString("password"));
				user.setUsername(rs.getString("username"));
				user.setGmail(rs.getString("email"));
			}
			return user;
		} catch (Exception e) {
			return null;
		} finally {
			con.close();
		}
	}

	public static ArrayList<UserBean> listUser() throws Exception {
		ArrayList<UserBean> listUser = new ArrayList<UserBean>();
		Connection con = DBConnect.getJDBCConnection();
		try {
			String sql = "select *from user";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				UserBean user = new UserBean();
				user.setId(rs.getInt("id"));
				user.setAddress(rs.getString("address"));
				user.setAge(rs.getInt("age"));
				user.setGmail(rs.getString("email"));
				user.setFullName(rs.getString("name"));
				listUser.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			con.close();
		}
		return listUser;
	}

	public static UserBean getUserById(int id) throws SQLException {
		Connection con = DBConnect.getJDBCConnection();
		UserBean user = new UserBean();
		try {
			String sql = "Select * from user where id =" + id;
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user.setAge(rs.getInt("age"));
				user.setId(rs.getInt("id"));
				user.setAddress(rs.getString("address"));
				user.setFullName(rs.getString("name"));
				user.setGmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setUsername(rs.getString("username"));
			}
		} catch (Exception e) {

		}
		return user;
	}

	public static boolean updateUserById(UserBean user) throws SQLException {
		Connection con = null;

		try {
			con = DBConnect.getJDBCConnection();
			String sql = "update user set name ='" + user.getFullName() + "', age = '" + user.getAge() + "', email = '"
					+ user.getGmail() + "' , address ='" + user.getAddress() + "', password = '" + user.getPassword()
					+ "' where id ='" + user.getId() + "'";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (Exception e) {
		} finally {
			con.close();
		}
		return true;
	}
	public static boolean insertUser(UserBean user) {
		Connection con = DBConnect.getJDBCConnection();
		try {
			
			String sql = "Insert into user(name, username, password, address, email, role,age )" + " values (?,?,?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getFullName());
			ps.setString(2, user.getUsername());
			ps.setString(3, user.getPassword());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getGmail());
            ps.setString(6, user.getRole());
            ps.setInt(7, user.getAge());
            ps.execute();
            con.close();
		} catch (SQLException e) {
			return false;
		}
		return true;
		
	}
	public static boolean deletedUserById(int id) throws SQLException
	{
		Connection con = DBConnect.getJDBCConnection();
		try {
			String sql = "Delete from user where id=" +id;
			PreparedStatement ps = con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (Exception e) {

				return false;
		}finally {
			con.close();
		}
		return true;
	}
}
