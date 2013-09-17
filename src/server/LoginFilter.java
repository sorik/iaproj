package server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.CookieInfo;
import utils.ServerUtils;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(urlPatterns={"/","/login.html","/main.html"})
public class LoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LoginFilter() {
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
		// place your code her
		
		HttpServletRequest sevReq = (HttpServletRequest)request;
		HttpServletResponse sevResp = (HttpServletResponse)response;
	    String requestedURL = sevReq.getServletPath();	
		CookieInfo cookieInfo = ServerUtils.get_cookie(sevReq);
		System.out.println(requestedURL);
		if (cookieInfo.is_logged_in) {
			if (ServerUtils.get_base_url().endsWith(requestedURL) ||
					requestedURL.endsWith("login.html")) {
				sevResp.sendRedirect("main.html");
			} else {
				// do nothing
				// pass the request along the filter chain
				chain.doFilter(request, response);				
			}
			
		} else {
			// no logged-in information in cookie
			if (requestedURL.endsWith("login.html")) {
				// pass the request along the filter chain
				chain.doFilter(request, response);
			} else {
				sevResp.sendRedirect("login.html");
			}
			
		}
		
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
