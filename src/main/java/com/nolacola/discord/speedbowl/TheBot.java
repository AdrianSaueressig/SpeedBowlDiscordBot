package com.nolacola.discord.speedbowl;

import org.apache.logging.log4j.Logger;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.examples.command.ShutdownCommand;
import com.nolacola.discord.speedbowl.commands.AnnouncementCommand;
import com.nolacola.discord.speedbowl.commands.ForumCommand;
import com.nolacola.discord.speedbowl.commands.LeaderboardCommand;
import com.nolacola.discord.speedbowl.commands.LinkCommand;
import com.nolacola.discord.speedbowl.commands.MySubmissionsCommand;
import com.nolacola.discord.speedbowl.commands.PrizesCommand;
import com.nolacola.discord.speedbowl.commands.RulesCommand;
import com.nolacola.discord.speedbowl.commands.SubmitCommand;
import com.nolacola.discord.speedbowl.commands.owner.AnnounceCommand;
import com.nolacola.discord.speedbowl.commands.owner.GetLogCommand;
import com.nolacola.discord.speedbowl.commands.owner.GetPropertiesCommand;
import com.nolacola.discord.speedbowl.commands.owner.JudgeCommand;
import com.nolacola.discord.speedbowl.commands.owner.ListCommand;
import com.nolacola.discord.speedbowl.commands.owner.PurgeCommand;
import com.nolacola.discord.speedbowl.commands.owner.RawCommand;
import com.nolacola.discord.speedbowl.commands.owner.SetupCommand;
import com.nolacola.discord.speedbowl.database.PersistenceConnection;
import com.nolacola.discord.speedbowl.database.PersistenceConnectionFactory;
import com.nolacola.discord.speedbowl.enums.PropertiesEnum;
import com.nolacola.discord.speedbowl.properties.PropertyManager;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import org.apache.logging.log4j.LogManager;

public class TheBot{
	
	private static final Logger log = LogManager.getLogger(TheBot.class);
	private static final PropertyManager propMan = new PropertyManager();
	
	@SuppressWarnings("unused")
	public static void main( String[] args ){
    	String token = propMan.getPropertyByKey(PropertiesEnum.TOKEN);
    	JDABuilder jdaBuilder = new JDABuilder(token);
    	log.info("Starting bot...");
    	
    	CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
    	commandClientBuilder.setOwnerId(propMan.getPropertyByKey(PropertiesEnum.OWNER));
    	commandClientBuilder.setCoOwnerIds(propMan.getPropertyByKey(PropertiesEnum.COOWNERS).split(PropertyManager.getPropertySeperator()));
		commandClientBuilder.setPrefix(propMan.getPropertyByKey(PropertiesEnum.PREFIX));
    	commandClientBuilder.setActivity(Activity.playing(propMan.getPropertyByKey(PropertiesEnum.PREFIX)+"help for a list of commands"));
    	commandClientBuilder.setEmojis(
    			propMan.getPropertyByKey(PropertiesEnum.REACTION_CONFIRM),
    			"",
    			propMan.getPropertyByKey(PropertiesEnum.REACTION_REJECT)
		);
    	
    	// Customize per-guild unique settings
    	// commandClientBuilder.setGuildSettingsManager(new MyBotsGuildSettingsManager());
    	
    	PersistenceConnection persistenceConnection = null;
    	try {
    		persistenceConnection = PersistenceConnectionFactory.createPersistenceConnection();
		} catch (NolaException e) {
			log.error(e,e);
		}
    	
    	commandClientBuilder.addCommands(
    			new SetupCommand(),
    			new RulesCommand(),
    			new ForumCommand(),
    			new AnnounceCommand(),
    			new AnnouncementCommand(),
    			new PrizesCommand(),
    			new GetPropertiesCommand(),
    			new GetLogCommand(),
    			new PurgeCommand(persistenceConnection),
    			new ListCommand(persistenceConnection),
    			new JudgeCommand(persistenceConnection),
    			new SubmitCommand(persistenceConnection),
    			new MySubmissionsCommand(persistenceConnection),
    			new LeaderboardCommand(persistenceConnection),
    			new RawCommand(persistenceConnection),
    			new LinkCommand(persistenceConnection),
    			
    			new ShutdownCommand());
    	CommandClient commandClient = commandClientBuilder.build();
    	
    	jdaBuilder.addEventListeners(commandClient);
    	
		try {
			JDA jdaBot = jdaBuilder.build();
		} catch (Exception e) {
			log.error(e,e);
		}
	}
}
