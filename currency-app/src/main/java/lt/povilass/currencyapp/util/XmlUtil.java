package lt.povilass.currencyapp.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

import lombok.extern.slf4j.Slf4j;
import lt.povilass.currencyapp.data.CurrencyRate;
import lt.povilass.currencyapp.data.xml.CcyAmt;
import lt.povilass.currencyapp.data.xml.FxRate;
import lt.povilass.currencyapp.data.xml.FxRates;
import lt.povilass.currencyapp.data.xml.XMLNamespaceFilter;

@Slf4j
public class XmlUtil {

	private static JAXBContext context;
	private static Unmarshaller unmarshaller;
	
	private static SAXParserFactory factory;
	private static XMLReader reader;
	
	private static XMLFilterImpl xmlFilter;

	static {
		try {
			factory = SAXParserFactory.newInstance();
			reader = factory.newSAXParser().getXMLReader();
			xmlFilter = new XMLNamespaceFilter(reader);
			context = JAXBContext.newInstance(FxRates.class);
			unmarshaller = context.createUnmarshaller();
			
		} catch (Exception e) {
			log.error("XmlUtil. Error creating marshaller");
		}
	}
	
	public static FxRates getRatesFromStream(InputStream is) throws UnsupportedOperationException, IOException, JAXBException {
				
		SAXSource source = new SAXSource(xmlFilter, new InputSource(is));

		FxRates rates = (FxRates) unmarshaller.unmarshal(source);

		return rates;
	}
	
	
	/**
	 * Methods gathers data from collection of XML based POJO to simple List.
	 * @param ratesXml POJO generated from XML
	 * @return map for CurrencyRates of null if xmlRate is null or invalid
	 */
	public static Map<String, CurrencyRate> getRates(FxRates ratesXml){
		//Used map instead of list because later it can be used for calculating currency (faster search for needed currency)
		Map<String, CurrencyRate> rates = new HashMap<>();
		
		if(ratesXml == null) {
			return null;
		}
		
		for(FxRate fxRate : ratesXml.getFxRate()) {
			CurrencyRate rate = new CurrencyRate();
			
			rate.setDate(fxRate.getDt());
			if(fxRate.getCcyAmt().length == 2) {
				
				CcyAmt currAmnt = fxRate.getCcyAmt()[1];
				if(currAmnt != null) {
					rate.setCurrency(currAmnt.getCcy());
					rate.setAmount(new BigDecimal(currAmnt.getAmt()));
					
					rates.put(rate.getCurrency(), rate);
				} else {
					log.error("Missing data in CcyAmt!");
					return null;
				}
			} else {
				log.error("Invalid lenght of data in FxRates.getCcyAmt()!");
				return null;
			}
		}
		
		return rates;
	}

}
