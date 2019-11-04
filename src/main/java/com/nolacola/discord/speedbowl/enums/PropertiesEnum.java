package com.nolacola.discord.speedbowl.enums;

public enum PropertiesEnum {
	PREFIX("prefix", "!", false),
	OWNER("owner", "", false),
	COOWNERS("coowners", "", false),
	TOKEN("token", "", false),
	RACESTART("raceStart", "", true),
	RACEEND("raceEnd", "", true),
	SUBMITCHANNEL("submitChannel", "", true),
	JUDGECHANNEL("judgeChannel", "", true),
	RULELINK("ruleLink", "", true),
	FORUMLINK("forumLink", "", true),
	PRICETEXT("priceText", "", true),
	ANNOUNCEMENT("announcement", "", true),
	REACTION_CONFIRM("reactionConfirm", "U+2705", false),
	REACTION_REJECT("reactionReject", "U+274E", false),
	PERSISTENCETYPE("persistenceType", PersistenceType.DB.name(), false);
	
	private final String key;
	private final String defaultValue;
	private boolean purgeable;

	private PropertiesEnum(String key, String defaultValue, boolean purgeable) {
	    this.key = key;
	    this.defaultValue = defaultValue;
	    this.purgeable = purgeable;
	  }
	
	public boolean isPurgeable() {
		return purgeable;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return key + ": " + defaultValue;
	}
}
