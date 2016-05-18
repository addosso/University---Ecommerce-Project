package ecommerce_2016_1610889.auth;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ecommerce_2016_1610889.data.DataLayer;
import ecommerce_2016_1610889.data.DataLayerFactory;
import ecommerce_2016_1610889.db.Connector;
import ecommerce_2016_1610889.db.XmlDb;

/**
 * Servlet implementation class Registrati
 */

public class Registrati extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	public void init(ServletConfig conf) throws ServletException{
		super.init(conf);
	
	}
    public Registrati() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DataLayerFactory database = new DataLayerFactory();
		Connector c = new Connector();
		c.setUrlXmlDocument(getServletContext().getRealPath(getServletContext().getInitParameter("XMLName")));
		c.setXmlMode(Boolean.valueOf(getServletConfig().getInitParameter("XML")));
		c.setDbName(getServletContext().getInitParameter("DBName"));
		c.setDbUser(getServletContext().getInitParameter("DBUser"));
		c.setDbPassword(getServletContext().getInitParameter("DBPassword"));
		DataLayer data = database.getDataBase(c);
	  
		String name = null;
		String username="";
		String password="";
		String cognome =null;
		
		username = request.getParameter("username");
		password = request.getParameter("password");
		name= request.getParameter("realname");
		cognome= request.getParameter("realsurname");
		
		
		if(username.equals("") || password.equals("") || !data.addUser(username, password, name, cognome, "regular") ){
				response.sendRedirect("signup.html");
		}else{ 
			
			
			response.sendRedirect("login.html");
		}
		
	}

}
