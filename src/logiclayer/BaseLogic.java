package logiclayer;

//Handles all of the methods that the Servlets call

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import objectlayer.User;
import persistlayer.DbAccessImpl;

public class BaseLogic {
	//Variables
	DbAccessImpl db = new DbAccessImpl();
	Connection con;

	//Constructor
	public BaseLogic() {
		con = db.connect();
	}
	
	// Registers a user
	public boolean register(String username, String password, String email, String securityQuestion, String securityAnswer) {
		String query = "insert into user(name, password, email, securityQuestion, securityAnswer) values(\"" + username	+ "\",\"" + password + "\",\"" + email + "\",\"" + securityQuestion + "\",\"" + securityAnswer + "\");";
		String noDupes = "select * from user where name = \"" + username + "\";";
		ResultSet results = db.retrieve(con, noDupes);
		try {
			if (results.next()) {
				// The username has already been taken
				return false;
			} else {
				// The username is not taken, and can be registered
				db.create(con, query);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}//End of register

	// Logs the user in
	public boolean login(String username, String password) {
		String findQuery = "select * from user where name = \"" + username + "\";";
		ResultSet results = db.retrieve(con, findQuery);
		try {
			if (results.next()) {
				User user = new User(results.getInt(1), results.getString(2), results.getString(3),	results.getString(4), results.getString(5));
				if (user.getName().equals(username) && user.getPassword().equals(password)) {
					// Username and password match
					return true;
				} else {
					// Wrong username and password
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//method to change the password of a user
	public void changePassword(String username, String newPassword){ 
		String changeQuery = "update user set password = \""+newPassword+"\" where name = \""+username+"\";"; 
		db.update(con, changeQuery);
	}
	
	// Disconnect from server
	public void disconnect() {
		db.disconnect(con);
	}

}
