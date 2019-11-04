package com.nolacola.discord.speedbowl.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.nolacola.discord.speedbowl.enums.PropertiesEnum;
import com.nolacola.discord.speedbowl.properties.PropertyManager;

import net.dv8tion.jda.api.entities.MessageChannel;

public class Checks {
	
	private static PropertyManager propMan = new PropertyManager();
	private static final String DATEFORMAT = "yyyyMMdd HH:mm";

	public static boolean isInRacePeriod() {
		String raceStartString = propMan.getPropertyByKey(PropertiesEnum.RACESTART);
		String raceEndString = propMan.getPropertyByKey(PropertiesEnum.RACEEND);
		
		if(StringUtils.isAnyBlank(raceStartString, raceEndString)) {
			return false;
		}
		
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATEFORMAT).withZoneUTC();
		DateTime raceStart = DateTime.parse(raceStartString, dateTimeFormatter);
		DateTime raceEnd = DateTime.parse(raceEndString, dateTimeFormatter);
		
		return raceStart.isBeforeNow() && raceEnd.isAfterNow();
		
	}
	
	public static boolean isInAdminChannel(MessageChannel channel) {
		return channel.getId().equals(propMan.getPropertyByKey(PropertiesEnum.JUDGECHANNEL));
	}
	
	public static boolean isInPublicChannel(MessageChannel channel) {
		return channel.getId().equals(propMan.getPropertyByKey(PropertiesEnum.SUBMITCHANNEL));
	}
}
