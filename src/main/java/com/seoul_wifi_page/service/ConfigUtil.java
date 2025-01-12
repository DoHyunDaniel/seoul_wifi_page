package com.seoul_wifi_page.service;

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
	// TODO Auto-generated method stub
	public static String getApiKey() {
		Properties prop = new Properties();
		try (InputStream input = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new RuntimeException("Sorry, unable to find config.properties");
			}
			prop.load(input);
			return prop.getProperty("api.key");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
