package persistlayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//May need to add @Override to all methods?

/**
 * DbAccessImpl - The persist class that implements the methods defined in DbAccessInterface.
 * 				  Connects to and executes the SQL queries for the database.
 */
public class DbAccessImpl {
	/**
	 * connect - Connects to our database using the values set in DbAccessConfiguration 
	 * @return - Returns the database connection
	 */
	public Connection connect() {
		Connection con = null;
		try {
			Class.forName(DbAccessConfiguration.DB_DRIVE_NAME);
			con = DriverManager.getConnection(DbAccessConfiguration.DB_CONNECTION_URL,
					DbAccessConfiguration.DB_CONNECTION_USERNAME, DbAccessConfiguration.DB_CONNECTION_PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	} //connect

	/**
	 * retrieve - Retrieves info from the database
	 * @param con - Holds the connection to the MySQL database
	 * @param queryString - The SQL query used to retrieve the specified
	 * 						information from the database
	 * @return - The retrieved ResultSet holding the requested database info
	 */
	public ResultSet retrieve(Connection con, String queryString) {
		ResultSet results = null;
		try {
			Statement statement = con.createStatement();
			results = statement.executeQuery(queryString);
			return results;
		} catch (SQLException e) {
			e.printStackTrace();
			return results;
		}
	} //retrieve

	/**
	 * create - Creates or inserts information to our database 
	 * @param con - Holds the connection to our database
	 * @param query - The specified SQL query used to create 
	 * @return - Returns an integer indicating whether the query was executed 
	 * 			 (0 if unsuccessful)
	 */
	public int create(Connection con, String query) {
		int i = 0;

		try {
			Statement statement = con.createStatement();
			i = statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return i;
	} //create

	/**
	 * update: Updates the information in the MySQL database
	 * @param con: Holds the connection to the database
	 * @param query: The SQL query used to update the specified
	 * 				 database table
	 * @return - Returns an integer indicating whether the query was executed 
	 * 			 (0 if unsuccessful)
	 */
	public int update(Connection con, String query) {
		int i = 0;
		
		try{
			Statement statement = con.createStatement();
			i= statement.executeUpdate(query);
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return i;
	} //update

	/**
	 * delete: Executes an SQL query to delete information from
	 * the database
	 * @param con: Holds the connection to the database
	 * @param query: The SQL query that is used to delete the info
	 * @return - Returns an integer indicating whether the query was executed 
	 * 			 (0 if unsuccessful)
	 */
	public int delete(Connection con, String query) {
		int i = 0;

		try {
			Statement statement = con.createStatement();
			i = statement.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return i;
	} //delete

	/**
	 * disconnect: Disconnects the MySQL database
	 * @param con: Holds the connection for our database
	 */
	public void disconnect(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} //disconnect
}
