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



final String DATA_DIRECTORY = "files/images";
  final int MAX_MEMORY_SIZE = 1024 * 1024 * 2;
  final int MAX_REQUEST_SIZE = 1024 * 1024;
 

	boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	 
    if (!isMultipart) {
        return;
    }
   
    // Create a factory for disk-based file items
    DiskFileItemFactory factory = new DiskFileItemFactory();

    // Sets the size threshold beyond which files are written directly to
    // disk.
    factory.setSizeThreshold(MAX_MEMORY_SIZE);

    // Sets the directory used to temporarily store files that are larger
    // than the configured size threshold. We use temporary directory for
    // java
    factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

    // constructs the folder where uploaded file will be stored
    String uploadFolder = getServletContext().getRealPath("")
            + File.separator + DATA_DIRECTORY;

    // Create a new file upload handler
    ServletFileUpload upload = new ServletFileUpload(factory);

    // Set overall request size constraint
    upload.setSizeMax(MAX_REQUEST_SIZE);
    String itemId ="";
    String categoryId = "";
  	Item toInsert = new Item();
    try {
        // Parse the request
        List items = upload.parseRequest(request);
        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
				String fieldName = item.getFieldName();
            	if(fieldName.equals("categoryId")){
            		categoryId = item.getString();
            	}
            	if(fieldName.equals("itemId")){
            		itemId = item.getString();
            	}
            
        }
		
       

    } catch (FileUploadException ex) {
    	
        throw new ServletException(ex);
    } catch (Exception ex) {
        throw new ServletException(ex);
    }
    finally{
    	x.removeItem(Integer.valueOf(categoryId), Integer.valueOf(itemId));
    
    }

%>

<jsp:forward page="products.jsp" ></jsp:forward>
    
</body>
</html>