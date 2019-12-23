package lt.povilass.currencyapp.data;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CurrencyRate implements Comparable<CurrencyRate>{

	private String currency;
	private BigDecimal amount;
	private String date;
	
	private BigDecimal change;
	private BigDecimal percentile;
	
	
	@Override
	public int compareTo(CurrencyRate o) {
		if(this.currency.equals(o.currency) && this.date.equals(o.date)) {
			return 0;
		} else if (this.currency.compareTo(o.currency) > 0 || this.currency.compareTo(o.currency) == 0 && this.currency.compareTo(o.currency) > 0) {
			return 1;
		} else {
			return -1;
		}
		
	}
}
