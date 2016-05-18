<%@page import="ecommerce_2016_1610889.db.Connector"%>
<%@page import="ecommerce_2016_1610889.utils.Tuple"%>
<%@page import="ecommerce_2016_1610889.data.Item"%>
<%@page import="java.util.List"%>
<%@page import="ecommerce_2016_1610889.data.Carrello"%>
<%@page import="ecommerce_2016_1610889.data.DataLayer"%>
<%@page import="ecommerce_2016_1610889.data.DataLayerFactory"%>
<%@page import="ecommerce_2016_1610889.data.Category"%>
<%@page import="java.util.ArrayList"%>
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

HttpSession userIn = request.getSession(true);
Carrello carrello = null;
		 
		if(userIn.getAttribute("logged") == null)
		{
		%>	
		<a href="login.html">Login</a> <a href=".">Categorie</a>			
		<% 	
		}
		else{
			
			
			String user = "";
			carrello = (Carrello)userIn.getAttribute("carrello");
			String cc = request.getParameter("item");
			if(request.getParameter("item") != null) 
				carrello.updateItem(request.getParameter("item"), Integer.valueOf(request.getParameter("quantity")));
				
						
			userIn.setAttribute("carrello", carrello);

		
			if( userIn.getAttribute("name") != "" || userIn.getAttribute("surname") != "")
				user = userIn.getAttribute("name")+" "+ userIn.getAttribute("surname");
			else
				user = userIn.getAttribute("username")+"";
				%>
				Salve,  <%= user %> <br>
				
				Carrello: #Oggetti: <%= carrello.getNumbersOfItems() %> e Prezzo Totale <%= carrello.getTotalPrice() %>
				<a href="logout">Logout</a><br>
				<a href=".">Categorie</a>
		<%
		}
		%>
		

<br>

<%

String imagesDir = getServletContext().getContextPath()+getServletContext().getInitParameter("ImagesDir");
HttpSession x = request.getSession(true);
if( getInitParameter("XML").equals("true" )) {
	
	
	
	
	int pageNumber =1;
	int itemsPerPage = Integer.valueOf( getServletContext().getInitParameter("itemsPerPage") ); 
	
	if(x.getAttribute("n") != null) itemsPerPage = (Integer) x.getAttribute("n");
	
	String sortBy = "id";
	if(request.getParameter("p") != null) pageNumber = Integer.parseInt(request.getParameter("p"));
	if(request.getParameter("n") != null){
		itemsPerPage = Integer.parseInt(request.getParameter("n"));
		x.setAttribute("n", Integer.parseInt(request.getParameter("n")));
	}
	if(request.getParameter("sortBy") != null) sortBy = request.getParameter("sortBy");
	
	
	List<Tuple> items = carrello.getItems();
	
	
	
	int totalNumberOfItem = items.size();
	

	int numbersOfPages = totalNumberOfItem/itemsPerPage;
	int offSettingPage = totalNumberOfItem%itemsPerPage;
	if(offSettingPage > 0)
		numbersOfPages +=1;
	
	if(pageNumber > numbersOfPages){
			%>
			<h1>Nessun oggetto nel carrello!</h1>
			<%
	}else{
	
				
	
	int startRange = (pageNumber-1)*itemsPerPage ;
	int finishRange = (pageNumber-1)*itemsPerPage+itemsPerPage-1;
	int showedItem =0;
	for(int i=0; i<totalNumberOfItem; i++){
			if((showedItem >= startRange && showedItem <=finishRange)){
				
				%>
				
				<h1>Categoria: <%= items.get(i).getItem().getCategory().getName() %> </h1>				
			
				
			<p>Titolo: <%= items.get(i).getItem().getTitle() %> </p>
			
			<img src="<%= imagesDir + items.get(i).getItem().getImgSrc()  %>" />
			
			<p>Descrizione: <%= items.get(i).getItem().getDescription()%> </p>
			<p>Disponibilità: <%= items.get(i).getItem().getDate().toLocaleString() %>  </p>
			
			<p>Prezzo: <%= items.get(i).getItem().getPrice() %>  </p>
			
			<% if(session.getAttribute("logged") != null){
				
			%>
			<form method="GET" action="trolley.jsp">
			
			 <input type="hidden" name="item" value="<%= items.get(i).getItem().getTitle()%>"/>
			<p>Quantità: <input type="number" min="1" name="quantity" value="<%= items.get(i).getNumberOfItems()%>"></p>
			<p>Prezzo totale: <%= items.get(i).getTotalPrice()%></p>
			
			<p><input type="submit" value="Aggiorna quantità" />
			</form>
			<form method="post" action="trolley.jsp">
			
			 <input type="hidden" name="item" value="<%= items.get(i).getItem().getTitle()%>"/>
			 <input type="hidden" name="quantity" value="-1"></p>
			
				
			<p><input type="submit" value="Elimina dal carrello" />
			</form>
			
			<br><br>
				<%} %>
				<% 	 			
			}
			showedItem++;
		}
	}

	int nextP = pageNumber+1;
	int previousP = pageNumber-1;
	if(nextP >= numbersOfPages ){
		nextP = numbersOfPages;
	}
	if(previousP <= 1){
		previousP = 1;
	}
	
	
	%>
	<a href="trolley.jsp?
		p=<%= previousP %>">Precedente</a>
		
	<%
	for(int i=1;i<numbersOfPages+1;i++){
		%>
		<a href="trolley.jsp?
		p=<%= i %>"><%= i %></a>	
<% 
	}
	%>
	<a href="trolley.jsp?
		p=<%= nextP %>">Successivo</a>
	<%
	
	
	
	
}

%>


</body>
</html>