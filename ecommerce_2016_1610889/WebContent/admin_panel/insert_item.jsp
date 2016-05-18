<%@page import="ecommerce_2016_1610889.data.Currency"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="ecommerce_2016_1610889.data.Item"%>
<%@page import="java.util.Set"%>
<%@page import="ecommerce_2016_1610889.data.Category"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.FileUploadException"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="ecommerce_2016_1610889.data.DataLayer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>InsertItem</title>
</head>
<body>
<%DataLayer x = (DataLayer)request.getAttribute("database"); 

%>

<form action="" method="post"  enctype="multipart/form-data" >
Categoria:<select name="categoryId">

<% Set<Category> cat = x.getCategories();
	for(Iterator<Category> it = cat.iterator(); it.hasNext();){
		Category catNow = it.next();
		%> 
		<option value="<%= catNow.getId() %>"> <%= catNow.getName() %></option>
		<%
	}
%> 

</select> </br>
Show: <input type="checkbox" name="show" value="true"><br>
Title:<input type="text" name="title" ><br>
Description:<input type="text" name="description"><br>
Available: <input type="datetime-local" name="available"><br>
Image(opzionale)<input type="file" name="item_image" /><br>
Price: <input type="number" name="price" step="0.01" min="0.00" /><br>
Currency Name: <input type="text" name="currencyName"/><br>
toEur: <input type="number" name="toEur" step="0.01" min="0.01"/><br>
toDollar: <input type="number" name="toDollar" step="0.01" min="0.01"/><br>
<input type="submit" name="action" value="insert">
</form><br><br>
<%
  final String DATA_DIRECTORY = "files/images";
  final int MAX_MEMORY_SIZE = 1024 * 1024 * 2;
  final int MAX_REQUEST_SIZE = 1024 * 1024;
 

	boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	 
    if (!isMultipart) {
        return;
    }
    DiskFileItemFactory factory = new DiskFileItemFactory();
    factory.setSizeThreshold(MAX_MEMORY_SIZE);
    factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
    String uploadFolder = getServletContext().getRealPath("")
            + File.separator + DATA_DIRECTORY;
    ServletFileUpload upload = new ServletFileUpload(factory);
    upload.setSizeMax(MAX_REQUEST_SIZE);
    String action ="";
    String itemId ="";
    String fileName ="";
    String categoryId = "";
    String oldImage = "";
  	Item toInsert = new Item();
  	Currency currency = new Currency();
    try {
        // Parse the request
        List items = upload.parseRequest(request);
        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
			if (!item.isFormField()) {
                fileName = new File(item.getName()).getName();
                
                String filePath = uploadFolder + File.separator + fileName;
                File uploadedFile = new File(filePath);
               
                if(!fileName.equals("")){
                	toInsert.setImgSrc("images/"+fileName);
                	item.write(uploadedFile);
                }
               
            }else{
            	String fieldName = item.getFieldName();
            	if(fieldName.equals("old_img")){
            		oldImage = item.getString();
            	}
            	if(fieldName.equals("itemId")){
            		itemId = item.getString();
            	}
            	if(fieldName.equals("action")){
            		action = item.getString();
            	}
            	if(fieldName.equals("categoryId")){
            		categoryId = item.getString();
            	}
            	if(fieldName.equals("show")){	
            		if(item.getString().equals("false"))
            		toInsert.setVisible(true);
            	}
            	if(fieldName.equals("title")){
            		toInsert.setTitle(item.getString());
            	}
            	if(fieldName.equals("description")){
            		toInsert.setDescription(item.getString());
            	}
            	if(fieldName.equals("price"))
            	{
            		toInsert.setPrice(Double.valueOf(item.getString()));
            	}
            	if(fieldName.equals("toEur")){
            		currency.setToEur(Double.valueOf(item.getString()));
            	}
            	if(fieldName.equals("toDollar")){
            		currency.setToDollar(Double.valueOf(item.getString()));
            	}
            	if(fieldName.equals("currencyName")){
            		currency.setName(item.getString());
            	}
            	if(fieldName.equals("available")){
            	 
            		String startDateString = item.getString();
            	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"); 
            	    Date startDate;
            	    try {
            	        startDate = df.parse(startDateString);
            	        toInsert.setDate(startDate);
            	        String newDateString = df.format(startDate);
            	        System.out.println(newDateString);
            	    } catch (ParseException e) {
            	        e.printStackTrace();
            	        
            	    }
            	}
            	
            }
        }
		
       

    } catch (FileUploadException ex) {
    	
        throw new ServletException(ex);
    } catch (Exception ex) {
        throw new ServletException(ex);
    }
    if(action.equals("insert")){
    	toInsert.setCurrency(currency);
    	x.addItem(Integer.valueOf(categoryId), toInsert);
    }
    if(action.equals("delete")){
    	x.removeItem(Integer.valueOf(categoryId), Integer.valueOf(itemId));
    }
    if(action.equals("modify")){
    	toInsert.setCurrency(currency);
    	if(fileName.equals("") ){
    		toInsert.setImgSrc(oldImage);
    	}
    	x.removeItem(Integer.valueOf(categoryId), Integer.valueOf(itemId));
    	x.addItem(Integer.valueOf(categoryId), Integer.valueOf(itemId), toInsert);
    	
    }
    

%>
    
</body>
</html>