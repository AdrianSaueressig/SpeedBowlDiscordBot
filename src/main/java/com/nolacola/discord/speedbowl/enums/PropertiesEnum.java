package com.nolacola.discord.speedbowl.enums;

public enum PropertiesEnum {
	PREFIX("prefix", "!"),
	OWNER("owner", null),
	TOKEN("token", null);

	private final String key;
	private final String defaultValue;

	private PropertiesEnum(String key, String defaultValue) {
	    this.key = key;
	    this.defaultValue = defaultValue;
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
