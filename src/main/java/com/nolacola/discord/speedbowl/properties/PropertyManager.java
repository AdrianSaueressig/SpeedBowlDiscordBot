package com.nolacola.discord.speedbowl.properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nolacola.discord.speedbowl.TheBot;
import com.nolacola.discord.speedbowl.enums.ErrorCodes;
import com.nolacola.discord.speedbowl.enums.PropertiesEnum;

public class PropertyManager {

	private static final Logger log = LogManager.getLogger(TheBot.class);
	
	private static final String PROPERTIES_FILE = "settings.properties";
	private static final String PROPERTY_SEPERATOR = ";";
	private Properties sysProps = null;

	{
		loadProperties();
	}
	
	public void setProperty(PropertiesEnum property, String value){
		String key = property.getKey();
		log.debug("Setting property " + key + "=" + value);
		sysProps.setProperty(key, value);
		
		try(OutputStream out = new FileOutputStream(PROPERTIES_FILE)){
			sysProps.store(out, null);
			log.debug("Property value " + value + " for " + key + " set!");
		} catch (IOException e) {
			log.error(ErrorCodes.ERROR_STORING_PROPERTIES.getDescription(), e);
		}
	}
	
	public String getPropertyByKey(PropertiesEnum property) {
		loadProperties();
		return sysProps.getProperty(property.getKey(), property.getDefaultValue());
	}
	
	private void loadProperties(){
		log.debug("Loading properties...");
		sysProps = new Properties();
		try(InputStream in = new FileInputStream(PROPERTIES_FILE)){
			sysProps.load(in);
			log.debug("Properties loaded!");
		} catch (IOException e) {
			log.error(ErrorCodes.ERROR_LOADING_PROPERTIES.getDescription(), e);
		}
	}
	
	public void resetToDefault(PropertiesEnum prop) {
		setProperty(prop, prop.getDefaultValue());
	}
	
	public void getAllProperties(PrintWriter out) {
		sysProps.list(out);
	}

	public static String getPropertySeperator() {
		return PROPERTY_SEPERATOR;
	}

}
