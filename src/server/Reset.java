package server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import projExceptions.DBException;
import utils.CookieInfo;
import utils.ServerResponse;
import utils.ServerUtils;
import utils.Utils;

/**
 * Servlet implementation class Reset
 * Manage Password reset process
 */
@WebServlet("/Reset")
public class Reset extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Reset() {
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
		
		// Reset the password of the email that is currently logged in
		
		System.out.println("GET Reset");
		
		ServerResponse serverRes = ServerResponse.UNKNOWN;
		String param = "";
		
		CookieInfo cookieInfo = ServerUtils.get_cookie(request);
		if (cookieInfo.is_logged_in) {
			// reset password and send email to corresponding email address.
			// for log out,
			// remove access-token from the database
			// remove cookie
			try {
				String password = Utils.generate_random_password();
				
				User.set(cookieInfo.email, "password", password);
				User.set(cookieInfo.email, "access-token", cookieInfo.access_token);
				serverRes = ServerResponse.SENT_RESET;
				param = password;
				
			} catch (DBException e) {
				// should not be here
				System.out.println(e);
				serverRes = ServerResponse.UNKNOWN;
			}
			
		} else {
			serverRes = ServerResponse.NOT_MATCH_LOGGIN_INFO;
		}
		
		ServerUtils.response(serverRes, response, cookieInfo.email, param);
	}

}
