
import java.util.Random;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;


/*
 * Provides utility methods.
 */
public class Utils {
	
	static final String base_string = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static Random rand = new Random();
	/*
	 * Validate email address format.
	 */
	public static boolean validate_email(String email) {
		try {
		    InternetAddress emailAddr = new InternetAddress(email);
		    emailAddr.validate();
		} catch (AddressException ex) {
		    return false;
		}
		return true;
	}
	
	public static String generate_random_token() {
		int length = 20;
		return generate_random_string(length);
	}
	
	public static String generate_random_password() {
		int length = 6;
		return generate_random_string(length);
	}
	
	private static String generate_random_string(int length) {

        StringBuilder token = new StringBuilder(length);
		for( int i = 0; i < length; i++ ) 
			token.append(base_string.charAt(rand.nextInt(base_string.length())));
		return token.toString();		
	}

}
