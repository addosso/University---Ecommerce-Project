package ecommerce_2016_1610889.product;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ecommerce_2016_1610889.data.Carrello;
import ecommerce_2016_1610889.data.Category;
import ecommerce_2016_1610889.data.DataLayer;
import ecommerce_2016_1610889.data.DataLayerFactory;
import ecommerce_2016_1610889.db.Connector;
import ecommerce_2016_1610889.db.XmlDb;

/**
 * Servlet implementation class ProductsPage
 */
@WebServlet(name="index",
		urlPatterns={""},
		initParams={ @WebInitParam(name = "XML", value ="false")})
public class ProductsPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String pathImages;
	private String jsDir;
	private HttpSession userIn; 
   
	/**
     * @see HttpServlet#HttpServlet()
     */
	
	public void init(ServletConfig conf) throws ServletException{
		super.init(conf);
		pathImages = conf.getServletContext().getContextPath()
		+conf.getServletContext().getInitParameter("ImagesDir")+"";
		jsDir = conf.getServletContext().getContextPath()
				+"/"+conf.getServletContext().getInitParameter("JsDir");
			
			
	}
    public ProductsPage() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DataLayerFactory database = new DataLayerFactory();
		Connector conn = new Connector();
		conn.setUrlXmlDocument(getServletContext().getRealPath(getServletContext().getInitParameter("XMLName")));
		conn.setXmlMode(Boolean.valueOf(getServletConfig().getInitParameter("XML")));
		conn.setDbName(getServletContext().getInitParameter("DBName"));
		conn.setDbUser(getServletContext().getInitParameter("DBUser"));
		conn.setDbPassword(getServletContext().getInitParameter("DBPassword"));
		DataLayer data = database.getDataBase(conn);
		
		userIn = request.getSession(true);
		String role = "";
		PrintWriter out = response.getWriter();
		out.append("<html><head>"
				+ "<script src='"+jsDir+"ecommerce_2016_1610889.js'></script></head><body>");
		
		if(userIn.getAttribute("role") == null)
		out.append("<a href='login.html'>Login </a> <br><br>");
		
		else{
			String user = "";
			if( userIn.getAttribute("name") != "" || userIn.getAttribute("surname") != "")
				user = userIn.getAttribute("name")+" "+ userIn.getAttribute("surname");
			else
				user = userIn.getAttribute("username")+"";
			
			if(userIn.getAttribute("role").equals("administrator")){
				role =  (String) userIn.getAttribute("role");
				request.setAttribute("database",data );			  
				request.getRequestDispatcher("/admin_panel/insert_category.jsp").include(request, response);
			}else{
			Carrello x = (Carrello)userIn.getAttribute("carrello");
			out.append("<a href='trolley.jsp'>Carrello</a>: #Oggetti:"+x.getNumbersOfItems()+ " Prezzo finale:"+x.getTotalPrice()+"<br>");
			}
			out.append("Salve, "+user+"<br>");
			out.append("<a href='logout'>Logout</a><br>");
		
		}
		Set<Category> categories = data.getCategories(); 
		if(categories !=  null){
			for(Iterator<Category> c = categories.iterator(); c.hasNext();){
				Category categoryNow = c.next();
				
				if(role.equals("administrator")){
					out.append("<form action='.' method='post' enctype='multipart/form-data'>");
					if( categoryNow.getImageUrl() != null && !categoryNow.getImageUrl().equals(""))
						out.append("<img src='"+ pathImages+""+categoryNow.getImageUrl()+""+"'>");
						out.append("<input type='hidden' name='old_img' value='"+categoryNow.getImageUrl()+"'> ");
						out.append("<input type='hidden' name='categoryId' value='"+categoryNow.getId()+"'> ");
						
						out.append("New image: <input type='file' name='category_image' ><br>");
						
						out.append("<input type='checkbox' name='categories' "
								+ "value='"+categoryNow.getId()+"'/><input type='text' name='category_name' value='"+categoryNow.getName()+"'/><br>");
						
					
							userIn.setAttribute("database", data);
							out.append("<a href='delete_category.jsp?"
									+ "categoryID="+categoryNow.getId()+"'>DELETE</a>");
							out.append("<input type='submit' name='action 'value='modify'/></form><br>");
						
					
				}
				else{
				if(categoryNow.getImageUrl() != null && !categoryNow.getImageUrl().equals("") )
				out.append("<img src='"+ pathImages+""+categoryNow.getImageUrl()+""+"'>");
				
				out.append("<input type='checkbox' name='categories' "
						+ "value='"+categoryNow.getId()+"'>"+categoryNow.getName()+"</input><br>");
				
				if(role.equals("administrator")){
					userIn.setAttribute("database", data);
					out.append("<a href='delete_category.jsp?"
							+ "categoryID="+categoryNow.getId()+"'>DELETE</a><br>");
				}
				}	
		};
		}
		out.append("<button onclick='rewriteUrl(\"products.jsp\");'>Cerca</button>");
		out.append("</form></body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		doGet(request, response);
	}

	


}
