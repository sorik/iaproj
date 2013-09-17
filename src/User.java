import org.json.simple.JSONObject;
import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;

import projExceptions.DBException;


/*
 * Provides Database Interface.
 */
public class User {
	
	private static CouchDbClient dbEmailReg = new CouchDbClient("couchdb.properties");
	
	public static void create(String email, String token, String token_time) throws DBException {
		try {
			JSONObject doc = dbEmailReg.find(JSONObject.class, email);
			if(doc != null)
			{
				throw new DBException("Already exits!");
			}
		} catch(NoDocumentException e) {
			JSONObject new_doc = new JSONObject();
			new_doc.put("_id", email);
			new_doc.put("token", token);
			new_doc.put("token_time", token_time);
			dbEmailReg.save(new_doc); 
		}
	}
	public static void set(String email, String tag, String value) throws DBException {
		try{
			JSONObject doc = dbEmailReg.find(JSONObject.class, email);
			doc.put(tag, value);
			dbEmailReg.update(doc);	
		} catch(NoDocumentException e) {
			throw new DBException("Not found!");
		}
	}
	
	public static String get(String email, String tag) throws DBException {
		try{
			JSONObject value = null;
			value = dbEmailReg.find(JSONObject.class, email);
			return (String) value.get(tag);
		} catch(NoDocumentException e) {
			throw new DBException("Not found!");
		}
	}
	
	public static void delete(String email, String access_token) throws DBException {
		try {
			JSONObject value = null;
			value = dbEmailReg.find(JSONObject.class, email);

			if(value.get("access_token").equals(access_token))
				dbEmailReg.remove(value);
			else
				throw new DBException("Not valid token!");
		} catch(NoDocumentException e) {
			throw new DBException("Not found!");
		}
	}
}
