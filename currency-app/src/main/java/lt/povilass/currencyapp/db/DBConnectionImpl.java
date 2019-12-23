package lt.povilass.currencyapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBConnectionImpl implements DBConnection {
	
	private Connection conn;
	
	@Override
	public void openConnection(String connString, String username, String password) throws SQLException {
		log.debug("openConnection. Opening connection to database: {}, {}, {}", connString, username, password);
		conn = DriverManager.getConnection(connString, username, password);
		log.debug("openConnection. Connection to database was opened!");
		
	}
	
	@Override
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		log.debug("getConnection. Returning database connection...");
		return conn;
	}

	@Override
	public void closeConnection() throws SQLException {
		log.debug("getConnection. Closing database connection...");
		conn.close();
		log.debug("getConnection. Database connection was closed.");

	}

	

}
