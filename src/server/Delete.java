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

/**
 * Servlet implementation class Delete
 * Delete the email that is registered
 */
@WebServlet("/Delete")
public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Delete() {
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
		
		// Take an email and an access-token.
		// Check their validation.
		// If validate, then delete the email from the database.
		
		System.out.println("POST Delete");
		
		ServerResponse serverRes = ServerResponse.UNKNOWN;
		String param = "";
		
		CookieInfo cookieInfo = ServerUtils.get_cookie(request);
		if (cookieInfo.is_logged_in) {
			// delete the email from database
			// remove cookie
			try {
				User.delete(cookieInfo.email, cookieInfo.access_token);
				serverRes = ServerResponse.DELETED;
				System.out.println("GET Deleted");
				
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
