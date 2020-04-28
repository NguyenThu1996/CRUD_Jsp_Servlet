
<%@page import="nguyenthu.dao.UserDao"%>
<%@page import="nguyenthu.bean.UserBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%int id; %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin</title>
<style>
input[type=text] {
	width: 130px;
	box-sizing: border-box;
	border: 2px solid #ccc;
	border-radius: 4px;
	font-size: 16px;
	background-color: white;
	background-image: url('searchicon.png');
	background-position: 10px 10px;
	background-repeat: no-repeat;
	padding: 12px 20px 12px 40px;
	padding-right: 0;
	-webkit-transition: width 0.4s ease-in-out;
	transition: width 0.4s ease-in-out;
	-webkit-transition: width 0.4s ease-in-out;
}

input[type=text]:focus {
	width: 100%;
}

.logout {
	position: absolute;
	left: 1250px;
	right: 100px;
}
</style>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>

</head>
 <% 
 	if(request.getSession(false).getAttribute("Admin") == null)
 	{
 		RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
 		rd.forward(request, response);
 	}
      ArrayList<UserBean> listUser = new ArrayList<UserBean>();
      listUser = UserDao.listUser();
 %>
<body>
	<script type="text/javascript">
		function ConfirmDelete() {
			var x = confirm("Are you sure you want to delete?");
			if (x)
				return true;
			else
				return false;
		}
	</script>
	<div class="container">
		<div>
			<a href=""><button type="button" class="btn btn-success">Add User</button></a> 
			<a href="LogoutServlet" class="logout"><button type="button" class="btn btn-secondary" >Logout</button></a>
		</div>

		<div class="contentPage">
			<h2>ManagerUser</h2>

			<table class="table table-hover">
				<thead>
					<tr>
						<th>ID</th>
						<th>Name</th>
						<th>Age</th>
						<th>Address</th>
						<th>Gmail</th>
						<th>Action</th>


					</tr>
				</thead>
				<tbody>
					<% 
					   for(UserBean item : listUser)
					   {
					%>
					<tr>
						<td><%= item.getId()%></td>
						<td><%= item.getFullName()%></td>
						<td><%= item.getAge() %></td>
						<td><%= item.getAddress()%></td>
						<td><%= item.getGmail() %></td>
						<td><a href="Admin_EditUser.jsp?id=<%=item.getId()%>">
								<button type="button" class="btn btn-primary">Edit</button></a>
							<button onclick="actionDelete($(this));" type="button" class="btn btn-danger" data-id="<%=item.getId() %>">DELETE</button>
							</div>
					</tr>
					<% }%>
				</tbody>
			</table>
		</div>
	</div>
	<!-- Modal -->
	<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">DELETE</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">Do you want to delete this?</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">NO</button>
					<a id="action-delete" href="#"><button type="button" class="btn btn-primary">YES</button></a>
				</div>
			</div>
		</div>
		</div>
		<script type ="text/javascript">
			function actionDelete(e){
				var id = e.data("id");
				var url = "DeletedServlet?id=" + id;
				$('#action-delete').attr('href', url);
				$('#exampleModal').modal('show');
			}
		   
		</script>
</body>