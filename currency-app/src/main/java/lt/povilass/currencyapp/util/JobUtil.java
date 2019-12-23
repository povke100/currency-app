package lt.povilass.currencyapp.util;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import lombok.extern.slf4j.Slf4j;
import lt.povilass.currencyapp.config.Config;
import lt.povilass.currencyapp.data.CurrencyRate;
import lt.povilass.currencyapp.data.xml.FxRates;
import lt.povilass.currencyapp.db.query.DBQueries;
import lt.povilass.currencyapp.network.ConnectionImpl;

@Slf4j
public class JobUtil {

	/**
	 * Method for scheduled execution
	 * @param oldRates
	 * @return current exchange rates of currency
	 * @throws Exception
	 */
	public static Map<String, CurrencyRate> processRates(Map<String, CurrencyRate> oldRates) throws Exception {

		LocalDate now = LocalDate.now();
		log.debug("processRates. date: {}", now);

		List<NameValuePair> payload = new LinkedList<NameValuePair>();
		payload.add(new BasicNameValuePair("tp", "EU"));
		payload.add(new BasicNameValuePair("dt", now.toString()));

		ConnectionImpl conn = new ConnectionImpl();

		FxRates fxRates = conn.sendPost(payload, Config.getLBEndpoint());
		log.debug("Raw rates: {}", fxRates);

		Map<String, CurrencyRate> newRates = XmlUtil.getRates(fxRates);
		log.debug("Processed rates: {}", newRates);

		CalculationUtil.calculateChange(oldRates, newRates);
		log.debug("Processed rates with change: {}", newRates);
		
		DBQueries.insertNewestRates(newRates);

		return newRates;
	}
}
