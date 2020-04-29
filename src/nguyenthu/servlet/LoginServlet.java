package nguyenthu.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nguyenthu.bean.LoginBean;
import nguyenthu.bean.UserBean;
import nguyenthu.dao.LoginDao;
import nguyenthu.dao.UserDao;
import tool.SendMail;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
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
		RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
		Controller c = new Controller();
		doGet(request, response);
		String username = request.getParameter("username");
		String password = request.getParameter("pass");
		
		HttpSession session1 = request.getSession();
		session1.setAttribute("user", username);
		session1.setAttribute("pass", password);
		
		LoginBean loginBean = new LoginBean();
		loginBean.setPassword(password);
		loginBean.setUsername(username);

		LoginDao loginDao = new LoginDao();
		UserDao userDao = new UserDao();
		String userValidate = loginDao.authenticateUser(loginBean);
		try {
			if (userValidate.equals("Admin_Role")) {
				System.out.println("Admin_Role");
				HttpSession session = request.getSession();// Create session;
				session.setAttribute("Admin", username); // setting session Attr
				session.setAttribute("pass", password);
				request.setAttribute("username", username);
				/*
				 * SendMail sendMails = new SendMail(); String subject = "MAIL NOTIFICATION";
				 * String body = "you have login";
				 */
			    //sendMails.senMailLogin(subject, body);
				response.sendRedirect("Admin");
				return;
				
			}
			if (userValidate.equals("Editor_Role")) {
				System.out.println("Editor_Role");
				HttpSession session = request.getSession();
				session.setAttribute("Editor", username);
				request.setAttribute("username", username);
				
				response.sendRedirect("Admin");
				return;
			}
			if (userValidate.equals("User_Role")) {
				System.out.println("User_Role");
				HttpSession session = request.getSession();
				session.setAttribute("User", username);
				session.setAttribute("pass", password);
				request.setAttribute("username", username);
				
				UserBean user = new UserBean();
				user = userDao.getUserByUsername(username);
                request.setAttribute("user",user);				
				RequestDispatcher rd = request.getRequestDispatcher("User.jsp");
				rd.forward(request, response);
				return;
			} else {
				System.out.println("Error message = " + userValidate);
				request.setAttribute("errMessage", userValidate);
				request.getRequestDispatcher("login.jsp").forward(request, response);
				return;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
