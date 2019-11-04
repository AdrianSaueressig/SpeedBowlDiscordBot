package com.nolacola.discord.speedbowl.enums;

public enum ErrorCodes {
	  ERROR_READING_FILE(1, "Error reading a file."),
	  UNPREDICTABLE_ERROR(2, "An unpredictable error occurred."),
	  NO_PROPERTY_KEY_IN_MESSAGE(3, "There was no property key supplied."),
	  ERROR_STORING_PROPERTIES(4, "An error occurred while saving properties."),
	  ERROR_LOADING_PROPERTIES(5, "An error occurred while loading properties."),
	  NO_PROPERTY_WITH_GIVEN_NAME(6, "No property with the given name exists"),
	  COULDNT_CONNECT_TO_PERSISTENCE(7, "Couldn't instantiate PersistenceConnection."),
	  CANT_JUDGE_ALREADY_JUDGED_SUBMISSION(8, "Can't judge an already judged submission.");
	
	  private final int code;
	  private final String description;
	
	  private ErrorCodes(int code, String description) {
	    this.code = code;
	    this.description = description;
	  }
	
	  public String getDescription() {
	     return description;
	  }
	
	  public int getCode() {
	     return code;
	  }
	
	  @Override
	  public String toString() {
	    return code + ": " + description;
	  }
}
