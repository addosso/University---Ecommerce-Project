package ecommerce_2016_1610889.auth;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ecommerce_2016_1610889.data.Carrello;
import ecommerce_2016_1610889.data.DataLayer;
import ecommerce_2016_1610889.data.DataLayerFactory;
import ecommerce_2016_1610889.data.User;
import ecommerce_2016_1610889.db.Connector;
import ecommerce_2016_1610889.db.XmlDb;

/**
 * Servlet implementation class Login
 */

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);

	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		DataLayerFactory database = new DataLayerFactory();
		Connector c = new Connector();
		c.setUrlXmlDocument(getServletContext().getRealPath(getServletContext().getInitParameter("XMLName")));
		c.setXmlMode(Boolean.valueOf(getServletConfig().getInitParameter("XML")));
		c.setDbName(getServletContext().getInitParameter("DBName"));
		c.setDbUser(getServletContext().getInitParameter("DBUser"));
		c.setDbPassword(getServletContext().getInitParameter("DBPassword"));
		DataLayer data = database.getDataBase(c);
		

		User currentUser = data.getUser(request.getParameter("username"), request.getParameter("password"));
		if (currentUser != null) {
			HttpSession session = request.getSession();
			session.setAttribute("role", currentUser.getAccountType());
			session.setAttribute("logged", true);
			session.setAttribute("username", currentUser.getUsername());
			if (currentUser.getName() != null)
				session.setAttribute("name", currentUser.getName());
			if (currentUser.getSurname() != null)
				session.setAttribute("surname", currentUser.getSurname());
			session.setAttribute("carrello", new Carrello());
			response.sendRedirect("");
		} else
			response.sendRedirect("login.html");
	}

}
