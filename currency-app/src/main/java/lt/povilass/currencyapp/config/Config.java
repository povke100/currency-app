package lt.povilass.currencyapp.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import lt.povilass.currencyapp.util.ConstantUtil;

@Slf4j
public class Config {
	
	private static Properties properties = new Properties();
	
	private static final String CONGIF_FILE_NAME = "currencyapp.properties";
	
	
	
	static {
		try {	
			if(Files.exists(Paths.get("./" + CONGIF_FILE_NAME))) {
				log.debug("Loading properties from external file");
				properties.load(new FileInputStream("./" + CONGIF_FILE_NAME));
			} else {
				log.debug("Loading properties from classpath config file...");
				properties.load(Config.class.getClassLoader().getResourceAsStream(CONGIF_FILE_NAME));
			}
			Class.forName(Config.getDBDriver());
		} catch (IOException | ClassNotFoundException e) {
			log.error("Loading of properties has failed.", e);
		}
	}
	
	public static String getConnectionTimeout() {
		return properties.getProperty(ConstantUtil.CGF_CONNECTION_TIMEOUT, "5000");
	}
	
	public static String getSocketTimeout() {
		return properties.getProperty(ConstantUtil.CFG_SOCKET_TIMEOUT, "5000");
	}
	
	public static String getLBEndpoint() {
		return properties.getProperty(ConstantUtil.CFG_LB_ENDPOINT, "");
	}
	
	public static String getDBDriver() {
		return properties.getProperty(ConstantUtil.CFG_DB_DRIVER, "");
	}
	
	public static String getDBConnString() {
		return properties.getProperty(ConstantUtil.CFG_DB_CONN_STRING, "");
	}
	
	public static String getDBUsername() {
		return properties.getProperty(ConstantUtil.CFG_DB_USERNAME, "");
	}
	
	public static String getDBPassword() {
		return properties.getProperty(ConstantUtil.CFG_DB_PASSWORD, "");
	}
	
	
	
	
		
	
}