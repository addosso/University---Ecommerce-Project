<%@page import="ecommerce_2016_1610889.data.Category"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="ecommerce_2016_1610889.data.DataLayer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>
<%@ page import="org.apache.commons.io.output.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert Category</title>
</head>
<body>


<form action="" method="post"  enctype="multipart/form-data" >
Nome categoria: <input type="text" name="category_name" ><br>
<input type="file" name="category_image" /><br>
<input type="submit" name="action" value="crea categoria">
</form>
<%
  final String DATA_DIRECTORY = "files/images";
  final int MAX_MEMORY_SIZE = 1024 * 1024 * 2;
  final int MAX_REQUEST_SIZE = 1024 * 1024;
 

	DataLayer x = (DataLayer)request.getAttribute("database");
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
    String oldImage = "";
    String action ="";
    String categoryId = "";
    Category toInsert = new Category();
    String fileName;

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
                System.out.println(filePath);
                // saves the file to upload directory
                if(!fileName.equals("")){
                toInsert.setImageUrl("images/"+fileName);
                	item.write(uploadedFile);
                
                }
               
            }else{
            	String fieldName = item.getFieldName();
            	if(fieldName.equals("category_name")){
            		toInsert.setName(item.getString());
            	}
            	if(fieldName.equals("action")){
            		action = item.getString();
            	}
            	if(fieldName.equals("old_img")){
            		oldImage = item.getString();
            		toInsert.setImageUrl(oldImage);
            	}
            	if(fieldName.equals("categoryId")){
            		categoryId = item.getString();
            		toInsert.setId(Integer.valueOf(categoryId));
            	}
            }
        }
		
       

    } catch (FileUploadException ex) {
    	
        throw new ServletException(ex);
    } catch (Exception ex) {
        throw new ServletException(ex);
    }
    finally{
    	
    	if(action.equals("crea categoria")){
    	x.addCategory(toInsert);
    	}
    	if(action.equals("modify")){
    		x.modifyCategory(toInsert);
    		
    	}
    	
    }





%>



</body>
</html>