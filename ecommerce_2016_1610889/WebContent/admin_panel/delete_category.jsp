<%@page import="java.util.ArrayList"%>
<%@page import="ecommerce_2016_1610889.data.Category"%>
<%@page import="java.util.List"%>
<%@page import="ecommerce_2016_1610889.data.Item"%>
<%@page import="java.util.Iterator"%>
<%@page import="ecommerce_2016_1610889.data.DataLayer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Delete Category</title>
</head>
<body>

<%
String imagesDir = getServletContext().getContextPath()+getServletContext().getInitParameter("ImagesDir");
DataLayer x =(DataLayer) request.getSession().getAttribute("database");
if(request.getParameter("categoryID_delete") != null){
	x.removeCategory(Integer.valueOf(request.getParameter("categoryID_delete")));
	response.sendRedirect(".");
}

if(request.getParameter("categoryID") != null){
	
	ArrayList<Category> n = new ArrayList<Category>();
	n.add( x.getCategoryById(Integer.valueOf(request.getParameter("categoryID"))));
	%>
	<a href=".">Categorie (senza modificare la base di dati)</a><br>
	<form method="get" action="">
	<input type="hidden" name="categoryID_delete" value="<%= request.getParameter("categoryID") %>">
	<input type="submit" name="cancella" value="cancella">
	</form>
	<%
	for(Iterator<Item> c = x.getItemsSortedBy(n, "id").iterator(); c.hasNext();){
		Item curr = c.next();
		%>
		<p>Titolo: <%= curr.getTitle() %> </p>
			<img src="<%= imagesDir + curr.getImgSrc()  %>" />
			<p>Descrizione: <%= curr.getDescription()%> </p>
			<p>Disponibilità: <%= curr.getDate().toLocaleString() %>  </p>
			<p>Prezzo: <%= curr.getPrice() %>  </p>

		<%
	}
	
	
}

%>

</body>
</html>