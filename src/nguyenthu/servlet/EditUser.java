package nguyenthu.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import nguyenthu.bean.LoginBean;
import nguyenthu.bean.UserBean;
import nguyenthu.dao.LoginDao;
import nguyenthu.dao.UserDao;

/**
 * Servlet implementation class EditUser
 */
@WebServlet("/EditUser")
public class EditUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);

		int id = Integer.parseInt(request.getParameter("id"));
		String username = request.getParameter("username");
		String fullname = request.getParameter("fullname");
		String address = request.getParameter("address");
		String password = request.getParameter("password");
		int age = Integer.parseInt(request.getParameter("age"));
		String gmail = request.getParameter("gmail");

		UserBean user = new UserBean();
		user.setAddress(address);
		user.setAge(age);
		user.setPassword(password);
		user.setFullName(fullname);
		user.setUsername(username);
		user.setId(id);
		user.setGmail(gmail);
		
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("user");
		String pass = (String) session.getAttribute("pass");
		
		LoginBean loginBean = new LoginBean();
		loginBean.setPassword(pass);
		loginBean.setUsername(userName);
		
		LoginDao loginDao = new LoginDao();
		String Role = loginDao.authenticateUser(loginBean);
		try {
			if (UserDao.updateUserById(user)) {
				System.out.println("Edit_User");
				request.setAttribute("user", user);
				if (Role.equals("User_Role")) {
					RequestDispatcher rd = request.getRequestDispatcher("User.jsp");
					rd.forward(request, response);
				}
				if (Role.equals("Admin_Role")) {
					RequestDispatcher rd = request.getRequestDispatcher("Admin.jsp");
					rd.forward(request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
