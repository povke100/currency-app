package lt.povilass.currencyapp.data.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.ToString;

@ToString
@XmlRootElement(name="FxRates")
@XmlAccessorType(XmlAccessType.FIELD)
public class FxRates
{
	
    private FxRate[] FxRate;

    public FxRate[] getFxRate ()
    {
        return FxRate;
    }

    public void setFxRate (FxRate[] FxRate)
    {
        this.FxRate = FxRate;
    }
}
