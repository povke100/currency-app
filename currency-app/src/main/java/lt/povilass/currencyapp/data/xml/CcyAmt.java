package lt.povilass.currencyapp.data.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.ToString;

@ToString
@XmlRootElement(name="CcyAmt")
@XmlAccessorType(XmlAccessType.FIELD)
public class CcyAmt {
	
	private String Ccy;
	private String Amt;

	public String getCcy() {
		return Ccy;
	}

	public void setCcy(String Ccy) {
		this.Ccy = Ccy;
	}

	public String getAmt() {
		return Amt;
	}

	public void setAmt(String Amt) {
		this.Amt = Amt;
	}

}
