package com.shoory.framework.starter.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;
import org.springframework.stereotype.Component;

@Component
public class I18nComponent {
	private Hashtable<String, Properties> messages = new Hashtable<String, Properties>();

	public I18nComponent() {
		this.loadResources();
	}

	public String getMessage(String code, String lang) {
		Properties prop = messages.get(lang);
		return prop != null ? prop.getProperty(code) : null;
	}

	private void loadResources() {
		Properties propIndex = loadProperty("i18n/index.properties");
		propIndex.forEach((filename, lang) -> {
			Properties propLang = loadProperty("i18n/"+filename+".properties");
			messages.put(lang.toString(), propLang);
		});
    }
	
	private Properties loadProperty(String resourcePath) {
		Properties prop = new Properties();
		
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourcePath);
			prop.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return prop;
	}

	public Hashtable<String, Properties> getMessages() {
		return messages;
	}

}
