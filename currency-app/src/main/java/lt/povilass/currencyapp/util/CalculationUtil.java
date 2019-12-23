package lt.povilass.currencyapp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import lt.povilass.currencyapp.data.CurrencyRate;

public class CalculationUtil {

	/**
	 * Calculates the change for list of currencies based on previous rates
	 * @param oldRates 
	 * @param newRates
	 */
	public static void calculateChange(Map<String, CurrencyRate> oldRates, Map<String, CurrencyRate> newRates) {
		
		newRates.forEach((code, newRate) -> {
			if(oldRates.containsKey(code)) {
				CurrencyRate oldRate = oldRates.get(code);
				
				// if a date for new currency rate is same as old one, then calculation is skipped 
				//(occurs when for new rates for given day is not published) 
				if(!oldRate.getDate().equals(newRate.getDate())) {
					BigDecimal change = newRate.getAmount().subtract(oldRate.getAmount());
					BigDecimal percentile = change.divide(oldRate.getAmount(), RoundingMode.HALF_UP);
					
					newRate.setChange(change);
					newRate.setPercentile(percentile);
				} else {
					newRate.setChange(oldRate.getChange());
					newRate.setPercentile(oldRate.getPercentile());
				}
				
				
				
			} else {
				newRate.setChange(BigDecimal.valueOf(0.0));
				newRate.setPercentile(BigDecimal.valueOf(0.0));
				
			}
		});
	}

	/**
	 * Calculate Euro equivalent for given sum of money
	 * @param rate - rate of other currency
	 * @param money - amount of money to be calculated
	 * @return Euro equivalent of amount of money
	 */
	public static BigDecimal calculateMoney(BigDecimal rate, BigDecimal money) {
		BigDecimal decimal = money.divide(rate);
		return decimal;
	}

}
