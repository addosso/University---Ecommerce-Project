package ecommerce_2016_1610889.session;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class CookieMode
 */

public class CookieMode implements Filter {

	private boolean cookieModeStrict;
	private boolean one;

	private String dono = "";
    /**
     * Default constructor. 
     */
    public CookieMode() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		final HttpServletRequest req = (HttpServletRequest)request;
		final HttpServletResponse res = (HttpServletResponse)response;
		HttpSession session = req.getSession();
		if(cookieModeStrict){
			if(req.getCookies() == null){
				Cookie strictMode = new Cookie("strictMode", "true");
				res.addCookie(strictMode);
				res.sendError(401, "Abilita i cookie per utilizzare il sito");
				return;
			}
		
		}
		if(req.getRequestURI().contains("cookie")){
			Cookie strictMode = new Cookie("strictMode", "true");
			res.addCookie(strictMode);
			cookieModeStrict = true;
			res.sendRedirect("/ecommerce_2016_1610889/");
			return;	
		}
	
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
