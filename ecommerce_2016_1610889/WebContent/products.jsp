<%@page import="ecommerce_2016_1610889.db.Connector"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="ecommerce_2016_1610889.utils.Tuple"%>
<%@page import="ecommerce_2016_1610889.data.Carrello"%>
<%@page import="java.util.List"%>
<%@page import="ecommerce_2016_1610889.data.Item"%>
<%@page import="ecommerce_2016_1610889.data.Category"%>
<%@page import="ecommerce_2016_1610889.data.DataLayer"%>
<%@page import="ecommerce_2016_1610889.data.DataLayerFactory"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="ecommerce_2016_1610889.db.XmlDb"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.NodeList"%>


<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Ecommerce_2016_1610889 || ProductsPage</title>
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
DataLayer dataBase = database.getDataBase(c);

request.setAttribute("database", dataBase);




HttpSession userIn = request.getSession(true);
Carrello carrello = null;
if(userIn.getAttribute("role") != null && userIn.getAttribute("role").equals("administrator")){
	request.getRequestDispatcher("insert_item.jsp").include(request, response);
}

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
				carrello.insertItem(
						new Tuple
						(Integer.valueOf(request.getParameter("quantity")),
								dataBase.getItem(Integer.valueOf(request.getParameter("item_category")), Integer.valueOf(request.getParameter("item"))  )));
			userIn.setAttribute("carrello", carrello);
			if( userIn.getAttribute("name") != "" || userIn.getAttribute("surname") != "")
				user = userIn.getAttribute("name")+" "+ userIn.getAttribute("surname");
			else
				user = userIn.getAttribute("username")+"";
				%>
				Salve,  <%= user %> <br>
				
				<a href="trolley.jsp">Carrello </a>: #Oggetti: <%= carrello.getNumbersOfItems() %> e Prezzo Totale <%= carrello.getTotalPrice() %>
				<a href="logout">Logout</a><br>
				<a href=".">Categorie</a>
		<%
		}
		%>
		

<br>
<form action="products.jsp" method="post" >
<label>Vedi :
</label>
<input type="hidden" name="categories" value="<%= request.getParameter("categories")%>"/>
 <input type="text" name="n"/> prodotti per pagina
<br>
<input type="submit" value="cambia numero di elementi">
</form>
<br>
<form action="products.jsp" method="post" >
<input type="hidden" name="categories" value="<%= request.getParameter("categories")%>"/>

Ordina per : <select name="sortBy" >
<option value="id">ID</option>
<option value="price">Prezzo</option>
<option value="title">Titolo</option>
<option value="available">Disponibilità</option>
<input type="submit" value="cambia ordinamento" />
</select>
</form>
<%

String imagesDir = getServletContext().getContextPath()+getServletContext().getInitParameter("ImagesDir");
HttpSession x = request.getSession(true);

	
	String role="";
	if(x.getAttribute("role") != null){
		role = (String) x.getAttribute("role");
	}
	
	
	String categoriesArray = request.getParameter("categories").replace("%5B","[").replace("%5D", "]");
	String categories = request.getParameter("categories").replace("%5B","[").replace("%5D", "]").replace("[", "").replace("]", "");
	ArrayList<Category> cat = new ArrayList<Category>();
	for(int i=0; i<categories.length();i++){
		if(categories.charAt(i) == '-'){
			continue;
		}	
		cat.add(dataBase.getCategoryById(Integer.valueOf(categories.charAt(i)+"")));
	}
	
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
	
	
	List<Item> items = dataBase.getItemsSortedBy(cat, sortBy);
	
	//Elimino gli items che gia ho dentro il carrello dalla view
	if(userIn.getAttribute("logged") != null){
		
		for(int i=0; i< carrello.getItems().size(); i++){
			items.remove(carrello.getItems().get(i).getItem());
		}
	}
	
	//Se è un utente normale che visualizza la pagina, inoltre devo anche levargli gli elementi che hanno isVisible a false
	if(role.equals("") || role.equals("regular")){
		for(int i=0; i< items.size(); i++){
			if(!items.get(i).isVisible())
			items.remove(items.get(i));
		}
	}
	int totalNumberOfItem = items.size();
	

	int numbersOfPages = totalNumberOfItem/itemsPerPage;
	int offSettingPage = totalNumberOfItem%itemsPerPage;
	if(offSettingPage > 0)
		numbersOfPages +=1;
	
	if(pageNumber > numbersOfPages){
			%>
			<h1>Nessun oggetto nella pagina!</h1>
			<%
	}else{
	
				
	
	int startRange = (pageNumber-1)*itemsPerPage ;
	int finishRange = (pageNumber-1)*itemsPerPage+itemsPerPage-1;
	int showedItem =0;
	for(int i=0; i<totalNumberOfItem; i++){
			if((showedItem >= startRange && showedItem <=finishRange)){
				
				
				// TODO Riporta le modifiche con il currencyNAMEEE!!!
				if(role.equals("administrator")){
					String dateFormatted ="";
					DateFormat df =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					dateFormatted = df.format(items.get(i).getDate());
				%>
				<form action="" method="post" id="modify_item" enctype="multipart/form-data">
				
				<h1>Categoria: <%= items.get(i).getCategory().getName() %> </h1>	
				
				<input type="hidden" name="itemId" value="<%= items.get(i).getId() %>">
				<input type="hidden" name="categoryId" value="<%= items.get(i).getCategory().getId() %>">
				
				<p> ID: <%= items.get(i).getId() %></p>	
						
				<p>Showable <input type="checkbox" name="show" value="<%= items.get(i).isVisible()%>" <%= items.get(i).isVisible()? "checked" : "" %>/></p>
				<p>Titolo: <input type="text" name="title" value="<%= items.get(i).getTitle() %>" /> </p>
				<input type="hidden" name="old_img" value="<%= items.get(i).getImgSrc() %>">
				<img src="<%= imagesDir + items.get(i).getImgSrc()  %>" />
				<p>New image<input type="file" name="item_image"/></p>
				<p>Descrizione: <textarea form="modify_item" name="description" ><%= items.get(i).getDescription()%></textarea> </p>
				<p>Disponibilità: <input type="datetime-local" name="available" value="<%= dateFormatted %>"/> </p>
				<p>Prezzo: <input type="number" name="price" value="<%= items.get(i).getPrice() %>"  step="0.01" min="0.00"/>  </p>
				<p> Currency Name: <input type="text" name="currencyName" value="<%= items.get(i).getCurrency().getName()%>"/> </p>
				<p> Currency toEur: <input type="number" name="toEur" value="<%= items.get(i).getCurrency().getToEur()%>" step="0.01" min="0.00"/> </p>
				<p> Currency toDollar: <input type="number" name="toDollar" value="<%= items.get(i).getCurrency().getToDollar()%>" step="0.01" min="0.00"/> </p>
				<input type="submit" name="action" value="modify"> 
				<input type="submit" name="action" value="delete"> 
				</form>
				
				<%} else{ %>
				<h1>Categoria: <%= items.get(i).getCategory().getName() %> </h1>
				<p>Titolo:<%= items.get(i).getTitle()%></a> </p>
				<img src="<%= imagesDir + items.get(i).getImgSrc()  %>" />
				<p>Descrizione: <%= items.get(i).getDescription()%> </p>
				<p>Disponibilità: <%= items.get(i).getDate().toLocaleString() %>  </p>
				<p>Prezzo: <%= items.get(i).getPrice()%>  </p>
				<% if(session.getAttribute("logged") != null){
				%>
				<form method="post" action="products.jsp">
				<input type="hidden" name="categories" value="<%= request.getParameter("categories")%>"/>
				<input type="hidden" name="item" value="<%= items.get(i).getId()%>"/>
				<input type="hidden" name="item_category" value="<%= items.get(i).getCategory().getId() %>"/>
				<p>Quantità: <input type="number" name="quantity"></p>
				<p><<input type="submit" value="Aggiungi al Carrello" />
				</form>
				<br><br>
				<%} %>
				<% 	 			
			}
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
	<a href="products.jsp?
		categories=
		<%= categoriesArray%>&p=<%= previousP %>
		&n=<%= itemsPerPage %>&sortBy=<%=sortBy %>">Precedente</a>
		
	<%
	for(int i=1;i<numbersOfPages+1;i++){
		%>
		<a href="products.jsp?
		categories=
		<%= categoriesArray%>&p=<%= i %>
		&sortBy=<%=sortBy %>"><%= i %></a>	
<% 
	}
	%>
	<a href="products.jsp?
		categories=
		<%= categoriesArray%>&p=<%= nextP %>
		&sortBy=<%=sortBy %>">Successivo</a>
	<%
	
	
	
	


%>


</body>
</html>