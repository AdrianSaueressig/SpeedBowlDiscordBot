package com.nolacola.discord.speedbowl;

import com.nolacola.discord.speedbowl.enums.ErrorCodes;

public class NolaException extends Exception {

	private static final long serialVersionUID = 5678060739174943132L;
	private ErrorCodes errorCode = null;
	
	public NolaException(ErrorCodes errorCode) {
		this.setErrorCode(errorCode);
	}

	public ErrorCodes getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCodes errorCode) {
		this.errorCode = errorCode;
	}
	
}
