package com.nolacola.discord.speedbowl.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	
	private static final String NEW_LINE = " \n";

	@Id
	private String cmdrId;
	
	private String cmdrName;
	
	public String getCmdrId() {
		return cmdrId;
	}
	public void setCmdrId(String cmdrId) {
		this.cmdrId = cmdrId;
	}
	public String getCmdrName() {
		return cmdrName;
	}
	public void setCmdrName(String cmdrName) {
		this.cmdrName = cmdrName;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("cmdrId = ")
				.append(cmdrId + NEW_LINE)
				.append("cmdrName = ")
				.append(cmdrName + NEW_LINE);
		return builder.toString();
	}
}
