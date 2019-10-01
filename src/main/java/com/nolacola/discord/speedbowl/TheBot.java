package com.nolacola.discord.speedbowl;

import java.awt.Color;

import javax.security.auth.login.LoginException;

import org.apache.logging.log4j.Logger;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.AboutCommand;
import com.jagrosh.jdautilities.examples.command.GuildlistCommand;
import com.jagrosh.jdautilities.examples.command.PingCommand;
import com.jagrosh.jdautilities.examples.command.ShutdownCommand;
import com.nolacola.discord.speedbowl.enums.PropertiesEnum;
import com.nolacola.discord.speedbowl.properties.PropertyManager;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;

import org.apache.logging.log4j.LogManager;

public class TheBot{
	
	private static final Logger log = LogManager.getLogger(TheBot.class);
	private static final PropertyManager propMan = new PropertyManager();
	
    @SuppressWarnings("unused")
	public static void main( String[] args ) throws LoginException, IllegalArgumentException, InterruptedException{
    	String token = propMan.getPropertyByKey(PropertiesEnum.TOKEN);
    	JDABuilder jdaBuilder = new JDABuilder(token);
    	log.info("Starting bot...");
    	
    	CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
    	commandClientBuilder.setOwnerId(propMan.getPropertyByKey(PropertiesEnum.OWNER));
		commandClientBuilder.setPrefix(propMan.getPropertyByKey(PropertiesEnum.PREFIX));
    	commandClientBuilder.setActivity(Activity.playing("Prefix: " + propMan.getPropertyByKey(PropertiesEnum.PREFIX)));
    	
    	// Customize per-guild unique settings
    	// commandClientBuilder.setGuildSettingsManager(new MyBotsGuildSettingsManager());
    	
    	commandClientBuilder.addCommands(new PingCommand(), new ShutdownCommand(), new AboutCommand(Color.CYAN, "Description here", new String[] {"Feature 1: beep", "Feature 2: boop"}, Permission.MANAGE_CHANNEL), new GuildlistCommand(new EventWaiter()));
    	CommandClient commandClient = commandClientBuilder.build();
    	
    	jdaBuilder.addEventListeners(commandClient);
		JDA jdaBot = jdaBuilder.build();
	}
    
}
