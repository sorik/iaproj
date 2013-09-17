package server;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import projExceptions.DBException;

/**
 * Servlet implementation class Root
 */
@WebServlet("/dddddindex.html")
public class Root extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Root() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// when index.html is called, we need to route the page to either login.html or main.html
		System.out.println("GET ROOT");
		
		String path = request.getRequestURL().toString();
		String[] path_split = path.split("/");
		String last_path = path_split[path_split.length-1];
		System.out.println(last_path);
		String email = "";
		String token = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null ) {
			for (int i = 0; i < cookies.length ; i++) {
				if ("email".equals(cookies[i].getName())) {
					email = cookies[i].getValue();
				} else if ("access-token".equals(cookies[i].getName())) {
					token = cookies[i].getValue();
				}				
			}			
		}

		
//		ServerRoute route = ServerRoute.LOGIN;  // default
		String route = "";
		System.out.println(email);
		System.out.println(token);
		if (email.isEmpty() || token.isEmpty()) {
			// go to login
			route = "login.html";
			System.out.println("1");
		}
		
		// check db
		try {
			String db_token = User.get(email, "access_token");
			if (token.equals(db_token)) {
				// go to main
				System.out.println("2");
				route = "main.html";
			} else {
				// go to login
				System.out.println("3");
				route = "login.html";
			}
		} catch (DBException e) {
			// go to login
			System.out.println("4");
			route = "login.html";
		}

		if (route.startsWith(last_path))
		{
			try {
				RequestDispatcher dispatcher = request.getRequestDispatcher(route);
			    dispatcher.forward(request, response);
			} catch (IOException e) {
				
			}			
		} else {
			try {
				response.sendRedirect(route);
			} catch (IOException e) {
				
			}			
		}


		


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
