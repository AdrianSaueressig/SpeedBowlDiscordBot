package com.nolacola.discord.speedbowl.commands.owner;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.nolacola.discord.speedbowl.enums.PropertiesEnum;
import com.nolacola.discord.speedbowl.properties.PropertyManager;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandInfo(
    name = "SetupCommand",
    description = "Sets everything up for the race",
    requirements = {
        "The user is the bot's owner."
    }
)
public class SetupCommand extends Command{
	private static final Logger log = LogManager.getLogger(SetupCommand.class);
	
	private static final String DATEFORMAT = "yyyyMMdd HH:mm";
	private EventWaiter eventWaiter = null;
	private PropertyManager propMan = new PropertyManager();
	private MessageChannel channel = null;
	private User author = null;

	private static final String SETUP_START = "Starting the setup process...";
	private static final String SETUP_FINISH = "Thank you. All done!";
	private static final String SETUP_TIMEOUT = "Timeout - Setup aborted. All changes were reverted.";
	private static final String SETUP_ALREADY_IN_PROGRESS = "Setup is already in progress. Please don't start a new one.";

	private static final String SETUP_ASK_RACESTART = "When's the race going to start? [yyyyMMdd] 00:00:00 UTC";
	private static final String SETUP_ASK_RACEEND = "When's the race going to finish? [yyyyMMdd] 23:59:59 UTC";
	private static final String SETUP_ASK_SUBMITCHANNEL = "In which channel would you like people to submit their stuff? [#channel]";
	private static final String SETUP_ASK_JUDGECHANNEL = "In which channel would you like to judge submissions? [#channel]";
	private static final String SETUP_ASK_RULES = "What link should I give people when asked for the rules?";
	private static final String SETUP_ASK_FORUM = "What link should I give people when asked for the forumpost?";
	private static final String SETUP_ASK_PRICETEXT = "What should I answer when asked for prizes?";
	
	private boolean alreadyInSetup = false;
	
	public SetupCommand() {
		this.name="setup";
		this.ownerCommand = true;
		this.help="Sets everything up for the race";
	}

	@Override
	protected void execute(CommandEvent event) {
		log.info("setup called");
		
		if(alreadyInSetup) {
			log.info("Setup already in progress.");
			event.reply(SETUP_ALREADY_IN_PROGRESS);
			return;
		}
		alreadyInSetup = true;
		channel = event.getChannel();
		author = event.getMessage().getAuthor();
		
		if(eventWaiter == null) {
			eventWaiter = new EventWaiter();
			event.getJDA().addEventListener(eventWaiter);
		}
		
		event.reply(SETUP_START + "\r\n" + SETUP_ASK_RACESTART);
		
		addWaitStepToEventWaiter(msgEvent -> this.receiveRaceStart(msgEvent));
	}

	private void receiveRaceStart(GuildMessageReceivedEvent receivedMessage) {
		String stringRaceStart = receivedMessage.getMessage().getContentRaw() + " 00:00";
		try {
			DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATEFORMAT).withZoneUTC();
			DateTime dateTime = DateTime.parse(stringRaceStart, dateTimeFormatter);
			
			propMan.setProperty(PropertiesEnum.RACESTART, dateTime.toString(dateTimeFormatter));
			markDone(receivedMessage);
			askRaceEnd();
		} catch (Exception e) {
			log.error(e,e);
			channel.sendMessage("Couldn't parse date. Please make sure to use the correct date format: yyyyMMdd").queue();
			addWaitStepToEventWaiter(msgEvent -> this.receiveRaceStart(msgEvent));
		}
	}

	private void askRaceEnd() {
		channel.sendMessage(SETUP_ASK_RACEEND).queue();
		
		addWaitStepToEventWaiter(msgEvent -> this.receiveRaceEnd(msgEvent));
	}
	
	private void receiveRaceEnd(GuildMessageReceivedEvent receivedMessage) {
		String stringRaceEnd = receivedMessage.getMessage().getContentRaw() + " 23:59";
		try {
			DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATEFORMAT).withZoneUTC();
			DateTime dateTime = DateTime.parse(stringRaceEnd, dateTimeFormatter);
			
			propMan.setProperty(PropertiesEnum.RACEEND, dateTime.toString(dateTimeFormatter));
			markDone(receivedMessage);
			askSubmissionChannel();
		} catch (Exception e) {
			log.error(e,e);
			channel.sendMessage("Couldn't parse date. Please make sure to use the correct date format: yyyyMMdd").queue();
			addWaitStepToEventWaiter(msgEvent -> this.receiveRaceEnd(msgEvent));
		}
	}
	
	private void askSubmissionChannel() {
		channel.sendMessage(SETUP_ASK_SUBMITCHANNEL).queue();
		
		addWaitStepToEventWaiter(msgEvent -> this.receiveSubmissionChannel(msgEvent));
	}

	private void receiveSubmissionChannel(GuildMessageReceivedEvent receivedMessage) {
		String channelRaw = receivedMessage.getMessage().getContentRaw();
		String channelId = channelRaw.substring(2, channelRaw.length()-1);
		GuildChannel guildChannel = receivedMessage.getJDA().getGuildChannelById(channelId);
		if(guildChannel == null) {
			channel.sendMessage("Couldn't find channel. Please use the discord 'mention' function #channel").queue();
			addWaitStepToEventWaiter(msgEvent -> this.receiveSubmissionChannel(msgEvent));
		}else {
			propMan.setProperty(PropertiesEnum.SUBMITCHANNEL, channelId);
			markDone(receivedMessage);
			askJudgeChannel();
		}
	}

	private void askJudgeChannel() {
		channel.sendMessage(SETUP_ASK_JUDGECHANNEL).queue();
		
		addWaitStepToEventWaiter(msgEvent -> this.receiveJudgeChannel(msgEvent));
	}

	private void receiveJudgeChannel(GuildMessageReceivedEvent receivedMessage) {
		String channelRaw = receivedMessage.getMessage().getContentRaw();
		String channelId = channelRaw.substring(2, channelRaw.length()-1);
		GuildChannel guildChannel = receivedMessage.getJDA().getGuildChannelById(channelId);
		if(guildChannel == null) {
			channel.sendMessage("Couldn't find channel. Please use the discord 'mention' function #channel").queue();
			addWaitStepToEventWaiter(msgEvent -> this.receiveJudgeChannel(msgEvent));
		}else {
			propMan.setProperty(PropertiesEnum.JUDGECHANNEL, channelId);
			markDone(receivedMessage);
			askRuleLink();
		}
	}

	private void askRuleLink() {
		channel.sendMessage(SETUP_ASK_RULES).queue();
		
		addWaitStepToEventWaiter(msgEvent -> this.receiveRules(msgEvent));
	}

	private void receiveRules(GuildMessageReceivedEvent receivedMessage) {
		propMan.setProperty(PropertiesEnum.RULELINK, receivedMessage.getMessage().getContentRaw());
		markDone(receivedMessage);
		askForumLink();
	}

	private void askForumLink() {
		channel.sendMessage(SETUP_ASK_FORUM).queue();
		
		addWaitStepToEventWaiter(msgEvent -> this.receiveForumLink(msgEvent));
	}

	private void receiveForumLink(GuildMessageReceivedEvent receivedMessage) {
		propMan.setProperty(PropertiesEnum.FORUMLINK, receivedMessage.getMessage().getContentRaw());
		markDone(receivedMessage);
		askPrizes();
	}

	private void askPrizes() {
		channel.sendMessage(SETUP_ASK_PRICETEXT).queue();
		
		addWaitStepToEventWaiter(msgEvent -> this.receivePrizes(msgEvent));
	}
	
	private void receivePrizes(GuildMessageReceivedEvent receivedMessage) {
		propMan.setProperty(PropertiesEnum.PRICETEXT, receivedMessage.getMessage().getContentRaw());
		markDone(receivedMessage);
		alreadyInSetup = false;
		channel.sendMessage(SETUP_FINISH).queue();
	}
	
	private void addWaitStepToEventWaiter(Consumer<GuildMessageReceivedEvent> setupMessageConsumer) {
		eventWaiter.waitForEvent(GuildMessageReceivedEvent.class,
				msgEvent -> checkIsSameUserAndChannelAndIsNoCommand(msgEvent),
				setupMessageConsumer,
				10, TimeUnit.MINUTES, () -> {
					this.timeout();
				});
	}

	private boolean checkIsSameUserAndChannelAndIsNoCommand(GuildMessageReceivedEvent msgEvent) {
		return msgEvent.getChannel().equals(channel)
				&&  msgEvent.getAuthor().equals(author)
				&& !msgEvent.getMessage().getContentRaw().startsWith(propMan.getPropertyByKey(PropertiesEnum.PREFIX));
	}

	private void markDone(GuildMessageReceivedEvent receivedMessage) {
		String reactionEmoji = propMan.getPropertyByKey(PropertiesEnum.REACTION_CONFIRM);
		receivedMessage.getMessage().addReaction(reactionEmoji).queue();
	}

	private void timeout() {
		log.info("timeout in setup. resetting everthing");
		propMan.setProperty(PropertiesEnum.RACESTART, "");
		propMan.setProperty(PropertiesEnum.RACEEND, "");
		propMan.setProperty(PropertiesEnum.SUBMITCHANNEL, "");
		propMan.setProperty(PropertiesEnum.JUDGECHANNEL, "");
		propMan.setProperty(PropertiesEnum.RULELINK, "");
		propMan.setProperty(PropertiesEnum.FORUMLINK, "");
		propMan.setProperty(PropertiesEnum.PRICETEXT, "");
		channel.sendMessage(SETUP_TIMEOUT).queue();
		alreadyInSetup = false;
	}
	
}