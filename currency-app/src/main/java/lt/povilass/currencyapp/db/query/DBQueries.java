package lt.povilass.currencyapp.db.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import lt.povilass.currencyapp.config.Config;
import lt.povilass.currencyapp.data.CurrencyRate;
import lt.povilass.currencyapp.db.DBConnection;
import lt.povilass.currencyapp.db.DBConnectionImpl;

@Slf4j
public class DBQueries {

	private static DBConnection conn = new DBConnectionImpl();
	
	private static String connectionString = Config.getDBConnString();
	private static String username = Config.getDBUsername();
	private static String password = Config.getDBPassword();

	/**
	 * Collects all currency rates for given currency code
	 * 
	 * @param code - Currency code
	 * @return List of rates for given currency
	 * @throws SQLException           - thrown if execution of SQL query fails
	 * @throws ClassNotFoundException - Thrown if Database driver is not found
	 */
	public static List<CurrencyRate> getRateHistoryForCurrency(String code) throws SQLException, ClassNotFoundException {
		List<CurrencyRate> rates = new ArrayList<>();

		conn.openConnection(connectionString, username, password);

		PreparedStatement stmt = conn.getConnection()
				.prepareStatement("SELECT DATE, CODE, AMOUNT, CHANGE, PERCENT FROM CR_SCHEMA.RATES WHERE CODE = ? ORDER BY DATE DESC");
		stmt.setString(1, code);
		
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			CurrencyRate rate = new CurrencyRate();

			rate.setDate(rs.getString("DATE"));
			rate.setCurrency(rs.getString("CODE"));
			rate.setAmount(rs.getBigDecimal("AMOUNT"));
			rate.setChange(rs.getBigDecimal("CHANGE"));
			rate.setPercentile(rs.getBigDecimal("PERCENT"));

			rates.add(rate);
		}

		stmt.close();
		conn.closeConnection();
		//Collections.sort(rates);
		return rates;

	}

	/**
	 * Collects newest currency rates from database
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static Map<String, CurrencyRate> getNewestfromDB() throws SQLException, ClassNotFoundException {
		Map<String, CurrencyRate> newest = new HashMap<String, CurrencyRate>();

		conn.openConnection(connectionString, username, password);

		PreparedStatement stmt = conn.getConnection()
				.prepareStatement("SELECT F.M_DATE AS DATE, F.CODE, T.AMOUNT, T.CHANGE, T.PERCENT FROM ( " + 
						"SELECT MAX(DATE) AS M_DATE, CODE " + 
						"FROM CR_SCHEMA.RATES " + 
						"GROUP BY CODE) F " + 
						"INNER JOIN CR_SCHEMA.RATES T " + 
						"ON T.DATE = F.M_DATE AND T.CODE = F.CODE ORDER BY DATE DESC, CODE ASC;");
		
		if(!stmt.execute()) {
			throw new SQLException("Result after statement execution was not ResultSet.");
		}
		
		
		ResultSet rs = stmt.getResultSet();

		while (rs.next()) {
			CurrencyRate rate = new CurrencyRate();

			rate.setDate(rs.getString("DATE")); 
			rate.setCurrency(rs.getString("CODE"));
			rate.setAmount(rs.getBigDecimal("AMOUNT"));
			rate.setChange(rs.getBigDecimal("CHANGE"));
			rate.setPercentile(rs.getBigDecimal("PERCENT"));

			newest.put(rate.getCurrency() ,rate);
		}

		stmt.close();
		conn.closeConnection();

		return newest;
	}

	/**
	 * Inserts newest currency rates to database
	 * @param rates
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void insertNewestRates(Map<String, CurrencyRate> rates) throws SQLException, ClassNotFoundException {
		conn.openConnection(connectionString, username, password);
		
		PreparedStatement stmt = conn.getConnection().prepareStatement("MERGE INTO CR_SCHEMA.RATES VALUES (?, ?, ?, ?, ?)");

		rates.forEach((key, value) -> {
			log.debug("insertNewestRates. Currency rate:{}", value);			
			
			try {
				stmt.setString(1, value.getDate());
				stmt.setString(2, value.getCurrency());
				stmt.setBigDecimal(3, value.getAmount());
				stmt.setBigDecimal(4, value.getChange());
				stmt.setBigDecimal(5, value.getPercentile());

				stmt.executeUpdate();

				stmt.clearParameters();
			} catch (SQLException e) {
				log.error("");
			}

		});

		stmt.close();
		conn.closeConnection();

	}
}
