package lt.povilass.currencyapp.data.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.ToString;

@ToString
@XmlRootElement(name="FxRate")
@XmlAccessorType(XmlAccessType.FIELD)
public class FxRate {
	
	private String Dt;
	private CcyAmt[] CcyAmt;
	private String Tp;

	public String getDt() {
		return Dt;
	}

	public void setDt(String Dt) {
		this.Dt = Dt;
	}

	public CcyAmt[] getCcyAmt() {
		return CcyAmt;
	}

	public void setCcyAmt(CcyAmt[] CcyAmt) {
		this.CcyAmt = CcyAmt;
	}

	public String getTp() {
		return Tp;
	}

	public void setTp(String Tp) {
		this.Tp = Tp;
	}
}
