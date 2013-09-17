

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import projExceptions.DBException;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// NO GET method yet
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Check email and password. 
		// If successful issue access token, and save it to db.
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		String result = "";
		String message = "";
		String access_token = "";
		String validation = validate(email, password);
		if ( validation == null) {
			// valid data
			// issue access token and save it to the db.
			access_token = Utils.generate_random_token();
			
			// XXX save to db
			try {
				User.set(email, "access_token", access_token);
			} catch (DBException ex) {
				// not found
			}
			
			// send response
			JSONObject obj = new JSONObject();
			obj.put("type", "login");
			obj.put("result", "success");
			obj.put("access_token", access_token);
			try {
				PrintWriter out = response.getWriter();
				out.print(obj);
				out.close();
				
			} catch (IOException e) {
				System.out.println(e);
			}	
			
		} else {
			// not valid data
		
			// send response
			JSONObject obj = new JSONObject();
			obj.put("type", "login");
			obj.put("result", "fail");
			obj.put("message", validation);
			try {
				PrintWriter out = response.getWriter();
				out.print(obj);
				out.close();
			} catch (IOException e) {
				System.out.println(e);
			}	
		}
		

	}
	
	/*
	 * Validate email and password.
	 * Return null if they are valid.
	 * Return error message if they are not valid.
	 */
	private String validate(String email, String password) {
		
		if (email.isEmpty()) {
			return "Email is empty";
		}
		
		if (password.isEmpty()) {
			return "Password is empty";
		}
		
		try {
			String db_password = User.get(email, "password");
			if (db_password.equals(password)) {
				
			} else {
				// not matched
				return "bot";
			}
		} catch (DBException ex) {
			// not found
			return "bot";
		}
		
		
		
		return null;
	}

}
