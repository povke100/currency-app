package lt.povilass.currencyapp.network;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import lombok.extern.slf4j.Slf4j;
import lt.povilass.currencyapp.config.Config;
import lt.povilass.currencyapp.data.xml.FxRates;
import lt.povilass.currencyapp.util.XmlUtil;

@Slf4j
public class ConnectionImpl implements Connection{
	

	@Override
	public FxRates sendPost(List<NameValuePair> payload, String endpoint) throws Exception {
		
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost request = new HttpPost(endpoint);
		
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(Integer.valueOf(Config.getConnectionTimeout()))
				.setSocketTimeout(Integer.valueOf(Config.getSocketTimeout()))
				.build();
				
		 
		request.setConfig(config);
		
		request.addHeader("Content-type", "application/x-www-form-urlencoded");
		request.setEntity(new UrlEncodedFormEntity(payload));
		
		HttpResponse response = client.execute(request);
		
		FxRates rates = XmlUtil.getRatesFromStream(response.getEntity().getContent());		
		log.debug("RATES: {}", rates);
		client.close();
		
		return rates;
	}


}
