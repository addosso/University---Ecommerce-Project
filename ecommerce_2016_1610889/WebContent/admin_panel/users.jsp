<%@page import="ecommerce_2016_1610889.db.Connector"%>
<%@page import="ecommerce_2016_1610889.data.User"%>
<%@page import="java.util.List"%>
<%@page import="ecommerce_2016_1610889.data.DataLayer"%>
<%@page import="ecommerce_2016_1610889.data.DataLayerFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

</head>
<body>
<%
DataLayerFactory database = new DataLayerFactory();
Connector c = new Connector();
c.setUrlXmlDocument(getServletContext().getRealPath(getServletContext().getInitParameter("XMLName")));
c.setXmlMode(Boolean.valueOf(getServletConfig().getInitParameter("XML")));
c.setDbName(getServletContext().getInitParameter("DBName"));
c.setDbUser(getServletContext().getInitParameter("DBUser"));
c.setDbPassword(getServletContext().getInitParameter("DBPassword"));
DataLayer data = database.getDataBase(c);

if(request.getParameter("action") !=  null){
	
	if(request.getParameter("action").equals("create_user")){
	
	data.addUser(request.getParameter("username"),
			request.getParameter("password"), request.getParameter("realname"), 
			request.getParameter("realsurname"), request.getParameter("admin"));
	}
	
	if(request.getParameter("action").equals("modify")){
		data.deleteUser(request.getParameter("o_username"));
		data.addUser(request.getParameter("username"),
				request.getParameter("password"), request.getParameter("realname"), 
				request.getParameter("realsurname"), request.getParameter("role"));
		
	}
	if(request.getParameter("action").equals("delete")){
		data.deleteUser(request.getParameter("o_username"));
	}
}

%>

<form action="users.jsp" method="post">
Username:<input type="text" name="username" /><br>
Password:<input type="text" name="password" /><br>
RealName<input type="text" name="realname" /><br>
RealSurname<input type="text" name="realsurname"/><br>
Administrator <input type="checkbox" name="admin" value="administrator">
<input type="hidden" name="createUser" value="yes">
<input type="submit" name="action" value="create_user">
</form>

<h1>Administrators:</h1><br>
<table border="1">
<tr>

<td>Username</td>
<td>Password</td>
<td>RealName</td>
<td>RealSurname</td>
</tr>
<%
List<User> administratorUsers = data.getUsersByRole("administrator");
for(int i=0; i < administratorUsers.size();i++){
	User currentUser = administratorUsers.get(i);
	%> 
<tr>
<form action="" method="post">
<input type="hidden" name="o_username" value="<%= currentUser.getUsername()%>">
<input type="hidden" name="role" value="administrator">
<td><input type="text" name="username" value="<%= currentUser.getUsername()%>"></td>
<td><input type="text" name="password" value="<%= currentUser.getPassword()%>"></td>
<td><input type="text" name="realname" value="<%= currentUser.getName()%>"></td>
<td><input type="text" name="realsurname" value="<%= currentUser.getSurname()%>"></td>
<td><input type="submit" name="action" value="modify"></td>
<td><input type="submit" name="action" value="delete"></td>
</form>
</tr>
	
	<% 
}


%>


</table><br>
<h1>Regular:</h1> <br>
<table border="1">
<tr>

<td>Username</td>
<td>Password</td>
<td>RealName</td>
<td>RealSurname</td>
</tr>
<%
List<User> regularUsers = data.getUsersByRole("regular");
for(int i=0; i < regularUsers.size();i++){
	User currentUser = regularUsers.get(i);
	%> 

<tr>
<form action="" method="post">
<input type="hidden" name="o_username" value="<%= currentUser.getUsername()%>">
<input type="hidden" name="role" value="regular">
<td><input type="text" name="username" value="<%= currentUser.getUsername()%>"></td>
<td><input type="text" name="password" value="<%= currentUser.getPassword()%>"></td>
<td><input type="text" name="realname" value="<%= currentUser.getName()%>"></td>
<td><input type="text" name="realsurname" value="<%= currentUser.getSurname()%>"></td>
<td><input type="submit" name="action" value="modify"></td>
<td><input type="submit" name="action" value="delete"></td>
</form>
</tr>

	<% 
}


%>


</table><br>

</body>
</html>