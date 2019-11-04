package com.nolacola.discord.speedbowl.commands.owner;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.nolacola.discord.speedbowl.database.PersistenceConnection;
import com.nolacola.discord.speedbowl.enums.PropertiesEnum;
import com.nolacola.discord.speedbowl.properties.PropertyManager;
import com.nolacola.discord.speedbowl.util.Checks;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PurgeCommand extends Command{
	
	private static final Logger log = LogManager.getLogger(PurgeCommand.class);

	private static final String PURGE_CONFIRM = "This will delete ALL database entries (submissions + user) and reset all properties to default values. Are you sure? y/N";

	private PropertyManager propMan = new PropertyManager();
	private PersistenceConnection persistenceConn;
	private EventWaiter eventWaiter = null;
	private MessageChannel channel = null;
	private User author = null;

	public PurgeCommand(PersistenceConnection persistenceConnection) {
		this.help="Purge DB and purgeable properties for a fresh start. Everyone needs one, once in a while.";
		this.name="purge";
		this.ownerCommand = true;
		this.persistenceConn = persistenceConnection;
	}

	@Override
	protected void execute(CommandEvent event) {
		if(!Checks.isInAdminChannel(event.getChannel())) {
			return;
		}
		
		if(eventWaiter == null) {
			eventWaiter = new EventWaiter();
			event.getJDA().addEventListener(eventWaiter);
		}
		channel = event.getChannel();
		author = event.getAuthor();
		
		event.reply(PURGE_CONFIRM);
		
		addWaitStepToEventWaiter(msgEvent -> this.purge(msgEvent));
	}
	
	private void addWaitStepToEventWaiter(Consumer<GuildMessageReceivedEvent> purgeConsumer) {
		eventWaiter.waitForEvent(GuildMessageReceivedEvent.class,
				msgEvent -> checkIsSameUserAndChannel(msgEvent),
				purgeConsumer,
				1, TimeUnit.MINUTES, () -> {
					this.timeout();
				});
	}
	
	private void timeout() {
		channel.sendMessage("Purging aborted.");
	}
	
	private boolean checkIsSameUserAndChannel(GuildMessageReceivedEvent msgEvent) {
		return msgEvent.getChannel().equals(channel)
				&&  msgEvent.getAuthor().equals(author);
	}

	private void purge(GuildMessageReceivedEvent event) {
		if (event.getMessage().getContentRaw().equalsIgnoreCase("y")
				|| event.getMessage().getContentRaw().equalsIgnoreCase("yes")) {
			try {
				persistenceConn.purge();
				Arrays.stream(PropertiesEnum.values()).filter(e -> e.isPurgeable()).forEach(propMan::resetToDefault);
			} catch (Exception e) {
				log.error(e, e);
			}

		}
		event.getMessage().addReaction(propMan.getPropertyByKey(PropertiesEnum.REACTION_CONFIRM)).queue();
	}
}
