package lt.povilass.currencyapp.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBConnection {
	
	public Connection getConnection() throws ClassNotFoundException, SQLException;

	public void closeConnection() throws SQLException;

	public void openConnection(String connString, String username, String password) throws SQLException;

}
