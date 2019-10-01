package com.nolacola.discord.speedbowl.messages;

import java.awt.Color;

import com.nolacola.discord.speedbowl.NolaException;
import com.nolacola.discord.speedbowl.enums.EmbeddedMessageType;
import com.nolacola.discord.speedbowl.enums.ErrorCodes;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class MessageHandler {
	
	private static final String ERROR_ICON_URL = "https://upload.wikimedia.org/wikipedia/commons/c/c4/Icon_Error.png";
	private static final String WARNING_ICON_URL = "http://www.stickpng.com/assets/thumbs/5a81af7d9123fa7bcc9b0793.png";
	
	private static final Color COLOR_ERROR = Color.RED;
	private static final Color COLOR_OK = Color.GREEN;
	private static final Color COLOR_WARN = Color.YELLOW;
		
	/**
	 * actually sends the message
	 * @param message message to send
	 * @param channel channel to send it to
	 */
	public void sendMessage(String message, MessageChannel channel) {
		channel.sendMessage(message).queue();
	}	
	
	/**
	 * actually sends the message
	 * @param message message to send
	 * @param channel channel to send it to
	 */
	public void sendMessage(MessageEmbed message, MessageChannel channel) {
		channel.sendMessage(message).queue();
	}
	
	public void sendErrorMessage(Exception e, MessageChannel channel) {
		if(e instanceof NolaException) {
			NolaException nolaEx = (NolaException) e;
			sendMessage(generateEmbedErrorMessage(e == null ? "No Error" : "An error occurred: " + nolaEx.getErrorCode().getDescription()), channel);
		}else {
			sendMessage(generateEmbedErrorMessage(e == null ? "No Error" : "An error occurred: " + e.getMessage()), channel);
		}
	}
	
	public void sendErrorMessage(ErrorCodes errorCode, MessageChannel channel) {
		sendMessage(generateEmbedErrorMessage(errorCode.getDescription()), channel);
	}

	/**
	 * @param championKey
	 * @return
	 */
	private EmbedBuilder generateBasicEmbed(String championKey, EmbeddedMessageType embedType) {
		EmbedBuilder builder = new EmbedBuilder();
		// builder.setFooter("Brought to you by SpeedBowlBot", FOOTER_ICON);
		if(EmbeddedMessageType.OK.equals(embedType)) {
		//	builder.setThumbnail(CHAMPION_ICON_URL + championKey +".png");
			builder.setColor(COLOR_OK);
		}else if(EmbeddedMessageType.ERROR.equals(embedType)) {
			builder.setThumbnail(ERROR_ICON_URL);
			builder.setColor(COLOR_ERROR);
		}else if(EmbeddedMessageType.WARN.equals(embedType)) {
			builder.setThumbnail(WARNING_ICON_URL);
			builder.setColor(COLOR_WARN);
		}
		return builder;
	}
	
	public MessageEmbed generateEmbedErrorMessage(String errorMessage) {
		EmbedBuilder builder = generateBasicEmbed(null, EmbeddedMessageType.ERROR);
		builder.addField(new Field("An error occurred:", errorMessage, true));
		return builder.build();
	}
	
	public MessageEmbed generateEmbedWarnMessage(String errorMessage) {
		EmbedBuilder builder = generateBasicEmbed(null, EmbeddedMessageType.WARN);
		builder.addField(new Field("An error occurred:", errorMessage, true));
		return builder.build();
	}
	
	public MessageEmbed generateEmbedWarnMessage(ErrorCodes errorCode) {
		EmbedBuilder builder = generateBasicEmbed(null, EmbeddedMessageType.WARN);
		builder.addField(new Field("An error occurred:", errorCode.getDescription(), true));
		return builder.build();
	}

}
