package com.nolacola.discord.speedbowl.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nolacola.discord.speedbowl.NolaException;
import com.nolacola.discord.speedbowl.enums.ErrorCodes;
import com.nolacola.discord.speedbowl.enums.PersistenceType;
import com.nolacola.discord.speedbowl.enums.PropertiesEnum;
import com.nolacola.discord.speedbowl.properties.PropertyManager;

public class PersistenceConnectionFactory {

	private static final Logger log = LogManager.getLogger(PersistenceConnectionFactory.class);
	private static PropertyManager propMan = new PropertyManager();
	
	public static PersistenceConnection createPersistenceConnection() throws NolaException {
		PersistenceType persistenceType;
		try {
			String persistenceTypeAsString = propMan.getPropertyByKey(PropertiesEnum.PERSISTENCETYPE);
			persistenceType = PersistenceType.valueOf(persistenceTypeAsString);
		}catch(Exception e) {
			log.error(e,e);
			throw new NolaException(ErrorCodes.COULDNT_CONNECT_TO_PERSISTENCE);
		}
		
		switch(persistenceType) {
			case CSV: return new CSVManager();
			case DB: return new DBConnection();
			default: throw new NolaException(ErrorCodes.COULDNT_CONNECT_TO_PERSISTENCE);
		}
	}
}
