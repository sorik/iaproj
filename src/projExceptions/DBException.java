package projExceptions;

public class DBException extends Exception{
	
	public DBException() { 
		super(); 
	}
	
	public DBException(String msg) {
		super(msg);
	}
	
}
