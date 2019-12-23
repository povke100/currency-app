package lt.povilass.currencyapp.network;

import java.util.List;

import org.apache.http.NameValuePair;

import lt.povilass.currencyapp.data.xml.FxRates;

public interface Connection {
	
	public FxRates sendPost(List<NameValuePair> payload, String endpoint) throws Exception;

}
